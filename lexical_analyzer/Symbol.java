package lexical_analyzer;

public class Symbol {
    private String token;
    private Object val;

    public Symbol(String token, Object val) {
        this.token = token;
        this.val = val;
    }

    public Symbol(String token) {
        this.token = token;
    }
}