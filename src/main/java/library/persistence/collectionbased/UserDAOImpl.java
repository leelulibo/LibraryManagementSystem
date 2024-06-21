package library.persistence.collectionbased;

import org.example.Library.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final String JDBC_URL = "jdbc:sqlserver://PC025_MAINFLOOR\\MSSQLSERVER2022:1433;databaseName=Library;integratedSecurity=true";

    static {
        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();
        dao.testConnection();  // Test the connection
        User user = new User("John Doe", 123456789, LocalDate.of(1990, 1, 1), "123 Main St", "123-456-7890", "john.doe@example.com", "johndoe", "password", "password");
        boolean result = dao.saveUser(user);
        System.out.println("User saved: " + result);
    }

    public void testConnection() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (name, idNumber, dateOfBirth, address, phoneNumber, emailAddress, username, password, confirmPassword) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setLong(2, user.getIdNumber());
            pstmt.setDate(3, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, user.getEmailAddress());
            pstmt.setString(7, user.getUsername());
            pstmt.setString(8, user.getPassword());
            pstmt.setString(9, user.getConfirmPassword());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User saved successfully!");
                return true;
            } else {
                System.out.println("Failed to save user.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserByUsername(String username, String password) {
        return null;
    }

    @Override
    public User getUserByUsername(String email) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId((int) rs.getInt("id"));
                    user.setUsername(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    // Set other user properties
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log or handle the exception
        }
        return null;  // Return null if user not found
    }


    @Override
    public Optional<User> findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setIdNumber(rs.getLong("idNumber"));
                user.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
                user.setAddress(rs.getString("address"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setEmailAddress(rs.getString("emailAddress"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setConfirmPassword(rs.getString("confirmPassword"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE emailAddress = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setIdNumber(rs.getLong("idNumber"));
                user.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
                user.setAddress(rs.getString("address"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setEmailAddress(rs.getString("emailAddress"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setConfirmPassword(rs.getString("confirmPassword"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> getAllUsers() {
        // Implementation for getting all users if needed
        return null;
    }

    @Override
    public Optional<User> findUserByUsername(String username, String password) {
        return Optional.empty();
    }
}
