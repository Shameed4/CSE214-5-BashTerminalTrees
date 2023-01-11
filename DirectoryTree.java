/**
 * The DirectoryTree is a tree of DirectoryNodes, containing files and folders.
 * 
 * @author Sean Erfan
 *
 */
public class DirectoryTree {
	private DirectoryNode root;
	private DirectoryNode cursor;
	private DirectoryNode[] cursorLocation;

	/**
	 * Instantiates a DirectoryTree and initializes its variables
	 */
	public DirectoryTree() {
		root = new DirectoryNode("root", false);
		cursor = root;
		cursorLocation = new DirectoryNode[1];
		cursorLocation[0] = root;
	}

	/**
	 * Sets cursor to the root
	 */
	public void resetCursor() {
		cursor = root;
		cursorLocation = new DirectoryNode[1];
		cursorLocation[0] = cursor;
	}

	/**
	 * Sets the cursor to the directory
	 * 
	 * @param name The name of the directory to change the cursor to
	 * @throws NotADirectoryException If there is no directory with the given
	 *                                name
	 */
	public void changeDirectory(String name) throws NotADirectoryException {
		if (name.indexOf("/") != -1) {
			DirectoryNode[] tempLocation = pathToNode(name);
			if (tempLocation == null)
				throw new NotADirectoryException(
				        "ERROR: No such directory named '" + name + "'.a");
			else if (tempLocation[tempLocation.length - 1].isFile())
				throw new NotADirectoryException(
				        "ERROR: Cannot change directory into a file.");
			cursor = tempLocation[tempLocation.length - 1];
			cursorLocation = tempLocation;
			return;
		}
		DirectoryNode tempNode = cursor.search(name);
		if (tempNode == null)
			throw new NotADirectoryException(
			        "ERROR: No such directory named '" + name + "''.");
		if (tempNode.isFile())
			throw new NotADirectoryException(
			        "ERROR: Cannot change directory into a file.");
		else {
			DirectoryNode[] tempLocation = new DirectoryNode[cursorLocation.length
			        + 1];
			for (int i = 0; i < cursorLocation.length; i++) {
				tempLocation[i] = cursorLocation[i];
			}
			cursor = tempNode;
			tempLocation[cursorLocation.length] = tempNode;
			cursorLocation = tempLocation;
		}
	}

	/**
	 * Moves a DirectoryNode to a different directory
	 * 
	 * @param srcPath  The path of the node to move
	 * @param destPath The path to move the node to
	 * @throws NotADirectoryException   When srcPath or destPath does not exist
	 *                                  or destPath is a file
	 * @throws IllegalArgumentException When srcPath is root
	 * @throws FullDirectoryException   When destPath is full
	 */
	public void move(String srcPath, String destPath)
	        throws NotADirectoryException, IllegalArgumentException,
	        FullDirectoryException {
		DirectoryNode[] src = pathToNode(srcPath);
		if (src == null)
			throw new NotADirectoryException(
			        "ERROR: No such directory named '" + srcPath + "''.");
		if (src.length == 1)
			throw new IllegalArgumentException("ERROR: Cannot remove root.");
		DirectoryNode[] dest = pathToNode(destPath);
		if (dest == null || dest[dest.length - 1].isFile())
			throw new NotADirectoryException(
			        "ERROR: No such directory named '" + srcPath + "''.");
		dest[dest.length - 1].addChild(src[src.length - 1]);
		src[src.length - 2].removeChild(src[src.length - 1]);
	}

	/**
	 * Looks through tree and prints out all paths
	 * 
	 * @param name The name of the DirectoryNode to find
	 * @throws NotADirectoryException No DirectoryNode with name found
	 */
	public void find(String name) throws NotADirectoryException {
		root.find(name);
	}

	/**
	 * Returns the path of directory names
	 * 
	 * @return Returns a String containing the path of directory names from the
	 *         root node of the tree to the cursor, with each name separated by
	 *         a forward slash "/".
	 */
	public String presentWorkingDirectory() {
		String s = "";
		for (int i = 0; i < cursorLocation.length - 1; i++) {
			s += cursorLocation[i].getName() + "/";
		}
		return s + cursorLocation[cursorLocation.length - 1].getName();
	}

	/**
	 * @return A String containing a space-separated list of names of all the
	 *         child directories or files of the cursor
	 */
	public String listDirectory() {
		String s = "";
		for (int i = 0; i < DirectoryNode.MAX_CHILDREN; i++) {
			try {
				if (cursor.getNthChild(i) == null)
					return s;
				s += cursor.getNthChild(i).getName() + " ";
			}
			catch (NotADirectoryException e) {
				// this shouldn't happen?
				System.out.println(e.getMessage());
			}
		}
		return s;
	}

	/**
	 * Prints tree in preorder from CURSOR
	 */
	public void printDirectoryTree() {
		cursor.printTree();
	}

	/**
	 * Prints tree in preorder from ROOT
	 */
	public void printEntireTree() {
		root.printTree();
	}

	/**
	 * Adds a directory to cursor
	 * 
	 * @param name The name of the directory (contains "/" or " ")
	 * @throws IllegalArgumentException If name is invalid
	 * @throws FullDirectoryException   If cursor directory is full
	 * @throws NotADirectoryException   If cursor is a file
	 */
	public void makeDirectory(String name) throws IllegalArgumentException,
	        FullDirectoryException, NotADirectoryException {
		if (name.indexOf(" ") != -1 || name.indexOf("/") != -1)
			throw new IllegalArgumentException("ERROR: Illegal name");
		DirectoryNode temp = new DirectoryNode(name, false);
		cursor.addChild(temp);
	}

	/**
	 * Adds a file to cursor
	 * 
	 * @param name The name of the file
	 * @throws IllegalArgumentException If name is invalid (contains "/" or " ")
	 * @throws FullDirectoryException   If cursor directory is full
	 * @throws NotADirectoryException   If cursor is a file
	 */
	public void makeFile(String name) throws IllegalArgumentException,
	        FullDirectoryException, NotADirectoryException {
		if (name.indexOf(" ") != -1 || name.indexOf("/") != -1)
			throw new IllegalArgumentException("ERROR: Illegal name");
		DirectoryNode temp = new DirectoryNode(name, true);
		cursor.addChild(temp);
	}

	/**
	 * Moves cursor to its parent
	 * 
	 * @throws IndexOutOfBoundsException If cursor has no parent (cursor==root)
	 */
	public void changeDirectoryToParent() throws IndexOutOfBoundsException {
		if (cursorLocation.length < 2)
			throw new IndexOutOfBoundsException(
			        "ERROR: Already at root directory.");
		DirectoryNode[] temp = new DirectoryNode[cursorLocation.length - 1];
		for (int i = 0; i < cursorLocation.length - 1; i++) {
			temp[i] = cursorLocation[i];
		}
		cursorLocation = temp;
		cursor = temp[temp.length - 1];
	}

	/**
	 * Follows the path and returns the DirectoryNode that is found. If there is
	 * no DirectoryNode with that path, returns null
	 * 
	 * @param path The path to follow
	 * @return The DirectoryNode found with the path, or null if none is found.
	 */
	public DirectoryNode[] pathToNode(String path) {
		return cursor.followPath(path, 0);
	}
}
