package parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {
	public static void main(String[] args) {
		System.out.println("Enter an expression: ");
		Scanner scan = new Scanner(System.in);
		
		Pattern pattern = Pattern.compile("([a-zA-Z0-9]+|\\+|\\-|\\*|/|\\(|\\))");
		Matcher m = pattern.matcher(scan.nextLine());
		
		scan.close();
		
		List<Token> tokens = ListHandler.createEmptyList();
		while (m.find()) {
			tokens.add(TerminalSymbol.stringToToken(m.group(0)));
		}
		
		Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);

		if (parseRoot.isPresent()) {
			System.out.println(parseRoot.get().getChildren());
			System.out.println(getStringRepresentation(parseRoot.get()));
		} else {
			System.out.println("Invalid expression");
		}
	}
	
	private static class NodeContainer {
		private final Node node;
		private final int width;
		
		public NodeContainer(Node node) {
			this.width = -1;
			this.node = node;
		}
		
		public NodeContainer(int width) {
			this.width = width;
			this.node = null;
		}
		
		public int getWidth() {
			return node == null ? width : node.getWidth();
		}
		
		private static String repeatedSpaces(int numSpaces) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < numSpaces; i++) {
				builder.append(" ");
			}
			return builder.toString();
		}
		
		public String toString() {
			if (node == null) {
				return repeatedSpaces(width);
			} else if (node instanceof InternalNode) {
				return "[" + repeatedSpaces(node.getWidth() - 2) + "]";
			} else {
				return node.toString();
			}
		}
	}

	/**
	 * Method that prints textual representation of the corresponding tree
	 * @param input of a tree that is to be printed
	 * @return pretty string representation of the tree 
	 */
	private static String getStringRepresentation(Node root) {
		StringBuilder representation = new StringBuilder();
		
		Queue<NodeContainer> currentQueue = new LinkedList<>();
		currentQueue.add(new NodeContainer(root));
		
		boolean moreChildren = true;
		while (moreChildren) {
			moreChildren = false;
			Queue<NodeContainer> nextQueue = new LinkedList<>();
			
			while (!currentQueue.isEmpty()) {
				NodeContainer curNode = currentQueue.remove();
				if (curNode.node instanceof InternalNode) {
					moreChildren = true;
					representation.append(curNode);
					
					nextQueue.add(new NodeContainer(1));
					for (Node child : curNode.node.getChildren()) {
						nextQueue.add(new NodeContainer(child));
					}
					nextQueue.add(new NodeContainer(1));
				} else {
					nextQueue.add(new NodeContainer(curNode.getWidth()));
					representation.append(curNode);
				}
			}
			representation.append("\n");
			currentQueue = nextQueue;
		}
		
		return representation.toString();
	}
}
