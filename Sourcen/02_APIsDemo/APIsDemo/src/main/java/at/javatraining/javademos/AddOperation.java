package at.javatraining.javademos;

public class AddOperation implements Operation{
    @Override
    public int operate(int nr1, int nr2) {
        return nr1 + nr2;
    }
}
