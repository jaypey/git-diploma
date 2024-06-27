package gitolite;

import com.googlecode.concurentlocks.ReadWriteUpdateLock;
import com.googlecode.concurentlocks.ReentrantReadWriteUpdateLock;
import gitolite.config.Config;
import gitolite.git.GitManager;
import gitolite.keystore.KeyStore;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The {@code ManagedConfig} decorates a {@link gitolite.keystore.KeyStore} and a {@link gitolite.config.Config} with a
 * {@link gitolite.git.GitManager}, in order to {@link ManagedConfig#applyChanges() apply} changes
 * to the remote config.
 *
 * @author Jan-Willem Gmelig Meyling
 */
public class ManagedConfig {

	/**
	 * The configuration folder.
	 */
	public static final String CONFDIR_REL_PATH = "conf";

	/**
	 * The configuration file name.
	 */
	public static final String GITOLITE_CONF_FILE = "gitolite.conf";

	/**
	 * The {@code GitManager} to use.
	 */
	private final gitolite.git.GitManager gitManager;

	/**
	 * The {@code KeyStore} to use.
	 */
	private final gitolite.keystore.KeyStore keyStore;

	/**
	 * The {@code Config}.
	 */
	private final gitolite.config.Config config;

	/**
	 * Locks used for operations on the {@code Config} and {@code KeyStore}.
	 */
	private final ReadWriteUpdateLock readWriteLock = new ReentrantReadWriteUpdateLock();

    public ManagedConfig(GitManager g, gitolite.keystore.KeyStore k, gitolite.config.Config c) {
        gitManager = g;
        keyStore = k;
        config = c;
    }

    /**
	 * Commit and push changes to the remote.
	 */
	protected void applyChanges() {
		File confDir = new File(gitManager.getWorkingDirectory(), CONFDIR_REL_PATH);
		File configurationFile = new File(confDir, GITOLITE_CONF_FILE);
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configurationFile, false))) {
			config.write(bufferedWriter);
			gitManager.commitChanges();
			gitManager.push();
		} catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Perform read operations to the {@link gitolite.config.Config} within a {@code ReadLock}.
	 * @param configInteraction function
	 * @param <T> return type
	 * @return return value
	 */
	public <T> T readConfigWithReturn(Function<? super gitolite.config.Config, T> configInteraction) {
		readWriteLock.updateLock().lock();
		try {
			return configInteraction.apply(config);
		}
		finally {
			readWriteLock.updateLock().unlock();
		}
	}

	/**
	 * Perform read operations to the {@link gitolite.config.Config} within a {@code ReadLock}.
	 * @param configInteraction function
	 */
	public void readConfig(Consumer<? super gitolite.config.Config> configInteraction) {
		readWriteLock.updateLock().lock();
		try {
			configInteraction.accept(config);
		}
		finally {
			readWriteLock.updateLock().unlock();
		}
	}

	/**
	 * Perform write operations to the {@link gitolite.config.Config} within a {@code WriteLock}.
	 * Applies the changes to the repository when done.
	 * @param configInteraction function
	 * @see ManagedConfig#applyChanges()
	 */
	public void writeConfig(Consumer<? super gitolite.config.Config> configInteraction) {
		readWriteLock.writeLock().lock();
		try {
			configInteraction.accept(config);
			applyChanges();
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * Perform write operations to the {@link gitolite.config.Config} within a {@code WriteLock}.
	 * Applies the changes to the repository when done.
	 * @param configInteraction function
	 * @param <T> return type
	 * @return return value
	 * @see ManagedConfig#applyChanges()
	 */
	public <T> T writeConfigWithReturn(Function<? super Config, T> configInteraction) {
		readWriteLock.writeLock().lock();
		try {
			T res = configInteraction.apply(config);
			applyChanges();
			return res;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * Perform read operations to the {@link gitolite.keystore.KeyStore} within a {@code ReadLock}.
	 * @param configInteraction function
	 * @param <T> return type
	 * @return return value
	 */
	public <T> T readKeyStore(Function<? super gitolite.keystore.KeyStore, T> configInteraction) {
		readWriteLock.updateLock().lock();
		try {
			return configInteraction.apply(keyStore);
		}
		finally {
			readWriteLock.updateLock().unlock();
		}
	}

	/**
	 * Perform write operations to the {@link gitolite.keystore.KeyStore} within a {@code WriteLock}.
	 * Applies the changes to the repository when done.
	 * @param configInteraction function
	 * @param <T> return type
	 * @return return value
	 * @see ManagedConfig#applyChanges()
	 */
	public <T> T writeKeyStoreWithReturn(ThrowingFunction<? super gitolite.keystore.KeyStore, T> configInteraction) {
		readWriteLock.writeLock().lock();
		try {
			T res = configInteraction.apply(keyStore);
			applyChanges();
			return res;
		} catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
			readWriteLock.writeLock().unlock();
		}
	}

	public interface ThrowingConsumer<T> {
		void accept(T value) throws IOException, InterruptedException;
	}

	public interface ThrowingFunction<T, R> {
		R apply(T value) throws IOException, InterruptedException;
	}

	/**
	 * Perform write operations to the {@link gitolite.keystore.KeyStore} within a {@code WriteLock}.
	 * Applies the changes to the repository when done.
	 * @param configInteraction function
	 * @see ManagedConfig#applyChanges()
	 */
	public void writeKeyStore(ThrowingConsumer<? super KeyStore> configInteraction) {
		readWriteLock.writeLock().lock();
		try {
			configInteraction.accept(keyStore);
			applyChanges();
		} catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
			readWriteLock.writeLock().unlock();
		}
	}

}
