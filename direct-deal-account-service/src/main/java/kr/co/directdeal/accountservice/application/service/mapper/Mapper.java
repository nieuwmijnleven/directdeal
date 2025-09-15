package kr.co.directdeal.accountservice.application.service.mapper;

/**
 * Generic Mapper interface to convert between Entity and DTO objects.
 *
 * @param <Entity> the domain/entity type
 * @param <DTO> the data transfer object type
 */
public interface Mapper<Entity, DTO> {

    /**
     * Converts a DTO to an Entity.
     *
     * @param dto the DTO to convert
     * @return the converted Entity
     */
    Entity toEntity(DTO dto);

    /**
     * Converts an Entity to a DTO.
     *
     * @param entity the Entity to convert
     * @return the converted DTO
     */
    DTO toDTO(Entity entity);
}
