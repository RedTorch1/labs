package repository;

import dao.impl.*;
import model.*;
import org.junit.jupiter.api.*;
import repository.impl.*;
import java.sql.*;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryIntegrationTest {
    private static Connection conn;
    private static UserRepository userRepo;
    private static FunctionRepository funcRepo;
    private static PointRepository pointRepo;

    private static long userId;
    private static long funcId;
    private static long pointId;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");

        userRepo = new UserRepositoryImpl(new UserDaoImpl(conn));
        funcRepo = new FunctionRepositoryImpl(new FunctionDaoImpl(conn));
        pointRepo = new PointRepositoryImpl(new PointDaoImpl(conn));
    }

    @Test @Order(1)
    void testUserCreation() {
        User u = userRepo.create("repo_user_" + System.nanoTime(), "hash");
        List<User> all = userRepo.findAll();
        Assertions.assertTrue(all.size() > 0);
        userId = all.get(all.size() - 1).getId();
    }

    @Test @Order(2)
    void testFunctionCreation() {
        FunctionEntity f = funcRepo.create(userId, "square", "x*x");
        List<FunctionEntity> list = funcRepo.findByUser(userId);
        Assertions.assertFalse(list.isEmpty());
        funcId = list.get(0).getId();
    }

    @Test @Order(3)
    void testPointCreation() {
        PointEntity p = pointRepo.create(funcId, 2.0, 4.0);
        List<PointEntity> list = pointRepo.findByFunction(funcId);
        Assertions.assertFalse(list.isEmpty());
        pointId = list.get(0).getId();
    }

    @Test @Order(4)
    void testDeletions() {
        pointRepo.delete(pointId);
        funcRepo.delete(funcId);
        userRepo.delete(userId);

        Assertions.assertTrue(pointRepo.findByFunction(funcId).isEmpty());
        Assertions.assertTrue(funcRepo.findByUser(userId).isEmpty());
    }

    @AfterAll
    static void cleanup() throws Exception {
        conn.close();
    }
}
