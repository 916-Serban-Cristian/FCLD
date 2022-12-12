import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    // lr0
    public static void main(String[] args) {
        var g = new Grammar("resources/g2.in");
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
            // var parseTree = lr.parse(readFromSequence());
            var parseTree = lr.parse(readFromPif());
            parseTree.sort(Comparator.comparingInt(a -> a.index));
            for (var row : parseTree) {
                System.out.println(row.index + ": " + row.info + ", " + row.parent + ", " + row.rightSibling);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nNot accepted");
        }

    }

    private static List<String> readFromPif() throws Exception {
        var lines = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader("resources/PIF.out"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var parts = line.split("->");
                var leftPart = parts[0].trim();
                lines.add(leftPart);
            }
        }
        return lines;
    }

    private static List<String> readFromSequence() throws Exception {
        var lines = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader("resources/seq.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var parts = line.split(" ");
                Collections.addAll(lines, parts);
            }
        }
        return lines.stream().map(String::trim).collect(Collectors.toList());
    }
}