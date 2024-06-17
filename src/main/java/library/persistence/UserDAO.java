package library.persistence;

import org.example.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDAO {
    void saveUser(User user);
    Collection<User> getAllUsers();
    Optional<User> findUserByEmail(String email); // Use Optional
}
