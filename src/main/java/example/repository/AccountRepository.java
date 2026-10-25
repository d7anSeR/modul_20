package example.repository;

import example.model.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Account account) {
        // try-with-resources: сессия закроется автоматически, даже если случится ошибка
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                session.save(account);
                trans.commit();
            } catch (RuntimeException e) {
                trans.rollback(); // откатываем транзакцию при ошибке
                throw e;
            }
        }
    }

    public Optional<Account> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Account.class, id));
        }
    }

    public List<Account> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Account", Account.class).list();
        }
    }

    /** @return true, если аккаунт существовал и был удалён */
    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trans = session.beginTransaction();
            try {
                Account account = session.get(Account.class, id);
                if (account != null) {
                    session.delete(account);
                }
                trans.commit();
                return account != null;
            } catch (RuntimeException e) {
                trans.rollback();
                throw e;
            }
        }
    }
}
