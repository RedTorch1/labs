package dao.impl;

import dao.PointDao;
import model.PointEntity;
import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointDaoImpl implements PointDao {
    private static final Logger log = LoggerFactory.getLogger(PointDaoImpl.class);
    private final Connection conn;

    public PointDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(PointEntity p) {
        String sql = "INSERT INTO point (function_id, x_value, y_value) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, p.getFunctionId());
            ps.setDouble(2, p.getxValue());
            ps.setDouble(3, p.getyValue());
            ps.executeUpdate();
            log.info("Inserted point (x={}, y={})", p.getxValue(), p.getyValue());
        } catch (SQLException e) {
            log.error("Error inserting point", e);
        }
    }

    @Override
    public List<PointEntity> findByFunction(long funcId) {
        List<PointEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM point WHERE function_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, funcId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PointEntity(
                        rs.getLong("id"),
                        rs.getLong("function_id"),
                        rs.getDouble("x_value"),
                        rs.getDouble("y_value")
                ));
            }
        } catch (SQLException e) {
            log.error("Error fetching points for function {}", funcId, e);
        }
        return list;
    }

    @Override
    public void update(PointEntity p) {
        String sql = "UPDATE point SET x_value=?, y_value=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, p.getxValue());
            ps.setDouble(2, p.getyValue());
            ps.setLong(3, p.getId());
            ps.executeUpdate();
            log.info("Updated point id={}", p.getId());
        } catch (SQLException e) {
            log.error("Error updating point", e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM point WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            log.info("Deleted point id={}", id);
        } catch (SQLException e) {
            log.error("Error deleting point", e);
        }
    }
}
