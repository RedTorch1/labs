package spring.mapper;

import spring.entity.AppUser;
import spring.dto.AppUserDTO;
import spring.dto.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppUserMapper {

    private static final Logger log = LoggerFactory.getLogger(AppUserMapper.class);

    public AppUserDTO toDTO(AppUser entity) {
        log.debug("Converting AppUser entity to DTO: {}", entity);
        if (entity == null) {
            log.warn("Attempt to convert null AppUser entity to DTO");
            return null;
        }
        AppUserDTO dto = new AppUserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        log.debug("Successfully converted AppUser to DTO: {}", dto);
        return dto;
    }

    public AppUser toEntity(CreateUserRequest request) {
        log.debug("Converting CreateUserRequest to AppUser entity: {}", request);
        if (request == null) {
            log.warn("Attempt to convert null CreateUserRequest to entity");
            return null;
        }
        AppUser entity = new AppUser();
        entity.setUsername(request.getUsername());
        entity.setPasswordHash(request.getPasswordHash());
        entity.setEmail(request.getEmail());
        log.debug("Successfully converted CreateUserRequest to AppUser entity: {}", entity);
        return entity;
    }
    public AppUser toEntity(AppUserDTO dto) {
        log.debug("Converting AppUserDTO to entity: {}", dto);
        if (dto == null) {
            log.warn("Attempt to convert null AppUserDTO to entity");
            return null;
        }
        AppUser entity = new AppUser();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        log.debug("Successfully converted AppUserDTO to entity: {}", entity);
        return entity;
    }
}