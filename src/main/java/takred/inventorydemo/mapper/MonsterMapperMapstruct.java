package takred.inventorydemo.mapper;

import org.mapstruct.Mapper;
import takred.inventorydemo.dto.MonsterDto;
import takred.inventorydemo.entity.Monster;

@Mapper(componentModel = "spring")
public interface MonsterMapperMapstruct {

    MonsterDto map(Monster entity);
}
