package tpcompiladores;

import java.io.IOException;

public class TPCompiladores {

    public static void main(String[] args) throws Exception {
        try {
//            Lexer l = new Lexer("teste3correto.txt");
//            l.runLexer();
            Sintax sin = new Sintax("teste2.txt");
        } catch (IOException e) {

        }
    }
}
