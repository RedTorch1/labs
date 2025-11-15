package dao;

import model.FunctionEntity;
import java.util.List;
import java.util.Map;

public interface AdvancedFunctionDao {

    // Одиночный поиск
    FunctionEntity findByName(String name);
    FunctionEntity findByExpression(String expression);

    // Множественный поиск
    List<FunctionEntity> findByNameContaining(String substring);
    List<FunctionEntity> findByExpressionContaining(String substring);
    List<FunctionEntity> findByUserAndName(long userId, String name);

    // Поиск с сортировкой
    List<FunctionEntity> findAllSortedByName(boolean ascending);
    List<FunctionEntity> findByUserSorted(long userId, String sortBy, boolean ascending);

    // Поиск по иерархии (функции пользователя с их точками)
    Map<FunctionEntity, List<Object>> getUserFunctionsHierarchy(long userId);

    // Поиск в глубину (DFS) по связанным данным
    List<FunctionEntity> deepSearchByUser(long userId, int maxDepth);

    // Поиск в ширину (BFS) по связанным данным
    List<FunctionEntity> breadthSearchByUser(long userId, int maxLevel);
}