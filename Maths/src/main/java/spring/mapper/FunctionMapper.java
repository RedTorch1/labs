package spring.mapper;

import spring.entity.Function;
import spring.dto.FunctionDTO;
import spring.dto.CreateFunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FunctionMapper {

    private static final Logger log = LoggerFactory.getLogger(FunctionMapper.class);

    public FunctionDTO toDTO(Function entity) {
        log.debug("Converting Function entity to DTO: {}", entity);

        if (entity == null) {
            log.warn("Attempt to convert null Function entity to DTO");
            return null;
        }

        FunctionDTO dto = new FunctionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setExpression(entity.getExpression());
        dto.setUserId(entity.getUserId());

        log.debug("Successfully converted Function to DTO: {}", dto);
        return dto;
    }

    public Function toEntity(CreateFunctionRequest request) {
        log.debug("Converting CreateFunctionRequest to Function entity: {}", request);

        if (request == null) {
            log.warn("Attempt to convert null CreateFunctionRequest to entity");
            return null;
        }

        Function entity = new Function();
        entity.setName(request.getName());
        entity.setExpression(request.getExpression());
        entity.setUserId(request.getUserId());

        log.debug("Successfully converted CreateFunctionRequest to Function entity: {}", entity);
        return entity;
    }

    public Function toEntity(FunctionDTO dto) {
        log.debug("Converting FunctionDTO to entity: {}", dto);

        if (dto == null) {
            log.warn("Attempt to convert null FunctionDTO to entity");
            return null;
        }

        Function entity = new Function();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setExpression(dto.getExpression());
        entity.setUserId(dto.getUserId());

        log.debug("Successfully converted FunctionDTO to entity: {}", entity);
        return entity;
    }
}