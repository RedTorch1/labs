package repository;

import model.FunctionEntity;
import java.util.List;

public interface FunctionRepository {
    FunctionEntity create(long userId, String name, String expression);
    List<FunctionEntity> findByUser(long userId);
    void delete(long id);
}
