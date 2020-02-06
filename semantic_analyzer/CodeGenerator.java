package semantic_analyzer;

import java.util.ArrayList;
import java.util.Stack;

import lexical_analyzer.Scanner;
import lexical_analyzer.Symbol;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<Symbol> semantic_stack;
    private ArrayList<String> code;
    private int variable_count;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
        code = new ArrayList<>();
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

    public String convert_type(String type) {
        switch (type) {
            case "integer":
                return "i32";
            case "real":
                return "float";
            case "long":
                return "i64";
            case "char":
                return "i8";
        }
        return null;
    }

    public int type_size(String type) {
        switch (type) {
            case "integer":
            case "real":
                return 4;
            case "long":
                return 8;
            case "char":
            case "boolean":
                return 1;
        }
        return 0;
    }

    public void generate_code(String func) {
        Symbol res, expr1, expr2, tmp;
        String type, inst, value, cl;
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
                type = convert_type(expr1.getVal());
                int size = type_size(expr1.getVal());
//                cl = "%" + variable_count + " = alloca " + type + ", align " + size;
//                code.add(cl);
                cl = "%" + variable_count + " = " + inst + " " + type + " " + expr1.getVal() + ", " + expr2.getVal();
                code.add(cl);
                res = new Symbol(type, "%" + variable_count);
                variable_count++;
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

            case "push_type":
                res = scanner.get_current();
                System.out.println(res.getVal());
                res.setToken("type");
                semantic_stack.push(res);
                break;


            case "set_type":
                expr1 = semantic_stack.pop(); // type
                expr2 = semantic_stack.pop(); // id
                System.out.println("token: " + expr1.getToken());
                System.out.println("val: " + expr1.getVal());
                System.out.println("token: " + expr2.getToken());
                System.out.println("val: " + expr2.getVal());
                type = convert_type(expr1.getVal());
                int size = type_size(expr1.getVal());
                res = new Symbol("dcl", "%" + expr2.getVal() + " = alloca " + type + ", align " + size);
                semantic_stack.push(res);
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
//                expr1 = semantic_stack.pop(); // type
//                expr2 = semantic_stack.pop(); // func def
//                res = new Symbol("func", "define " + convert_type(expr1.getVal()) + " " + expr2.getVal() = " {\n");
//                semantic_stack.push(res);
                break;

//            case ""

        }
        System.out.println("_______________________");
        System.out.println(func);
        System.out.println(res.getVal());
        System.out.println("_______________________");

    }


}