package gitolite.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gitolite.objects.Identifiable;
import gitolite.parser.rules.*;
import gitolite.permission.Permission;
import gitolite.config.Config;
import gitolite.config.ConfigImpl;
import gitolite.objects.Identifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Jan-Willem Gmelig Meyling
 */
public class TokenizerBasedParser {

	private final StreamTokenizer streamTokenizer;
	private final Map<String, gitolite.parser.rules.GroupRule> groupRuleMap = Maps.newHashMap();

	private boolean eof = false;

	public TokenizerBasedParser(final Reader reader) {
		streamTokenizer = new StreamTokenizer(reader);
		streamTokenizer.resetSyntax();
		// Set everything as word char
		streamTokenizer.wordChars(33, 126);
		// Allow strings within quotes
		streamTokenizer.quoteChar('\'');
		streamTokenizer.quoteChar('\"');
		// EOL is significant
		streamTokenizer.eolIsSignificant(true);
		// Slice off comments
		streamTokenizer.commentChar('#');
	}

	/**
	 * Check if there is a next token on the {@code StreamTokenizer}.
	 * @return true if there is a new token
	 * @throws IOException IO exception if a new token cannot be read.
	 */
	protected boolean hasNext() throws IOException {
		return hasNext(false);
	}

	/**
	 * Check if there is a next token on the {@code StreamTokenizer}.
	 * @param pattern a pattern to search for.
	 * @return true if there is a next token and it matches the pattern.
	 * @throws IOException IO exception if a new token cannot be read.
	 */
	protected boolean hasNext(String pattern) throws IOException {
		boolean val = false;
		if(hasNext()) {
			String next = next();
			val = next.startsWith(pattern);
			streamTokenizer.pushBack();
		}
		return val;
	}

	/**
	 * Check if there is a next token on the same line.
	 * @param sameLine set this to true to check for the same line.
	 * @return true if there is a next token and it matches the pattern.
	 * @throws IOException IO exception if a new token cannot be read.
	 */
	protected boolean hasNext(boolean sameLine) throws IOException {
		for(int token; !eof;) {
			token = streamTokenizer.nextToken();
			switch (token) {
				case StreamTokenizer.TT_EOF:
					eof = true;
					return false;
				case StreamTokenizer.TT_EOL:
					if(sameLine) {
						return false;
					}
					else {
						continue;
					}
				case '\"': // Double quoted strings
				case '\'': // Single quoted strings
				case StreamTokenizer.TT_WORD:
				case StreamTokenizer.TT_NUMBER:
					streamTokenizer.pushBack();
					return true;
			}
		}
		return false;
	}

	/**
	 * Get the next token.
	 * @return The next token.
	 * @throws IOException If the token could not be read.
	 */
	protected String next() throws IOException {
		return next(false);
	}

	/**
	 * Get the next token on the current line.
	 * @param sameLine Whether or not to look on the same line.
	 * @return The next token.
	 * @throws IOException If the next token could not be read.
	 */
	protected String next(boolean sameLine) throws IOException {
		for(int token; hasNext(sameLine);) {
			token = streamTokenizer.nextToken();
			switch (token) {
				case '\"': // Double quoted strings
				case '\'': // Single quoted strings
				case StreamTokenizer.TT_WORD:
					return streamTokenizer.sval;
				case StreamTokenizer.TT_NUMBER:
					return Long.toString(Math.round(streamTokenizer.nval));
			}
		}
		throw new NoSuchElementException();
	}

	/**
	 * Assert that the next token matches a certain pattern.
	 * @param pattern pattern to match
	 * @return the next token
	 * @throws IOException if the next token could not be read.
	 * @throws NoSuchElementException if the next token did not match the pattern.
	 */
	protected String next(String pattern) throws IOException {
		String next = next();
		if(!next.startsWith(pattern)) {
			throw new NoSuchElementException(String.format("%s does not match %s", next, pattern));
		}
		return next;
	}

	/**
	 * Consume a list of tokens {@code [@foo bar]} from a {@code Scanner} and put them in their
	 * corresponding {@code List}.
	 * @param groups {@code List} for {@link gitolite.parser.rules.GroupRule} references.
	 * @param members {@code List} for {@link gitolite.objects.Identifiable} references.
	 * @throws IOException  if the next token could not be read.
	 */
	public void parseInlineGroup(final List<? super gitolite.parser.rules.GroupRule> groups, final List<? super Identifier> members) throws IOException {
		while(hasNext(true)) {
			String idName = next();
			if(idName.startsWith("@")) {
				gitolite.parser.rules.GroupRule groupRule;
				if(idName.equals(gitolite.parser.rules.GroupRule.ALL.getPattern())) {
					groupRule = gitolite.parser.rules.GroupRule.ALL;
				}
				else {
					groupRule = groupRuleMap.get(idName);
					if(groupRule == null) throw new NoSuchElementException();
				}
				groups.add(groupRule);
			}
			else {
				members.add(Identifier.valueOf(idName));
			}
		}
	}

	/**
	 * Parse a GroupRule
	 * @return the parsed GroupRule
	 * @throws IOException  if the next token could not be read.
	 */
	public gitolite.parser.rules.GroupRule parseGroupRule() throws IOException {
		String name = next();
		this.next("=");

		gitolite.parser.rules.GroupRule parent = groupRuleMap.get(name);
		List<gitolite.parser.rules.GroupRule> groups = Lists.newArrayList();
		List<Identifier> members = Lists.newArrayList();
		parseInlineGroup(groups, members);

		gitolite.parser.rules.GroupRule rule = new gitolite.parser.rules.GroupRule(name, parent, members, groups);
		groupRuleMap.put(name, rule);
		return rule;
	}

	/**
	 * Parse a ConfigKey
	 * @return the parsed ConfigKey
	 * @throws IOException  if the next token could not be read.
	 */
	public gitolite.parser.rules.ConfigKey parseConfigRule() throws IOException {
		next("config");
		String option = next(true);
		next("=");
		String value = next(true);
		return new gitolite.parser.rules.ConfigKey(option, value);
	}

	/**
	 * Parse a Option
	 * @return the parsed Option
	 * @throws IOException  if the next token could not be read.
	 */
	public gitolite.parser.rules.Option parseOption() throws IOException {
		next("option");
		String option = next(true);
		next("=");
		String value = next(true);
		return new Option(option, value);
	}

	/**
	 * Parse a {@link gitolite.parser.rules.AccessRule}
	 * @return the parsed {@code AccessRule}
	 * @throws IOException  if the next token could not be read.
	 */
	public gitolite.parser.rules.AccessRule parseAccessRule() throws IOException {
		gitolite.permission.Permission permission = Permission.valueOf(next());
		String next = next();
		String refex = null;
		if(!next.equals("=")) {
			refex = next;
			next("=");
		}
		List<gitolite.parser.rules.GroupRule> groups = Lists.newArrayList();
		List<Identifier> members = Lists.newArrayList();
		parseInlineGroup(groups, members);
		return new gitolite.parser.rules.AccessRule(permission, refex, groups, members);
	}

	/**
	 * Parse a {@link gitolite.parser.rules.RepositoryRule}
	 * @return the parsed {@link gitolite.parser.rules.RepositoryRule}
	 * @throws IOException  if the next token could not be read.
	 */
	public gitolite.parser.rules.RepositoryRule parseRepositoryRule() throws IOException {
		next("repo");

		List<Identifiable> identifiables = Lists.newArrayList();
		parseInlineGroup(identifiables, identifiables);

		List<AccessRule> rules = Lists.newArrayList();
		List<ConfigKey> configKeys = Lists.newArrayList();

		for(;;) {
			if(hasNext("repo") || hasNext("@")) break;
			else if(hasNext("config")) configKeys.add(parseConfigRule());
			else if(hasNext("option")) configKeys.add(parseOption());
			else if(hasNext()) rules.add(parseAccessRule());
			else break;
		}

		return new gitolite.parser.rules.RepositoryRule(identifiables, rules, configKeys);
	}

	/**
	 * Parse rules and put them in a collection.
	 * @param rules Collection to add the parsed rules to.
	 * @throws IOException if the document could not be parsed.
	 */
	public void parse(final Collection<? super Rule> rules) throws IOException {
		parse(rules, rules);
	}

	/**
	 * Parse rules and put them in a collection.
	 * @param repositoryRules Collection to add the parsed {@link gitolite.parser.rules.RepositoryRule} rules to.
	 * @param groupRules Collection to add the parsed {@link gitolite.parser.rules.GroupRule} rules to.
	 * @throws IOException if the document could not be parsed.
	 */
	public void parse(final Collection<? super gitolite.parser.rules.RepositoryRule> repositoryRules, final Collection<? super gitolite.parser.rules.GroupRule> groupRules) throws IOException {
		for(;;) {
			if(hasNext("repo")) repositoryRules.add(parseRepositoryRule());
			else if(hasNext("@")) groupRules.add(parseGroupRule());
			else break;
		}
	}

	/**
	 * Parse a Config from a file
	 * @param configurationFile file to parse
	 * @return the parsed config
	 * @throws IOException if the document could not be parsed.
	 */
	public static Config parse(final File configurationFile) throws IOException {
		List<GroupRule> groupRules = Lists.newArrayList();
		List<RepositoryRule> repositoryRules = Lists.newArrayList();

		try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configurationFile)))) {
			TokenizerBasedParser parser = new TokenizerBasedParser(bufferedReader);
			parser.parse(repositoryRules, groupRules);
		}

		return new ConfigImpl(groupRules, repositoryRules);
	}

}
