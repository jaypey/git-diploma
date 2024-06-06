package nl.tudelft.ewi.gitolite.git;

/**
 * @author Jan-Willem Gmelig Meyling
 */
public class GitException extends RuntimeException {

	public GitException() {
	}

	public GitException(Throwable cause) {
		super(cause);
	}

	public GitException(String message, Throwable cause) {
		super(message, cause);
	}
}
