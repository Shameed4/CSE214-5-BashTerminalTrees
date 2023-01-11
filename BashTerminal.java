/**
 * The BashTerminal class allows users to create and interact with files and directories
 * 
 * @author Sean Erfan
 */
import java.util.Scanner;

public class BashTerminal {
	/**
	 * Prompts input and calls other methods
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		DirectoryTree t = new DirectoryTree();
		System.out.println("Starting terminal...");
		System.out.print("[erf@host]: $ ");
		String input = s.nextLine().trim(); // replace s2 with s later
		// System.out.println(input + " test");
		String firstWord;
		while (!input.equals("exit")) {
			if (input.indexOf(" ") != -1)
				firstWord = input.substring(0, input.indexOf(" "));
			else
				firstWord = input;
			switch (firstWord) {
			case "pwd":
				System.out.println(t.presentWorkingDirectory());
				break;

			case "ls":
				if (input.equals("ls -R"))
					t.printDirectoryTree();
				else
					System.out.println(t.listDirectory());
				break;

			case "cd":
				if (input.equals("cd /"))
					t.resetCursor();
				else if (input.equals("cd ..")) {
					try {
						t.changeDirectoryToParent();
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println(e.getMessage());
					}
				}
				else if (input.indexOf(" ") != -1) {
					String secondWord = input.substring(input.indexOf(" "))
					        .trim();
					try {
						t.changeDirectory(secondWord);
					}
					catch (NotADirectoryException e) {
						System.out.println(e.getMessage());
					}
				}
				break;

			case "mkdir":
				if (input.indexOf(" ") != -1) {
					String secondWord = input.substring(input.indexOf(" "))
					        .trim();
					try {
						t.makeDirectory(secondWord);
					}
					catch (IllegalArgumentException | FullDirectoryException
					        | NotADirectoryException e) {
						System.out.println(e.getMessage());
					}
				}
				break;

			case "touch":
				if (input.indexOf(" ") != -1) {
					String secondWord = input.substring(input.indexOf(" "))
					        .trim();
					try {
						t.makeFile(secondWord);
					}
					catch (IllegalArgumentException | FullDirectoryException
					        | NotADirectoryException e) {
						System.out.println(e.getMessage());
					}
				}
				break;

			case "find":
				if (input.indexOf(" ") != -1) {
					String secondWord = input.substring(input.indexOf(" "))
					        .trim();
					try {
						t.find(secondWord);
					}
					catch (NotADirectoryException e) {
						System.out.println(e.getMessage());
					}
				}
				break;
			case "mv":
				if (input.indexOf(" ") != -1) {
					String secondWord = input.substring(input.indexOf(" "))
					        .trim();
					if (secondWord.indexOf(" ") != -1) {
						String thirdWord = secondWord
						        .substring(secondWord.indexOf(" ")).trim();
						secondWord = secondWord.substring(0,
						        secondWord.indexOf(" "));
						try {
							t.move(secondWord, thirdWord);
						}
						catch (IllegalArgumentException | NotADirectoryException
						        | FullDirectoryException e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
			System.out.print("[erf@host]: $ ");
			input = s.nextLine().trim(); // replace with s later
			// System.out.println(input);
		}
		System.out.println("Program terminating normally");
	}
}
