/**
 * FullDirectoryException is thrown when there is an attempt to add a
 * DirectoryNode to a full DirectoryTree
 * 
 * @author Sean Erfan
 *
 */
public class FullDirectoryException extends Exception {
	/**
	 * @param msg The message to be sent when exception is called
	 */
	public FullDirectoryException(String msg) {
		super(msg);
	}
}
