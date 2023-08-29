package at.javatraining.javademos;

@FunctionalInterface
public interface Operation {
    int operate (int nr1, int nr2);

    // Concrete methods (als Beispiel was im Interface nun möglich ist. Unabhängig von Lambda).
    default void foo() {}
    static void bar() {}
}
