package spring.mapper;

import spring.entity.AppUser;
import spring.dto.AppUserDTO;
import spring.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class AppUserMapperTest {

    private AppUserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AppUserMapper();
    }

    @Test
    void testToDTO() {
        // Given
        AppUser entity = new AppUser();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setPasswordHash("password123");
        entity.setEmail("test@mail.ru");

        // When
        AppUserDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    @Test
    void testToDTO_NullEntity() {
        // When
        AppUserDTO dto = mapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void testToEntityFromRequest() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newuser");
        request.setPasswordHash("password123");
        request.setEmail("new@mail.ru");

        // When
        AppUser entity = mapper.toEntity(request);

        // Then
        assertNotNull(entity);
        assertEquals(request.getUsername(), entity.getUsername());
        assertEquals(request.getPasswordHash(), entity.getPasswordHash());
        assertEquals(request.getEmail(), entity.getEmail());
    }

    @Test
    void testToEntityFromDTO() {
        // Given
        AppUserDTO dto = new AppUserDTO();
        dto.setId(1L);
        dto.setUsername("testuser");
        dto.setEmail("test@mail.ru");

        // When
        AppUser entity = mapper.toEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(dto.getEmail(), entity.getEmail());
    }
}