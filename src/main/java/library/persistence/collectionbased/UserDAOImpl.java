package library.persistence.collectionbased;

import org.example.User;
import library.persistence.UserDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }
}
