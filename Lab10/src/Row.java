import java.util.Map;

public class Row {
    public final StateType action;
    public final Map<String, Integer> goTo;
    public final Integer reductionIndex;

    public Row(StateType action, Map<String, Integer> goTo, Integer reductionIndex) {
        this.action = action;
        this.goTo = goTo;
        this.reductionIndex = reductionIndex;
    }

    @Override
    public String toString() {
        return switch (action) {
            case REDUCE -> String.format("REDUCE %d", reductionIndex);
            case ACCEPT -> "ACCEPT";
            case SHIFT -> String.format("SHIFT %s", goTo);
            default -> "";
        };
    }
}
