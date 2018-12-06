package tpcompiladores;

import java.io.IOException;

public class Sintax {

    public Lexer lex;
    public Token tok;
    public Token tokAnterior;
    public boolean condition = false;
    private static final boolean debug = false;

    public Sintax() {
    }

    public Sintax(String file) throws Exception {
        lex = new Lexer(file);
        tok = lex.scan();
        while (tok.tag == Tag.COMMENT) {
            eat(Tag.COMMENT, false);
        }
        if (tok.tag == Tag.START) {
            program();
        } else {
            throw new Exception("Excecao Sintatica:  Esperava-se token inicializador na linha " + Lexer.line);
        }
    }

    private void advance() throws Exception {

        tokAnterior = tok;
        tok = lex.scan();
        if(tok.tag == Tag.AND || tok.tag == Tag.OR){
           condition = true;
        }
        if (tok.tag == Tag.COMMENT) {
            System.out.println("comment();");
            eat(Tag.COMMENT, false);
        }
    }
    
    private boolean eat(int t, boolean checkType) throws Exception {
        if (tok.tag == t) {
            if(checkType && tok.tag == Tag.ID && tok.tipoSemantico == tipoSemantico.NULL){
                throw new Exception ("Excecao Semantica: Variavel (" + tok.toString() + ") nao declarada na linha " + Lexer.line);
            }
            //System.out.println("\tEAT(" + tok.toString() + ")");
            advance();
            return true;
        } else {
            throw new Exception ("Token encontrado (" + tok.toString() + ") nao era o esperado na linha: " + Lexer.line);
            //return false;
        }
    }

    private void erro(int t) throws Exception {
        throw new Exception ("\tExcecao Sintatica: Erro - Token tag:" + t + "na linha " + Lexer.line);

    }

    private void erro() throws Exception {
        throw new Exception("\tExcecao Sintatica: Erro - Token inesperado na linha " + Lexer.line);
    }

    private void identifier() throws Exception {
        if (debug) {
            System.out.println("identifier();");
        }
        if (tok.tag == Tag.ID) {
            eat(Tag.ID, false);
        } else {
            erro();
        }
    }

    private tipoSemantico literal() throws Exception {
        if (debug) {
            System.out.println("literal();");
        }
        if (tok.tag == Tag.STRING) {
            tok.setTipoSemantico(tipoSemantico.STRING);
            eat(Tag.STRING, false);
            return tokAnterior.getTipoSemantico();
        } else {
            throw new Exception("Excecao Sintatica: Erro - Token inesperado na linha " + Lexer.line);
        }
    }

    private tipoSemantico float_const() throws Exception {
        if (debug) {
            System.out.println("float_const();");
        }
        if (tok.tag == Tag.FLOAT) {
            tok.setTipoSemantico(tipoSemantico.FLOAT);
            eat(Tag.FLOAT, false);
            return tokAnterior.getTipoSemantico();
        } else {
            throw new Exception("Excecao Sintatica: Erro - Token inesperado na linha " + Lexer.line);
        }
    }

    private tipoSemantico integer_const() throws Exception {
        if (debug) {
            System.out.println("inter_const();");
        }
        if (tok.tag == Tag.INT) {
            tok.setTipoSemantico(tipoSemantico.INT);
            eat(Tag.INT, false);
            return tokAnterior.getTipoSemantico();
        } else {
            throw new Exception("Excecao Sintatica: Erro - Token inesperado na linha " + Lexer.line);
        }
    }

    private tipoSemantico constant() throws Exception {
        if (debug) {
            System.out.println("constant();");
        }
        switch (tok.tag) {
            case Tag.INT:
                return integer_const();

            case Tag.FLOAT:
                return float_const();

            case Tag.STRING:
                return literal();
                
            default:
                throw new Exception ("Excecao Sintatica: Erro - Token inesperado na linha " + Lexer.line);
        }
    }

    private void mulop() throws Exception {
        if (debug) {
            System.out.println("mulop();");
        }
        switch (tok.tag) {
            case Tag.MULT:
                eat(Tag.MULT, false);
                break;

            case Tag.DIV:
                eat(Tag.DIV, false);
                break;

            case Tag.AND:
                eat(Tag.AND, false);
                break;

            default:
                erro();
        }
    }

    private void addop() throws Exception {
        if (debug) {
            System.out.println("addop();");
        }
        tipoSemantico tipo = null;
        switch (tok.tag) {
            case Tag.ADD:
                eat(Tag.ADD, false);
                break;

            case Tag.SUB:
                eat(Tag.SUB, false);
                break;

            case Tag.OR:
                eat(Tag.OR, false);
                break;

            default:
                erro();
        }
    }

    private void relop() throws Exception {
        if (debug) {
            System.out.println("relop();");
        }
        switch (tok.tag) {
            case Tag.EE: // ==
                eat(Tag.EE, false);
                break;

            case Tag.GT: // >
                eat(Tag.GT, false);
                break;

            case Tag.GE: // >=
                eat(Tag.GE, false);
                break;

            case Tag.LT: // <
                eat(Tag.LT, false);
                break;

            case Tag.LE: // <=
                eat(Tag.LE, false);
                break;

            case Tag.NE: // <>
                eat(Tag.NE, false);
                break;

            default:
                erro();
        }
    }

    private tipoSemantico factor() throws Exception {
        if (debug) {
            System.out.println("factor();");
        }
        tipoSemantico tipo = tipoSemantico.NULL;
        switch (tok.tag) {
            case Tag.ID:
                tipo = tok.tipoSemantico;
                if(debug){
                    System.out.println("identifier();");
                }
                eat(Tag.ID, true);
                break;

            case Tag.STRING:                
            case Tag.INT:                
            case Tag.FLOAT:
                return constant();

            case Tag.OPENPARENT:
                eat(Tag.OPENPARENT, false);
                tipo = expression();
                eat(Tag.CLOSEPARENT, false);
                break;
        }
        return tipo;
    }

    private tipoSemantico factor_a() throws Exception {
        if (debug) {
            System.out.println("factor_a();");
        }
        switch (tok.tag) {
            case Tag.NOT:
                eat(Tag.NOT, false);
                return factor();

            case Tag.SUB:
                eat(Tag.SUB, false);
                return factor();

            default:
                return factor();
        }
    }

    private tipoSemantico term() throws Exception {
        if (debug) {
            System.out.println("term();");
        }
        tipoSemantico tipoFactor = factor_a();
        if (tok.tag == Tag.MULT || tok.tag == Tag.DIV) {
            mulop();
            tipoSemantico tipoTerm = term();
            if(tipoTerm != tipoSemantico.NULL && tipoTerm != tipoFactor){
                throw new Exception("Excecao Semantica: Tipo do factor (" + tipoFactor + " - " + tok.toString()+ ") diferente do tipo do termo (" + tipoTerm + ") na linha " + Lexer.line + "  "+tok.tag+"  " + tokAnterior.tag);
            }
        }else if(tok.tag == Tag.AND){
            mulop();
            tipoSemantico tipoTerm = term();
            if(tipoTerm != tipoSemantico.NULL && tipoTerm != tipoFactor && condition == false){
                throw new Exception("Excecao Semantica: Tipo do factor (" + tipoFactor + " - " + tok.toString()+ ") diferente do tipo do termo (" + tipoTerm + ") na linha " + Lexer.line + "  "+tok.tag+"  " + tokAnterior.tag);
            }
        }
        return tipoFactor;
    }


    private tipoSemantico simple_expr() throws Exception {
        if (debug) {
            System.out.println("simple_expr();");
        }
        tipoSemantico tipoTerm = term();
        if (tok.tag == Tag.ADD || tok.tag == Tag.SUB) {
            addop();
            tipoSemantico tipoExpr = simple_expr();
            if(tipoExpr != tipoSemantico.NULL && tipoExpr != tipoTerm){
                throw new Exception("Excecao Semantica: Tipo do termo (" + tipoTerm + ") diferente do tipo da expressao (" + tipoExpr + ") na linha " + Lexer.line);
            }
        }
        else if (tok.tag == Tag.OR) {
            addop();
            tipoSemantico tipoExpr = simple_expr();
            if(tipoExpr != tipoSemantico.NULL && tipoExpr != tipoTerm && condition == false){
                throw new Exception("Excecao Semantica: Tipo do termo (" + tipoTerm + ") diferente do tipo da expressao (" + tipoExpr + ") na linha " + Lexer.line);
            }
        }
        return tipoTerm;
    }

    private tipoSemantico expression() throws Exception {
        if (debug) {
            System.out.println("expression();");
        }
        tipoSemantico tipo = simple_expr();
        if (tok.tag == Tag.EE || tok.tag == Tag.EE || tok.tag == Tag.GT || tok.tag == Tag.GE || tok.tag == Tag.LT || tok.tag == Tag.LE || tok.tag == Tag.NE) {
            relop();
            simple_expr();
        }
        return tipo;
    }

    private void writable() throws Exception {
        if (debug) {
            System.out.println("writable();");
        }
        switch (tok.tag) {
            case Tag.STRING:
                literal();
                break;

            case Tag.OPENPARENT:
            case Tag.ID:
                simple_expr();
                break;

            default:
                erro();
                break;
        }
    }

    private void write_stmt() throws Exception {
        if (debug) {
            System.out.println("write_stmt();");
        }
        eat(Tag.PRINT, false);
        eat(Tag.OPENPARENT, false);
        writable();
        eat(Tag.CLOSEPARENT, false);
    }

    private void read_stmt() throws Exception {
        if (debug) {
            System.out.println("read_stmt();");
        }
        eat(Tag.SCAN, false);
        eat(Tag.OPENPARENT, false);
        if(debug){
            System.out.println("identifier();");
        }
        eat(Tag.ID, true);
        eat(Tag.CLOSEPARENT, false);
    }

    private void stmt_sufix() throws Exception {
        if (debug) {
            System.out.println("stmt_sufix();");
        }
        eat(Tag.WHILE, false);
        eat(Tag.OPENPARENT, false);
        condition();
        eat(Tag.CLOSEPARENT, false);
        eat(Tag.END, false);
    }

    private void while_stmt() throws Exception {
        if (debug) {
            System.out.println("while_stmt();");
        }
        eat(Tag.DO, false);
        stmt_list();
        stmt_sufix();
    }

    private void condition() throws Exception {
        if (debug) {
            System.out.println("condition();");
        }
        expression();
    }

    private void if_stmt() throws Exception {
        if (debug) {
            System.out.println("if_stmt();");
        }
        eat(Tag.IF, false);
        condition();
        eat(Tag.THEN, false);
        condition = false;
        stmt_list();
        if (tok.tag == Tag.ELSE) {
            eat(Tag.ELSE, false);
            stmt_list();
        }
        eat(Tag.END, false);
    }

    private void stmt() throws Exception {
        if (debug) {
            System.out.println("stmt();");
        }
        //System.out.println("\n" + tok.toString() + " - " + tok.getTag());
        switch (tok.tag) {
            case Tag.ID:
                if(tok.tipoSemantico != tipoSemantico.NULL){
                    if (debug) {
                        System.out.println("assign_stmt();");
                        System.out.println("identifier();");
                    }
                    eat(Tag.ID, true);
                    tipoSemantico tipo = tokAnterior.tipoSemantico;
                    eat(Tag.EQ, false);
                    tipoSemantico tipoExpr = simple_expr();
                    if(tipo != tipoExpr){
                        throw new Exception("Excecao Semantica: Tipo da variável (" + tipo + ") diferente do tipo da expressao (" + tipoExpr + ") na linha " + Lexer.line);
                    }
                }
                eat(Tag.DOTCOMMA, false);
                break;

            case Tag.IF:
                if_stmt();
                break;

            case Tag.DO:
                while_stmt();
                break;

            case Tag.SCAN:
                read_stmt();
                eat(Tag.DOTCOMMA, false);
                break;

            case Tag.PRINT:
                write_stmt();
                eat(Tag.DOTCOMMA, false);
                break;
        }
    }

    private void stmt_list() throws Exception {
        if (debug) {
            System.out.println("stmt_list();");
        }
        //stmt();
        while (tok.tag == Tag.ID || tok.tag == Tag.IF || tok.tag == Tag.DO || tok.tag == Tag.SCAN || tok.tag == Tag.PRINT) {
            stmt();
        }
    }

    private void type() throws Exception {
        if (debug) {
            System.out.println("type();");
        }
        switch (tok.tag) {
            case Tag.INT:
                eat(Tag.INT, false);
                break;

            case Tag.FLOAT:
                eat(Tag.FLOAT, false);
                break;

            case Tag.STRING:
                eat(Tag.STRING, false);
                break;

            default:
                erro();
        }
    }

    private void ident_list(tipoSemantico tipo) throws Exception {
        if (debug) {
            System.out.println("ident_list();");
        }
        if(tok.tipoSemantico != tipoSemantico.NULL){
            throw new Exception("Excecao Semantica: Variavel (" + tok.toString() + ") ja foi declarada como (" + tok.tipoSemantico + ") na linha " + Lexer.line);
        }
        tok.setTipoSemantico(tipo);
        identifier();
        if (tok.tag == Tag.COM) {
            eat(Tag.COM, false);
            ident_list(tipo);
        }
    }

    private void delc() throws Exception {
        if (debug) {
            System.out.println("delc();");
        }
        type();
        tipoSemantico tipo = tokAnterior.getTipoSemantico();
        ident_list(tipo);
        eat(Tag.DOTCOMMA, false);
    }

    private void delc_list() throws Exception {
        if (debug) {
            System.out.println("delc_list();");
        }
        while (tok.tag == Tag.INT || tok.tag == Tag.FLOAT || tok.tag == Tag.STRING) {
            delc();
        }
    }

    private void program() throws Exception{
        if (debug) {
            System.out.println("program();");
        }
        eat(Tag.START, false);
        delc_list();
        stmt_list();
        eat(Tag.EXIT, false);
        System.out.println("Teste compilado com sucesso.");
    }
}
