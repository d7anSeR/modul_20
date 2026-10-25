package example.service;

import example.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    void createPerson(Person person);

    Optional<Person> getPerson(Long id);

    List<Person> getAllPersons();

    boolean deletePerson(Long id);
}
