package gitolite;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import gitolite.config.Config;
import gitolite.git.GitManager;
import gitolite.git.GitManagerFactory;
import gitolite.git.NativeGitManagerFactory;
import gitolite.keystore.KeyStore;
import gitolite.keystore.KeyStoreImplFactory;
import gitolite.parser.TokenizerBasedParser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;

/**
 * The {@code ManagedConfigFactory} can be used to bind custom {@link gitolite.git.GitManager} and
 * {@link gitolite.keystore.KeyStore} implementations to a {@link ManagedConfig}.
 *
 * <p>Example usage:</p>
 *
 * {@code <pre>
 *    ManagedConfig config = new ManagedConfigFactory()
 *			.gitManagerFactory(new NativeGitManagerFactory())
 *			.keyStoreImplFactory(new KeyStoreImplFactory())
 *			.repositoryFolder(new File("/path/to/folder"))
 *			.init("ssh://localhost:2222/gitolite-admin");
 * </pre>}
 *
 * @author Jan-Willem Gmelig Meyling
 */
@Data
@Accessors(fluent = true)
public class ManagedConfigFactory {

	/**
	 * The key folder.
	 */
	public static final String KEYDIR_REL_PATH = "keydir";

	/**
	 * The configuration folder.
	 */
	public static final String CONFDIR_REL_PATH = "conf";

	/**
	 * The configuration file name.
	 */
	public static final String GITOLITE_CONF_FILE = "gitolite.conf";

	/**
	 * The {@link gitolite.git.GitManagerFactory} used to create the {@link gitolite.git.GitManager}.
	 */
	private GitManagerFactory gitManagerFactory = new NativeGitManagerFactory();

	/**
	 * The {@link gitolite.keystore.KeyStoreImplFactory} to create the {@link gitolite.keystore.KeyStore}.
	 */
	private gitolite.keystore.KeyStoreImplFactory keyStoreImplFactory = new KeyStoreImplFactory();

	/**
	 * Define the repository folder to use. Defaults to a temporary directory.
	 */
	private File repositoryFolder;

	/**
	 * Initialize a {@link ManagedConfig}.
	 * @param gitoliteAdminRepo Administration repository to clone.
	 * @return The instantiated {@code ManagedConfig}.
	 * @throws IOException If an I/O error occurs.
	 * @throws InterruptedException If the thread was interrupted.
	 */
	public ManagedConfig init(String gitoliteAdminRepo) throws IOException, InterruptedException {
		Preconditions.checkNotNull(gitoliteAdminRepo);
		ensureRepositoryFolderExists();
		gitolite.git.GitManager gitManager = getGitManager();
		ensureRepositoryExists(gitManager, gitoliteAdminRepo);

		gitolite.keystore.KeyStore keyStore = getKeyStore();
		gitolite.config.Config config = parseConfig();

		return new ManagedConfig(gitManager, keyStore, config);
	}

	/**
	 * Create a temporary folder if null.
	 */
	protected void ensureRepositoryFolderExists() {
		if(repositoryFolder == null) {
			repositoryFolder = Files.createTempDir();
		}
	}

	/**
	 * Function to initialize the {@code GitManager} for the {@code ManagedConfig}.
	 * @return the GitManager implementation
	 */
	protected gitolite.git.GitManager getGitManager() {
		return gitManagerFactory.create(repositoryFolder);
	}

	/**
	 * Function to initialize the {@code KeyStore} for the {@code ManagedConfig}.
	 * @return the KeyStore implementation.
	 */
	protected KeyStore getKeyStore() {
		return keyStoreImplFactory.create(new File(repositoryFolder, KEYDIR_REL_PATH));
	}

	/**
	 * Parse the {@code Config} from the configuration file.
	 * @return the parsed config.
	 * @throws IOException If an I/O error occurs.
	 */
	protected Config parseConfig() throws IOException {
		File confDir = new File(repositoryFolder, CONFDIR_REL_PATH);
		File configurationFile = new File(confDir, GITOLITE_CONF_FILE);
		return TokenizerBasedParser.parse(configurationFile);
	}

	/**
	 * Ensure that the administration repository exists. Pull if exists, else,
	 * clone the administration repository.
	 * @param gitManager initialized {@code GitManager}.
	 * @param gitoliteAdminRepo administration repository URL.
	 * @throws IOException If an I/O error occurs.
	 * @throws InterruptedException If the thread was interrupted.
	 */
	protected void ensureRepositoryExists(GitManager gitManager, String gitoliteAdminRepo) throws IOException, InterruptedException {
		if(gitManager.exists()) {
			gitManager.pull();
		}
		else {
			gitManager.clone(gitoliteAdminRepo);
		}
	}

}
