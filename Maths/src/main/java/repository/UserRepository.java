package repository;

import model.User;
import java.util.List;

public interface UserRepository {
    User create(String username, String passwordHash);
    User findById(long id);
    List<User> findAll();
    void delete(long id);
}