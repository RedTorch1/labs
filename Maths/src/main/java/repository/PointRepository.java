package repository;

import model.PointEntity;
import java.util.List;

public interface PointRepository {
    PointEntity create(long funcId, double x, double y);
    List<PointEntity> findByFunction(long funcId);
    void delete(long id);
}
