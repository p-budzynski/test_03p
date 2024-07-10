package pl.kurs.task2.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer<T> implements Serializable {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Predicate<T> condition;
    private Node<T> head;

    public ObjectContainer(Predicate<T> condition) {
        this.condition = condition;
    }

    public  ObjectContainer() {
        this.condition = obj -> true;
    }

    public boolean add(T item) {
        if (condition.test(item)) {
            Node<T> newNode = new Node<>(item);
            if (head == null) {
                head = newNode;
            } else {
                Node<T> current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            return true;
        } else {
            return false;
        }
    }

    public List<T> getWithFilter(Predicate<T> filter) {
        List<T> result = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            if (filter.test(current.data)) {
                result.add(current.data);
            }
            current = current.next;
        }
        return result;
    }

    public void removeIf(Predicate<T> filter) {
        Node<T> current = head;
        Node<T> prev = null;

        while (current != null) {
            if (filter.test(current.data)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
            } else {
                prev = current;
            }
            current = current.next;
        }
    }

    public void storeToFile(String fileName, Predicate<T> filter, Function<T, String> formatter) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            Node<T> current = head;
            while (current != null) {
                if (filter.test(current.data)) {
                    writer.println(formatter.apply(current.data));
                }
                current = current.next;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void storeToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            Node<T> current = head;
            while (current != null) {
                oos.writeObject(current.data);
                current = current.next;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> ObjectContainer<T> fromFile(String fileName) {
        ObjectContainer<T> objectContainer = new ObjectContainer<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    T obj = (T) inputStream.readObject();
                    objectContainer.add(obj);
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return objectContainer;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            stringBuilder.append(current.data.toString()).append("\n");
            current = current.next;
        }
        return stringBuilder.toString();
    }
}
