package spring.mapper;

import spring.entity.Point;
import spring.dto.PointDTO;
import spring.dto.CreatePointRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PointMapper {

    private static final Logger log = LoggerFactory.getLogger(PointMapper.class);

    public PointDTO toDTO(Point entity) {
        log.debug("Converting Point entity to DTO: {}", entity);

        if (entity == null) {
            log.warn("Attempt to convert null Point entity to DTO");
            return null;
        }

        PointDTO dto = new PointDTO();
        dto.setId(entity.getId());
        dto.setFunctionId(entity.getFunctionId());
        dto.setXValue(entity.getXValue());
        dto.setYValue(entity.getYValue());

        log.debug("Successfully converted Point to DTO: {}", dto);
        return dto;
    }

    public Point toEntity(CreatePointRequest request) {
        log.debug("Converting CreatePointRequest to Point entity: {}", request);

        if (request == null) {
            log.warn("Attempt to convert null CreatePointRequest to entity");
            return null;
        }

        Point entity = new Point();
        entity.setFunctionId(request.getFunctionId());
        entity.setXValue(request.getXValue());
        entity.setYValue(request.getYValue());

        log.debug("Successfully converted CreatePointRequest to Point entity: {}", entity);
        return entity;
    }

    public Point toEntity(PointDTO dto) {
        log.debug("Converting PointDTO to entity: {}", dto);

        if (dto == null) {
            log.warn("Attempt to convert null PointDTO to entity");
            return null;
        }

        Point entity = new Point();
        entity.setId(dto.getId());
        entity.setFunctionId(dto.getFunctionId());
        entity.setXValue(dto.getXValue());
        entity.setYValue(dto.getYValue());

        log.debug("Successfully converted PointDTO to entity: {}", entity);
        return entity;
    }
}