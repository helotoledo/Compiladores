package tpcompiladores;

public class Token {

    public final int tag; //constante que representa o token
    public String content;
    public tipoSemantico tipoSemantico;

    public Token(int t) {
        tag = t;
        tipoSemantico = tipoSemantico.NULL;
    }

    public tipoSemantico getTipoSemantico() {
        switch(tag){
            case Tag.INT:
                return tipoSemantico.INT;
                
            case Tag.FLOAT:
                return tipoSemantico.FLOAT;
                
            case Tag.STRING:
                return tipoSemantico.STRING;
                
            default:
                return tipoSemantico.ERROR;    
        }
    }

    public void setTipoSemantico(tipoSemantico tipoSemantico) {
        this.tipoSemantico = tipoSemantico;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return "" + tag;
    }

    public String toString() {
        return "" + tag;
    }
}
