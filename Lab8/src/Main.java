import java.util.Scanner;

public class Main {
    // l0
    public static void main(String[] args) {
        var g = new Grammar("resources/g2.in");
        System.out.println("Nonterminals: " + g.nonTerminals);
        System.out.println("Terminals: " + g.terminals);
        System.out.println("Starting symbol: " + g.startingSymbol);
        System.out.println("Production set:\n" + g.productionSet);
        System.out.println(g.checkCFG() ? "CFG" : "Not CFG");
        while (true) {
            System.out.print("Enter a nonterminal (or exit to quit): ");
            var nonterminal = new Scanner(System.in).nextLine().trim();
            if (nonterminal.equals("exit"))
                break;
            var productions = g.productionSet.getProductionsOf(nonterminal);
            System.out.println(productions.isEmpty() ? "No productions" : productions);
        }
    }
}