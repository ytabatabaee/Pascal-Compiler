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

    public void generate_code(String func) {
        Symbol res, expr1, expr2, tmp;

        switch (func) {
            case "push_int":
                tmp = scanner.get_current();
                tmp.setToken("i32");
                semantic_stack.push(tmp);
                break;

            case "push_real":
                //similar to int
                break;

            case "multiply":
                expr1 = semantic_stack.pop();
                expr2 = semantic_stack.pop();
                res = new Symbol(expr1.getToken(), "mul " + expr1.getToken() + expr1 + "," + expr2);
                semantic_stack.push(res);
                break;

            case "add":
                //similar to mult
                break;
        }

    }


}