package lexical_analyzer;

public class Symbol {
    private String token;
    private String val;

    public Symbol(String token, String val) {
        this.token = token;
        this.val = val;
    }

    public Symbol(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getVal() {
        return val;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setVal(String val) {
        this.val = val;
    }
}