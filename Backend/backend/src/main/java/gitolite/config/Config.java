package gitolite.config;

import gitolite.objects.Identifiable;
import gitolite.parser.rules.GroupRule;
import gitolite.parser.rules.RepositoryRule;
import gitolite.parser.rules.Rule;
import gitolite.parser.rules.Writable;

import java.util.Collection;

/**
 * @author Jan-Willem Gmelig Meyling
 */
public interface Config extends Writable {

	/**
	 * Get a group from the config
	 * @param name the name for the group
	 * @return the group
	 */
	gitolite.parser.rules.GroupRule getGroup(String name);

	/**
	 * Add a group.
	 * @param groupRule GroupRule for the group.
	 */
	void addGroup(gitolite.parser.rules.GroupRule groupRule);

	/**
	 * Delete a group from the config.
	 * @param groupRule GroupRule for the group.
	 * @return true if the group was removed.
	 */
	boolean deleteGroup(gitolite.parser.rules.GroupRule groupRule);

	/**
	 * @return all group rules
	 */
	Collection<? extends GroupRule> getGroupRules();

	/**
	 * Remove an identifier
	 * @param identifier The identifier to use
	 */
	void deleteIdentifierUses(gitolite.objects.Identifiable identifier);

	/**
	 * Get the repository rules for a set of identifiables.
	 * @param identifiables Set of identifiables
	 * @return a collection of repository rules
	 */
	Collection<? extends gitolite.parser.rules.RepositoryRule> getRepositoryRule(gitolite.objects.Identifiable... identifiables);

	/**
	 * Get the repository rule for a set of identifiables.
	 * @param identifiables Set of identifiables.
	 * @return a repository rule.
	 */
	gitolite.parser.rules.RepositoryRule getFirstRepositoryRule(Identifiable... identifiables);

	/**
	 * Add a RepositoryRule.
	 * @param repositoryRule RepositoryRule to add.
	 */
	void addRepositoryRule(gitolite.parser.rules.RepositoryRule repositoryRule);

	/**
	 * Delete a repository rule.
	 * @param rule Rule to remove.
	 * @return true if the repository rule was removed.
	 */
	boolean deleteRepositoryRule(RepositoryRule rule);

	/**
	 * @return a list of rules
	 */
	Collection<Rule> getRules();

	void clear();
}
