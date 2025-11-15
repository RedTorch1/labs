package dao;

import model.PointEntity;
import java.util.List;

public interface AdvancedPointDao {

    // Одиночный поиск
    PointEntity findByCoordinates(long functionId, double x, double y);
    PointEntity findNearestPoint(long functionId, double targetX, double targetY);

    // Множественный поиск
    List<PointEntity> findByXValueRange(long functionId, double minX, double maxX);
    List<PointEntity> findByYValueRange(long functionId, double minY, double maxY);
    List<PointEntity> findByFunctionAndX(long functionId, double xValue);

    // Поиск с сортировкой
    List<PointEntity> findByFunctionSortedByX(long functionId, boolean ascending);
    List<PointEntity> findByFunctionSortedByY(long functionId, boolean ascending);

    // Поиск экстремумов
    PointEntity findMaxXPoint(long functionId);
    PointEntity findMinXPoint(long functionId);
    PointEntity findMaxYPoint(long functionId);
    PointEntity findMinYPoint(long functionId);

    // Статистические поиски
    List<PointEntity> findPointsAboveY(long functionId, double yThreshold);
    List<PointEntity> findPointsBelowY(long functionId, double yThreshold);
}