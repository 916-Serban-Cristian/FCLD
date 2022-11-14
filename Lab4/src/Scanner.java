import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class implements a scanner for the compiler. It takes the program as a string, the list of reserved tokens
 * and creates the corresponding symbol table, constant table and pif.
 */
public class Scanner {
    private final String program;
    private final List<String> tokens;
    private final SymbolTable<String> symbolTable;
    private final List<Map.Entry<String, Integer>> pif;
    private int index;
    private int currentLine;

    public Scanner(String program, List<String> tokens) {
        this.program = program;
        this.tokens = tokens;
        this.symbolTable = new SymbolTable<>();
        this.pif = new ArrayList<>();
        this.index = 0;
        this.currentLine = 1;
    }

    /**
     * Skips characters that are considered blank.
     *
     * @return true if any skips happened, false otherwise.
     */
    private boolean skipWhiteSpace() {
        boolean changed = false;
        while (index < program.length() && Character.isWhitespace(program.charAt(index))) {
            if (program.charAt(index) == '\n') {
                currentLine++;
                changed = true;
            }
            index++;
        }
        return changed;
    }

    /**
     * Skip comments that start with "//".
     *
     * @return true if any comments were skipped, false otherwise.
     */
    private boolean skipComment() {
        boolean changed = false;
        if (program.startsWith("//", index)) {
            changed = true;
            while (index < program.length() && program.charAt(index) != '\n') {
                index++;
            }
        }
        return changed;
    }

    /**
     * Uses regex to match string constants.
     *
     * @return true if a match was found, false otherwise.
     * @throws ScannerException if an invalid string constant was found.
     */
    private boolean stringConstant() throws ScannerException {
        var strRegex = Pattern.compile("^\"([^\"]*)\"");
        var matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) {
            var token = matcher.group(1);
            pif.add(new AbstractMap.SimpleEntry<>("strConst", symbolTable.posStringConstant(token)));
            index += matcher.end();
            return true;
        }
        strRegex = Pattern.compile("^\"");
        matcher = strRegex.matcher(program.substring(index));
        if (matcher.find()) throw new ScannerException("Lexical error: String not closed on line " + currentLine);
        return false;
    }

    /**
     * Uses regex to match int constants.
     *
     * @return true if a match was found, false otherwise.
     */
    private boolean intConstant() {
//        var intRegex = Pattern.compile("^(-?[1-9]\\d*|0)(?!\\w)");
//        var matcher = intRegex.matcher(program.substring(index));
//        if (matcher.find()) {
//            var token = matcher.group(1);
//            if (pif.size() > 0) {
//                var pif_last = pif.get(pif.size() - 1).getValue();
//                if ((token.charAt(0) == '+' || token.charAt(0) == '-') && (pif_last == -1 || pif_last == -2)) {
//                    return false;
//                }
//            }
//            pif.add(new AbstractMap.SimpleEntry<>("intConst", symbolTable.posIntConstant(token)));
//            index += matcher.end();
//            return true;
//        }
//        return false;
        var fa = new FiniteAutomaton("resources/intConstant.in");
        var intConstant = fa.getSubstringAccepted(program.substring(index));
        if (intConstant == null) return false;
        if (intConstant.equals("0")) {
            var nextConstant = fa.getSubstringAccepted(program.substring((index + 1)));
            if (nextConstant != null) return false;
        }
        if (pif.size() > 0) {
            var pif_last = pif.get(pif.size() - 1).getValue();
            if ((intConstant.charAt(0) == '+' || intConstant.charAt(0) == '-') && (pif_last == -1 || pif_last == -2)) {
                return false;
            }
        }
        pif.add(new AbstractMap.SimpleEntry<>("intConst", symbolTable.posIntConstant(intConstant)));
        index += intConstant.length();
        return true;
    }

    /**
     * Matches the current token against the list of reserved tokens.
     *
     * @return true if a match was found, false otherwise.
     */
    private boolean tokenFromList() {
        for (var token : tokens) {
            if (program.startsWith(token, index)) {
                if (pif.size() > 0) {
                    var pif_last = pif.get(pif.size() - 1).getValue();
                    if ((token.equals("+") || token.equals("-")) && (pif_last == -1 || pif_last == -2)) {
                        pif.add(new AbstractMap.SimpleEntry<>(token, 0));
                        index++;
                        return true;
                    }
                }
                if ((token.equals("+") || token.equals("-")) && Character.isDigit(program.charAt(index + 1)))
                    return false;
                if ((token.equals("=") || token.equals("<") || token.equals(">")) && program.charAt(index + 1) == '=') {
                    pif.add(new AbstractMap.SimpleEntry<>(token + "=", 0));
                    index += 2;
                    return true;

                }
                pif.add(new AbstractMap.SimpleEntry<>(token, 0));
                index += token.length();
                return true;
            }
        }
        return false;
    }

    /**
     * Uses regex to match identifiers.
     *
     * @return true if a match was found, false otherwise.
     */
    private boolean identifier() {
//        var idRegex = Pattern.compile("^([a-zA-Z_]+[a-zA-Z0-9_]*)");
//        var matcher = idRegex.matcher(program.substring(index));
//        if (matcher.find()) {
//            var token = matcher.group(1);
//            pif.add(new AbstractMap.SimpleEntry<>("id", symbolTable.posIdentifier(token)));
//            index += matcher.end();
//            return true;
//        }
//        return false;
        var fa = new FiniteAutomaton("resources/identifier.in");
        var identifier = fa.getSubstringAccepted(program.substring(index));
        if (identifier == null) return false;
        pif.add(new AbstractMap.SimpleEntry<>("id", symbolTable.posIdentifier(identifier)));
        index += identifier.length();
        return true;
    }

    /**
     * Processes a new token.
     *
     * @throws ScannerException When a lexical error occurs.
     */
    private void nextToken() throws ScannerException {
        while (true) {
            if (!skipWhiteSpace() && !skipComment()) break;
        }
        if (index == program.length()) return;
        if (stringConstant() || intConstant() || tokenFromList() || identifier()) {
            return;
        }
        throw new ScannerException("Lexical error: Cannot classify token on line " + currentLine + ":\n" + program.substring(index, program.substring(index).indexOf('\n') + index));
    }

    /**
     * Performs all the scanning.
     *
     * @throws ScannerException When a lexical error occurs.
     */
    public void scan() throws ScannerException {
        while (index < program.length()) {
            nextToken();
        }
    }

    /**
     * Prints the symbol table to a file.
     *
     * @throws IOException When the file writer fails.
     */
    public void outST() throws IOException {
        var writer = new FileWriter("resources/ST.out");
        writer.write(symbolTable.toString());
        writer.close();
    }

    /**
     * Prints the pif to a file.
     *
     * @throws IOException When the file writer fails.
     */
    public void outPIF() throws IOException {
        var writer = new FileWriter("resources/PIF.out");
        var str = new StringBuilder();
        pif.forEach(e -> str.append(e.getKey()).append(" -> ").append(e.getValue()).append('\n'));
        writer.write(str.toString());
        writer.close();
    }
}
