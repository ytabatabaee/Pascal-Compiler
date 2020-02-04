import java.io.FileReader;

import lexical_analyzer.Scanner;
import syntax_analyzer.Parser;
import semantic_analyzer.CodeGenerator;


public class Program {

    public static void main(String[] args) {
        try {
            FileReader input_file = new FileReader("test.txt");
            String parse_table_path = "syntax_analyzer/syntax.npt";
            Scanner scanner = new Scanner(input_file);
            CodeGenerator code_generator = new CodeGenerator(scanner);
            Parser parser = new Parser(scanner, code_generator, parse_table_path);
            parser.parse();
            System.out.println("Compilation to LLVM ended successfully.");
        } catch (Exception e) {
            System.out.println("Compilation Failed");
            e.printStackTrace();
        }

    }

}