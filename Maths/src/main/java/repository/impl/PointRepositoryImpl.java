package repository.impl;

import dao.PointDao;
import model.PointEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PointRepository;
import java.util.List;

public class PointRepositoryImpl implements PointRepository {
    private static final Logger log = LoggerFactory.getLogger(PointRepositoryImpl.class);
    private final PointDao pointDao;

    public PointRepositoryImpl(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    @Override
    public PointEntity create(long funcId, double x, double y) {
        PointEntity p = new PointEntity(0, funcId, x, y);
        pointDao.insert(p);
        log.info("Point created: (x={}, y={})", x, y);
        return p;
    }

    @Override
    public List<PointEntity> findByFunction(long funcId) {
        return pointDao.findByFunction(funcId);
    }

    @Override
    public void delete(long id) {
        pointDao.delete(id);
        log.info("Point deleted: {}", id);
    }
}
