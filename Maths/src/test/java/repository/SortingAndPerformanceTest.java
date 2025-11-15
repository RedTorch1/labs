package repository;

import dao.impl.*;
import model.FunctionEntity;
import model.PointEntity;
import model.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SortingAndPerformanceTest {
    private static final Logger log = LoggerFactory.getLogger(SortingAndPerformanceTest.class);

    private static Connection conn;
    private static AdvancedUserDaoImpl advancedUserDao;
    private static AdvancedFunctionDaoImpl advancedFunctionDao;
    private static AdvancedPointDaoImpl advancedPointDao;
    private static UserDaoImpl userDao;
    private static FunctionDaoImpl functionDao;
    private static PointDaoImpl pointDao;

    private static final int TEST_DATA_SIZE = 1000;
    private static final int PERFORMANCE_ITERATIONS = 100;
    private static List<Long> userIds = new ArrayList<>();
    private static List<Long> functionIds = new ArrayList<>();

    @BeforeAll
    static void setup() throws Exception {
        log.info("🚀 Initializing Sorting and Performance Test");

        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");

        userDao = new UserDaoImpl(conn);
        functionDao = new FunctionDaoImpl(conn);
        pointDao = new PointDaoImpl(conn);
        advancedUserDao = new AdvancedUserDaoImpl(conn);
        advancedFunctionDao = new AdvancedFunctionDaoImpl(conn);
        advancedPointDao = new AdvancedPointDaoImpl(conn);

        cleanupTestData();
        setupTestData();
    }

    static void cleanupTestData() {
        log.info("🧹 Cleaning up existing test data...");
        try {
            // Удаляем тестовые данные в правильном порядке
            List<FunctionEntity> allFunctions = functionDao.findAll();
            for (FunctionEntity func : allFunctions) {
                if (func.getName().contains("perf_test_") || func.getName().contains("sort_test_")) {
                    pointDao.findByFunction(func.getId()).forEach(point ->
                            pointDao.delete(point.getId())
                    );
                    functionDao.delete(func.getId());
                }
            }

            List<User> allUsers = userDao.findAll();
            for (User user : allUsers) {
                if (user.getUsername().contains("perf_test_") || user.getUsername().contains("sort_test_")) {
                    userDao.delete(user.getId());
                }
            }
        } catch (Exception e) {
            log.warn("⚠️ Error during cleanup: {}", e.getMessage());
        }
    }

    static void setupTestData() {
        log.info("📝 Setting up test data ({} records)...", TEST_DATA_SIZE);

        Random random = new Random();

        // Создаем пользователей
        for (int i = 0; i < TEST_DATA_SIZE / 10; i++) {
            User user = new User(0,
                    "perf_test_user_" + String.format("%04d", i) + "_" + random.nextInt(1000),
                    "hash_" + System.nanoTime());
            userDao.insert(user);
        }

        // Получаем ID созданных пользователей
        List<User> users = userDao.findAll().stream()
                .filter(u -> u.getUsername().contains("perf_test_user_"))
                .collect(Collectors.toList());
        userIds = users.stream().map(User::getId).collect(Collectors.toList());

        // Создаем функции для каждого пользователя
        String[] functionTypes = {"linear", "quadratic", "cubic", "exponential", "logarithmic"};
        for (User user : users) {
            for (int j = 0; j < 2; j++) { // По 2 функции на пользователя
                String funcType = functionTypes[random.nextInt(functionTypes.length)];
                FunctionEntity func = new FunctionEntity(0,
                        user.getId(),
                        "sort_test_" + funcType + "_" + String.format("%06d", random.nextInt(1000000)),
                        generateExpression(funcType));
                functionDao.insert(func);
            }
        }

        // Получаем ID созданных функций
        List<FunctionEntity> functions = functionDao.findAll().stream()
                .filter(f -> f.getName().contains("sort_test_"))
                .collect(Collectors.toList());
        functionIds = functions.stream().map(FunctionEntity::getId).collect(Collectors.toList());

        // Создаем точки для функций
        for (FunctionEntity func : functions) {
            for (int k = 0; k < 10; k++) { // По 10 точек на функцию
                double x = random.nextDouble() * 100 - 50; // -50 до 50
                double y = calculateY(x, func.getExpression());
                PointEntity point = new PointEntity(0, func.getId(), x, y);
                pointDao.insert(point);
            }
        }

        log.info("✅ Test data setup complete: {} users, {} functions, points created",
                userIds.size(), functionIds.size());
    }

    private static String generateExpression(String type) {
        Random random = new Random();
        switch (type) {
            case "linear": return random.nextDouble() + " * x + " + random.nextDouble();
            case "quadratic": return random.nextDouble() + " * x * x + " + random.nextDouble() + " * x + " + random.nextDouble();
            case "cubic": return random.nextDouble() + " * x * x * x + " + random.nextDouble() + " * x";
            case "exponential": return random.nextDouble() + " * Math.exp(" + random.nextDouble() + " * x)";
            case "logarithmic": return random.nextDouble() + " * Math.log(x + " + (random.nextDouble() + 1) + ")";
            default: return "x";
        }
    }

    private static double calculateY(double x, String expression) {
        // Упрощенный расчет Y для тестовых данных
        Random random = new Random();
        if (expression.contains("x * x")) return x * x + random.nextDouble() * 10;
        if (expression.contains("x * x * x")) return x * x * x + random.nextDouble() * 10;
        if (expression.contains("exp")) return Math.exp(x * 0.1) + random.nextDouble();
        if (expression.contains("log")) return Math.log(x + 10) + random.nextDouble();
        return x * 2 + random.nextDouble() * 5;
    }

    @Test
    @Order(1)
    void testUserSorting() {
        log.info("\n🎯 TEST 1: User Sorting Operations");

        Map<String, Long> sortingTimes = new LinkedHashMap<>();

        // Сортировка пользователей по имени (ASC)
        long startTime = System.nanoTime();
        List<User> usersAsc = advancedUserDao.findAllSortedByUsername(true);
        long endTime = System.nanoTime();
        sortingTimes.put("Users by name ASC", (endTime - startTime) / 1000);

        Assertions.assertFalse(usersAsc.isEmpty(), "Should find sorted users ASC");
        log.info("✅ Users sorted by name ASC: {} users, {} μs",
                usersAsc.size(), sortingTimes.get("Users by name ASC"));

        // Проверка правильности сортировки
        for (int i = 1; i < Math.min(10, usersAsc.size()); i++) {
            Assertions.assertTrue(usersAsc.get(i-1).getUsername()
                            .compareTo(usersAsc.get(i).getUsername()) <= 0,
                    "Users should be sorted in ascending order");
        }

        // Сортировка пользователей по имени (DESC)
        startTime = System.nanoTime();
        List<User> usersDesc = advancedUserDao.findAllSortedByUsername(false);
        endTime = System.nanoTime();
        sortingTimes.put("Users by name DESC", (endTime - startTime) / 1000);

        // Проверка правильности сортировки
        for (int i = 1; i < Math.min(10, usersDesc.size()); i++) {
            Assertions.assertTrue(usersDesc.get(i-1).getUsername()
                            .compareTo(usersDesc.get(i).getUsername()) >= 0,
                    "Users should be sorted in descending order");
        }

        // Сортировка по ID
        startTime = System.nanoTime();
        List<User> usersById = advancedUserDao.findAllSortedById(true);
        endTime = System.nanoTime();
        sortingTimes.put("Users by ID ASC", (endTime - startTime) / 1000);

        logSortingResults("User Sorting", sortingTimes);
    }

    @Test
    @Order(2)
    void testFunctionSorting() {
        log.info("\n🎯 TEST 2: Function Sorting Operations");

        Map<String, Long> sortingTimes = new LinkedHashMap<>();

        if (!userIds.isEmpty()) {
            long testUserId = userIds.get(0);

            // Сортировка функций по имени (ASC)
            long startTime = System.nanoTime();
            List<FunctionEntity> functionsAsc = advancedFunctionDao.findByUserSorted(testUserId, "name", true);
            long endTime = System.nanoTime();
            sortingTimes.put("Functions by name ASC", (endTime - startTime) / 1000);

            // Проверка правильности сортировки
            for (int i = 1; i < Math.min(10, functionsAsc.size()); i++) {
                Assertions.assertTrue(functionsAsc.get(i-1).getName()
                                .compareTo(functionsAsc.get(i).getName()) <= 0,
                        "Functions should be sorted in ascending order");
            }

            // Сортировка функций по имени (DESC)
            startTime = System.nanoTime();
            List<FunctionEntity> functionsDesc = advancedFunctionDao.findByUserSorted(testUserId, "name", false);
            endTime = System.nanoTime();
            sortingTimes.put("Functions by name DESC", (endTime - startTime) / 1000);

            // Сортировка функций по выражению
            startTime = System.nanoTime();
            List<FunctionEntity> functionsByExpr = advancedFunctionDao.findByUserSorted(testUserId, "expression", true);
            endTime = System.nanoTime();
            sortingTimes.put("Functions by expression", (endTime - startTime) / 1000);

            // Все функции с сортировкой
            startTime = System.nanoTime();
            List<FunctionEntity> allFunctions = advancedFunctionDao.findAllSortedByName(true);
            endTime = System.nanoTime();
            sortingTimes.put("All functions by name", (endTime - startTime) / 1000);

            logSortingResults("Function Sorting", sortingTimes);
        }
    }

    @Test
    @Order(3)
    void testPointSorting() {
        log.info("\n🎯 TEST 3: Point Sorting Operations");

        Map<String, Long> sortingTimes = new LinkedHashMap<>();

        if (!functionIds.isEmpty()) {
            long testFunctionId = functionIds.get(0);

            // Сортировка точек по X (ASC)
            long startTime = System.nanoTime();
            List<PointEntity> pointsXAsc = advancedPointDao.findByFunctionSortedByX(testFunctionId, true);
            long endTime = System.nanoTime();
            sortingTimes.put("Points by X ASC", (endTime - startTime) / 1000);

            // Проверка правильности сортировки
            for (int i = 1; i < Math.min(10, pointsXAsc.size()); i++) {
                Assertions.assertTrue(pointsXAsc.get(i-1).getxValue() <= pointsXAsc.get(i).getxValue(),
                        "Points should be sorted by X in ascending order");
            }

            // Сортировка точек по X (DESC)
            startTime = System.nanoTime();
            List<PointEntity> pointsXDesc = advancedPointDao.findByFunctionSortedByX(testFunctionId, false);
            endTime = System.nanoTime();
            sortingTimes.put("Points by X DESC", (endTime - startTime) / 1000);

            // Сортировка точек по Y (ASC)
            startTime = System.nanoTime();
            List<PointEntity> pointsYAsc = advancedPointDao.findByFunctionSortedByY(testFunctionId, true);
            endTime = System.nanoTime();
            sortingTimes.put("Points by Y ASC", (endTime - startTime) / 1000);

            logSortingResults("Point Sorting", sortingTimes);
        }
    }

    @Test
    @Order(4)
    void testSearchPerformanceComparison() {
        log.info("\n🎯 TEST 4: Search Performance Comparison");

        Map<String, Long> performanceResults = new LinkedHashMap<>();

        if (!userIds.isEmpty() && !functionIds.isEmpty()) {
            long testUserId = userIds.get(0);
            long testFunctionId = functionIds.get(0);

            // Производительность поиска пользователей
            performanceResults.putAll(testUserSearchPerformance(testUserId));

            // Производительность поиска функций
            performanceResults.putAll(testFunctionSearchPerformance(testUserId));

            // Производительность поиска точек
            performanceResults.putAll(testPointSearchPerformance(testFunctionId));

            // Запись результатов в CSV
            writePerformanceResultsToCSV(performanceResults);

            logPerformanceResults("Search Performance", performanceResults);
        }
    }

    private Map<String, Long> testUserSearchPerformance(long testUserId) {
        Map<String, Long> results = new LinkedHashMap<>();

        // Одиночный поиск
        long singleSearchTime = measurePerformance(() ->
                        advancedUserDao.findByUsername("perf_test_user_0000"),
                PERFORMANCE_ITERATIONS
        );
        results.put("User single search", singleSearchTime);

        // Множественный поиск
        long multipleSearchTime = measurePerformance(() ->
                        advancedUserDao.findByUsernameContaining("perf_test"),
                PERFORMANCE_ITERATIONS / 10
        );
        results.put("User multiple search", multipleSearchTime);

        // Иерархический поиск
        long hierarchySearchTime = measurePerformance(() ->
                        advancedUserDao.getUserCompleteHierarchy(testUserId),
                PERFORMANCE_ITERATIONS / 20
        );
        results.put("User hierarchy search", hierarchySearchTime);

        return results;
    }

    private Map<String, Long> testFunctionSearchPerformance(long testUserId) {
        Map<String, Long> results = new LinkedHashMap<>();

        // Одиночный поиск
        long singleSearchTime = measurePerformance(() ->
                        advancedFunctionDao.findByNameContaining("linear").stream().findFirst(),
                PERFORMANCE_ITERATIONS
        );
        results.put("Function single search", singleSearchTime);

        // Множественный поиск
        long multipleSearchTime = measurePerformance(() ->
                        advancedFunctionDao.findByExpressionContaining("x"),
                PERFORMANCE_ITERATIONS / 10
        );
        results.put("Function multiple search", multipleSearchTime);

        // Иерархический поиск
        long hierarchySearchTime = measurePerformance(() ->
                        advancedFunctionDao.getUserFunctionsHierarchy(testUserId),
                PERFORMANCE_ITERATIONS / 20
        );
        results.put("Function hierarchy search", hierarchySearchTime);

        return results;
    }

    private Map<String, Long> testPointSearchPerformance(long testFunctionId) {
        Map<String, Long> results = new LinkedHashMap<>();

        // Поиск по диапазону
        long rangeSearchTime = measurePerformance(() ->
                        advancedPointDao.findByXValueRange(testFunctionId, 0, 50),
                PERFORMANCE_ITERATIONS
        );
        results.put("Point range search", rangeSearchTime);

        // Поиск экстремумов
        long extremumSearchTime = measurePerformance(() ->
                        advancedPointDao.findMaxXPoint(testFunctionId),
                PERFORMANCE_ITERATIONS
        );
        results.put("Point extremum search", extremumSearchTime);

        // Статистический поиск
        long statsSearchTime = measurePerformance(() ->
                        advancedPointDao.findPointsAboveY(testFunctionId, 0),
                PERFORMANCE_ITERATIONS
        );
        results.put("Point statistical search", statsSearchTime);

        return results;
    }

    private long measurePerformance(Runnable operation, int iterations) {
        long totalTime = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            operation.run();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        return totalTime / iterations / 1000; // Возвращаем среднее время в микросекундах
    }

    @Test
    @Order(5)
    void testDFSvsBFSPerformance() {
        log.info("\n🎯 TEST 5: DFS vs BFS Performance Comparison");

        if (!userIds.isEmpty()) {
            long testUserId = userIds.get(0);

            Map<String, Long> traversalResults = new LinkedHashMap<>();

            // Производительность DFS
            long dfsTime = measurePerformance(() ->
                            advancedUserDao.deepSearchConnectedUsers(testUserId, 2),
                    PERFORMANCE_ITERATIONS / 10
            );
            traversalResults.put("DFS traversal", dfsTime);

            // Производительность BFS
            long bfsTime = measurePerformance(() ->
                            advancedUserDao.breadthSearchConnectedUsers(testUserId, 2),
                    PERFORMANCE_ITERATIONS / 10
            );
            traversalResults.put("BFS traversal", bfsTime);

            // Сравнение производительности
            logPerformanceResults("DFS vs BFS Performance", traversalResults);

            // Анализ разницы
            long difference = Math.abs(dfsTime - bfsTime);
            String faster = dfsTime < bfsTime ? "DFS" : "BFS";
            log.info("📊 {} is faster by {} μs (DFS: {} μs, BFS: {} μs)",
                    faster, difference, dfsTime, bfsTime);
        }
    }

    private void logSortingResults(String testName, Map<String, Long> results) {
        log.info("📈 {} Results:", testName);
        results.forEach((operation, time) ->
                log.info("   ⏱️  {}: {} μs", operation, time)
        );

        // Находим самую быструю и медленную операцию
        Map.Entry<String, Long> fastest = results.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);
        Map.Entry<String, Long> slowest = results.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (fastest != null && slowest != null) {
            log.info("   🏆 Fastest: {} ({} μs)", fastest.getKey(), fastest.getValue());
            log.info("   🐌 Slowest: {} ({} μs)", slowest.getKey(), slowest.getValue());
            log.info("   📊 Difference: {} μs", slowest.getValue() - fastest.getValue());
        }
    }

    private void logPerformanceResults(String testName, Map<String, Long> results) {
        log.info("🚀 {} Results (average of {} iterations):", testName, PERFORMANCE_ITERATIONS);
        results.forEach((operation, time) ->
                log.info("   ⚡ {}: {} μs", operation, time)
        );
    }

    private void writePerformanceResultsToCSV(Map<String, Long> performanceResults) {
        String filename = "sorting_performance_results.csv";

        try (FileWriter writer = new FileWriter(filename)) {
            // Заголовок CSV
            writer.write("Test Category,Operation,Time (μs),Data Size,Iterations\n");

            // Записываем результаты сортировки
            writer.write("Sorting,User by name ASC," + performanceResults.getOrDefault("Users by name ASC", 0L) +
                    "," + TEST_DATA_SIZE + "," + "1\n");
            writer.write("Sorting,User by name DESC," + performanceResults.getOrDefault("Users by name DESC", 0L) +
                    "," + TEST_DATA_SIZE + "," + "1\n");
            writer.write("Sorting,Function by name ASC," + performanceResults.getOrDefault("Functions by name ASC", 0L) +
                    "," + TEST_DATA_SIZE + "," + "1\n");
            writer.write("Sorting,Point by X ASC," + performanceResults.getOrDefault("Points by X ASC", 0L) +
                    "," + TEST_DATA_SIZE + "," + "1\n");

            // Записываем результаты поиска
            writer.write("Search,User single," + performanceResults.getOrDefault("User single search", 0L) +
                    "," + TEST_DATA_SIZE + "," + PERFORMANCE_ITERATIONS + "\n");
            writer.write("Search,User multiple," + performanceResults.getOrDefault("User multiple search", 0L) +
                    "," + TEST_DATA_SIZE + "," + (PERFORMANCE_ITERATIONS / 10) + "\n");
            writer.write("Search,Function hierarchy," + performanceResults.getOrDefault("Function hierarchy search", 0L) +
                    "," + TEST_DATA_SIZE + "," + (PERFORMANCE_ITERATIONS / 20) + "\n");
            writer.write("Search,Point range," + performanceResults.getOrDefault("Point range search", 0L) +
                    "," + TEST_DATA_SIZE + "," + PERFORMANCE_ITERATIONS + "\n");

            log.info("✅ Performance results written to: {}", filename);

        } catch (Exception e) {
            log.error("❌ Error writing performance results to CSV", e);
        }
    }

    @AfterAll
    static void cleanup() throws Exception {
        log.info("\n🧹 Cleaning up test data...");

        // Можно оставить данные для дальнейшего анализа или очистить
        // cleanupTestData();

        if (conn != null && !conn.isClosed()) {
            conn.close();
        }

        log.info("✅ Sorting and Performance Test completed successfully!");
    }
}