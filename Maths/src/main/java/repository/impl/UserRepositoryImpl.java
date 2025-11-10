package repository.impl;

import dao.UserDao;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(String username, String passwordHash) {
        User user = new User(0, username, passwordHash);
        userDao.insert(user);
        log.info("User created: {}", username);
        return user;
    }

    @Override
    public User findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void delete(long id) {
        userDao.delete(id);
        log.info("User deleted: {}", id);
    }
}
