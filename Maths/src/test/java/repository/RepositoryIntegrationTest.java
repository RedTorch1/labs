package repository;

import dao.impl.FunctionDaoImpl;
import dao.impl.PointDaoImpl;
import dao.impl.UserDaoImpl;
import model.FunctionEntity;
import model.PointEntity;
import model.User;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryIntegrationTest {
    private static Connection conn;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;
    private static PointDaoImpl pointDao;

    private static long userId;
    private static long functionId;
    private static long pointId;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");

        userDao = new UserDaoImpl(conn);
        functionDao = new FunctionDaoImpl(conn);
        pointDao = new PointDaoImpl(conn);

        System.out.println("🚀 Manual Complete Integration Test Started");
    }

    @Test
    @Order(1)
    void testCompleteWorkflow() {
        // 1. Create User
        User user = new User(0, "integration_user_" + System.nanoTime(), "integration_hash");
        userDao.insert(user);

        List<User> users = userDao.findAll();
        userId = users.stream()
                .filter(u -> u.getUsername().startsWith("integration_user_"))
                .findFirst()
                .map(User::getId)
                .orElseThrow();

        System.out.println("✅ Step 1: User created with ID: " + userId);

        // 2. Create Function
        FunctionEntity function = new FunctionEntity(0, userId, "integration_function", "x * 2");
        functionDao.insert(function);

        List<FunctionEntity> functions = functionDao.findByUser(userId);
        Assertions.assertFalse(functions.isEmpty());
        functionId = functions.get(0).getId();

        System.out.println("✅ Step 2: Function created with ID: " + functionId);

        // 3. Create Point
        PointEntity point = new PointEntity(0, functionId, 5.0, 10.0);
        pointDao.insert(point);

        List<PointEntity> points = pointDao.findByFunction(functionId);
        Assertions.assertFalse(points.isEmpty());
        pointId = points.get(0).getId();

        System.out.println("✅ Step 3: Point created with ID: " + pointId);

        // 4. Verify all data exists
        User foundUser = userDao.findById(userId);
        FunctionEntity foundFunction = functionDao.findById(functionId);
        List<PointEntity> foundPoints = pointDao.findByFunction(functionId);

        Assertions.assertNotNull(foundUser);
        Assertions.assertNotNull(foundFunction);
        Assertions.assertFalse(foundPoints.isEmpty());

        System.out.println("✅ Step 4: All data verification passed");
    }

    @Test
    @Order(2)
    void testCleanup() {
        // Clean up in reverse order (points -> functions -> users)
        pointDao.delete(pointId);
        functionDao.delete(functionId);
        userDao.delete(userId);

        // Verify cleanup
        Assertions.assertTrue(pointDao.findByFunction(functionId).isEmpty());
        Assertions.assertTrue(functionDao.findByUser(userId).isEmpty());
        Assertions.assertNull(userDao.findById(userId));

        System.out.println("✅ Step 5: Cleanup completed successfully");
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        System.out.println("🎯 Manual Complete Integration Test Finished");
    }
}