package semantic_analyzer;

import java.util.Stack;

import lexical_analyzer.Scanner;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<String> semantic_stack;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
    }

    public void generate_code(String func) {
        String res, expr1, expr2;

        switch (func) {
            case "push_int":
                semantic_stack.push(scanner.get_current().getVal());
                break;

            case "push_real":
                semantic_stack.push(scanner.get_current().getVal());
                break;

            case "multiply":
                expr1 = semantic_stack.pop();
                expr2 = semantic_stack.pop();
                res = expr1 + " * " + expr2;
                semantic_stack.push(res);
                break;

            case "add":
                expr1 = semantic_stack.pop();
                expr2 = semantic_stack.pop();
                res = expr1 + " + " + expr2;
                semantic_stack.push(res);
                break;
        }

    }

    public void generate_binary_expr(String operator) {
        String expr1 = semantic_stack.pop();
        String expr2 = semantic_stack.pop();
        String res = expr1 + operator + expr2;
        semantic_stack.push(res);
    }


}