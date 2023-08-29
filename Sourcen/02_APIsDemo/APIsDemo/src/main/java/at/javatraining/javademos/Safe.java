package at.javatraining.javademos;

import java.util.Objects;

public class Safe<K extends String /* type of password */, V /* type of value */> {
    private K password;
    private V value;

    public Safe(K password, V value) {
        this.password = password;
        this.value = value;
    }

    public V getValue(K password) {
        if (!Objects.equals(password, this.password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return value;
    }
}
