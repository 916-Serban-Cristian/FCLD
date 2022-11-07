import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class FiniteAutomaton {
    private List<String> states;
    private List<String> alphabet;
    private String initialState;
    private List<String> outputStates;
    private List<Transition> transitions;

    public FiniteAutomaton(String filename) {
        try {
            var reader = new BufferedReader(new FileReader(filename));
            reader.lines().forEach(line -> {
                var pattern = Pattern.compile("^([a-z_]*)=");
                var matcher = pattern.matcher(line);
                if (matcher.find()) {
                    var key = matcher.group(1);
                    var value = line.substring(matcher.end()).replaceAll("[{}]", "");
                    switch (key) {
                        case "states" -> states = List.of(value.split(","));
                        case "alphabet" -> alphabet = List.of(value.split(","));
                        case "in_state" -> initialState = value;
                        case "out_states" -> outputStates = List.of(value.split(","));
                        case "transitions" -> {
                            var transitionStrings = value.split(";");
                            transitions = new ArrayList<>();
                            for (var transitionString : transitionStrings) {
                                var transitionParts = transitionString.split(",");
                                transitions.add(new Transition(transitionParts[0].strip(), transitionParts[1].strip(), transitionParts[2].strip()));
                            }
                        }
                    }
                }
            });
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private List<String> stringToList(String string) {
        return List.of(string.split(""));
    }

    private String listToString(List<String> list) {
        return String.join("", list);
    }

    private void printListOfString(String listName, List<String> list) {
        System.out.print(listName + "={");
        IntStream.range(0, list.size()).forEach(i -> {
            System.out.print(list.get(i));
            if (i != list.size() - 1) System.out.print(",");
        });
        System.out.println("}");
    }

    public void printStates() {
        printListOfString("states", states);
    }

    public void printAlphabet() {
        printListOfString("alphabet", alphabet);
    }

    public void printInitialState() {
        System.out.println("initialState=" + initialState);
    }

    public void printOutputStates() {
        printListOfString("outputStates", outputStates);
    }

    public void printTransitions() {
        System.out.println("transitions={");
        transitions.forEach(System.out::println);
        System.out.println("}");
    }

    public boolean checkAccepted(List<String> word) {
        var currentState = initialState;
        for (var letter : word) {
            var found = false;
            for (var transition : transitions) {
                if (transition.from().equals(currentState) && transition.label().equals(letter)) {
                    currentState = transition.to();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        for (var outputState : outputStates) {
            if (outputState.equals(currentState)) return true;
        }
        return false;
    }

    public boolean checkAccepted(String word) {
        return checkAccepted(stringToList(word));
    }

    public List<String> getNextAccepted(List<String> word) {
        var currentState = initialState;
        var nextAccepted = new ArrayList<String>();
        for (var letter : word) {
            var found = false;
            for (var transition : transitions) {
                if (transition.from().equals(currentState) && transition.label().equals(letter)) {
                    currentState = transition.to();
                    found = true;
                    break;
                }
            }
            if (!found) return null;
            nextAccepted.add(letter);
        }
        for (var outputState : outputStates) {
            if (outputState.equals(currentState)) return nextAccepted;
        }
        return null;
    }

    public String getNextAccepted(String word) {
        return listToString(getNextAccepted(stringToList(word)));
    }
}
