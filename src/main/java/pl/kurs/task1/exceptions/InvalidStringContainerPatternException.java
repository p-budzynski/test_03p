package pl.kurs.task1.exceptions;

public class InvalidStringContainerPatternException extends RuntimeException {
    public InvalidStringContainerPatternException(String pattern) {
        super("Invalid pattern: " + pattern);
    }
}
