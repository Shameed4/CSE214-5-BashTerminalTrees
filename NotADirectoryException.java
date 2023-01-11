/**
 * NotADirectoryException is thrown when there is an attempt to perform an
 * operation on a file that can only be done on a folder
 * 
 * @author Sean Erfan
 *
 */
public class NotADirectoryException extends Exception {
	/**
	 * @param msg The message to be sent when exception is called
	 */
	public NotADirectoryException(String msg) {
		super(msg);
	}
}
