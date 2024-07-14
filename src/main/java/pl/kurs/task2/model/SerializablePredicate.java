package pl.kurs.task2.model;

import java.io.Serializable;
import java.util.function.Predicate;

@FunctionalInterface
public interface SerializablePredicate<T> extends Predicate<T>, Serializable {
}
