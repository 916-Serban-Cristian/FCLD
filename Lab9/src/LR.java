import java.util.*;

public class LR {
    private final Grammar grammar;
    private final Grammar workingGrammar;

    private final List<Map.Entry<String, List<String>>> orderedProductions;

    public LR(Grammar grammar) {
        this.grammar = grammar;
        workingGrammar = (grammar.isEnriched) ? grammar : grammar.getEnrichedGrammar();
        orderedProductions = workingGrammar.productionSet.getOrderedProductions();
    }

    private String getDotPrecededNonTerminal(Item item) {
        String term = item.dotPosition > 0 && item.dotPosition < item.rhs.size() ? item.rhs.get(item.dotPosition) : null;
        if (!grammar.nonTerminals.contains(term)) {
            return null;
        }
        return term;
    }

    private State closure(Item item) {
        Set<Item> oldClosure;
        Set<Item> currentClosure = new HashSet<>();
        currentClosure.add(item);
        do {
            oldClosure = currentClosure;
            Set<Item> newClosure = new HashSet<>(currentClosure);
            for (Item it : currentClosure) {
                String nonTerminal = getDotPrecededNonTerminal(it);
                if (nonTerminal == null) continue;
                for (var production : grammar.productionSet.getProductionsOf(nonTerminal)) {
                    Item currentItem = new Item(nonTerminal, production, 0);
                    newClosure.add(currentItem);
                }
            }
            currentClosure = newClosure;
        } while (!oldClosure.equals(currentClosure));
        return new State(currentClosure);
    }

    private State goTo(State state, String element) {
        Set<Item> result = new HashSet<>();
        for (Item item : state.items) {
            String nonTerminal = item.dotPosition > 0 && item.dotPosition < item.rhs.size() ? item.rhs.get(item.dotPosition) : null;
            if (Objects.equals(nonTerminal, element)) {
                Item nextItem = new Item(item.lhs, item.rhs, item.dotPosition + 1);
                result.addAll(closure(nextItem).items);
            }
        }
        return new State(result);
    }

    public CanonicalCollection canonicalCollection() {
        CanonicalCollection canonicalCollection = new CanonicalCollection();
        canonicalCollection.addState(closure(new Item(workingGrammar.startingSymbol, workingGrammar.productionSet.getProductionsOf(workingGrammar.startingSymbol).get(0), 0)));
        int i = 0;
        while (i < canonicalCollection.states.size()) {
            for (String symbol : canonicalCollection.states.get(i).getSymbolsSucceedingTheDot()) {
                State newState = goTo(canonicalCollection.states.get(i), symbol);
                int indexInStates = canonicalCollection.states.indexOf(newState);
                if (indexInStates == -1) {
                    canonicalCollection.addState(newState);
                    indexInStates = canonicalCollection.states.size() - 1;
                }
                canonicalCollection.connectStates(i, symbol, indexInStates);
            }
            i++;
        }
        return canonicalCollection;
    }
}
