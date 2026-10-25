package example.service;

import example.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    void createAccount(Account account);

    Optional<Account> getAccount(Long id);

    List<Account> getAllAccounts();

    boolean deleteAccount(Long id);
}
