package tpcompiladores;

import java.util.HashMap;
import java.util.Map;

public class Tag {

    public final static int START = 256,
            EXIT = 257,
            END = 258,
            ELSE = 259,
            IF = 300,
            THEN = 301,
            SCAN = 302,
            PRINT = 303,
            DO = 304,
            WHILE = 305,
            OR = 306,
            AND = 307,
            ID = 334,
            NOT = 335,
            //Tipos
            INT = 308,
            FLOAT = 309,
            STRING = 310,
            CHAR = 311,
            //NUM = 312,
            LITERAL = 313,
            //OPERADORES
            EE = 314,
            GT = 315,
            GE = 316,
            LT = 317,
            LE = 318,
            NE = 319,
            EQ = 331,
            //BOLEANOS
            FALSE = 320,
            TRUE = 321,
            //CARACTERES
            DOTCOMMA = 322,
            OPENPARENT = 323,
            CLOSEPARENT = 324,
            ADD = 325,
            SUB = 326,
            MULT = 327,
            DIV = 328,
            COM = 332,
            DOT = 333,
            //COMENTARIO E ERROS
            COMMENT = 330,
            ERRO = 400,
            FIMARQV = 65535;

}
