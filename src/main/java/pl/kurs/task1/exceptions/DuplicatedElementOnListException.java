package pl.kurs.task1.exceptions;

public class DuplicatedElementOnListException extends RuntimeException {
    public DuplicatedElementOnListException(String value) {
        super("Duplicated value: " + value);
    }
}
