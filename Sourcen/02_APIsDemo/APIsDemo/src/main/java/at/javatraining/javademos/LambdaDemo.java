package at.javatraining.javademos;

public class LambdaDemo {
    public static void main(String[] args) {
        // Variante 1: concrete class
        Calculator calculator1 = new Calculator(new AddOperation());

        System.out.println(calculator1.calculate(1, 2));

        // Variante 2: ananymous inner class
        Calculator calculator2 = new Calculator(new Operation() {
            @Override
            public int operate(int nr1, int nr2) {
                return nr1 + nr2;
            }
        });

        System.out.println(calculator2.calculate(1, 2));

        // Variante 3a: Lambda (soforn nur eine Methode im Interface definiert ist!)
        Calculator calculator3 = new Calculator (
            (nr1, nr2) -> {
                return nr1 + nr2;
            }
        );

        System.out.println(calculator3.calculate(1, 2));

        // Variante 3b: Lambda kompaktest und mit anderen Operator
        Calculator calculator4 = new Calculator (
                (nr1, nr2) -> nr1 * nr2
        );

        System.out.println(calculator4.calculate(1, 2));

        // Variante 3c: Lambda mit expliziter Operation
        Operation operation = (a, b) -> a - b;
        Calculator calculator5 = new Calculator (operation);

        System.out.println(calculator5.calculate(1, 2));
    }
}
