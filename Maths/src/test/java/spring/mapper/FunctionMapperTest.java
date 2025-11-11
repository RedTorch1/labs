package spring.mapper;

import spring.entity.Function;
import spring.dto.FunctionDTO;
import spring.dto.CreateFunctionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class FunctionMapperTest {

    private FunctionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FunctionMapper();
    }

    @Test
    void testToDTO() {
        // Given
        Function entity = new Function();
        entity.setId(1L);
        entity.setName("Quadratic");
        entity.setExpression("x^2");
        entity.setUserId(1L);

        // When
        FunctionDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getExpression(), dto.getExpression());
        assertEquals(entity.getUserId(), dto.getUserId());
    }

    @Test
    void testToEntityFromRequest() {
        // Given
        CreateFunctionRequest request = new CreateFunctionRequest();
        request.setName("Linear");
        request.setExpression("2*x + 1");
        request.setUserId(1L);

        // When
        Function entity = mapper.toEntity(request);

        // Then
        assertNotNull(entity);
        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getExpression(), entity.getExpression());
        assertEquals(request.getUserId(), entity.getUserId());
    }
}