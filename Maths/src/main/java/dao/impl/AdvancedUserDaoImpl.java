package dao.impl;

import dao.AdvancedUserDao;
import model.User;
import model.FunctionEntity;
import model.PointEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedUserDaoImpl implements AdvancedUserDao {
    private static final Logger log = LoggerFactory.getLogger(AdvancedUserDaoImpl.class);
    private final Connection conn;
    private final UserDaoImpl userDao;
    private final FunctionDaoImpl functionDao;
    private final PointDaoImpl pointDao;

    public AdvancedUserDaoImpl(Connection conn) {
        this.conn = conn;
        this.userDao = new UserDaoImpl(conn);
        this.functionDao = new FunctionDaoImpl(conn);
        this.pointDao = new PointDaoImpl(conn);
    }

    @Override
    public User findByUsername(String username) {
        log.info("🔍 Starting single search by username: {}", username);
        String sql = "SELECT * FROM app_user WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User result = extractUser(rs);
                log.debug("✅ Found user by username '{}': {}", username, result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching user by username: {}", username, e);
        }
        log.warn("⚠️ User not found by username: {}", username);
        return null;
    }

    @Override
    public User findByExactUsername(String username) {
        log.info("🔍 Starting exact username search: {}", username);
        // Тот же метод что и выше, но с более строгим логированием
        return findByUsername(username);
    }

    @Override
    public List<User> findByUsernameContaining(String substring) {
        log.info("🔍 Starting multiple search by username containing: {}", substring);
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM app_user WHERE username LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + substring + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractUser(rs));
            }
            log.info("✅ Found {} users with username containing '{}'", results.size(), substring);
        } catch (SQLException e) {
            log.error("❌ Error searching users by username containing: {}", substring, e);
        }
        return results;
    }

    @Override
    public List<User> findByPasswordHashPattern(String pattern) {
        log.info("🔍 Starting search by password hash pattern: {}", pattern);
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM app_user WHERE password_hash LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + pattern + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractUser(rs));
            }
            log.info("✅ Found {} users with password hash pattern '{}'", results.size(), pattern);
        } catch (SQLException e) {
            log.error("❌ Error searching users by password hash pattern", e);
        }
        return results;
    }

    @Override
    public List<User> findAllSortedByUsername(boolean ascending) {
        log.info("📈 Starting sorted search all users by username ({})", ascending ? "ASC" : "DESC");
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM app_user ORDER BY username " + (ascending ? "ASC" : "DESC");
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                results.add(extractUser(rs));
            }
            log.info("✅ Found {} users sorted by username", results.size());
        } catch (SQLException e) {
            log.error("❌ Error searching all users sorted by username", e);
        }
        return results;
    }

    @Override
    public List<User> findAllSortedById(boolean ascending) {
        log.info("📈 Starting sorted search all users by ID ({})", ascending ? "ASC" : "DESC");
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM app_user ORDER BY id " + (ascending ? "ASC" : "DESC");
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                results.add(extractUser(rs));
            }
            log.info("✅ Found {} users sorted by ID", results.size());
        } catch (SQLException e) {
            log.error("❌ Error searching all users sorted by ID", e);
        }
        return results;
    }

    @Override
    public Map<User, Map<Object, List<Object>>> getUserCompleteHierarchy(long userId) {
        log.info("🏗️ Starting complete hierarchical search for user {}", userId);
        Map<User, Map<Object, List<Object>>> hierarchy = new HashMap<>();

        try {
            // Находим пользователя
            User user = userDao.findById(userId);
            if (user == null) {
                log.warn("⚠️ User {} not found for hierarchy", userId);
                return hierarchy;
            }

            Map<Object, List<Object>> functionsWithPoints = new HashMap<>();

            // Получаем все функции пользователя (user_id → functions)
            List<FunctionEntity> functions = functionDao.findByUser(userId);
            log.debug("📊 User {} has {} functions", userId, functions.size());

            for (FunctionEntity function : functions) {
                // Для каждой функции получаем точки (function_id → points)
                List<PointEntity> points = pointDao.findByFunction(function.getId());
                log.debug("📈 Function '{}' has {} points", function.getName(), points.size());

                // Преобразуем точки в Object для гибкости
                List<Object> pointsData = new ArrayList<>(points);
                functionsWithPoints.put(function, pointsData);
            }

            hierarchy.put(user, functionsWithPoints);
            log.info("✅ Built complete hierarchy for user {}: {} functions with total points",
                    userId, functions.size());

        } catch (Exception e) {
            log.error("❌ Error building complete hierarchy for user {}", userId, e);
        }

        return hierarchy;
    }

    @Override
    public List<User> deepSearchConnectedUsers(long startUserId, int maxDepth) {
        log.info("🌊 Starting DFS for user hierarchy from user {} with max depth {}", startUserId, maxDepth);
        List<User> results = new ArrayList<>();
        Set<Long> visitedUsers = new HashSet<>();

        dfsUserHierarchy(startUserId, results, visitedUsers, 0, maxDepth);

        log.info("✅ User hierarchy DFS completed: explored {} users", results.size());
        return results;
    }

    private void dfsUserHierarchy(long userId, List<User> results, Set<Long> visitedUsers, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) {
            log.debug("📏 Reached max depth {} at level {}", maxDepth, currentDepth);
            return;
        }

        if (visitedUsers.contains(userId)) {
            log.debug("🔄 User {} already visited, skipping", userId);
            return;
        }

        visitedUsers.add(userId);
        log.debug("🔍 DFS level {} - processing user {}", currentDepth, userId);

        // Добавляем текущего пользователя в результаты (кроме стартового на глубине > 0)
        User user = userDao.findById(userId);
        if (user != null && currentDepth > 0) {
            results.add(user);
            log.trace("📥 Added user {} to DFS results at depth {}", user.getUsername(), currentDepth);
        }

        // Исследуем функции пользователя (уровень 1 иерархии)
        if (currentDepth < maxDepth) {
            List<FunctionEntity> userFunctions = functionDao.findByUser(userId);
            log.debug("🔄 User {} has {} functions to explore at depth {}", userId, userFunctions.size(), currentDepth);

            for (FunctionEntity function : userFunctions) {
                log.trace("   📊 Exploring function '{}' for user {}", function.getName(), userId);

                // Исследуем точки функции (уровень 2 иерархии)
                if (currentDepth < maxDepth - 1) {
                    List<PointEntity> functionPoints = pointDao.findByFunction(function.getId());
                    log.trace("      📈 Function '{}' has {} points", function.getName(), functionPoints.size());
                }
            }

            // Для демонстрации иерархического поиска: находим пользователей с похожими функциями
            if (currentDepth < maxDepth - 1) {
                exploreSimilarUsers(userId, results, visitedUsers, currentDepth + 1, maxDepth);
            }
        }
    }

    private void exploreSimilarUsers(long sourceUserId, List<User> results, Set<Long> visitedUsers, int currentDepth, int maxDepth) {
        log.debug("🔎 Looking for users with similar functions to user {}", sourceUserId);

        // Получаем функции исходного пользователя
        List<FunctionEntity> sourceFunctions = functionDao.findByUser(sourceUserId);
        Set<String> sourceFunctionNames = sourceFunctions.stream()
                .map(FunctionEntity::getName)
                .collect(Collectors.toSet());

        // Ищем других пользователей с похожими функциями
        List<User> allUsers = userDao.findAll();
        for (User otherUser : allUsers) {
            if (!visitedUsers.contains(otherUser.getId()) && otherUser.getId() != sourceUserId) {
                List<FunctionEntity> otherUserFunctions = functionDao.findByUser(otherUser.getId());
                Set<String> otherFunctionNames = otherUserFunctions.stream()
                        .map(FunctionEntity::getName)
                        .collect(Collectors.toSet());

                // Проверяем совпадение имён функций
                otherFunctionNames.retainAll(sourceFunctionNames);
                if (!otherFunctionNames.isEmpty()) {
                    log.debug("🤝 User {} has {} similar functions with user {}",
                            otherUser.getUsername(), otherFunctionNames.size(), sourceUserId);
                    dfsUserHierarchy(otherUser.getId(), results, visitedUsers, currentDepth, maxDepth);
                }
            }
        }
    }

    @Override
    public List<User> breadthSearchConnectedUsers(long startUserId, int maxLevel) {
        log.info("🌊 Starting BFS for user hierarchy from user {} with max level {}", startUserId, maxLevel);
        List<User> results = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visitedUsers = new HashSet<>();
        Map<Long, Integer> levels = new HashMap<>();

        queue.offer(startUserId);
        levels.put(startUserId, 0);
        visitedUsers.add(startUserId);

        while (!queue.isEmpty()) {
            long currentUserId = queue.poll();
            int currentLevel = levels.get(currentUserId);

            if (currentLevel > maxLevel) {
                log.debug("📏 Reached max level {} at BFS", maxLevel);
                break;
            }

            log.debug("🔍 BFS level {} - processing user {}", currentLevel, currentUserId);

            // Добавляем пользователя в результаты (кроме стартового)
            if (currentLevel > 0) {
                User user = userDao.findById(currentUserId);
                if (user != null) {
                    results.add(user);
                    log.trace("📥 Added user {} to BFS results at level {}", user.getUsername(), currentLevel);
                }
            }

            // Исследуем следующий уровень иерархии
            if (currentLevel < maxLevel) {
                exploreBFSLevel(currentUserId, queue, visitedUsers, levels, currentLevel);
            }
        }

        log.info("✅ User hierarchy BFS completed: found {} users across {} levels", results.size(), maxLevel);
        return results;
    }

    private void exploreBFSLevel(long currentUserId, Queue<Long> queue, Set<Long> visitedUsers, Map<Long, Integer> levels, int currentLevel) {
        // Ищем пользователей с похожими функциями для добавления в очередь
        List<FunctionEntity> currentUserFunctions = functionDao.findByUser(currentUserId);
        Set<String> currentFunctionNames = currentUserFunctions.stream()
                .map(FunctionEntity::getName)
                .collect(Collectors.toSet());

        List<User> allUsers = userDao.findAll();
        for (User otherUser : allUsers) {
            if (!visitedUsers.contains(otherUser.getId()) && otherUser.getId() != currentUserId) {
                List<FunctionEntity> otherUserFunctions = functionDao.findByUser(otherUser.getId());
                Set<String> otherFunctionNames = otherUserFunctions.stream()
                        .map(FunctionEntity::getName)
                        .collect(Collectors.toSet());

                // Проверяем совпадение имён функций
                otherFunctionNames.retainAll(currentFunctionNames);
                if (!otherFunctionNames.isEmpty()) {
                    queue.offer(otherUser.getId());
                    levels.put(otherUser.getId(), currentLevel + 1);
                    visitedUsers.add(otherUser.getId());
                    log.trace("⏭️ Added user {} to BFS queue at level {} ({} similar functions)",
                            otherUser.getUsername(), currentLevel + 1, otherFunctionNames.size());
                }
            }
        }
    }

    @Override
    public List<User> findUsersWithFunctionCount(int minFunctions) {
        log.info("📊 Starting search for users with at least {} functions", minFunctions);
        List<User> results = new ArrayList<>();

        // Сначала получаем всех пользователей
        List<User> allUsers = userDao.findAll();

        for (User user : allUsers) {
            List<FunctionEntity> userFunctions = functionDao.findByUser(user.getId());
            if (userFunctions.size() >= minFunctions) {
                results.add(user);
                log.debug("📋 User {} has {} functions (meets threshold {})",
                        user.getUsername(), userFunctions.size(), minFunctions);
            }
        }

        log.info("✅ Found {} users with at least {} functions", results.size(), minFunctions);
        return results;
    }

    @Override
    public List<User> findUsersWithNoFunctions() {
        log.info("📊 Starting search for users with no functions");
        List<User> results = new ArrayList<>();

        List<User> allUsers = userDao.findAll();
        for (User user : allUsers) {
            List<FunctionEntity> userFunctions = functionDao.findByUser(user.getId());
            if (userFunctions.isEmpty()) {
                results.add(user);
                log.debug("📭 User {} has no functions", user.getUsername());
            }
        }

        log.info("✅ Found {} users with no functions", results.size());
        return results;
    }

    @Override
    public Map<User, Integer> getUsersWithFunctionStats() {
        log.info("📈 Starting statistical analysis of users and their functions");
        Map<User, Integer> stats = new HashMap<>();

        List<User> allUsers = userDao.findAll();
        for (User user : allUsers) {
            List<FunctionEntity> userFunctions = functionDao.findByUser(user.getId());
            stats.put(user, userFunctions.size());
            log.debug("📊 User {}: {} functions", user.getUsername(), userFunctions.size());
        }

        log.info("✅ Generated stats for {} users", stats.size());
        return stats;
    }

    @Override
    public List<User> findByUsernameAndPattern(String usernamePattern, String passwordPattern) {
        log.info("🔍 Starting combined search: username like '{}', password like '{}'",
                usernamePattern, passwordPattern);
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM app_user WHERE username LIKE ? AND password_hash LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + usernamePattern + "%");
            ps.setString(2, "%" + passwordPattern + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractUser(rs));
            }
            log.info("✅ Found {} users matching both patterns", results.size());
        } catch (SQLException e) {
            log.error("❌ Error in combined user search", e);
        }
        return results;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                null,  // email
                "USER" // роль по умолчанию
        );
    }
}