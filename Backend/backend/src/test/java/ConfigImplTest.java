import gitolite.config.ConfigImpl;
import gitolite.config.CyclicDependencyException;
import gitolite.objects.Identifier;
import gitolite.parser.rules.AccessRule;
import gitolite.parser.rules.GroupRule;
import gitolite.parser.rules.RepositoryRule;
import gitolite.parser.rules.Rule;
import gitolite.permission.BasePermission;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Jan-Willem Gmelig Meyling
 */
public class ConfigImplTest {


	private final static Identifier foo = new Identifier("foo");
	private final static Identifier bar = new Identifier("bar");
	private final static Identifier baz = new Identifier("baz");

	private ConfigImpl config;

	@Before
	public void setUp() {
		config = new ConfigImpl();
	}

	@Test
	public void testGetInsertedGroup() {
		GroupRule test = GroupRule.builder()
			.pattern("@test").member(foo).member(bar).build();
		config.addGroup(test);
		assertEquals(test, config.getGroup("@test"));
	}

	@Test
	public void testTopologicalSort() {
		GroupRule test = GroupRule.builder()
			.pattern("@test").member(foo).member(bar).build();
		config.addGroup(test);

		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep").member(baz).build();
		config.addGroup(bliep);

		test.add(bliep);

		Collection<Rule> rules = config.getRules();
		Assert.assertThat(rules, Matchers.contains(bliep, test));
	}

	@Test(expected = CyclicDependencyException.class)
	public void testCyclicDependencyException() {
		GroupRule test = GroupRule.builder()
			.pattern("@test").member(foo).member(bar).build();
		config.addGroup(test);

		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep").member(baz).build();
		config.addGroup(bliep);

		test.add(bliep);
		bliep.add(test);
		config.getRules();
	}

	@Test
	public void testDeleteGroup() {
		GroupRule test = GroupRule.builder()
			.pattern("@test").member(foo).member(bar).build();
		config.addGroup(test);

		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep")
			.group(test)
			.member(baz).build();
		config.addGroup(bliep);

		assertThatStream(bliep.getOwnGroupsStream(), contains(test));

		config.deleteGroup(test);
		Collection<Rule> rules = config.getRules();
		Assert.assertThat(rules, contains(bliep));
		assertThatStream(bliep.getOwnGroupsStream(), empty());
	}

	@Test
	public void testAddRepositoryRule() {
		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(foo)
			.rule(new AccessRule(BasePermission.RW_PLUS, bar))
			.build();

		config.addRepositoryRule(repositoryRule);

		Assert.assertThat(config.getFirstRepositoryRule(foo), equalTo(repositoryRule));
		Assert.assertThat(config.getRepositoryRule(foo), contains(repositoryRule));
	}

	@Test
	public void testAddRepositoryRuleWithGroup() {
		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep").member(baz).build();

		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(foo)
			.rule(new AccessRule(BasePermission.RW_PLUS, Collections.singleton(bliep), Collections.singleton(bar)))
			.build();

		config.addRepositoryRule(repositoryRule);
		Assert.assertThat(config.getRules(), Matchers.contains(bliep, repositoryRule));
	}

	@Test
	public void testGroupRemoveCascadesToRepository() {
		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep").member(baz).build();

		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(foo).identifiable(bliep)
			.rule(new AccessRule(BasePermission.RW_PLUS, bar))
			.build();

		Assert.assertThat(repositoryRule.getIdentifiables(), Matchers.contains(foo, bliep));

		config.addRepositoryRule(repositoryRule);
		config.deleteGroup(bliep);

		Assert.assertThat(repositoryRule.getIdentifiables(), contains(foo));
		Assert.assertThat(config.getRules(), contains(repositoryRule));
	}

	@Test
	public void testCascadedRepositoryRemoval() {
		GroupRule bliep = GroupRule.builder()
			.pattern("@bliep").member(baz).build();

		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(bliep)
			.rule(new AccessRule(BasePermission.RW_PLUS, bar))
			.build();

		config.addRepositoryRule(repositoryRule);
		config.deleteGroup(bliep);

		Assert.assertThat(repositoryRule.getIdentifiables(), empty());
	}

	@Test
	public void testRepositoryWithAllGroup() {
		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(foo)
			.rule(new AccessRule(BasePermission.RW_PLUS, GroupRule.ALL))
			.build();

		config.addRepositoryRule(repositoryRule);
		Assert.assertThat(config.getRules(), contains(repositoryRule));

	}

	@Test
	public void testRemoveIdentifierUses() {
		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(foo)
			.rule(new AccessRule(BasePermission.RW_PLUS, GroupRule.ALL))
			.build();

		config.addRepositoryRule(repositoryRule);

		config.deleteIdentifierUses(foo);
		Assert.assertThat(config.getRules(), empty());
	}

	@Test
	public void testRemoveAGroupThatWasReferencedByAGroup() {
		GroupRule groupRule = new GroupRule("@test", foo);
		GroupRule groupRuleB = new GroupRule("@bliep", null, Collections.emptyList(), Collections.singleton(groupRule));
		GroupRule groupRuleC = new GroupRule("@lupa", null, Collections.emptyList(), Collections.singleton(groupRuleB));

		config.addGroup(groupRule);
		config.addGroup(groupRuleB);
		config.addGroup(groupRuleC);

		Collection<GroupRule> groupRules = config.getGroupRules();
		Assert.assertThat(groupRules, Matchers.contains(groupRule, groupRuleB, groupRuleC));

		config.deleteGroup(groupRule);

		Assert.assertThat(config.getGroupRules(), empty());
	}

	@Test
	public void testRemoveIdentifierUsesInBigConfig() {
		GroupRule groupRule = new GroupRule("@test", foo);
		GroupRule groupRuleB = new GroupRule("@bliep", null, Collections.emptyList(), Collections.singleton(groupRule));
		GroupRule groupRuleC = new GroupRule("@lupa", null, Collections.emptyList(), Collections.singleton(groupRuleB));

		RepositoryRule repositoryRule = RepositoryRule.builder()
			.identifiable(bar)
			.rule(new AccessRule(BasePermission.RW_PLUS, GroupRule.ALL))
			.build();

		RepositoryRule repositoryRule2 = RepositoryRule.builder()
			.identifiable(baz)
			.rule(new AccessRule(BasePermission.RW_PLUS, GroupRule.ALL))
			.build();

		config.addGroup(groupRule);
		config.addGroup(groupRuleB);
		config.addGroup(groupRuleC);
		config.addRepositoryRule(repositoryRule);
		config.addRepositoryRule(repositoryRule2);

		config.deleteIdentifierUses(bar);

		Assert.assertThat(
			config.getRules(),
			Matchers.contains(groupRule, groupRuleB, groupRuleC, repositoryRule2)
		);

	}

	public static <T> void assertThatStream(Stream<T> stream, Matcher<? super List<T>> matcher) {
		Assert.assertThat(stream.collect(toList()), matcher);
	}

}
