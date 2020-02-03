package syntax_analyzer;

public class PTCell {
    static class Action {
        static final int Error = 0;
        static final int Shift = 1;
        static final int Goto = 2;
        static final int PushGoto = 3;
        static final int Reduce = 4;
        static final int Accept = 5;
    }

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
