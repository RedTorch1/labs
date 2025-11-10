package dao;

import model.User;
import java.util.List;

public interface UserDao {
    void insert(User user);
    User findById(long id);
    List<User> findAll();
    void update(User user);
    void delete(long id);
}
