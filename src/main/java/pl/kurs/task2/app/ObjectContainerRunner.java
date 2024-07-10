package pl.kurs.task2.app;

import pl.kurs.task2.model.ObjectContainer;
import pl.kurs.task2.model.Person;

import java.util.List;

public class ObjectContainerRunner {
    public static void main(String[] args) {
        ObjectContainer<Person> peopleFromWarsaw = new ObjectContainer<>(p -> p.getCity().equals("Warsaw"));

        System.out.println(peopleFromWarsaw.add(new Person("Jan", "Warsaw", 30)));
        System.out.println(peopleFromWarsaw.add(new Person("Weronika", "Warsaw", 20)));
        System.out.println(peopleFromWarsaw.add(new Person("Waldek", "Monaco", 34)));
        System.out.println(peopleFromWarsaw.add(new Person("Adam", "Warsaw", 55)));
        System.out.println(peopleFromWarsaw.add(new Person("Karol", "Warsaw", 18)));

        System.out.println(peopleFromWarsaw);

        peopleFromWarsaw.removeIf(p -> p.getAge() > 50);

        List<Person> females = peopleFromWarsaw.getWithFilter(p -> p.getName().endsWith("a"));

        System.out.println(females);

        System.out.println(peopleFromWarsaw);

        peopleFromWarsaw.storeToFile("youngPeopleFromWarsaw.txt", p -> p.getAge() < 30, p -> p.getName()+";"+p.getAge()+";"+p.getCity());

        peopleFromWarsaw.storeToFile("warsawPeople.txt");

        ObjectContainer<Person> peopleFromWarsawFromFile = ObjectContainer.fromFile("warsawPeople.txt");
    }
}
