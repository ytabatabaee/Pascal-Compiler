package syntax_analyzer;

import java.util.Stack;

import lexical_analyzer.Scanner;
import semantic_analyzer.CodeGenerator;

public class Parser {
    private Scanner scanner;
    private CodeGenerator code_generator;
    private String[] symbols;
    private PTCell[][] parse_table;
    private Stack<Integer> parse_stack;


    public Parser(Scanner scanner, CodeGenerator code_generator, String pt_path) {
        this.scanner = scanner;
        this.code_generator = code_generator;
        parse_stack = new Stack<>();
    }

    public void parse() {

    }

    public void call_cg() {

    }

    public void get_next_token() {

    }

}