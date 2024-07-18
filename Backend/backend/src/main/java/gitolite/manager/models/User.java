package gitolite.manager.models;

import java.util.Comparator;
import java.util.Map;

import gitolite.manager.exceptions.ModificationException;
import gitolite.manager.models.Recorder.Modification;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class represents a user in Gitolite.
 * 
 * @author Michael de Jong &lt;<a href="mailto:michaelj@minicom.nl">michaelj@minicom.nl</a>&gt;
 */
public final class User implements Identifiable {

	static final Comparator<User> SORT_BY_NAME = new Comparator<User>() {
		@Override
		public int compare(User arg0, User arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};

	private final String name;
	private final Map<String, String> keys;
	private final Recorder recorder;

	/**
	 * Constructs a new {@link User} object with the provided name and public key.
	 * 
	 * @param name
	 * 	The name of the user.
	 */
	User(String name) {
		this(name, new Recorder());
	}
	
	/**
	 * Constructs a new {@link User} object with the provided name and public key.
	 * 
	 * @param name
	 * 	The name of the user.
	 * 
	 * @param recorder
	 * 	The {@link Recorder} to use when recording changes of this {@link User}.
	 */
	User(String name, Recorder recorder) {
		Preconditions.checkNotNull(name);
		Preconditions.checkArgument(!name.isEmpty());
		Preconditions.checkArgument(name.matches("^\\w[\\w._\\@+-]+$"), "\"" + name + "\" is not a valid user name");
		Preconditions.checkNotNull(recorder);
		
		this.name = name;
		this.recorder = recorder;
		this.keys = Maps.newTreeMap();
	}

	/**
	 * @return
	 * 	The name of the {@link User}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method allows you to set (and override existing) SSH keys for this particular {@link User}.
	 * 
	 * @param name
	 * 	The name of the key. This may not be NULL.
	 * 
	 * @param content
	 * 	The content of the public key file. This may not be NULL.
	 */
	public void setKey(final String name, final String content) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(content);
		Preconditions.checkArgument(name.matches("^[\\w._+-]*$"), "\"" + name + "\" is not a valid key name");
		Preconditions.checkArgument(content.matches("^ssh-rsa\\s.+$"));

		synchronized (keys) {
			keys.put(name, content);
		}
		
		recorder.append(new Modification("Setting key: '%s' for user: '%s'", name, getName()) {
			@Override
			public void apply(Config config) throws ModificationException {
				config.getUser(getName()).setKey(name, content);
			}
		});
	}

	/**
	 * @return
	 * 	An {@link Map} of SSH keys for this user. The key of the {@link Map}
	 * 	is the name of the key, and the value is the contents of the associated key file.
	 */
	public ImmutableMap<String, String> getKeys() {
		synchronized (keys) {
			return ImmutableMap.copyOf(keys);
		}
	}

	/**
	 * This method removes the SSH key with the specified name from this {@link User} object.
	 *  
	 * @param name
	 * 	The name of the SSH key to remove.
	 */
	public void removeKey(final String name) {
		Preconditions.checkNotNull(name);

		synchronized (keys) {
			keys.remove(name);
		}
		
		recorder.append(new Modification("Removing key: '%s' for user: '%s'", name, getName()) {
			@Override
			public void apply(Config config) throws gitolite.manager.exceptions.ModificationException {
				config.getUser(getName()).removeKey(name);
			}
		});
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(name)
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof User)) {
			return false;
		}
		return new EqualsBuilder()
			.append(name, ((User) other).name)
			.isEquals();
	}
	
}
