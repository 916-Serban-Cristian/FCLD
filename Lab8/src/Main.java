import java.util.List;
import java.util.Scanner;

public class Main {
    // l0
    public static void main(String[] args) {
        Grammar g = new Grammar("resources/g1.in");
        System.out.println("Nonterminals: " + g.nonTerminals);
        System.out.println("Terminals: " + g.terminals);
        System.out.println("Starting symbol: " + g.startingSymbol);
        System.out.println("Production set:\n" + g.productionSet);
        System.out.println(g.checkCFG() ? "CFG" : "Not CFG");
        while (true) {
            System.out.print("Enter a nonterminal (or exit to quit): ");
            String nonterminal = new Scanner(System.in).nextLine().trim();
            if (nonterminal.equals("exit"))
                break;
            List<List<String>> productions = g.productionSet.getProductionsOf(nonterminal);
            System.out.println(productions.isEmpty() ? "No productions" : productions);
        }
    }
}