import java.util.*;
import java.util.stream.Collectors;

public class ProductionSet {
    private final HashMap<List<String>, List<List<String>>> productions;

    ProductionSet() {
        productions = new HashMap<>();
    }

    ProductionSet(HashMap<List<String>, List<List<String>>> productions) {
        this.productions = productions;
    }

    public List<List<String>> getProductionsOf(List<String> lhs) {
        return productions.getOrDefault(lhs, Collections.emptyList());
    }

    public List<List<String>> getProductionsOf(String lhs) {
        return getProductionsOf(Collections.singletonList(lhs));
    }

    public HashMap<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    public void addProduction(List<String> lhs, List<String> rhs) {
        if (!productions.containsKey(lhs))
            productions.put(lhs, new ArrayList<>());
        productions.get(lhs).add(rhs);
    }

    public ProductionSet copy() {
        return new ProductionSet(productions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<String> lhs : productions.keySet()) {
            for (List<String> rhs : productions.get(lhs)) {
                sb.append(lhs).append(" -> ").append(rhs).append("\n");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public List<Map.Entry<String, List<String>>> getOrderedProductions() {
        List<Map.Entry<String, List<String>>> orderedProductions = new ArrayList<>();
        for (List<String> lhs : productions.keySet()) {
            for (List<String> rhs : productions.get(lhs)) {
                orderedProductions.add(new AbstractMap.SimpleEntry<>(lhs.get(0), rhs));
            }
        }
        return orderedProductions.stream().sorted().collect(Collectors.toList());
    }
}
