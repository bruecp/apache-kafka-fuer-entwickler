package at.javatraining.javademos;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 5, 7, 9, 10, -1, 13, -2);

        System.out.println("Before streaming: " + numbers);
        longVariant(numbers);


        shortVariant(numbers);
    }

    private static void longVariant(List<Integer> numbers) {
        // request: square of all even numbers greater than 0
        List<Integer> result = numbers.stream()  // 1x Create a stream
                .filter(new Predicate<Integer>() {  // 0 - N x Intermediate method
                    @Override
                    public boolean test(Integer number) {
                        return number > 0 && number % 2 == 0;
                    }
                })
                .map(new Function<Integer, Integer>() { // 0 - N x Intermediate method
                    @Override
                    public Integer apply(Integer number) {
                        return number * number;
                    }
                })
                .toList(); // 1 x Terminal methode

        System.out.println("After streaming (long): " + result);
        result.forEach(number -> System.out.println(number)); // lambda

    }

    private static void shortVariant(List<Integer> numbers) {
        // request: square of all even numbers greater than 0

        List<Integer> result = numbers.stream()                     // 1x Create a stream
                .filter(number -> number > 0 && number % 2 == 0)    // 0 - N x Intermediate method
                .map(number -> number * number)                      // 0 - N x Intermediate method
                .toList();                                             // 1 x Terminal methode

        System.out.println("After streaming (short): " + result);
        result.forEach(System.out::println); // method reference

    }
}
