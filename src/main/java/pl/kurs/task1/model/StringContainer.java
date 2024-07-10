package pl.kurs.task1.model;

import pl.kurs.task1.exceptions.DuplicatedElementOnListException;
import pl.kurs.task1.exceptions.InvalidStringContainerPatternException;
import pl.kurs.task1.exceptions.InvalidStringContainerValueException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class StringContainer {
    private static final String REGEX_SEMICOLON = ";";

    private static class Node {
        String value;
        LocalDateTime dateTime;
        Node next;

        Node(String value, LocalDateTime dateTime) {
            this.value = value;
            this.dateTime = dateTime;
            this.next = null;
        }
    }

    private Pattern pattern;
    private Node head;
    private boolean noDuplicates;

    public StringContainer(String pattern) {
        this(pattern, false);
    }

    public StringContainer(String pattern, boolean noDuplicates) {
        try {
            this.pattern = Pattern.compile(pattern);
        } catch (Exception ex) {
            throw new InvalidStringContainerPatternException(pattern);
        }
        this.noDuplicates = noDuplicates;
        this.head = null;
    }

    public void add(String value) {
        if (!pattern.matcher(value).matches()) {
            throw new InvalidStringContainerValueException(value);
        }
        if (noDuplicates && contains(value)) {
            throw new DuplicatedElementOnListException(value);
        }

        Node newNode = new Node(value, LocalDateTime.now());
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
    }

    public void remove(String value) {
        if (head == null) {
            return;
        }
        if (head.value.equals(value)) {
            head = head.next;
            return;
        }

        Node current = head;
        while (current.next != null && !current.next.value.equals(value)) {
            current = current.next;
        }
        if (current.next != null) {
            current.next = current.next.next;
        }
    }

    public int size() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public String get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public StringContainer getDataBetween(LocalDateTime dateFrom, LocalDateTime dateTo) {
        StringContainer result = new StringContainer(pattern.toString(), noDuplicates);

        Node current = head;
        while (current != null) {
            if ((dateFrom == null || !current.dateTime.isBefore(dateFrom)) &&
                    (dateTo == null || !current.dateTime.isAfter(dateTo))) {
                result.add(current.value);
            }
            current = current.next;
        }
        return result;
    }

    public void storeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String patternLine = pattern.pattern();
            String duplicated = String.valueOf(noDuplicates);
            writer.write(patternLine + REGEX_SEMICOLON + duplicated + "\n");
            Node current = head;
            while (current != null) {
                writer.write(current.value + REGEX_SEMICOLON + current.dateTime + "\n");
                current = current.next;
            }
        }
    }

    public static StringContainer fromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                throw new IllegalArgumentException("Invalid input: Empty or null line detected.");
            }
            String[] split = line.split(REGEX_SEMICOLON);

            String pattern = split[0];
            boolean duplicate = Boolean.parseBoolean(split[1]);

            StringContainer result = new StringContainer(pattern, duplicate);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(REGEX_SEMICOLON);

                String value = parts[0];
                LocalDateTime dateTime = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_DATE_TIME);
                Node newNode = new Node(value, dateTime);
                if (result.head == null) {
                    result.head = newNode;
                } else {
                    Node current = result.head;
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
            }
            return result;
        }
    }

    private boolean contains(String value) {
        Node current = head;
        while (current != null) {
            if (current.value.equals(value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString() {
        return head.value;
    }
}
