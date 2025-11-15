package dao.impl;

import dao.AdvancedFunctionDao;
import model.FunctionEntity;
import model.PointEntity;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedFunctionDaoImpl implements AdvancedFunctionDao {
    private static final Logger log = LoggerFactory.getLogger(AdvancedFunctionDaoImpl.class);
    private final Connection conn;
    private final FunctionDaoImpl functionDao;
    private final PointDaoImpl pointDao;

    public AdvancedFunctionDaoImpl(Connection conn) {
        this.conn = conn;
        this.functionDao = new FunctionDaoImpl(conn);
        this.pointDao = new PointDaoImpl(conn);
    }

    @Override
    public FunctionEntity findByName(String name) {
        log.info("🔍 Starting single search by name: {}", name);
        String sql = "SELECT * FROM func WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FunctionEntity result = extractFunction(rs);
                log.debug("✅ Found function by name '{}': {}", name, result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching function by name: {}", name, e);
        }
        log.warn("⚠️ Function not found by name: {}", name);
        return null;
    }

    @Override
    public FunctionEntity findByExpression(String expression) {
        log.info("🔍 Starting single search by expression: {}", expression);
        String sql = "SELECT * FROM func WHERE expression = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, expression);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FunctionEntity result = extractFunction(rs);
                log.debug("✅ Found function by expression '{}': {}", expression, result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching function by expression: {}", expression, e);
        }
        log.warn("⚠️ Function not found by expression: {}", expression);
        return null;
    }

    @Override
    public List<FunctionEntity> findByNameContaining(String substring) {
        log.info("🔍 Starting multiple search by name containing: {}", substring);
        List<FunctionEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM func WHERE name LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + substring + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractFunction(rs));
            }
            log.info("✅ Found {} functions with name containing '{}'", results.size(), substring);
        } catch (SQLException e) {
            log.error("❌ Error searching functions by name containing: {}", substring, e);
        }
        return results;
    }

    @Override
    public List<FunctionEntity> findByExpressionContaining(String substring) {
        log.info("🔍 Starting multiple search by expression containing: {}", substring);
        List<FunctionEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM func WHERE expression LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + substring + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractFunction(rs));
            }
            log.info("✅ Found {} functions with expression containing '{}'", results.size(), substring);
        } catch (SQLException e) {
            log.error("❌ Error searching functions by expression containing: {}", substring, e);
        }
        return results;
    }

    @Override
    public List<FunctionEntity> findByUserAndName(long userId, String name) {
        log.info("🔍 Starting search by user {} and name: {}", userId, name);
        List<FunctionEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM func WHERE user_id = ? AND name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractFunction(rs));
            }
            log.info("✅ Found {} functions for user {} with name '{}'", results.size(), userId, name);
        } catch (SQLException e) {
            log.error("❌ Error searching functions by user and name", e);
        }
        return results;
    }

    @Override
    public List<FunctionEntity> findAllSortedByName(boolean ascending) {
        log.info("📈 Starting sorted search all functions by name ({})", ascending ? "ASC" : "DESC");
        List<FunctionEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM func ORDER BY name " + (ascending ? "ASC" : "DESC");
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                results.add(extractFunction(rs));
            }
            log.info("✅ Found {} functions sorted by name", results.size());
        } catch (SQLException e) {
            log.error("❌ Error searching all functions sorted by name", e);
        }
        return results;
    }

    @Override
    public List<FunctionEntity> findByUserSorted(long userId, String sortBy, boolean ascending) {
        log.info("📈 Starting sorted search for user {} by {} ({})", userId, sortBy, ascending ? "ASC" : "DESC");
        List<FunctionEntity> results = new ArrayList<>();

        // Валидация поля для сортировки
        Set<String> allowedSortFields = Set.of("name", "expression", "id");
        if (!allowedSortFields.contains(sortBy.toLowerCase())) {
            log.warn("⚠️ Invalid sort field: {}, using default 'name'", sortBy);
            sortBy = "name";
        }

        String sql = "SELECT * FROM func WHERE user_id = ? ORDER BY " + sortBy + " " + (ascending ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractFunction(rs));
            }
            log.info("✅ Found {} functions for user {} sorted by {}", results.size(), userId, sortBy);
        } catch (SQLException e) {
            log.error("❌ Error searching functions for user sorted by {}", sortBy, e);
        }
        return results;
    }

    @Override
    public Map<FunctionEntity, List<Object>> getUserFunctionsHierarchy(long userId) {
        log.info("🏗️ Starting hierarchical search for user {} functions", userId);
        Map<FunctionEntity, List<Object>> hierarchy = new LinkedHashMap<>();

        try {
            // Получаем все функции пользователя (user_id → functions)
            List<FunctionEntity> functions = functionDao.findByUser(userId);
            log.debug("📊 Found {} functions for user {}", functions.size(), userId);

            for (FunctionEntity function : functions) {
                // Для каждой функции получаем точки (function_id → points)
                List<PointEntity> points = pointDao.findByFunction(function.getId());
                log.debug("📈 Function '{}' has {} points", function.getName(), points.size());

                // Преобразуем точки в Object для гибкости
                List<Object> pointsData = new ArrayList<>(points);
                hierarchy.put(function, pointsData);
            }

            log.info("✅ Built hierarchy for user {}: {} functions with total {} points",
                    userId, hierarchy.size(),
                    hierarchy.values().stream().mapToInt(List::size).sum());

        } catch (Exception e) {
            log.error("❌ Error building hierarchy for user {}", userId, e);
        }

        return hierarchy;
    }

    @Override
    public List<FunctionEntity> deepSearchByUser(long userId, int maxDepth) {
        log.info("🌊 Starting DFS search for user {} functions with max depth {}", userId, maxDepth);
        List<FunctionEntity> results = new ArrayList<>();
        Set<Long> visitedFunctions = new HashSet<>();

        // Для функций глубина означает уровень вложенности точек/анализа
        dfsFunctionAnalysis(userId, results, visitedFunctions, 0, maxDepth);

        log.info("✅ Function DFS completed: found {} functions for user {}", results.size(), userId);
        return results;
    }

    private void dfsFunctionAnalysis(long userId, List<FunctionEntity> results, Set<Long> visitedFunctions, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) {
            log.debug("📏 Reached max depth {} at level {}", maxDepth, currentDepth);
            return;
        }

        log.debug("🔍 DFS level {} - analyzing functions for user {}", currentDepth, userId);

        // Получаем функции пользователя
        List<FunctionEntity> functions = functionDao.findByUser(userId);

        for (FunctionEntity function : functions) {
            if (visitedFunctions.contains(function.getId())) {
                log.debug("🔄 Function {} already visited, skipping", function.getName());
                continue;
            }

            visitedFunctions.add(function.getId());
            results.add(function);
            log.trace("📥 Added function '{}' to DFS results at depth {}", function.getName(), currentDepth);

            // Анализируем точки функции на следующем уровне глубины
            if (currentDepth < maxDepth) {
                analyzeFunctionPoints(function, currentDepth + 1, maxDepth);
            }
        }

        // Рекурсивно анализируем похожие функции других пользователей
        if (currentDepth < maxDepth - 1) {
            analyzeSimilarFunctions(userId, results, visitedFunctions, currentDepth + 1, maxDepth);
        }
    }

    private void analyzeFunctionPoints(FunctionEntity function, int currentDepth, int maxDepth) {
        log.debug("   📈 Analyzing points for function '{}' at depth {}", function.getName(), currentDepth);

        List<PointEntity> points = pointDao.findByFunction(function.getId());

        // Анализ характеристик точек
        if (!points.isEmpty()) {
            double avgX = points.stream().mapToDouble(PointEntity::getxValue).average().orElse(0);
            double avgY = points.stream().mapToDouble(PointEntity::getyValue).average().orElse(0);
            double minX = points.stream().mapToDouble(PointEntity::getxValue).min().orElse(0);
            double maxX = points.stream().mapToDouble(PointEntity::getxValue).max().orElse(0);
            double minY = points.stream().mapToDouble(PointEntity::getyValue).min().orElse(0);
            double maxY = points.stream().mapToDouble(PointEntity::getyValue).max().orElse(0);

            log.trace("      📊 Function '{}': {} points, X[{}-{}], Y[{}-{}], avgX={}, avgY={}",
                    function.getName(), points.size(), minX, maxX, minY, maxY, avgX, avgY);
        }

        // Дополнительный анализ на большей глубине
        if (currentDepth < maxDepth) {
            // Анализ распределения точек
            analyzePointDistribution(points, function.getName(), currentDepth);
        }
    }

    private void analyzePointDistribution(List<PointEntity> points, String functionName, int currentDepth) {
        if (points.size() < 2) return;

        // Группируем точки по квадрантам (для демонстрации глубинного анализа)
        long q1 = points.stream().filter(p -> p.getxValue() >= 0 && p.getyValue() >= 0).count();
        long q2 = points.stream().filter(p -> p.getxValue() < 0 && p.getyValue() >= 0).count();
        long q3 = points.stream().filter(p -> p.getxValue() < 0 && p.getyValue() < 0).count();
        long q4 = points.stream().filter(p -> p.getxValue() >= 0 && p.getyValue() < 0).count();

        log.trace("      🎯 Function '{}' point distribution: Q1={}, Q2={}, Q3={}, Q4={}",
                functionName, q1, q2, q3, q4);
    }

    private void analyzeSimilarFunctions(long sourceUserId, List<FunctionEntity> results, Set<Long> visitedFunctions, int currentDepth, int maxDepth) {
        log.debug("🔎 Looking for similar functions to user {} functions", sourceUserId);

        // Получаем функции исходного пользователя
        List<FunctionEntity> sourceFunctions = functionDao.findByUser(sourceUserId);

        // Ищем похожие функции у других пользователей
        List<FunctionEntity> allFunctions = functionDao.findAll();
        for (FunctionEntity otherFunction : allFunctions) {
            if (!visitedFunctions.contains(otherFunction.getId()) && otherFunction.getUserId() != sourceUserId) {
                // Проверяем схожесть по имени или выражению
                boolean isSimilar = sourceFunctions.stream()
                        .anyMatch(sourceFunc ->
                                sourceFunc.getName().equals(otherFunction.getName()) ||
                                        sourceFunc.getExpression().equals(otherFunction.getExpression()));

                if (isSimilar) {
                    log.debug("🤝 Found similar function '{}' from user {}",
                            otherFunction.getName(), otherFunction.getUserId());
                    // Рекурсивно анализируем похожую функцию
                    dfsFunctionAnalysis(otherFunction.getUserId(), results, visitedFunctions, currentDepth, maxDepth);
                }
            }
        }
    }

    @Override
    public List<FunctionEntity> breadthSearchByUser(long userId, int maxLevel) {
        log.info("🌊 Starting BFS search for user {} functions with max level {}", userId, maxLevel);
        List<FunctionEntity> results = new ArrayList<>();
        Queue<Long> userQueue = new LinkedList<>();
        Set<Long> visitedUsers = new HashSet<>();
        Map<Long, Integer> userLevels = new HashMap<>();

        userQueue.offer(userId);
        userLevels.put(userId, 0);
        visitedUsers.add(userId);

        while (!userQueue.isEmpty()) {
            long currentUserId = userQueue.poll();
            int currentLevel = userLevels.get(currentUserId);

            if (currentLevel > maxLevel) {
                log.debug("📏 Reached max level {} at BFS", maxLevel);
                break;
            }

            log.debug("🔍 BFS level {} - processing user {}", currentLevel, currentUserId);

            // Получаем функции текущего пользователя
            List<FunctionEntity> currentUserFunctions = functionDao.findByUser(currentUserId);
            results.addAll(currentUserFunctions);
            log.debug("📥 Added {} functions from user {} at level {}",
                    currentUserFunctions.size(), currentUserId, currentLevel);

            // Ищем следующих пользователей для BFS
            if (currentLevel < maxLevel) {
                findNextBFSLevel(currentUserId, currentUserFunctions, userQueue, visitedUsers, userLevels, currentLevel);
            }
        }

        log.info("✅ Function BFS completed: found {} functions across {} user levels",
                results.size(), maxLevel);
        return results;
    }

    private void findNextBFSLevel(long currentUserId, List<FunctionEntity> currentFunctions,
                                  Queue<Long> userQueue, Set<Long> visitedUsers,
                                  Map<Long, Integer> userLevels, int currentLevel) {
        // Собираем уникальные имена функций текущего пользователя
        Set<String> currentFunctionNames = currentFunctions.stream()
                .map(FunctionEntity::getName)
                .collect(Collectors.toSet());

        // Ищем других пользователей с похожими функциями
        List<User> allUsers = getAllUsers();
        for (User otherUser : allUsers) {
            if (!visitedUsers.contains(otherUser.getId()) && otherUser.getId() != currentUserId) {
                List<FunctionEntity> otherUserFunctions = functionDao.findByUser(otherUser.getId());
                Set<String> otherFunctionNames = otherUserFunctions.stream()
                        .map(FunctionEntity::getName)
                        .collect(Collectors.toSet());

                // Проверяем пересечение имён функций
                otherFunctionNames.retainAll(currentFunctionNames);
                if (!otherFunctionNames.isEmpty()) {
                    userQueue.offer(otherUser.getId());
                    userLevels.put(otherUser.getId(), currentLevel + 1);
                    visitedUsers.add(otherUser.getId());
                    log.trace("⏭️ Added user {} to BFS queue at level {} ({} similar functions)",
                            otherUser.getUsername(), currentLevel + 1, otherFunctionNames.size());
                }
            }
        }
    }

    private List<User> getAllUsers() {
        log.debug("👥 Fetching all users for BFS expansion");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM app_user";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                ));
            }
            log.trace("📋 Retrieved {} users from database", users.size());
        } catch (SQLException e) {
            log.error("❌ Error fetching all users", e);
        }
        return users;
    }

    private FunctionEntity extractFunction(ResultSet rs) throws SQLException {
        return new FunctionEntity(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("expression")
        );
    }
}