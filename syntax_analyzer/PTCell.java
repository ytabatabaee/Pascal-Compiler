package syntax_analyzer;

public class PTCell {
    private int action;
    private int index;
    private String semantic_function;

    public PTCell(int action, int index, String semantic_function) {
        this.action = action;
        this.index = index;
        this.semantic_function = semantic_function;
    }

    public int getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

    public String getSemantic_function() {
        return semantic_function;
    }
}
