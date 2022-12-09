import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    // l0
    public static void main(String[] args) {
        var g = new Grammar("resources/g1.in");
        System.out.println("Nonterminals: " + g.nonTerminals);
        System.out.println("Terminals: " + g.terminals);
        System.out.println("Starting symbol: " + g.startingSymbol);
        System.out.println("Production set:\n" + g.productionSet);
        System.out.println(g.checkCFG() ? "CFG" : "Not CFG");
        var lr = new LR(g);
        var canonicalCollection = lr.canonicalCollection();
        System.out.println("States");
        for (int i = 0; i < canonicalCollection.states.size(); i++) {
            System.out.printf("%d: %s%n", i, canonicalCollection.states.get(i));
        }
        System.out.println("State transitions");
        for (var pair : canonicalCollection.adjacencyList.entrySet()) {
            System.out.println("(" + pair.getKey().getKey() + ", " + pair.getKey().getValue() + ")" + " -> " + pair.getValue());
        }

        System.out.println(g.getEnrichedGrammar().productionSet.getOrderedProductions());

        System.out.println(lr.getParsingTable());

        try {
            var parseTree = lr.parse(Arrays.asList("a", "b", "b", "c"));
            parseTree.sort(Comparator.comparingInt(a -> a.index));
            for (ParsingTreeRow row : parseTree) {
                System.out.println(row.index + ": " + row.info + ", " + row.parent + ", " + row.rightSibling);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

    }
}