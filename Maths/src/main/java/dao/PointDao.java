package dao;

import model.PointEntity;
import java.util.List;

public interface PointDao {
    void insert(PointEntity point);
    List<PointEntity> findByFunction(long functionId);
    void update(PointEntity point);
    void delete(long id);
}
