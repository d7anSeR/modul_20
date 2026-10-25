package example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SocialNetwork {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // В одной соцсети много аккаунтов; владелец связи - Account.socialNetwork.
    // При удалении соцсети удаляются и её аккаунты.
    @OneToMany(mappedBy = "socialNetwork", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("socialNetwork")
    private List<Account> accounts = new ArrayList<>();

    // Обратная сторона связи Person <-> SocialNetwork (владелец - Person.socialNetworks).
    @ManyToMany(mappedBy = "socialNetworks")
    @JsonIgnoreProperties("socialNetworks")
    private List<Person> persons = new ArrayList<>();

    public SocialNetwork() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
