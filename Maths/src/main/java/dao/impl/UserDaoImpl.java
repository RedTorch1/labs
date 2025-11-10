package dao.impl;

import dao.UserDao;
import model.User;
import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO app_user (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.executeUpdate();
            log.info("Inserted user: {}", user.getUsername());
        } catch (SQLException e) {
            log.error("Error inserting user", e);
        }
    }

    @Override
    public User findById(long id) {
        String sql = "SELECT * FROM app_user WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            log.error("Error finding user", e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM app_user";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                ));
            }
        } catch (SQLException e) {
            log.error("Error fetching all users", e);
        }
        return list;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE app_user SET username=?, password_hash=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setLong(3, user.getId());
            ps.executeUpdate();
            log.info("Updated user id={}", user.getId());
        } catch (SQLException e) {
            log.error("Error updating user", e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM app_user WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            log.info("Deleted user id={}", id);
        } catch (SQLException e) {
            log.error("Error deleting user", e);
        }
    }
}
