import java.util.ArrayList;
import java.util.Map;

public class Table {
    public final Map<Integer, Row> tableRow;

    public Table(Map<Integer, Row> tableRow) {
        this.tableRow = tableRow;
    }

    @Override
    public String toString() {
        var entries = new ArrayList<>(tableRow.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        StringBuilder string = new StringBuilder();
        for (Map.Entry<Integer, Row> entry : entries) {
            string.append(String.format("%d: %s\n", entry.getKey(), entry.getValue()));
        }
        return string.toString();
    }
}
