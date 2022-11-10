import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        var fa = new FiniteAutomaton("resources/fa.in");
        System.out.println("1. Print states");
        System.out.println("2. Print alphabet");
        System.out.println("3. Print output states");
        System.out.println("4. Print in state");
        System.out.println("5. Print transitions");
        System.out.println("6. Check word");
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
                case "6" -> System.out.println(fa.checkAccepted(scanner.nextLine()));
                case "0" -> System.exit(0);
            }
        }
    }
}
