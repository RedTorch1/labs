package spring.mapper;

import spring.entity.Point;
import spring.dto.PointDTO;
import spring.dto.CreatePointRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PointMapperTest {

    private PointMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PointMapper();
    }

    @Test
    void testToDTO() {
        // Given
        Point entity = new Point();
        entity.setId(1L);
        entity.setFunctionId(1L);
        entity.setXValue(2.0);
        entity.setYValue(4.0);

        // When
        PointDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFunctionId(), dto.getFunctionId());
        assertEquals(entity.getXValue(), dto.getXValue());
        assertEquals(entity.getYValue(), dto.getYValue());
    }

    @Test
    void testToEntityFromRequest() {
        // Given
        CreatePointRequest request = new CreatePointRequest();
        request.setFunctionId(1L);
        request.setXValue(3.0);
        request.setYValue(9.0);
        // When
        Point entity = mapper.toEntity(request);
        // Then
        assertNotNull(entity);
        assertEquals(request.getFunctionId(), entity.getFunctionId());
        assertEquals(request.getXValue(), entity.getXValue());
        assertEquals(request.getYValue(), entity.getYValue());
    }
}