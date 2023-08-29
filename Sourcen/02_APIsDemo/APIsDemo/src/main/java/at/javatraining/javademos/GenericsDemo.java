package at.javatraining.javademos;

public class GenericsDemo {
    public static void main(String[] args) {
        Safe safe1 = new Safe("topsecret", "abc"); // eq. Safe<String, Object>
        String value1 = (String) safe1.getValue("topsecret");

        System.out.println("Value1: " + value1);

        Safe<String, String> safe2 = new Safe<>("topsecret", "xyz"); // <> ... diamond operator
        String value2 = safe2.getValue("topsecret"); // no cast required

        System.out.println("Value2: " + value2);
    }
}
