package dao;

import model.FunctionEntity;
import java.util.List;

public interface FunctionDao {
    void insert(FunctionEntity func);
    FunctionEntity findById(long id);
    List<FunctionEntity> findByUser(long userId);
    List<FunctionEntity> findAll();
    void update(FunctionEntity func);
    void delete(long id);
}
