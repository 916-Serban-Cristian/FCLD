import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<String> readSequence() {
        System.out.print("Number of letters in the word: ");
        var scanner = new Scanner(System.in);
        var n = Integer.parseInt(scanner.nextLine());
        var listOfLetters = new ArrayList<String>();
        for (var i = 1; i < n; i++) {
            System.out.print("> ");
            var letter = scanner.nextLine();
            listOfLetters.add(letter);
        }
        return listOfLetters;
    }

    public static void main(String[] args) {
        var fa = new FiniteAutomaton("resources/fa.in");
        System.out.println("1. Print states");
        System.out.println("2. Print alphabet");
        System.out.println("3. Print output states");
        System.out.println("4. Print in state");
        System.out.println("5. Print transitions");
        System.out.println("6. Check word with varying length letters");
        System.out.println("7. Check word with length 1 letters");
        System.out.println("8. Get matching substring with varying length letters");
        System.out.println("9. Get matching substring with length 1 letters");
        System.out.println("0. Exit");
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            var option = scanner.nextLine();
            switch (option) {
                case "1" -> fa.printStates();
                case "2" -> fa.printAlphabet();
                case "3" -> fa.printOutputStates();
                case "4" -> fa.printInitialState();
                case "5" -> fa.printTransitions();
                case "6" -> System.out.println(fa.checkAccepted(readSequence()));
                case "7" -> System.out.println(fa.checkAccepted(scanner.nextLine()));
                case "8" -> System.out.println(fa.getNextAccepted(readSequence()));
                case "9" -> System.out.println(fa.getNextAccepted(scanner.nextLine()));
                case "0" -> System.exit(0);
            }
        }
    }
}
