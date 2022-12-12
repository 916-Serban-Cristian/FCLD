import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    List<String> nonTerminals;
    List<String> terminals;
    String startingSymbol;
    ProductionSet productionSet;

    public static final String enrichedGrammarStartingSymbol = "S0";
    boolean isEnriched;

    private Grammar(List<String> nonTerminals, List<String> terminals, String startingSymbol, ProductionSet productionSet) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.startingSymbol = startingSymbol;
        this.productionSet = productionSet;
        this.isEnriched = true;
    }

    Grammar(String filename) {
        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        nonTerminals = Arrays.stream(lines.get(0).split(" +")).collect(Collectors.toList());
        terminals = Arrays.stream(lines.get(1).split(" +")).collect(Collectors.toList());
        startingSymbol = lines.get(2).trim();
        productionSet = new ProductionSet();
        for (int i = 3; i < lines.size(); i++) {
            var production = lines.get(i).trim().split("->");
            var lhs = Arrays.stream(production[0].trim().split(" ")).collect(Collectors.toList());
            var rhs = Arrays.stream(production[1].trim().split(" ")).filter(it -> !Objects.equals(it, "epsilon")).collect(Collectors.toList());
            productionSet.addProduction(lhs, rhs);
        }
        isEnriched = false;
    }


    public boolean checkCFG() {
        return productionSet.getProductions().entrySet().stream().allMatch(it -> it.getKey().size() == 1);
    }

    public Grammar getEnrichedGrammar() {
        if (isEnriched) {
            return this;
        }
        var newNonTerminals = new ArrayList<>(nonTerminals);
        newNonTerminals.add(enrichedGrammarStartingSymbol);
        Grammar newGrammar = new Grammar(newNonTerminals, terminals, enrichedGrammarStartingSymbol, productionSet.copy());
        newGrammar.productionSet.addProduction(List.of(enrichedGrammarStartingSymbol), Collections.singletonList(startingSymbol));
        return newGrammar;
    }

}
