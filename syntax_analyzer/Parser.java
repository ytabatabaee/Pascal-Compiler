package syntax_analyzer;

import java.io.FileInputStream;
import java.util.Stack;
import java.util.Arrays;
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
    private int start;


    public Parser(Scanner scanner, CodeGenerator code_generator, String pt_path) throws Exception {
        this.scanner = scanner;
        this.code_generator = code_generator;
        parse_stack = new Stack<>();
        load_parse_table(pt_path);
    }

    public void load_parse_table(String path) throws Exception {
        java.util.Scanner sc = new java.util.Scanner(new FileInputStream(path));
        String[] tmp_arr = sc.nextLine().trim().split(" ");
        int row_size = Integer.valueOf(tmp_arr[0]);
        int col_size = Integer.valueOf(tmp_arr[1]);
        this.start = Integer.valueOf(sc.nextLine());
        this.symbols = sc.nextLine().trim().split(",");
        // System.out.println(Arrays.toString(symbols));
        this.parse_table = new PTCell[row_size][col_size];
        for (int i = 0; i < row_size; i++) {
            if (!sc.hasNext())
                throw new Exception("Ivalid .npt file");
            tmp_arr = sc.nextLine().trim().split(",| ");
            // System.out.println(Arrays.toString(tmp_arr));
            if (tmp_arr.length == 1) {
                System.out.println("Anomally in .npt file, skipping one line");
                continue;
            }
            // System.out.println(i);
            if (tmp_arr.length != col_size * 3)
                throw new Exception("Ivalid line in .npt file");
            for (int j = 0; j < col_size; j++)
                this.parse_table[i][j] = new PTCell(Integer.valueOf(tmp_arr[j * 3]), Integer.valueOf(tmp_arr[j * 3 + 1]), tmp_arr[j * 3 + 2]);
        }
    }

    public void parse() throws Exception {
        boolean accepted = false;
        int token_id = next_token_id();
        int cur_node = start;
        while (!accepted) {
            String token = symbols[token_id];
            PTCell cell = parse_table[cur_node][token_id];
            System.out.println(token);
            System.out.println(cell.getAction());
            switch (cell.getAction()) {
                case PTCell.Action.Error:
                    throw new Exception(String.format("Compile Error (" + token + ") at line " + scanner.line_number() + " @ " + cur_node));

                case PTCell.Action.Shift:
                    call_cg(cell.getSemantic_function());
                    token_id = next_token_id();
                    cur_node = cell.getIndex();
                    break;

                case PTCell.Action.Goto:
                    call_cg(cell.getSemantic_function());
                    cur_node = cell.getIndex();
                    break;

                case PTCell.Action.PushGoto:
                    parse_stack.push(cur_node);
                    cur_node = cell.getIndex();
                    break;

                case PTCell.Action.Reduce:
                    if (parse_stack.size() == 0)
                        throw new Exception(String.format("Compile Error (" + token + ") at line " + scanner.line_number() + " @ " + cur_node));
                    cur_node = parse_stack.pop();
                    cell = parse_table[cur_node][cell.getIndex()];
                    call_cg(cell.getSemantic_function());
                    cur_node = cell.getIndex();
                    break;

                case PTCell.Action.Accept:
                    accepted = true;
                    break;
            }
        }
    }

    public void call_cg(String semantic) {
        if (semantic.equals("NoSem"))
            return;
        String[] functions = semantic.substring(1).split("[;]");
        for (String func : functions) {
            System.out.println(func);
            // code_generator.generate_code(func);
        }
    }

    public int next_token_id() throws Exception{
        String token = scanner.read_token();
        for (int i = 0; i < symbols.length; i++)
            if (symbols[i].equalsIgnoreCase(token))
                return i;
        throw new RuntimeException("Undefined token: " + token + " at line " + scanner.line_number());
    }

}