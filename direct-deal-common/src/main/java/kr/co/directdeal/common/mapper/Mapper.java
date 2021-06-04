package kr.co.directdeal.common.mapper;

public interface Mapper<Entity, DTO> {
	
	public Entity toEntity(DTO dto);
	
	public DTO toDTO(Entity entity);
}
