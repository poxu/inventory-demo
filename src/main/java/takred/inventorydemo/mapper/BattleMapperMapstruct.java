package takred.inventorydemo.mapper;

import takred.inventorydemo.dto.BattleDto;
import takred.inventorydemo.entity.Battle;

public interface BattleMapperMapstruct {

    BattleDto map(Battle entity);
}
