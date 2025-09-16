package kr.co.directdeal.common.mapper;

/**
 * Mapper interface for converting between Entity and DTO objects.
 *
 * @param <Entity> the entity type
 * @param <DTO> the data transfer object type
 *
 * @author Cheol Jeon
 */
public interface Mapper<Entity, DTO> {

    /**
     * Converts a DTO to an Entity.
     *
     * @param dto the DTO to convert
     * @return the converted Entity
     */
    public Entity toEntity(DTO dto);

    /**
     * Converts an Entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    public DTO toDTO(Entity entity);
}
