import java.util.List;

public class Item {
    public final String lhs;
    public final List<String> rhs;
    public final int dotPosition;

    public Item(String lhs, List<String> rhs, int dotPosition) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.dotPosition = dotPosition;
    }

    @Override
    public String toString() {
        var rhs1 = rhs.subList(0, dotPosition);
        var rhs2 = rhs.subList(dotPosition, rhs.size());
        return String.format("%s -> %s.%s", lhs, String.join("", rhs1), String.join("", rhs2));
    }
}
