package library.persistence.collectionbased;

import org.example.Library.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDAO {
    Collection<User> getAllUsers();
    Optional<User> findUserByUsername(String email,String password);

    User getUserByUsername(String email);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
    boolean saveUser(User user);

    User getUserByUsername(String username,String password);
}
