package parser;

import java.util.*;

/**
 * Java class NonTerminalSymbol
 * Represents a non-terminal symbol in an expression
 * 
 * @author Fangze Liu, Jacob Goldberg, Anna Sedlackova
 * @version 1.00, 31 Jan 2019
 */

public enum NonTerminalSymbol implements Symbol{
	EXPRESSION, EXPRESSION_TAIL, TERM, TERM_TAIL, UNARY, FACTOR;
	
	// Creating a hash table to represent productions
    private static final Map<NonTerminalSymbol, Map<TerminalSymbol, SymbolSequence>> production = new HashMap<>();
    
    private static Map<TerminalSymbol, SymbolSequence> getProductionTable(NonTerminalSymbol lookupSymbol) {
    	return production.get(lookupSymbol);
    }
    
    //hash table to map string to terminal symbol
    private static Map<String, TerminalSymbol> mapSymbol= new HashMap<>();
    
    /**
     * Add a production
     * @param lookupSymbol The type of NonTerminalSymbol to which this is applied
     * @param lookAhead The first TerminalSymbol to expect
     * @param symbols The sequence of symbols for this production
     */
    private static void addProduction(NonTerminalSymbol lookupSymbol, TerminalSymbol lookAhead, SymbolSequence symbols) {
    	production.putIfAbsent(lookupSymbol, new HashMap<>());
    	getProductionTable(lookupSymbol).put(lookAhead, symbols);
    }
    
    /**
     * Add a production that also includes the lookAhead
     * @param lookupSymbol The type of NonTerminalSymbol to which this is applied
     * @param lookAhead The first TerminalSymbol to expect
     * @param symbols All symbols to be expected after lookAhead
     */
    private static void addProduction(NonTerminalSymbol lookupSymbol, TerminalSymbol lookAhead, List<Symbol> symbols) {
    	addProduction(lookupSymbol, lookAhead, makeSymbolSequence(lookAhead, symbols));
    }
    
    /**
     * Make a symbol sequence from a symbol and a list of symbols
     * @param firstSymbol The first symbol to go into the list
     * @param remainder The remaining symbols to be added
     * @return Returns a SymbolSequence containing a list with the first symbol followed by symbols in remainder
     */
    private static SymbolSequence makeSymbolSequence(TerminalSymbol firstSymbol, List<Symbol> remainder) {
    	List<Symbol> symbols = ListHandler.shallowCopy(remainder);
    	symbols.add(0, firstSymbol);
    	return SymbolSequence.build(symbols);
    }
	
	static {
		//Each production has a list of symbol sequences that are inserted into hash table
		final SymbolSequence expressionSequence = SymbolSequence.build(TERM, EXPRESSION_TAIL);
	    addProduction(EXPRESSION, TerminalSymbol.VARIABLE, expressionSequence);
	    addProduction(EXPRESSION, TerminalSymbol.MINUS, expressionSequence);
	    addProduction(EXPRESSION, TerminalSymbol.OPEN, expressionSequence);
	    addProduction(EXPRESSION, null, SymbolSequence.EPSILON);
	
	    final List<Symbol> expressionTailList = Arrays.asList(TERM, EXPRESSION_TAIL);
	    addProduction(EXPRESSION_TAIL, TerminalSymbol.PLUS, expressionTailList);
	    addProduction(EXPRESSION_TAIL, TerminalSymbol.MINUS, expressionTailList);
	    addProduction(EXPRESSION_TAIL, TerminalSymbol.CLOSE, SymbolSequence.EPSILON);
	    addProduction(EXPRESSION_TAIL, null, SymbolSequence.EPSILON);
	    
	    final SymbolSequence termSequence = SymbolSequence.build(UNARY, TERM_TAIL);
	    addProduction(TERM, TerminalSymbol.VARIABLE, termSequence);
	    addProduction(TERM, TerminalSymbol.MINUS, termSequence);
	    addProduction(TERM, TerminalSymbol.OPEN, termSequence);
	    
	    final List<Symbol> termTailList = Arrays.asList(UNARY, TERM_TAIL);
	    addProduction(TERM_TAIL, TerminalSymbol.TIMES, termTailList);
	    addProduction(TERM_TAIL, TerminalSymbol.DIVIDE, termTailList);
	    addProduction(TERM_TAIL, TerminalSymbol.CLOSE, SymbolSequence.EPSILON);
	    addProduction(TERM_TAIL, TerminalSymbol.PLUS, SymbolSequence.EPSILON);
	    addProduction(TERM_TAIL, TerminalSymbol.MINUS, SymbolSequence.EPSILON);
	    addProduction(TERM_TAIL, null, SymbolSequence.EPSILON);
	    
	    final SymbolSequence unarySequence = SymbolSequence.build(FACTOR);
	    addProduction(UNARY, TerminalSymbol.MINUS, SymbolSequence.build(TerminalSymbol.MINUS, FACTOR));
	    addProduction(UNARY, TerminalSymbol.OPEN, unarySequence);
	    addProduction(UNARY, TerminalSymbol.VARIABLE, unarySequence);
	    
	    addProduction(FACTOR, TerminalSymbol.OPEN, Arrays.asList(EXPRESSION, TerminalSymbol.CLOSE));
	    addProduction(FACTOR, TerminalSymbol.VARIABLE, ListHandler.createEmptyList());
	    
	    mapSymbol.put("+", TerminalSymbol.PLUS);
	    mapSymbol.put("-", TerminalSymbol.MINUS);
	    mapSymbol.put("*", TerminalSymbol.TIMES);
	    mapSymbol.put("/", TerminalSymbol.DIVIDE);
	    mapSymbol.put("(", TerminalSymbol.OPEN);
	    mapSymbol.put(")", TerminalSymbol.CLOSE);   
	}
	
	/**
	 * Attempts to parse the input with an EXPRESSION, and returns the  root  node 
	 * if  the  parsing  process  is  successful  and  has  not  remainder,  and  an  empty  Optional otherwise.
	 * @param input a list of tokens
	 * @return a ParseState corresponding to the parsing result
	 */
	static final Optional<Node> parseInput(List<Token> input) {
		Objects.requireNonNull(input, "Input provided to NonTerminalSymbol::parseInput must not be null");
		
		ParseState state = EXPRESSION.parse(input);
		if (!state.getSuccess() || !state.hasNoRemainder()) {
			return Optional.empty();
		} else {
			return Optional.of(state.getNode());
		}
	}
	
	/**
	 * Non-terminal parses its input by going through its productions
	 *  in the order given by the table and attempting to match them to the input.
	 * @param input a list of tokens
	 * @return a ParseState corresponding to the parsing result
	 */
	@Override
	public ParseState parse(List<Token> input) {
		Objects.requireNonNull(input, "Input to NonTerminalSymbol::parse() cannot be null");
		
		TerminalSymbol lookAhead = input.isEmpty() ? null : ListHandler.listHead(input).getType();
		if (getProductionTable(this).containsKey(lookAhead)) {
			return getProductionTable(this).get(lookAhead).match(input);
		} else {
			return ParseState.FAILURE;
		}
	}
}
