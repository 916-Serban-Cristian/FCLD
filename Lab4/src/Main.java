import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            var programReader = new java.util.Scanner(new File("resources/p3.ciba"));
            var program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            var tokensReader = new java.util.Scanner(new File("resources/token.in"));
            var tokens = new ArrayList<String>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            var scanner = new Scanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                return;
            }
            scanner.outST();
            scanner.outPIF();
            System.out.println("lexically correct");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Source file not found");
        } catch (IOException e) {
            System.out.println("Can't write output");
        }
    }
}
