import java.util.Hashtable;

/**
 * This class implements a symbol table for the compiler. It is a wrapper around
 * the Java Hashtable class. It stores the tokens as values and their hash codes
 * as keys.
 *
 * @param <T> The type of the tokens.
 */
public class SymbolTable<T> {
    private final Hashtable<Integer, T> table;

    public SymbolTable() {
        table = new Hashtable<>();
    }

    /**
     * Add a new token to the symbol table.
     *
     * @param symbol The token to add.
     */
    public void add(T symbol) {
        table.put(symbol.hashCode(), symbol);
    }

    /**
     * Get token at a given position.
     *
     * @param position The position of the symbol
     * @return The symbol at the position
     */
    public T get(Integer position) {
        return table.get(position);
    }

    /**
     * Looks for a token in the symbol table and returns its position.
     * If the token is not found, it is added to the table and its position is returned.
     *
     * @param symbol The token to look for.
     * @return The position of the token in the symbol table.
     */
    public Integer pos(T symbol) {
        if (!table.contains(symbol.hashCode())) {
            add(symbol);
        }
        return symbol.hashCode();
    }

}
