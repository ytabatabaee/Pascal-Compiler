package syntax_analyzer;

import java.io.FileInputStream;
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
    private int start;


    public Parser(Scanner scanner, CodeGenerator code_generator, String pt_path) {
        this.scanner = scanner;
        this.code_generator = code_generator;
        parse_stack = new Stack<>();
        load_parse_table(pt_path);
    }

    public void load_parse_table(String path) {
        try {
            java.util.Scanner sc = new java.util.Scanner(new FileInputStream(path));
            String[] tmp_arr = sc.nextLine().trim().split(" ");
            int row_size = Integer.valueOf(tmp_arr[0]);
            int col_size = Integer.valueOf(tmp_arr[1]);
            this.start = Integer.valueOf(sc.nextLine());
            this.symbols = sc.nextLine().trim().split(" ");
            this.parse_table = new PTCell[row_size][col_size];
            for (int i = 0; i < row_size; i++) {
                if (!sc.hasNext())
                    throw new Exception("Ivalid .npt file");
                tmp_arr = sc.nextLine().trim().split(" ");
                if (tmp_arr.length == 1) {
                    System.out.println("Anomally in .npt file, skipping one line");
                    continue;
                }
                if (tmp_arr.length != col_size * 3)
                    throw new Exception("Ivalid line in .npt file");
                for (int j = 0; j < col_size; j++)
                    this.parse_table[i][j] = new PTCell(Integer.valueOf(tmp_arr[j * 3]), Integer.valueOf(tmp_arr[j * 3 + 1]), tmp_arr[j * 3 + 2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() {

    }

    public void call_cg(String semantic) {
        if (semantic.equals("NoSem"))
            return;
        String[] functions = semantic.substring(1).split("[;]");
        for (String func : functions)
            code_generator.generate_code(func);
    }

    public void get_next_token() {

    }

}