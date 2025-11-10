package repository.impl;

import dao.FunctionDao;
import model.FunctionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.FunctionRepository;
import java.util.List;

public class FunctionRepositoryImpl implements FunctionRepository {
    private static final Logger log = LoggerFactory.getLogger(FunctionRepositoryImpl.class);
    private final FunctionDao functionDao;

    public FunctionRepositoryImpl(FunctionDao functionDao) {
        this.functionDao = functionDao;
    }

    @Override
    public FunctionEntity create(long userId, String name, String expression) {
        FunctionEntity f = new FunctionEntity(0, userId, name, expression);
        functionDao.insert(f);
        log.info("Function created: {}", name);
        return f;
    }

    @Override
    public List<FunctionEntity> findByUser(long userId) {
        return functionDao.findByUser(userId);
    }

    @Override
    public void delete(long id) {
        functionDao.delete(id);
        log.info("Function deleted: {}", id);
    }
}
