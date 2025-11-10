package dao.impl;

import dao.FunctionDao;
import model.FunctionEntity;
import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionDaoImpl implements FunctionDao {
    private static final Logger log = LoggerFactory.getLogger(FunctionDaoImpl.class);
    private final Connection conn;

    public FunctionDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(FunctionEntity func) {
        String sql = "INSERT INTO func (user_id, name, expression) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, func.getUserId());
            ps.setString(2, func.getName());
            ps.setString(3, func.getExpression());
            ps.executeUpdate();
            log.info("Inserted function: {}", func.getName());
        } catch (SQLException e) {
            log.error("Error inserting function", e);
        }
    }

    @Override
    public FunctionEntity findById(long id) {
        String sql = "SELECT * FROM func WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new FunctionEntity(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("expression")
                );
            }
        } catch (SQLException e) {
            log.error("Error finding function", e);
        }
        return null;
    }

    @Override
    public List<FunctionEntity> findByUser(long userId) {
        List<FunctionEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM func WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new FunctionEntity(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("expression")
                ));
            }
        } catch (SQLException e) {
            log.error("Error fetching user functions", e);
        }
        return list;
    }

    @Override
    public List<FunctionEntity> findAll() {
        List<FunctionEntity> list = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM func");
            while (rs.next()) {
                list.add(new FunctionEntity(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("expression")
                ));
            }
        } catch (SQLException e) {
            log.error("Error fetching all functions", e);
        }
        return list;
    }

    @Override
    public void update(FunctionEntity func) {
        String sql = "UPDATE func SET name=?, expression=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, func.getName());
            ps.setString(2, func.getExpression());
            ps.setLong(3, func.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating function", e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM func WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting function", e);
        }
    }
}
