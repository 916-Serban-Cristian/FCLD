import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State {
    public final Set<Item> items;
    public final StateType stateType;

    public State(Set<Item> items) {
        this.items = items;
        this.stateType = (items.size() == 1 && items.iterator().next().rhs.size() == items.iterator().next().dotPosition && items.iterator().next().lhs.equals(Grammar.enrichedGrammarStartingSymbol))
                ? StateType.ACCEPT
                : (items.size() == 1 && items.iterator().next().rhs.size() == items.iterator().next().dotPosition)
                ? StateType.REDUCE
                : (items.size() > 0 && items.stream().allMatch(it -> it.rhs.size() > it.dotPosition))
                ? StateType.SHIFT
                : (items.size() > 1 && items.stream().allMatch(it -> it.rhs.size() == it.dotPosition))
                ? StateType.REDUCE_REDUCE_CONFLICT
                : StateType.SHIFT_REDUCE_CONFLICT;
    }

    public List<String> getSymbolsSucceedingTheDot() {
        Set<String> symbols = new HashSet<>();
        for (Item item : items) {
            if (item.dotPosition >= 0 && item.dotPosition < item.rhs.size()) {
                symbols.add(item.rhs.get(item.dotPosition));
            }
        }
        return new ArrayList<>(symbols);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", stateType, items);
    }
}
