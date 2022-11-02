import java.util.Hashtable;

/**
 * This class implements a symbol table for the compiler. It is a wrapper around
 * the Java Hashtable class. It stores the tokens as values and their hash codes
 * as keys.
 *
 * @param <T> The type of the tokens.
 */
public class SymbolTable<T> {
    private final Hashtable<Integer, T> identifierTable;
    private final Hashtable<Integer, T> intConstantsTable;
    private final Hashtable<Integer, T> stringConstantsTable;

    public SymbolTable() {
        identifierTable = new Hashtable<>();
        intConstantsTable = new Hashtable<>();
        stringConstantsTable = new Hashtable<>();
    }

    /**
     * Add a new token to the identifier table.
     *
     * @param symbol The token to add.
     */
    public void addIdentifier(T symbol) {
        identifierTable.put(symbol.hashCode(), symbol);
    }

    /**
     * Add a new token to the int constants table.
     *
     * @param symbol The token to add.
     */
    public void addIntConstant(T symbol) {
        intConstantsTable.put(symbol.hashCode(), symbol);
    }

    /**
     * Add a new token to the string constants table.
     *
     * @param symbol The token to add.
     */
    public void addStringConstant(T symbol) {
        stringConstantsTable.put(symbol.hashCode(), symbol);
    }

    /**
     * Get identifier at a given position.
     *
     * @param position The position of the symbol
     * @return The symbol at the position
     */
    public T getIdentifier(Integer position) {
        return identifierTable.get(position);
    }

    /**
     * Get int constant at a given position.
     *
     * @param position The position of the symbol
     * @return The symbol at the position
     */
    public T getIntConstant(Integer position) {
        return intConstantsTable.get(position);
    }

    /**
     * Get string constant at a given position.
     *
     * @param position The position of the symbol
     * @return The symbol at the position
     */
    public T getStringConstant(Integer position) {
        return stringConstantsTable.get(position);
    }

    /**
     * Looks for an identifier in the symbol table and returns its position.
     * If the identifier is not found, it is added to the table and its position is returned.
     *
     * @param symbol The token to look for.
     * @return The position of the token in the symbol table.
     */
    public Integer posIdentifier(T symbol) {
        if (!identifierTable.contains(symbol.hashCode())) {
            addIdentifier(symbol);
        }
        return symbol.hashCode();
    }

    @Override
    public String toString() {
        var str = new StringBuilder("Identifier table:\n");
        identifierTable.forEach((key, value) -> str.append(key).append(" -> ").append(value).append('\n'));
        str.append("\nInt constants table:\n");
        intConstantsTable.forEach((key, value) -> str.append(key).append(" -> ").append(value).append('\n'));
        str.append("\nString constants table:\n");
        stringConstantsTable.forEach((key, value) -> str.append(key).append(" -> ").append(value).append('\n'));
        return str.toString();
    }
}
