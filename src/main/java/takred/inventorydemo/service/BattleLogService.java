package takred.inventorydemo.service;

import org.springframework.stereotype.Service;
import takred.inventorydemo.ShortBattleInfo;
import takred.inventorydemo.entity.Battle;
import takred.inventorydemo.entity.BattleLog;
import takred.inventorydemo.entity.Person;
import takred.inventorydemo.repository.BattleLogRepository;
import takred.inventorydemo.repository.BattleRepository;
import takred.inventorydemo.repository.MonsterRepository;
import takred.inventorydemo.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BattleLogService {
    private final BattleLogRepository battleLogRepository;
    private final PersonRepository personRepository;
    private final MonsterRepository monsterRepository;
    private final BattleRepository battleRepository;

    public BattleLogService(BattleLogRepository battleLogRepository, PersonRepository personRepository, MonsterRepository monsterRepository, BattleRepository battleRepository) {
        this.battleLogRepository = battleLogRepository;
        this.personRepository = personRepository;
        this.monsterRepository = monsterRepository;
        this.battleRepository = battleRepository;
    }

    public List<ShortBattleInfo> getShortBattleInfo(UUID personId) {

        List<Battle> battles = battleRepository.findByPersonId(personId);
        List<ShortBattleInfo> shortBattleInfos = new ArrayList<>();
        if (battles == null) {
            return shortBattleInfos;
        }
        Optional<Person> optional = personRepository.findById(personId);
        if (!optional.isPresent()) {
            return shortBattleInfos;
        }
        Person person = optional.get();

        for (int i = 0; i < battles.size(); i++) {
            ShortBattleInfo shortBattleInfo = new ShortBattleInfo();
            shortBattleInfo.setBattleId(battles.get(i).getId());
            Battle battle = battles.get(i);
            String string = "";
            if (battle.getPersonId().equals(battle.getWinner())) {
                string = "Победил игрок.";
            } else if (battle.getMonsterId().equals(battle.getWinner())) {
                string = "Победил монстр.";
            }
            shortBattleInfo.setInfo(person.getName() + " против " + monsterRepository.findById(battles.get(i).getMonsterId()).get().getName() +
                    ". " + string);
            shortBattleInfos.add(shortBattleInfo);
        }
        return shortBattleInfos;
    }

    public List<BattleLog> getBattleLog(UUID battleId) {
       return battleLogRepository.findByBattleId(battleId);
    }
}
