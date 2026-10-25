package example.repository;

import example.model.Person;
import example.model.SocialNetwork;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SocialNetworkRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(SocialNetwork socialNetwork) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                session.save(socialNetwork);
                trans.commit();
            } catch (RuntimeException e) {
                trans.rollback();
                throw e;
            }
        }
    }

    public Optional<SocialNetwork> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            SocialNetwork socialNetwork = session.get(SocialNetwork.class, id);
            if (socialNetwork != null) {
                initialize(socialNetwork);
            }
            return Optional.ofNullable(socialNetwork);
        }
    }

    public List<SocialNetwork> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<SocialNetwork> list = session.createQuery("from SocialNetwork", SocialNetwork.class).list();
            // ponytail: N+1 запросов на коллекции, join fetch если списков станет много
            list.forEach(this::initialize);
            return list;
        }
    }

    /** @return true, если соцсеть существовала и была удалена (вместе с её аккаунтами) */
    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                SocialNetwork socialNetwork = session.get(SocialNetwork.class, id);
                if (socialNetwork != null) {
                    // Person - владелец связи: без этого строки в join-таблице нарушат FK
                    for (Person person : socialNetwork.getPersons()) {
                        person.getSocialNetworks().remove(socialNetwork);
                    }
                    session.delete(socialNetwork);
                }
                trans.commit();
                return socialNetwork != null;
            } catch (RuntimeException e) {
                trans.rollback();
                throw e;
            }
        }
    }

    // Сессия закрывается при выходе из репозитория, ленивые коллекции нужно загрузить до этого.
    private void initialize(SocialNetwork socialNetwork) {
        Hibernate.initialize(socialNetwork.getAccounts());
        Hibernate.initialize(socialNetwork.getPersons());
    }
}
