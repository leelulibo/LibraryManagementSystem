package library.persistence;

import org.example.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDAO {

    Collection<User> getAllUsers();
    Optional<User> findUserByEmail(String email);
    void saveUser(User user);
// Use Optional
}
//public interface PersonDAO {
//    Optional<Person> findPersonByEmail(String email);
//    Person savePerson(Person person);
//}