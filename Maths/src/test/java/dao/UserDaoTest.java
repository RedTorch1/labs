package dao;

import dao.impl.UserDaoImpl;
import model.User;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

public class UserDaoTest {
    private static Connection conn;
    private static UserDao dao;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");
        dao = new UserDaoImpl(conn);
    }

    @Test
    void testInsertFindUpdateDelete() {
        User u = new User(0, "test_" + System.nanoTime(), "hash123");
        dao.insert(u);

        List<User> users = dao.findAll();
        Assertions.assertTrue(users.size() > 0);

        User first = users.get(0);
        first.setUsername("updated_name");
        dao.update(first);

        dao.delete(first.getId());
    }

    @AfterAll
    static void cleanup() throws Exception {
        conn.close();
    }
}
