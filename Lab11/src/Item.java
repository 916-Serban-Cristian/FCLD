import java.util.List;

public record Item(String lhs, List<String> rhs, int dotPosition) {
    public String toString() {
        var rhs1 = rhs.subList(0, dotPosition);
        var rhs2 = rhs.subList(dotPosition, rhs.size());
        return String.format("%s -> %s.%s", lhs, String.join("", rhs1), String.join("", rhs2));
    }
}