public class ParsingTreeRow {
    public final int index;
    public final String info;
    public final int parent;
    public final int rightSibling;

    public ParsingTreeRow(int index, String info, int parent, int rightSibling) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.rightSibling = rightSibling;
    }
}
