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
        if (type1.equals("i32") && type2.equals("i32"))
            return "i32";
        if (type1.equals("float") && type2.equals("i32"))
            return "float";
        if (type1.equals("i32") && type2.equals("float"))
            return "float";
        if (type1.equals("float") && type2.equals("float"))
            return "float";

        return "";
    }

    public void generate_code(String func) {
        Symbol res, expr1, expr2, tmp;
        String type, inst;

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

            case "add":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fadd";
                else if (type.equals("i32"))
                    inst = "add";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "subtract":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fsub";
                else if (type.equals("i32"))
                    inst = "sub";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "multiply":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fmul";
                else if (type.equals("i32"))
                    inst = "mul";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "divide":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (!(type.equals("i32") || type.equals("float"))) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("float", "fdiv " + "float " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "mode":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (!type.equals("i32")) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("i32", "srem " + "i32 " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "bitwise_and":
            case "logical_and":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (!type.equals("i32")) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("i32", "and " + "i32 " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "bitwise_or":
            case "logical_or":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (!type.equals("i32")) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("i32", "or " + "i32 " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "xor":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (!type.equals("i32")) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("i32", "xor " + "i32 " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "negate":
                expr1 = semantic_stack.pop();
                if (!(expr1.getToken().equals("i32") || expr1.getToken().equals("float"))) {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol("float", "fneg " + "float " + expr1.getVal());
                semantic_stack.push(res);
                break;
        }

    }


}