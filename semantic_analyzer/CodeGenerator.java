package semantic_analyzer;

import java.util.ArrayList;
import java.util.Stack;

import lexical_analyzer.Scanner;
import lexical_analyzer.Symbol;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<Symbol> semantic_stack;
    private ArrayList<String> code;
    private ArrayList<SymTabCell> sym_tab;
    private int variable_count;
    private int if_count;
    private int loop_count;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
        code = new ArrayList<>();
        sym_tab = new ArrayList<>();
        variable_count = 1;
        if_count = 1;
        loop_count = 1;
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
            case "i32":
            case "float":
                return 4;
            case "long":
            case "i64":
                return 8;
            case "char":
            case "i8":
            case "boolean":
                return 1;
        }
        return 0;
    }

    public String type_of_id_in_symtab(String id) {
        for (SymTabCell cell : sym_tab) {
            Symbol symbol = cell.getSymbol();
            if (id.equals(symbol.getVal())) {
                return symbol.getToken();
            }
        }
        return null;
    }

    public String assign_type(String id_type, String val_type) {
        if (id_type.equals(val_type))
            return id_type;
        return null;
    }

    public void generate_code(String func) throws Exception {
        Symbol res, expr1, expr2, tmp;
        String type, type1, type2, inst, value, cl = null, val1, val2;
        boolean flag1, flag2;
        res = new Symbol(" ", " ");
        int size;

        switch (func) {
            case "push_id":
                tmp = scanner.get_current();
                tmp.setVal("%" + tmp.getVal());
                System.out.println("token: " + tmp.getToken());
                System.out.println("val: " + tmp.getVal());
                semantic_stack.push(tmp);
                break;

            case "push_integer_const":
                tmp = scanner.get_current();
                tmp.setToken("i32");
                semantic_stack.push(tmp);
                break;

            case "push_long_const":
                tmp = scanner.get_current();
                tmp.setToken("i64");
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
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fadd";
                else if (type.equals("i32"))
                    inst = "add";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "subtract":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fsub";
                else if (type.equals("i32"))
                    inst = "sub";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "multiply":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fmul";
                else if (type.equals("i32"))
                    inst = "mul";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "divide":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (!(type.equals("i32") || type.equals("float"))) {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + "fdiv" + " " + "float" + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol("float", "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "mode":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (!type.equals("i32")) {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + "srem" + " " + "i32" + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol("i32", "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "bitwise_and":
            case "and":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (!type.equals("i32")) {
                    throw new Exception("This operation with these types is not possible.");
                }
                cl = "%var" + variable_count + " = " + "and " + " " + "i32" + " " + expr1.getVal() + ", " + expr2.getVal();
                code.add(cl);
                res = new Symbol("i32", "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "bitwise_or":
            case "or":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (!type.equals("i32")) {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + "or" + " " + "i32" + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol("i32", "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "exclusive_add":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (!type.equals("i32")) {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + "xor" + " " + "i32" + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol("i32", "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "negate":
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                val1 = expr1.getVal();
                if (type1 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                if (!(type1.equals("i32") || type1.equals("float"))) {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + "fneg" + " " + "float" + " " + val1;
                code.add(cl);
                res = new Symbol("float", "%var" + variable_count);
                variable_count++;
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
                type = convert_type(expr1.getVal());
                size = type_size(expr1.getVal());
                cl = expr2.getVal() + " = alloca " + type + ", align " + size;
                code.add(cl);
                res = new Symbol(type, "%" + expr2.getVal());
                sym_tab.add(new SymTabCell(new Symbol(type, expr2.getVal()), new ArrayList()));
                System.out.println("res.token: " + res.getToken());
                semantic_stack.push(res);
                break;

            case "assign":
                expr1 = semantic_stack.pop(); // expr
                tmp = semantic_stack.pop(); // left-hand-side var
                type1 = tmp.getToken().equals("id") ? type_of_id_in_symtab(tmp.getVal()) : tmp.getToken();
                type2 = expr1.getToken().equals("id") ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type = assign_type(type1, type2);
                System.out.println(type1);
                System.out.println(type2);
                if (type == null)
                    throw new Exception("You can't assign a value with this type to that variable.");
                cl = "store " + type + " " + expr1.getVal() + ", " + type + "* " + tmp.getVal() + ", align " + type_size(type);
                code.add(cl);
                break;

            case "is_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " eq " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "is_not_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " ne " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "is_less_than":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " slt " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "is_less_than_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " sle " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "is_greater_than":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " sgt " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "is_greater_than_equal":
                expr2 = semantic_stack.pop();
                expr1 = semantic_stack.pop();
                flag1 = expr1.getToken().equals("id");
                flag2 = expr2.getToken().equals("id");
                type1 = flag1 ? type_of_id_in_symtab(expr1.getVal()) : expr1.getToken();
                type2 = flag2 ? type_of_id_in_symtab(expr2.getVal()) : expr2.getToken();
                val1 = expr1.getVal();
                val2 = expr2.getVal();
                if (type1 == null || type2 == null) {
                    throw new Exception("You didn't define this variable.");
                }
                type = resolve_type(type1, type2);
                if (type.equals("float"))
                    inst = "fcmp";
                else if (type.equals("i32"))
                    inst = "icmp";
                else {
                    throw new Exception("This operation with these types is not possible.");
                }
                if (flag1) {
                    cl = "%var" + variable_count + " = load " + type1 + ", " + type1 + "* " + val1 + ", align " + type_size(type1);
                    code.add(cl);
                    val1 = "%var" + variable_count;
                    variable_count++;
                }
                if (flag2) {
                    cl = "%var" + variable_count + " = load " + type2 + ", " + type2 + "* " + val2 + ", align " + type_size(type2);
                    code.add(cl);
                    val2 = "%var" + variable_count;
                    variable_count++;
                }
                cl = "%var" + variable_count + " = " + inst + " sge " + type + " " + val1 + ", " + val2;
                code.add(cl);
                res = new Symbol(type, "%var" + variable_count);
                variable_count++;
                semantic_stack.push(res);
                break;

            case "start_function":
                tmp = semantic_stack.pop();
                tmp.setVal(tmp.getVal().substring(1)); //removes the % before the name
                cl = "@" + tmp.getVal() + "(";
                code.add(cl);
                sym_tab.add(new SymTabCell(new Symbol("func", tmp.getVal()), new ArrayList()));
                break;

            case "end_function":
                cl = code.get(code.size() - 1);
                code.remove(cl);
                cl += ")";
                code.add(cl);
                break;

            case "set_func_type":
                expr1 = semantic_stack.pop(); // type
                cl = code.get(code.size() - 1); // func def
                code.remove(cl);
                cl = "define " + convert_type(expr1.getVal()) + " " + cl + " {";
                code.add(cl);
                break;

            case "close_function":
                code.add("}");
                break;

            case "jump_to_then":
                tmp = semantic_stack.pop();
                cl = "br i1 " + tmp.getVal() + ", label %if.then" + if_count + ", label %if.else" + if_count;
                code.add(cl);
                cl = "if.then" + if_count + ":";
                code.add(cl);
                break;

            case "jump_to_endif_then":
                cl = "br label %if.end" + if_count;
                code.add(cl);
                cl = "if.else" + if_count + ":";
                code.add(cl);
                cl = "if.end" + if_count + ":";
                code.add(cl);
                break;

            case "jump_to_endif_else":
                code.remove("if.end" + if_count + ":");
                cl = "br label %if.end" + if_count;
                code.add(cl);
                cl = "if.end" + if_count + ":";
                code.add(cl);
                if_count++;
                break;


            case "start_loop":
                cl = "while.start" + loop_count + ":";
                code.add(cl);
                break;

            case "loop_body":
                tmp = semantic_stack.pop();
                cl = "br i1 " + tmp.getVal() + ", label %while.body" + loop_count + ", label %while.end" + loop_count;
                code.add(cl);
                cl = "while.body" + loop_count + ":";
                code.add(cl);
                break;

            case "end_loop":
                cl = "br label %while.start" + loop_count;
                code.add(cl);
                cl = "while.end" + loop_count + ":";
                code.add(cl);
                loop_count++;
                break;


            case "return_id":
                tmp = scanner.get_current(); // return value (id)
                tmp.setVal("%" + tmp.getVal());
                System.out.println("token: " + tmp.getToken());
                System.out.println("val: " + tmp.getVal());
                // set the right type for it with symtab; check if exists
                cl = "ret " + tmp.getToken() + " " + tmp.getVal();
                code.add(cl);
                // todo bayad check konim ke type a func ba type a return yeki bashe
                break;

            case "return_int":
                tmp = scanner.get_current(); // return value
                cl = "ret i32 " + tmp.getVal();
                code.add(cl);
                break;

            case "start_func_call":
                //todo printf scanf strelen ezafe shan be symtable
                //todo function call haie addi check shan ke to symbol table bashan
                // tmp.getToken dar vaghe bayad type a function to symtab bashe
                tmp = semantic_stack.pop();
                tmp.setVal(tmp.getVal().substring(1)); //removes the % before the name
                if (tmp.getVal().equals("write")) {
                    if (!code.contains("declare i32 @printf(i8* noalias nocapture, ...)\n")) {
                        code.add(0, "declare i32 @printf(i8* noalias nocapture, ...)\n");
                        tmp.setVal("printf");
                    }
                } else if (tmp.getVal().equals("read")) {
                    if (!code.contains("declare i32 @scanf(i8* noalias nocapture, ...)\n")) {
                        code.add(0, "declare i32 @scanf(i8* noalias nocapture, ...)\n");
                        tmp.setVal("scanf");
                    }
                }
                cl = "call " + tmp.getToken() + " @" + tmp.getVal() + "(";
                code.add(cl);
                sym_tab.add(new SymTabCell(new Symbol("func", tmp.getVal()), new ArrayList()));
                break;

            case "end_func_call":
                cl = code.get(code.size() - 1);
                code.remove(cl);
                cl += ")";
                code.add(cl);
                return;

            case "next_argument":
                cl = code.get(code.size() - 1);
                code.remove(cl);
                cl += "; ";
                code.add(cl);
                break;

            case "add_argument":
                // set_type is called before this
                cl = code.get(code.size() - 1); // func def
                code.remove(cl);
                String[] parts = cl.trim().split(" ");
                cl = code.get(code.size() - 1);
                code.remove(cl);
                code.add(cl + parts[3].replace(",", "") + " " + parts[0]);
                break;

        }
        System.out.println("_______________________");
        System.out.println(func);
        System.out.println(res.getVal());
        System.out.println(res.getToken());
        System.out.println(cl);
        System.out.println("CODE:");
        System.out.println("++++++++++++++++++++++++++++++");
        for (String c : code)
            System.out.println(c);
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.println("_______________________");

    }


}