package repository;

import dao.impl.AdvancedFunctionDaoImpl;
import dao.impl.AdvancedPointDaoImpl;
import dao.impl.FunctionDaoImpl;
import dao.impl.PointDaoImpl;
import dao.impl.UserDaoImpl;
import model.FunctionEntity;
import model.PointEntity;
import model.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.impl.AdvancedUserDaoImpl;

import java.sql.*;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchSystemTest {
    private static final Logger log = LoggerFactory.getLogger(SearchSystemTest.class);

    private static Connection conn;
    private static AdvancedUserDaoImpl advancedUserDao; // ДОБАВЛЕНО
    private static AdvancedFunctionDaoImpl advancedFunctionDao;
    private static AdvancedPointDaoImpl advancedPointDao;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;
    private static PointDaoImpl pointDao;

    private static long testUserId;
    private static long testFunctionId;

    @BeforeAll
    static void setup() throws Exception {
        log.info("🚀 Initializing Search System Test");

        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");

        userDao = new UserDaoImpl(conn);
        functionDao = new FunctionDaoImpl(conn);
        pointDao = new PointDaoImpl(conn);
        advancedUserDao = new AdvancedUserDaoImpl(conn); // ДОБАВЛЕНО
        advancedFunctionDao = new AdvancedFunctionDaoImpl(conn);
        advancedPointDao = new AdvancedPointDaoImpl(conn);

        setupTestData();
    }

    static void setupTestData() {
        log.info("📝 Setting up test data...");

        // Create test user
        User testUser = new User(0, "search_test_user", "test_hash");
        userDao.insert(testUser);

        // Get user ID
        List<User> users = userDao.findAll();
        testUserId = users.stream()
                .filter(u -> u.getUsername().equals("search_test_user"))
                .findFirst()
                .map(User::getId)
                .orElseThrow();

        // Create test functions
        FunctionEntity func1 = new FunctionEntity(0, testUserId, "quadratic", "x * x");
        FunctionEntity func2 = new FunctionEntity(0, testUserId, "linear", "2 * x + 1");
        FunctionEntity func3 = new FunctionEntity(0, testUserId, "cubic_function", "x * x * x");

        functionDao.insert(func1);
        functionDao.insert(func2);
        functionDao.insert(func3);

        // Get function IDs and create points
        List<FunctionEntity> functions = functionDao.findByUser(testUserId);
        testFunctionId = functions.get(0).getId();

        for (FunctionEntity func : functions) {
            for (int i = 1; i <= 5; i++) {
                double x = i * 1.0;
                double y = func.getName().contains("quadratic") ? x * x :
                        func.getName().contains("linear") ? 2 * x + 1 : x * x * x;

                PointEntity point = new PointEntity(0, func.getId(), x, y);
                pointDao.insert(point);
            }
        }

        log.info("✅ Test data setup complete: user={}, functions={}, points created",
                testUserId, functions.size());
    }

    @Test
    @Order(1)
    void testUserSearchOperations() {
        log.info("\n🎯 TEST 1: User Search Operations");

        // Single user search
        User foundUser = advancedUserDao.findByUsername("search_test_user");
        Assertions.assertNotNull(foundUser, "Should find user by username");
        log.info("✅ Single user search: {}", foundUser);

        // Multiple user search
        List<User> usersWithSearch = advancedUserDao.findByUsernameContaining("search");
        Assertions.assertFalse(usersWithSearch.isEmpty(), "Should find users with 'search' in username");
        log.info("✅ Multiple user search: found {} users", usersWithSearch.size());

        // Sorted user search
        List<User> sortedUsers = advancedUserDao.findAllSortedByUsername(true);
        Assertions.assertFalse(sortedUsers.isEmpty(), "Should find sorted users");
        log.info("✅ Sorted user search: found {} users", sortedUsers.size());
    }

    @Test
    @Order(2)
    void testUserHierarchicalSearch() {
        log.info("\n🎯 TEST 2: User Hierarchical Search");

        Map<User, Map<Object, List<Object>>> completeHierarchy =
                advancedUserDao.getUserCompleteHierarchy(testUserId);
        Assertions.assertFalse(completeHierarchy.isEmpty(), "Should build complete hierarchy");

        log.info("✅ Complete hierarchical search: found user with data");
        completeHierarchy.forEach((user, functionsMap) -> {
            log.info("   👤 User: {}", user.getUsername());
            log.info("   📊 Has {} functions", functionsMap.size());
            functionsMap.forEach((func, points) ->
                    log.info("      📈 Function '{}' has {} points",
                            ((FunctionEntity)func).getName(), points.size())
            );
        });
    }

    @Test
    @Order(3)
    void testUserDFSandBFSSearch() {
        log.info("\n🎯 TEST 3: User DFS and BFS Search");

        // Create additional test users for connectivity
        User user2 = new User(0, "connected_user_1", "hash1");
        User user3 = new User(0, "connected_user_2", "hash2");
        userDao.insert(user2);
        userDao.insert(user3);

        // DFS Search for users
        List<User> dfsUsers = advancedUserDao.deepSearchConnectedUsers(testUserId, 2);
        log.info("✅ User DFS search: found {} connected users", dfsUsers.size());

        // BFS Search for users
        List<User> bfsUsers = advancedUserDao.breadthSearchConnectedUsers(testUserId, 2);
        log.info("✅ User BFS search: found {} connected users", bfsUsers.size());

        // Cleanup additional users
        List<User> allUsers = userDao.findAll();
        allUsers.stream()
                .filter(u -> u.getUsername().startsWith("connected_user_"))
                .forEach(u -> userDao.delete(u.getId()));
    }

    @Test
    @Order(4)
    void testUserStatisticalSearch() {
        log.info("\n🎯 TEST 4: User Statistical Search");

        // Users with functions count
        List<User> usersWithFunctions = advancedUserDao.findUsersWithFunctionCount(1);
        log.info("✅ Users with functions: found {} users", usersWithFunctions.size());

        // Users with no functions
        List<User> usersWithoutFunctions = advancedUserDao.findUsersWithNoFunctions();
        log.info("✅ Users without functions: found {} users", usersWithoutFunctions.size());

        // Complete statistics
        Map<User, Integer> userStats = advancedUserDao.getUsersWithFunctionStats();
        log.info("✅ User statistics: analyzed {} users", userStats.size());
        userStats.forEach((user, count) ->
                log.info("   📊 {}: {} functions", user.getUsername(), count)
        );
    }

    @Test
    @Order(5)
    void testCombinedUserSearch() {
        log.info("\n🎯 TEST 5: Combined User Search");

        List<User> combinedResults = advancedUserDao.findByUsernameAndPattern("test", "hash");
        log.info("✅ Combined search: found {} users matching both patterns", combinedResults.size());
    }

    @Test
    @Order(6)
    void testStatisticalSearch() {
        log.info("\n🎯 TEST 6: Statistical Search Operations");

        // Extremum search
        PointEntity maxX = advancedPointDao.findMaxXPoint(testFunctionId);
        PointEntity minX = advancedPointDao.findMinXPoint(testFunctionId);

        Assertions.assertNotNull(maxX, "Should find max X point");
        Assertions.assertNotNull(minX, "Should find min X point");

        log.info("✅ Statistical search - Max X: {}, Min X: {}", maxX.getxValue(), minX.getxValue());

        // Threshold search
        List<PointEntity> aboveThreshold = advancedPointDao.findPointsAboveY(testFunctionId, 5.0);
        List<PointEntity> belowThreshold = advancedPointDao.findPointsBelowY(testFunctionId, 5.0);

        log.info("✅ Threshold search - Above 5.0: {} points, Below 5.0: {} points",
                aboveThreshold.size(), belowThreshold.size());
    }

    @AfterAll
    static void cleanup() throws Exception {
        log.info("\n🧹 Cleaning up test data...");

        // Clean up in correct order
        List<FunctionEntity> functions = functionDao.findByUser(testUserId);
        for (FunctionEntity func : functions) {
            pointDao.findByFunction(func.getId()).forEach(point ->
                    pointDao.delete(point.getId())
            );
            functionDao.delete(func.getId());
        }

        userDao.delete(testUserId);

        if (conn != null && !conn.isClosed()) {
            conn.close();
        }

        log.info("✅ Search System Test completed successfully!");
    }
}