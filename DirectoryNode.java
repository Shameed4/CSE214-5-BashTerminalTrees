/**
 * The DirectoryNode represents a Node in the file tree.
 * 
 * @author Sean Erfan
 */
public class DirectoryNode {
	public static final int MAX_CHILDREN = 10;

	private String name;
	private boolean isFile;
	private DirectoryNode[] children;
	private int childCount = 0;

	/**
	 * Instantiates a DirectoryNode and instantiates its variables
	 * 
	 * @param name   The name of the node
	 * @param isFile If Node is a file (as opposed to a directory)
	 */
	public DirectoryNode(String name, boolean isFile) {
		this.setName(name);
		this.isFile = isFile;
		if (!isFile)
			children = new DirectoryNode[MAX_CHILDREN];
		childCount = 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isFile
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * @param isFile the isFile to set
	 */
	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}

	/**
	 * Returns the child of the directory of index n
	 * 
	 * @param n The nth child of the directory
	 * @return A child node
	 * @throws NotADirectoryException If this is a file node
	 */
	public DirectoryNode getNthChild(int n) throws NotADirectoryException {
		if (isFile)
			throw new NotADirectoryException("Cannot get child of a file.");
		return children[n];
	}

	/**
	 * Adds a child to the directory
	 * 
	 * @param newChild The child node that will be added
	 * @throws FullDirectoryException If there is no room left to add a child.
	 * @throws NotADirectoryException If this is a file node
	 */
	public void addChild(DirectoryNode newChild)
	        throws FullDirectoryException, NotADirectoryException {
		if (isFile)
			throw new NotADirectoryException("ERROR: Cannot add file to file.");
		if (childCount == children.length)
			throw new FullDirectoryException(
			        "ERROR: Present directory is full.");
		children[childCount++] = newChild;
	}

	/**
	 * Removes a child from the directory
	 * @param remove The child node that will be removed
	 */
	public void removeChild(DirectoryNode remove) {
		int i = 0;
		while (children[i] != remove)
			i++;
		for (int j = i; j < childCount - 1; j++) {
			children[j] = children[j + 1];
		}
		children[childCount - 1] = null;
		childCount--;
	}
	
	/**
	 * Finds the direct child with the the targetName
	 * @return The child with the given name
	 * @throws NotADirectoryException No child has this name
	 */
	public DirectoryNode search(String targetName)
	        throws NotADirectoryException {
		for (int i = 0; i < childCount; i++) {
			if (children[i].name.equals(targetName))
				return children[i];
		}
		throw new NotADirectoryException(
		        "ERROR: No such directory named '" + targetName + "'.");
	}

	/**
	 * Prints all paths that lead to a DirectoryNode with the given name,
	 * starting at this node
	 * 
	 * @param targetName The node name to find
	 */
	public void find(String targetName) throws NotADirectoryException {
		if (find(targetName, "", false))
			System.out.println();
		else
			throw new NotADirectoryException(
			        "ERROR: No such directory named '" + targetName + "'.");
	}

	/**
	 * Prints all paths that lead to a DirectoryNode with the given name,
	 * starting at this node
	 * 
	 * @param targetName  The node name to find
	 * @param path        The path to this node
	 * @param targetFound If target was found yet
	 * @return whether or not target was ever found
	 */
	public boolean find(String targetName, String path, boolean targetFound) {
		path += name;
		if (name.equals(targetName)) {
			System.out.println(path);
			targetFound = true;
		}
		if (isFile)
			return targetFound;
		for (int i = 0; i < childCount; i++) {
			targetFound = children[i].find(targetName, path + "/", targetFound)
			        || targetFound;
		}
		return targetFound;
	}

	/**
	 * Prints all elements of the tree in a hierarchical manner
	 */
	public void printTree() {
		printTree(0);
	}

	/**
	 * Prints all elements of the tree in a hierarchical manner
	 * 
	 * @param numSpaces Number of spaces to print before the given element.
	 */
	public void printTree(int numSpaces) {
		if (isFile) {
			System.out.println(" ".repeat(numSpaces) + "- " + name);
		}
		else {
			System.out.println(" ".repeat(numSpaces) + "|- " + name);
			for (int i = 0; i < childCount; i++) {
				children[i].printTree(numSpaces + 4);
			}
		}
	}

	/**
	 * Recursively follows a path to get a DirectoryNode
	 * 
	 * @param path The path to follow
	 * @return The DirectoryNode that the path leads to, or null if the path
	 *         doesn't lead anywhere
	 */
	public DirectoryNode[] followPath(String path, int searchCount) {
		if (name.equals(path)) {
			DirectoryNode[] ret = new DirectoryNode[searchCount + 1];
			ret[searchCount] = this;
			return ret;
		}

		if (path.indexOf("/") == -1)
			return null;

		if (name.equals(path.substring(0, path.indexOf("/")))) {
			for (int i = 0; i < childCount; i++) {
				DirectoryNode[] ret = children[i].followPath(
				        path.substring(path.indexOf("/") + 1), searchCount + 1);
				if (ret != null) {
					ret[searchCount] = this;
					return ret;
				}
			}
		}
		return null;
	}
}