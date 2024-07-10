package pl.kurs.task1.exceptions;

public class InvalidStringContainerValueException extends RuntimeException {
    public InvalidStringContainerValueException(String value) {
        super("Invalid value: " + value);
    }
}
