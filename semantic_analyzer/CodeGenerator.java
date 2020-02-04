package semantic_analyzer;

import java.util.Stack;

import lexical_analyzer.Scanner;
import lexical_analyzer.Symbol;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<Symbol> semantic_stack;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
    }

    public String resolve_type(String type1, String type2) {

        return "";
    }

    public void generate_code(String func) {
        Symbol res, expr1, expr2, tmp;

        switch (func) {
            case "push_int":
                tmp = scanner.get_current();
                tmp.setToken("i32");
                semantic_stack.push(tmp);
                break;

            case "push_real":
                tmp = scanner.get_current();
                tmp.setToken("float");
                semantic_stack.push(tmp);
                break;

            case "multiply":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                res = new Symbol(expr1.getToken(), "mul " + expr1.getToken() + expr1.getVal() + "," + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "add":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                res = new Symbol(expr1.getToken(), "add " + expr1.getToken() + expr1.getVal() + "," + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "subtract":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                res = new Symbol(expr1.getToken(), "sub " + expr1.getToken() + expr1.getVal() + "," + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "divide":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                res = new Symbol("float", "fdiv " + expr1.getToken() + expr1.getVal() + "," + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "mode":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                res = new Symbol("i32", "srem " + expr1.getToken() + expr1.getVal() + "," + expr2.getVal());
                semantic_stack.push(res);
                break;
        }

    }


}