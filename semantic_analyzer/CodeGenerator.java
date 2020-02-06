package semantic_analyzer;

import java.util.Stack;

import lexical_analyzer.Scanner;
import lexical_analyzer.Symbol;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<Symbol> semantic_stack;
    private int variable_count;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
        variable_count = 1;
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
        String type, inst, value;
        res = new Symbol(" ", " ");

        switch (func) {
            case "push_id":
                tmp = scanner.get_current();
                System.out.println("token: " + tmp.getToken());
                System.out.println("val: " + tmp.getVal());
                semantic_stack.push(tmp);
                break;

            case "push_integer_const":
                tmp = scanner.get_current();
                tmp.setToken("i32");
                semantic_stack.push(tmp);
                break;

            case "push_real_const":
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
            case "and":
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
            case "or":
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

            case "exclusive_add":
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

            case "set_type":
                tmp = semantic_stack.pop();
                expr1 = scanner.get_current();
                System.out.println("token: " + tmp.getToken());
                System.out.println("val: " + tmp.getVal());
                System.out.println("token: " + expr1.getToken());
                System.out.println("val: " + expr1.getVal());
                type = scanner.get_current().getToken();
                switch (type) {
                    case "boolean":
                    case "char":
                        tmp.setToken("i8");
                        res = new Symbol("dcl", tmp.getVal() + " = alloca i8, align 1");
                        semantic_stack.push(res);
                        break;
                    case "integer":
                        tmp.setToken("i32");
                        res = new Symbol("dcl", tmp.getVal() + " = alloca i32, align 4");
                        semantic_stack.push(res);
                        break;
                    case "long":
                        tmp.setToken("i64");
                        res = new Symbol("dcl", tmp.getVal() + " = alloca i64, align 8");
                        semantic_stack.push(res);
                        break;
                    case "real":
                        tmp.setToken("float");
                        res = new Symbol("dcl", tmp.getVal() + " = alloca float, align 4");
                        semantic_stack.push(res);
                        break;
                    case "string":
                        // TODO: I REALLY DON'T KNOW
                        break;
                    default:
                        System.out.println("There is no such type!");
                        break;
                }
                System.out.println("type : " + type);
                break;

            case "assign":
//                tmp = semantic_stack.pop();
//                value = scanner.get_current().getVal();

                break;

            case "is_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " eq " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "is_not_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " ne " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "is_less_than":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " slt " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "is_less_than_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " sle " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "is_greater_than":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " sgt " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "is_greater_than_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                type = resolve_type(expr1.getToken(), expr2.getToken());
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    System.out.println("This operation with these types is not possible.");
                    return;
                }
                res = new Symbol(type, inst + " sge " + type + " " + expr1.getVal() + ", " + expr2.getVal());
                semantic_stack.push(res);
                break;

            case "start_function":
                tmp = semantic_stack.pop();
                res = new Symbol("func", "@" + tmp.getVal() + "(");
                semantic_stack.push(res);
                break;

            case "end_function":
                tmp = semantic_stack.pop();
                res = new Symbol("func", tmp.getVal() + ")");
                semantic_stack.push(res);
                break;

            case "set_func_type":
                //TODO THIS HAS TO BE LIKE SET_TYPE (SUPPORTING ALL TYPES)
                tmp = semantic_stack.pop();
                res = new Symbol("func", "define " + scanner.get_current().getToken() + " "+ tmp.getVal() );
                semantic_stack.push(res);
                break;

        }
        System.out.println("_______________________");
        System.out.println(func);
        System.out.println(res.getVal());
        System.out.println("_______________________");

    }


}