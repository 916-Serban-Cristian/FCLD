import java.util.*;
import java.util.stream.IntStream;

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
        var term = item.dotPosition() >= 0 && item.dotPosition() < item.rhs().size() ? item.rhs().get(item.dotPosition()) : null;
        if (!grammar.nonTerminals.contains(term)) {
            return null;
        }
        return term;
    }

    private State closure(Item item) {
        Set<Item> oldClosure;
        var currentClosure = new LinkedHashSet<Item>();
        currentClosure.add(item);
        do {
            oldClosure = currentClosure;
            var newClosure = new LinkedHashSet<>(currentClosure);
            for (var it : currentClosure) {
                var nonTerminal = getDotPrecededNonTerminal(it);
                if (nonTerminal == null) continue;
                for (var production : grammar.productionSet.getProductionsOf(nonTerminal)) {
                    var currentItem = new Item(nonTerminal, production, 0);
                    newClosure.add(currentItem);
                }
            }
            currentClosure = newClosure;
        } while (!oldClosure.equals(currentClosure));
        return new State(currentClosure, State.getStateType(currentClosure));
    }

    private State goTo(State state, String element) {
        var result = new LinkedHashSet<Item>();
        for (Item item : state.items()) {
            String nonTerminal = item.dotPosition() >= 0 && item.dotPosition() < item.rhs().size() ? item.rhs().get(item.dotPosition()) : null;
            if (Objects.equals(nonTerminal, element)) {
                Item nextItem = new Item(item.lhs(), item.rhs(), item.dotPosition() + 1);
                result.addAll(closure(nextItem).items());
            }
        }
        return new State(result, State.getStateType(result));
    }

    public CanonicalCollection canonicalCollection() {
        var canonicalCollection = new CanonicalCollection();
        canonicalCollection.addState(closure(new Item(workingGrammar.startingSymbol, workingGrammar.productionSet.getProductionsOf(workingGrammar.startingSymbol).get(0), 0)));
        int i = 0;
        while (i < canonicalCollection.states.size()) {
            for (var symbol : canonicalCollection.states.get(i).getSymbolsSucceedingTheDot()) {
                var newState = goTo(canonicalCollection.states.get(i), symbol);
                int indexInStates = canonicalCollection.states.indexOf(newState);
                if (indexInStates == -1) {
                    canonicalCollection.addState(newState);
                    indexInStates = canonicalCollection.states.size() - 1;
                }
                canonicalCollection.connectStates(i, symbol, indexInStates);
            }
            ++i;
        }
        return canonicalCollection;
    }

    public Table getParsingTable() {
        var canonicalCollection = canonicalCollection();
        var table = new Table(new HashMap<>());
        canonicalCollection.adjacencyList.forEach((key, value) -> {
            State state = canonicalCollection.states.get(key.getKey());
            if (!table.tableRow.containsKey(key.getKey())) {
                table.tableRow.put(key.getKey(), new Row(state.stateType(), new HashMap<>(), null));
            }
            table.tableRow.get(key.getKey()).goTo.put(key.getValue(), value);
        });
        IntStream.range(0, canonicalCollection.states.size()).forEach((index) -> {
            State state = canonicalCollection.states.get(index);
            if (state.stateType() == StateType.REDUCE) {
                table.tableRow.put(index, new Row(state.stateType(), null, orderedProductions.indexOf(new AbstractMap.SimpleEntry<>(state.items().iterator().next().lhs(), state.items().iterator().next().rhs()))));
            }
            if (state.stateType() == StateType.ACCEPT) {
                table.tableRow.put(index, new Row(state.stateType(), null, null));
            }
        });
        return table;
    }

    public List<ParsingTreeRow> parse(List<String> word) throws Exception {
        var workingStack = new ArrayList<Map.Entry<String, Integer>>();
        var remainingStack = new ArrayList<>(word);
        var parsingTable = getParsingTable();
        workingStack.add(new AbstractMap.SimpleEntry<>("$", 0));

        var parsingTree = new ArrayList<ParsingTreeRow>();
        var treeStack = new ArrayList<Map.Entry<String, Integer>>();
        int currentIndex = 0;
        while (!remainingStack.isEmpty() || !workingStack.isEmpty()) {
            var tableValue = parsingTable.tableRow.get(workingStack.get(workingStack.size() - 1).getValue());
            if (tableValue == null) {
                throw new Exception("Invalid state " + workingStack.get(workingStack.size() - 1).getValue() + " in the working stack");
            }
            switch (tableValue.action) {
                case SHIFT -> {
                    String token = remainingStack.isEmpty() ? null : remainingStack.get(0);
                    if (token == null) {
                        throw new Exception("Action is shift but nothing else is left in the remaining stack");
                    }
                    Map<String, Integer> goTo = tableValue.goTo;
                    if (goTo == null) {
                        throw new Exception("Invalid goto value for state " + workingStack.get(workingStack.size() - 1).getValue());
                    }
                    Integer value = goTo.get(token);
                    if (value == null) {
                        throw new Exception("Invalid symbol \"" + token + "\" for goto of state " + workingStack.get(workingStack.size() - 1).getValue());
                    }
                    workingStack.add(new AbstractMap.SimpleEntry<>(token, value));
                    remainingStack.remove(0);
                    treeStack.add(new AbstractMap.SimpleEntry<>(token, currentIndex++));
                }
                case ACCEPT -> {
                    var lastElement = treeStack.remove(treeStack.size() - 1);
                    parsingTree.add(new ParsingTreeRow(lastElement.getValue(), lastElement.getKey(), -1, -1));
                    if (!remainingStack.isEmpty()) {
                        throw new Exception("Action is accept but the remaining stack is not empty");
                    }
                    return parsingTree;
                }
                case REDUCE -> {
                    var productionToReduceTo = orderedProductions.get(tableValue.reductionIndex);
                    if (productionToReduceTo == null) {
                        throw new Exception("Invalid reduction index " + tableValue.reductionIndex);
                    }
                    int parentIndex = currentIndex++;
                    int lastIndex = -1;
                    for (int j = 0; j < productionToReduceTo.getValue().size(); j++) {
                        workingStack.remove(workingStack.size() - 1);
                        var lastElement = treeStack.remove(treeStack.size() - 1);
                        parsingTree.add(new ParsingTreeRow(lastElement.getValue(), lastElement.getKey(), parentIndex, lastIndex));
                        lastIndex = lastElement.getValue();
                    }
                    treeStack.add(new AbstractMap.SimpleEntry<>(productionToReduceTo.getKey(), parentIndex));
                    var previous = workingStack.get(workingStack.size() - 1);
                    workingStack.add(new AbstractMap.SimpleEntry<>(productionToReduceTo.getKey(), parsingTable.tableRow.get(previous.getValue()).goTo.get(productionToReduceTo.getKey())));
                }
                case SHIFT_REDUCE_CONFLICT -> {
                    throw new Exception("Shift reduce conflict");
                }
            }
        }
        return parsingTree;
    }
}
