package semantic_analyzer;

import lexical_analyzer.Symbol;

import java.util.ArrayList;

public class SymTabCell {
    private Symbol symbol;
    private ArrayList dscp;

    public SymTabCell() {

    }

    public SymTabCell(Symbol symbol, ArrayList dscp) {
        this.symbol = symbol;
        this.dscp = dscp;
    }


    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public ArrayList getDscp() {
        return dscp;
    }

    public void setDscp(ArrayList dscp) {
        this.dscp = dscp;
    }
}
