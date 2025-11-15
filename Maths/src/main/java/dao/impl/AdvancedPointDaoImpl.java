package dao.impl;

import dao.AdvancedPointDao;
import model.PointEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdvancedPointDaoImpl implements AdvancedPointDao {
    private static final Logger log = LoggerFactory.getLogger(AdvancedPointDaoImpl.class);
    private final Connection conn;

    public AdvancedPointDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public PointEntity findByCoordinates(long functionId, double x, double y) {
        log.info("🔍 Starting single search by coordinates: function={}, x={}, y={}", functionId, x, y);
        String sql = "SELECT * FROM point WHERE function_id = ? AND x_value = ? AND y_value = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, x);
            ps.setDouble(3, y);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PointEntity result = extractPoint(rs);
                log.debug("✅ Found point by coordinates: {}", result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching point by coordinates", e);
        }
        log.warn("⚠️ Point not found by coordinates: function={}, x={}, y={}", functionId, x, y);
        return null;
    }

    @Override
    public PointEntity findNearestPoint(long functionId, double targetX, double targetY) {
        log.info("🎯 Starting nearest point search: function={}, targetX={}, targetY={}", functionId, targetX, targetY);
        String sql = "SELECT * FROM point WHERE function_id = ? ORDER BY ABS(x_value - ?) + ABS(y_value - ?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, targetX);
            ps.setDouble(3, targetY);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PointEntity result = extractPoint(rs);
                log.debug("✅ Found nearest point: {}", result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching nearest point", e);
        }
        return null;
    }

    @Override
    public List<PointEntity> findByXValueRange(long functionId, double minX, double maxX) {
        log.info("📊 Starting range search by X: function={}, minX={}, maxX={}", functionId, minX, maxX);
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? AND x_value BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, minX);
            ps.setDouble(3, maxX);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points in X range [{}, {}]", results.size(), minX, maxX);
        } catch (SQLException e) {
            log.error("❌ Error searching points by X range", e);
        }
        return results;
    }

    @Override
    public List<PointEntity> findByYValueRange(long functionId, double minY, double maxY) {
        log.info("📊 Starting range search by Y: function={}, minY={}, maxY={}", functionId, minY, maxY);
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? AND y_value BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, minY);
            ps.setDouble(3, maxY);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points in Y range [{}, {}]", results.size(), minY, maxY);
        } catch (SQLException e) {
            log.error("❌ Error searching points by Y range", e);
        }
        return results;
    }

    @Override
    public List<PointEntity> findByFunctionAndX(long functionId, double xValue) {
        log.info("🔍 Starting search by function and X: function={}, x={}", functionId, xValue);
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? AND x_value = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, xValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points with X={} for function {}", results.size(), xValue, functionId);
        } catch (SQLException e) {
            log.error("❌ Error searching points by function and X", e);
        }
        return results;
    }

    @Override
    public List<PointEntity> findByFunctionSortedByX(long functionId, boolean ascending) {
        log.info("📈 Starting sorted search by X: function={}, order={}", functionId, ascending ? "ASC" : "DESC");
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? ORDER BY x_value " + (ascending ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points sorted by X", results.size());
        } catch (SQLException e) {
            log.error("❌ Error searching points sorted by X", e);
        }
        return results;
    }

    @Override
    public List<PointEntity> findByFunctionSortedByY(long functionId, boolean ascending) {
        log.info("📈 Starting sorted search by Y: function={}, order={}", functionId, ascending ? "ASC" : "DESC");
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? ORDER BY y_value " + (ascending ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points sorted by Y", results.size());
        } catch (SQLException e) {
            log.error("❌ Error searching points sorted by Y", e);
        }
        return results;
    }

    @Override
    public PointEntity findMaxXPoint(long functionId) {
        log.info("⬆️ Starting search for max X point: function={}", functionId);
        return findExtremumPoint(functionId, "x_value", "DESC");
    }

    @Override
    public PointEntity findMinXPoint(long functionId) {
        log.info("⬇️ Starting search for min X point: function={}", functionId);
        return findExtremumPoint(functionId, "x_value", "ASC");
    }

    @Override
    public PointEntity findMaxYPoint(long functionId) {
        log.info("⬆️ Starting search for max Y point: function={}", functionId);
        return findExtremumPoint(functionId, "y_value", "DESC");
    }

    @Override
    public PointEntity findMinYPoint(long functionId) {
        log.info("⬇️ Starting search for min Y point: function={}", functionId);
        return findExtremumPoint(functionId, "y_value", "ASC");
    }

    @Override
    public List<PointEntity> findPointsAboveY(long functionId, double yThreshold) {
        log.info("📊 Starting search for points above Y: function={}, threshold={}", functionId, yThreshold);
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? AND y_value > ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, yThreshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points above Y threshold {}", results.size(), yThreshold);
        } catch (SQLException e) {
            log.error("❌ Error searching points above Y threshold", e);
        }
        return results;
    }

    @Override
    public List<PointEntity> findPointsBelowY(long functionId, double yThreshold) {
        log.info("📊 Starting search for points below Y: function={}, threshold={}", functionId, yThreshold);
        List<PointEntity> results = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ? AND y_value < ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ps.setDouble(2, yThreshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(extractPoint(rs));
            }
            log.info("✅ Found {} points below Y threshold {}", results.size(), yThreshold);
        } catch (SQLException e) {
            log.error("❌ Error searching points below Y threshold", e);
        }
        return results;
    }

    private PointEntity findExtremumPoint(long functionId, String column, String order) {
        String sql = "SELECT * FROM point WHERE function_id = ? ORDER BY " + column + " " + order + " LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, functionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PointEntity result = extractPoint(rs);
                log.debug("✅ Found {} point: {}", column, result);
                return result;
            }
        } catch (SQLException e) {
            log.error("❌ Error searching {} point", column, e);
        }
        log.warn("⚠️ No {} point found for function {}", column, functionId);
        return null;
    }

    private PointEntity extractPoint(ResultSet rs) throws SQLException {
        return new PointEntity(
                rs.getLong("id"),
                rs.getLong("function_id"),
                rs.getDouble("x_value"),
                rs.getDouble("y_value")
        );
    }
}