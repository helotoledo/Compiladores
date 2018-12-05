package tpcompiladores;

public class Word extends Token {

    private String lexeme = "";

    public static final Word eq = new Word("=", Tag.EQ);
    public static final Word ne = new Word("<>", Tag.NE);
    public static final Word le = new Word("<=", Tag.LE);
    public static final Word ge = new Word(">=", Tag.GE);
    public static final Word True = new Word("true", Tag.TRUE);
    public static final Word False = new Word("false", Tag.FALSE);
    public static final Word add = new Word("+", Tag.ADD);
    public static final Word mul = new Word("*", Tag.MULT);
    public static final Word sub = new Word("-", Tag.SUB);
    public static final Word div = new Word("/", Tag.DIV);
    public static final Word dotCom = new Word(";", Tag.DOTCOMMA);
    public static final Word com = new Word(",", Tag.COM);
    public static final Word dot = new Word(".", Tag.DOT);
    public static final Word ee = new Word("==", Tag.EE);
    public static final Word gt = new Word(">", Tag.GT);
    public static final Word lt = new Word("<", Tag.LT);

    public static final Word openParenteses = new Word("(", Tag.OPENPARENT);
    public static final Word closeParenteses = new Word(")", Tag.CLOSEPARENT);
    public static final Word fimArqv = new Word("FINAL", Tag.FIMARQV);

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "" + lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
}
