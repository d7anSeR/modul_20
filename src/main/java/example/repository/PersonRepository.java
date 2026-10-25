package example.repository;

import example.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Person person) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                session.save(person);
                trans.commit();
            } catch (RuntimeException e) {
                trans.rollback();
                throw e;
            }
        }
    }

    public Optional<Person> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Person person = session.get(Person.class, id);
            if (person != null) {
                Hibernate.initialize(person.getSocialNetworks());
            }
            return Optional.ofNullable(person);
        }
    }

    public List<Person> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Person> list = session.createQuery("from Person", Person.class).list();
            // ponytail: N+1 запросов на коллекции, join fetch если списков станет много
            list.forEach(p -> Hibernate.initialize(p.getSocialNetworks()));
            return list;
        }
    }

    /** @return true, если человек существовал и был удалён (строки join-таблицы Hibernate удалит сам) */
    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                Person person = session.get(Person.class, id);
                if (person != null) {
                    session.delete(person);
                }
                trans.commit();
                return person != null;
            } catch (RuntimeException e) {
                trans.rollback();
                throw e;
            }
        }
    }
}
