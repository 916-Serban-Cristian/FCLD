public record Transition(String from, String to, String label) {
    public String toString() {
        return from + " -> " + to + " : " + label;
    }
}
