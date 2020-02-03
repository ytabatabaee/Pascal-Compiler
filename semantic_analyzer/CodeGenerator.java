package semantic_analyzer;

import java.util.Stack;

import lexical_analyzer.Scanner;

public class CodeGenerator {
    private Scanner scanner;
    private Stack<Integer> semantic_stack;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        semantic_stack = new Stack<>();
    }

    public void generate_code(String func) {

    }

}