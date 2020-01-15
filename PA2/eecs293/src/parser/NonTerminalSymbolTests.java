package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * java class NonTerminalSymbolTests
 * The set of unit tests for the NonTerminalSymbol class
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 *
 */
class NonTerminalSymbolTests {
	//test that a parse is valid
	private void testValidParse(Optional<Node> node) {
		//test that the parse returns a node
		assertTrue(node.isPresent());
		
		//test that the parse has no empty lists
		assertEquals(node.get().toString().indexOf("[]"), -1);
	}
	
	// remove "optional" and outside most pair of brackets
	private String testSimplifiedParseTree(String input) {
		return input.substring(9, input.length() - 1);
	}
	
	//test that the parse methods reject null input
	@Test
	void testRejectsNull() {
		assertThrows(NullPointerException.class, () -> {
			NonTerminalSymbol.parseInput(null);
		});
		
		assertThrows(NullPointerException.class, () -> {
			NonTerminalSymbol.EXPRESSION.parse(null);
		});
	}
	
	//test that this can successfully handle a single variable
	@Test
	void testSuccessfulParseStateOneItem() {
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("a")
				)));
	}	
	
	//test that this can handle a list containing multiple items
	@Test
	void testSuccessfulParseMultipleItems() {
		List<Token> testList = Arrays.asList(
						Variable.build("a"), 
						Connector.build(TerminalSymbol.PLUS), 
						Variable.build("b"), 
						Connector.build(TerminalSymbol.DIVIDE),
						Variable.build("c")
						);
		
		testValidParse(NonTerminalSymbol.parseInput(testList));
		assertEquals(testSimplifiedParseTree(NonTerminalSymbol.parseInput(testList).toString()), "[a,+,[b,/,c]]");

	}
	
	//ensure an expression that STARTS with a valid expression but has a remainder fails
	@Test
	void testRejectsRemainder() {		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS)
		)).isPresent());
	}
	
	//test that this accepts a list of tokens with nested parenthesis: (A+(B*C))-D
	@Test
	void testAcceptsNestedParentheses() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D")
		)));
	}
	
	//test that this rejects an expression with an extraneous variable: (A+E(B*C))-D
	@Test
	void testRejectsMissingSymbol() {		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("E"),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D")
		)).isPresent());
	}
	
	@Test
	void testRejectsExtraneousSymbols() {		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("E"),
				Variable.build("E"),
				Connector.build(TerminalSymbol.DIVIDE),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D")
		)).isPresent());
		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("E"),
				Connector.build(TerminalSymbol.DIVIDE),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D"),
				Connector.build(TerminalSymbol.PLUS)
		)).isPresent());
		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D"),
				Variable.build("E")
		)).isPresent());
		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D"),
				Connector.build(TerminalSymbol.CLOSE)
		)).isPresent());
	}
	
	//test that this rejects an expression that is correct other than a missing closing parenthesis: (A+(B*C)-D
	@Test
	void testRejectsMissingCloseParenthesis() {		
		assertFalse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("C"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D")
		)).isPresent());
	}
	
	//test for long expressions with all cases (A+B*(C-D)/E) + F - (G*H/I)
	@Test
	void testLongExpressions() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("B"),
				Connector.build(TerminalSymbol.TIMES),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("C"),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("D"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("E"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("F"),
				Connector.build(TerminalSymbol.MINUS),
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("G"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("H"),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("I"),
				Connector.build(TerminalSymbol.CLOSE),
				Connector.build(TerminalSymbol.CLOSE)
		)));
	}
	
	//test for random expression A/B/C/D + E*F*G
	@Test
	void testRandomExpression() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("B"),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("C"),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("D"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("E"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("F"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("G")
		)));
	}
	
	
	//test for expression A + B + C
	@Test
	void testExpression() {
		List<Token> testList = Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("B"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("C")
		);
		
		testValidParse(NonTerminalSymbol.parseInput(testList));
		assertEquals(testSimplifiedParseTree(NonTerminalSymbol.parseInput(testList).toString()), "[A,+,B,+,C]");
	}
	
	//test for expression tail A + B 
	@Test
	void testExpressionTail() {
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.PLUS),
				Variable.build("B")
		)));
	}
	
	//test for expression tail A - B 
	@Test
	void testExpressionTail2() {
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.MINUS),
				Variable.build("B")
		)));
	}
	
	//test for term tail A * B
	@Test
	void testTermTail() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.TIMES),
				Variable.build("B")
		)));
	}
	
	//test for term tail A / B
	@Test
	void testTermTail2() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Variable.build("A"),
				Connector.build(TerminalSymbol.DIVIDE),
				Variable.build("B")
		)));
	}
	
	//test for factor (A)
	@Test
	void factor() {		
		testValidParse(NonTerminalSymbol.parseInput(Arrays.asList(
				Connector.build(TerminalSymbol.OPEN),
				Variable.build("A"),
				Connector.build(TerminalSymbol.CLOSE)
		)));
	}
}
