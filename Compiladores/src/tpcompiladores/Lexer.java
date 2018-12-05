package tpcompiladores;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.IntStream;

public class Lexer {

    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private Hashtable words = new Hashtable();
    private FileReader file = null;

    public Hashtable getWords() {
        return words;
    }

    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w) {
        words.put(w.getLexeme(), w);
        //HashTable
    }

    public Lexer(String fileName) throws FileNotFoundException {

        reserve(new Word("start", Tag.START));
        reserve(new Word("exit", Tag.EXIT));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("scan", Tag.SCAN));
        reserve(new Word("print", Tag.PRINT));
        reserve(new Word("and", Tag.AND));
        reserve(new Word("or", Tag.OR));
        reserve(new Word("end", Tag.END));

        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
    }

    private Token erro() {
        return new Word("Lexema nao reconhecido - Linha: " + line, Tag.ERRO);
    }

    public void runLexer() throws FileNotFoundException {
        try {
            Token tk = scan();
            do {
                if (tk.tag == Tag.FIMARQV) {
                    break;
                }
                System.out.println("Tag: " + tk.tag + "\t" + tk.toString() + "\t\t" + tk.tipoSemantico);
                tk = scan();
            } while (true);
        } catch (IOException e) {
            System.err.printf("Erro ao abrir o arquivo \n", e.getMessage());
        }

    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    public Token scan() throws IOException {

        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                line++; //conta linhas
            } else {
                break;
            }
        }
        switch (ch) {
            case '<':
                if (readch('=')) {
                    return Word.le;
                } else {
                    return Word.lt;
                }
            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '(':
                readch();
                return Word.openParenteses;
            case ')':
                readch();
                return Word.closeParenteses;
            case ',':
                readch();
                return Word.com;
            case ';':
                readch();
                return Word.dotCom;
            case '*':
                readch();
                return Word.mul;
            case '/':
                readch();
                return Word.div;
            case '+':
                readch();
                return Word.add;
            case '-':
                readch();
                return Word.sub;
            case '=':
                if (readch('=')) {
                    return Word.ee;
                } else {
                    return Word.eq;
                }
            case '"':
                String phrase = "";
                phrase = phrase + ch;
                readch();
                while (ch != '"') {
                    phrase = phrase + ch;
                    readch();
                    if (ch == Tag.FIMARQV) {
                        return erro();
                    } else if (ch == '\n') {
                        line++;
                        return erro();
                    }
                }
                phrase = phrase + ch;
                readch();
                return new Word(phrase, Tag.STRING);
            case '{':
                String comment = "";
                comment = comment + ch;
                readch();
                while (ch != '}') {
                    comment = comment + ch;
                    readch();
                    if (ch == Tag.FIMARQV) {
                        return erro();
                    } else if (ch == '\n') {
                        line++;
                    }
                }
                comment = comment + ch;
                readch();
                return new Word(comment, Tag.COMMENT);

            case Tag.FIMARQV:
                return Word.fimArqv;

        }
        if (Character.isDigit(ch)) {
            float value = 0, decimal = 0;
            int inteiro = 0;
            do {
                inteiro = 10 * inteiro + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            if (ch == '.') {
                readch();
                do {
                    decimal = 10 * decimal + Character.digit(ch, 10);
                    readch();
                } while (Character.isDigit(ch));
                do{
                    decimal = decimal / 10;
                }while(decimal > 1);
                value = inteiro + decimal;
                return new Float(value);
            } else {
                return new Int(inteiro);
            }
        }
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));

            String s = sb.toString();
            Word w = (Word) words.get(s);
            if (w != null) {
                return w; //palavra já existe na HashTable
            }
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        if (ch != Tag.FIMARQV) {
            readch();
            return erro();
        }

        return erro();
    }

}
