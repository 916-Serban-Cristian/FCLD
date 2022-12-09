import java.util.*;

public class CanonicalCollection {
    private final List<State> mutableStates = new ArrayList<>();
    public final List<State> states = Collections.unmodifiableList(mutableStates);
    private final Map<Map.Entry<Integer, String>, Integer> mutableAdjacencyList = new HashMap<>();
    public final Map<Map.Entry<Integer, String>, Integer> adjacencyList = Collections.unmodifiableMap(mutableAdjacencyList);

    public void addState(State state) {
        mutableStates.add(state);
    }

    public void connectStates(int indexFirstState, String symbol, int indexSecondState) {
        mutableAdjacencyList.put(new AbstractMap.SimpleEntry<>(indexFirstState, symbol), indexSecondState);
    }
}
