package dao;

import model.User;
import java.util.List;
import java.util.Map;

public interface AdvancedUserDao {

    // Одиночный поиск
    User findByUsername(String username);
    User findByExactUsername(String username);

    // Множественный поиск
    List<User> findByUsernameContaining(String substring);
    List<User> findByPasswordHashPattern(String pattern);

    // Поиск с сортировкой
    List<User> findAllSortedByUsername(boolean ascending);
    List<User> findAllSortedById(boolean ascending);

    // Поиск по иерархии (пользователь -> его функции -> точки)
    Map<User, Map<Object, List<Object>>> getUserCompleteHierarchy(long userId);

    // Поиск в глубину (DFS) - для связанных данных пользователя
    List<User> deepSearchConnectedUsers(long startUserId, int maxDepth);

    // Поиск в ширину (BFS) - для связанных данных пользователя
    List<User> breadthSearchConnectedUsers(long startUserId, int maxLevel);

    // Статистический поиск
    List<User> findUsersWithFunctionCount(int minFunctions);
    List<User> findUsersWithNoFunctions();
    Map<User, Integer> getUsersWithFunctionStats();

    // Комбинированный поиск
    List<User> findByUsernameAndPattern(String usernamePattern, String passwordPattern);
}