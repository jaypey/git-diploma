
import gitolite.parser.rules.GroupRule;
import gitolite.objects.Identifiable;
import gitolite.objects.Identifier;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.contains;

/**
 * @author Jan-Willem Gmelig Meyling
 */
public class TestGroupRule {

	@Test
	public void test1() throws IOException {
		Identifier foo = new Identifier("foo");
		Identifier bar = new Identifier("bar");

		GroupRule test = GroupRule.builder()
			.pattern("@test")
			.member(foo)
			.build();

		GroupRule other = GroupRule.builder()
			.pattern("@other")
			.group(test)
			.member(bar)
			.build();

		test.write(System.out);
		other.write(System.out);

		List<Identifiable> members = other.getMembersStream().collect(Collectors.toList());
		members.forEach(System.out::println);
		Assert.assertThat(members, Matchers.contains(foo, bar));
	}

}
