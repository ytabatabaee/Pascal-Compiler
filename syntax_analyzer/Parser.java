package syntax_analyzer;

import java.util.Stack;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public void load_parse_table(String path) {
        if (!Files.exists(Paths.get(nptPath))) {
            throw new RuntimeException("Invalid parse table path: " + nptPath);
        }

    }

    public void parse() {

    }

    public void call_cg(String function) {
        if (function.equals("NoSem")) {
            return;
        }

    }

    public void get_next_token() {

    }

}