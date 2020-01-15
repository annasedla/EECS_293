package typecheck;

import parser.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import parser.Connector;
import parser.TerminalSymbol;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeSynthesisTest {

    //types
    private Type cat;
    private Type dog;
    private Type mouse;
    private Type emptyType;

    //connectors
    private Connector plus;
    private Connector minus;
    private Connector times;
    private Connector emptyConnector;
    private Connector divide;
    private Connector opened;
    private Connector closed;

    //expressions
    private Expression exp;
    private Expression emptyExp;
    private Expression wrongTypeExp;

    //maps
    private ExpressionTypes et;
    private VariableTypes vt;

    //variables
    private Variable c;
    private Variable d;
    private Variable a;

    //tree
    private LeafNode node;
    private List<Token> tokens;
    private Optional<Node> root;

    //type synthesis
    private TypeSynthesis ts;
    private TypeSynthesis.TestHook th;

    @Before
    public void setUp() {

        //types
        cat = new Type("cat");
        mouse = new Type("mouse");
        dog = new Type("dog");
        emptyType = Type.EMPTY;

        //connectors
        plus = Connector.build(TerminalSymbol.PLUS);
        minus = Connector.build(TerminalSymbol.MINUS);
        times = Connector.build(TerminalSymbol.TIMES);
        divide = Connector.build(TerminalSymbol.DIVIDE);
        opened = Connector.build(TerminalSymbol.OPEN);
        closed = Connector.build(TerminalSymbol.CLOSE);
        emptyConnector = Connector.empty;

        //expressions
        exp = new Expression(cat, dog, plus);
        emptyExp = new Expression(emptyType, emptyType, emptyConnector);
        wrongTypeExp = new Expression(dog, cat, minus);

        //variables
        c = Variable.build("c");
        d = Variable.build("d");
        a = Variable.build("a");

        //maps
        et = new ExpressionTypes();
        vt = new VariableTypes();
        vt.addVariableType(c, cat);
        vt.addVariableType(d, dog);
        vt.addVariableType(a, mouse);
        et.addRule(new Expression(cat, dog, times), cat);
        et.addRule(new Expression(dog, cat, times), cat);
        et.addRule(new Expression(dog, cat, times), cat);
        et.addRule(new Expression(cat, cat, plus), cat);
        et.addRule(new Expression(cat, cat, times), cat);
        et.addRule(new Expression(dog, cat, plus), cat);
        et.addRule(new Expression(cat, dog, minus), cat);
        et.addRule(new Expression(mouse, cat, divide), dog);
        et.addRule(new Expression(cat, mouse, divide), mouse);
        et.addRule(new Expression(mouse, dog, divide), dog);
        et.addRule(new Expression(dog, mouse, divide), dog);
        et.addRule(new Expression(mouse, cat, times), mouse);
        et.addRule(new Expression(dog, mouse, divide), dog);
        et.addRule(new Expression(cat, mouse, plus), mouse);
        et.addRule(new Expression(mouse, cat, plus), mouse);
        et.addRule(exp, dog);

        //tree
        tokens = ListHandler.createEmptyList();

        root = NonTerminalSymbol.parseInput(tokens);
        node = LeafNode.build(c);

        ts = new TypeSynthesis(root, vt, et);
        //type synthesis
        th = ts.new TestHook();
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testIsParenthesisValid() {
        Token[] myStringArray = {
                Connector.build(TerminalSymbol.OPEN),
                Variable.build("d"),
                Connector.build(TerminalSymbol.CLOSE)
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.areParenthesisValid(parseRoot.get()));
    }

    /**
     * Structured basis: Internalnode inside parenthesis
     */
    @Test
    public void testIsParenthesisValidInternalNode() {
        Token[] myStringArray = {
                opened,
                Variable.build("a"),
                plus,
                Variable.build("b"),
                closed
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.areParenthesisValid(parseRoot.get()));
    }

    @Test
    public void testIsParenthesisValidInvalidParenthesis() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(opened));
        ib.addChild(LeafNode.build(Variable.build("a")));
        Node n = ib.build();
        assertFalse(TypeUtilities.areParenthesisValid(n));
    }

    /**
     * Bad data: test with too many closed paranthesis
     */
    @Test
    public void testIsParenthesisValidTooManyClosed() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(opened));
        ib.addChild(LeafNode.build(Variable.build("a")));
        ib.addChild(LeafNode.build(closed));
        ib.addChild(LeafNode.build(closed));
        Node n = ib.build();
        assertFalse(TypeUtilities.areParenthesisValid(n));
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testIsUnaryOpTreeValid() {
        Token[] myStringArray = {
                minus,
                Variable.build("b"),
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isUnaryOpTreeValid(parseRoot.get()));
    }

    /**
     * Structured basis: InternalNode
     */
    @Test
    public void testIsUnaryOpTreeValidInternalNode() {
        Token[] myStringArray = {
                minus,
                opened,
                Variable.build("a"),
                plus,
                Variable.build("b"),
                closed
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isUnaryOpTreeValid(parseRoot.get()));
    }

    @Test
    public void testIsUnaryOpTreeValidInvalidSize() {
        Token[] myStringArray = {
                Variable.build("a"),
                plus,
                Variable.build("b"),
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertFalse(TypeUtilities.isUnaryOpTreeValid(parseRoot.get()));
    }

    @Test
    public void testIsUnaryOpTreeValidInvalidOp() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(minus));
        ib.addChild(LeafNode.build(plus));
        Node n = ib.build();
        assertFalse(TypeUtilities.isUnaryOpTreeValid(n));
    }

    /**
     * Structured basis: child.isOperator equals doesAlternate
     */
    @Test
    public void testIsAlternatingBreaksAlternating() {
        Node child = LeafNode.build(plus);
        assertFalse(TypeUtilities.isAlternating(true, child));
    }

    @Test
    public void testIsAlternatingLeafNode() {
        Node child = LeafNode.build(Variable.build("a"));
        assertTrue(TypeUtilities.isAlternating(true, child));
    }

    @Test
    public void testIsAlternatingInternalNode() {
        Token[] myStringArray = {
                Variable.build("a"),
                plus,
                Variable.build("b"),
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isAlternating(true, parseRoot.get()));
    }

    @Test
    public void testIsAlternatingBadInternalNode() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(plus));
        ib.addChild(LeafNode.build(minus));
        Node n = ib.build();
        assertFalse(TypeUtilities.isAlternating(true, n));
    }


    @Test
    public void testIsBinaryOpTreeValidNominal() {
        Token[] myStringArray = {
                opened,
                Variable.build("a"),
                plus,
                Variable.build("b"),
                closed
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isBinaryOpTreeValid(parseRoot.get()));
    }

    @Test
    public void testIsBinaryOpTreeValidNotAlternate() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(Variable.build("a")));
        ib.addChild(LeafNode.build(Variable.build("b")));
        Node n = ib.build();
        assertFalse(TypeUtilities.isBinaryOpTreeValid(n));
    }

    @Test
    public void testIsBinaryOpTreeEndsInOp() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(Variable.build("a")));
        ib.addChild(LeafNode.build(plus));
        ib.addChild(LeafNode.build(Variable.build("b")));
        ib.addChild(LeafNode.build(plus));
        Node n = ib.build();
        assertFalse(TypeUtilities.isBinaryOpTreeValid(n));
    }

    @Test
    public void testIsTreeValidParenthesisInvalid() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(opened));
        ib.addChild(LeafNode.build(Variable.build("a")));
        Node n = ib.build();
        assertFalse(TypeUtilities.isTreeValid(n));
    }

    @Test
    public void testIsTreeValidStartedByOperator() {
        Token[] myStringArray = {
                minus,
                Variable.build("a"),
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isTreeValid(parseRoot.get()));
    }

    @Test
    public void testIsTreeValidStartedByNode() {
        Token[] myStringArray = {
                Variable.build("a"),
                minus,
                Variable.build("b"),
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertTrue(TypeUtilities.isTreeValid(parseRoot.get()));
    }

    @Test(expected=NullPointerException.class)
    public void testEvaluateRootTypeNullInput() {
        TypeSynthesis ts = new TypeSynthesis(root, null, et);
        //assertEquals(Type.UNDEFINED, ts.evaluateRootType());
    }

    @Test(expected=UndeterminableTypeException.class)
    public void testEvaluateRootTypeInvalidTree() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(Variable.build("a")));
        ib.addChild(LeafNode.build(Variable.build("b")));
        Node n = ib.build();
        TypeSynthesis ts = new TypeSynthesis(Optional.of(n), vt, et);
        ts.evaluateRootType();
    }

    @Test
    public void testEvaluateRootTypeNominal() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(Variable.build("d")));
        ib.addChild(LeafNode.build(plus));
        ib.addChild(LeafNode.build(Variable.build("c")));
        Node n = ib.build();
        TypeSynthesis ts = new TypeSynthesis(Optional.of(n), vt, et);
        assertEquals(cat, ts.evaluateRootType());
    }

    @Test
    public void testEvaluateRootTypeBad() {
        InternalNode.Builder ib = new InternalNode.Builder();
        ib.addChild(LeafNode.build(Variable.build("d")));
        ib.addChild(LeafNode.build(plus));
        ib.addChild(LeafNode.build(Variable.build("c")));
        Node n = ib.build();
        TypeSynthesis ts = new TypeSynthesis(Optional.of(n), vt, et);
        ts.evaluateRootType();
    }

    /**
     * Structured basis: nominal case
     */
    @Test
    public void testEvalExpressionAndSetLeft() {
       Expression t1 = th.evalExpressionAndSetLeft(exp);
       Expression t2 = new Expression (dog, emptyType, emptyConnector);
       assertTrue(t1.equals(t2));
    }

     //Branch coverage
     //Empty types: expression remains the same
    @Test
    public void testEvalExpressionAndSetLeftEmpty(){
        Expression t1 = th.evalExpressionAndSetLeft(emptyExp);
        assertTrue(t1.equals(emptyExp));
    }

    //Branch coverage
    //Nonexistent conversion rule
    @Test(expected=UndeterminableTypeException.class)
    public void testEvalExpressionAndSetLeftNoRules(){
        Expression t1 = th.evalExpressionAndSetLeft(wrongTypeExp);
        //Expression t2 = new Expression (Type.UNDEFINED, emptyType, emptyConnector);
        //assertTrue(t1.equals(t2));
    }

    /**
     * Structured basis: nominal case
     * expression has an empty connector
     */
    @Test
    public void testAddTypeToExpression(){
        Expression t1 = TypeUtilities.addTypeToExpression(dog, emptyExp);
        Expression t2 = new Expression (dog, emptyType, emptyConnector);
        assertTrue(t1.equals(t2));
    }

    //Branch coverage
    //connector is not empty
    @Test
    public void testAddTypeToExpressionConnectorNonEmpty(){
        Expression t1 = TypeUtilities.addTypeToExpression(dog, wrongTypeExp);
        Expression t2 = new Expression (dog, dog, minus);
        assertTrue(t1.equals(t2));
    }

    /**
     * Structured basis: nominal case
     * Good data: a
     */
    @Test
    public void testAddChildToExpression(){
        Token[] myStringArray = {
                Variable.build("a")};
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertEquals(th.addChildToExpression(parseRoot.get(), emptyExp), emptyExp);
    }

    //Good data: a + b
    @Test
    public void testAddChildToExpression2(){
        Token[] myStringArray = {
                Variable.build("c"),
                plus,
                Variable.build("d")
        };
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertEquals(th.addChildToExpression(parseRoot.get(), emptyExp), emptyExp);
    }

    /**
     * Structured basis: nominal case
     * parenthesis not present
     */
    @Test
    public void testIsParenthesisFalse(){
        assertFalse(TypeUtilities.isParenthesis(node));
    }

    //left parenthesis is present
    @Test
    public void testIsParenthesisLeftTrue(){
        LeafNode nodeParenthesis = LeafNode.build(Connector.build(TerminalSymbol.CLOSE));
        assertTrue(TypeUtilities.isParenthesis(nodeParenthesis));
    }

    //right parenthesis is present
    @Test
    public void testIsParenthesisRightTrue(){
        LeafNode nodeParenthesis = LeafNode.build(Connector.build(TerminalSymbol.OPEN));
        assertTrue(TypeUtilities.isParenthesis(nodeParenthesis));
    }


    /**
     * Structured basis: nominal case
     * Good data: no undefineds, no errors
     */
    @Test
    public void testTraverseTree(){

        Token[] myStringArray = {
                Variable.build("c"),
                plus,
                Variable.build("d")};
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertEquals(th.traverseTree(parseRoot.get()), dog);
    }

    //Good data: multiple expressions
    @Test
    public void testTraverseTreeGoodData(){

        Token[] myStringArray = {
                Variable.build("c"),
                times,
                Variable.build("d"),
                plus,
                Variable.build("c")};
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        assertEquals(th.traverseTree(parseRoot.get()),cat);
    }

    //Bad data: rule is missing
    @Test(expected=UndeterminableTypeException.class)
    public void testTraverseTreeNoRule(){

        Token[] myStringArray = {
                Variable.build("c"),
                divide,
                Variable.build("d")};
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        th.traverseTree(parseRoot.get());
    }

    //Bad data: variable is wrong
    @Test(expected=UndeterminableTypeException.class)
    public void testTraverseTreeWrongVariable(){
        Token[] myStringArray = {
                Variable.build("a"),
                divide,
                Variable.build("b")};
        tokens = Arrays.asList(myStringArray);
        Optional<Node> parseRoot = NonTerminalSymbol.parseInput(tokens);
        th.traverseTree(parseRoot.get());
    }

    /**
     * STRESS TEST
     */
    @Test
    public void testStressTest(){
        int j = 100000;
        List<Token> tokens = new ArrayList<>();
        InternalNode.Builder ib = new InternalNode.Builder();
        for (int i = 0; i < j; i++){
            InternalNode.Builder ib1 = new InternalNode.Builder();
            ib1.addChild(LeafNode.build(opened));

            InternalNode.Builder ib2 = new InternalNode.Builder();
            ib2.addChild(LeafNode.build(Variable.build("c")));
            ib2.addChild(LeafNode.build(times));
            ib2.addChild(LeafNode.build(Variable.build("d")));
            ib1.addChild(ib2.build());

            ib1.addChild(LeafNode.build(closed));

            ib.addChild(ib1.build());

            ib.addChild(LeafNode.build(times));

        }
        ib.addChild(LeafNode.build(Variable.build("d")));
        Node n = ib.build();
        Optional<Node> parseRoot = Optional.of(n);
        assertEquals(th.traverseTree(parseRoot.get()), cat);
    }
}
