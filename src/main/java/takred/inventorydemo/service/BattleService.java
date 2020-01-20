package takred.inventorydemo.service;

import org.springframework.stereotype.Service;
import takred.inventorydemo.entity.*;
import takred.inventorydemo.repository.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BattleService {
    private final PersonRepository personRepository;
    private final MonsterRepository monsterRepository;
    private final BattleRepository battleRepository;
    private final BattleLogRepository battleLogRepository;
    private final DeathAndResurrectionLogRepository deathAndResurrectionLogRepository;

    public BattleService(PersonRepository personRepository, MonsterRepository monsterRepository, BattleRepository battleRepository, BattleLogRepository battleLogRepository, DeathAndResurrectionLogRepository deathAndResurrectionLogRepository
    ) {
        this.personRepository = personRepository;
        this.monsterRepository = monsterRepository;
        this.battleRepository = battleRepository;
        this.battleLogRepository = battleLogRepository;
        this.deathAndResurrectionLogRepository = deathAndResurrectionLogRepository;
    }

    public String battle(UUID personId, UUID monsterId) {
        Monster monster = new Monster(monsterRepository.findById(monsterId).get());
        Person person = new Person(personRepository.findById(personId).get());
        List<Battle> allBattlesPerson = battleRepository.findByPersonId(personId);
        Battle battle = new Battle();
        battle.setPersonId(person.getId());
        battle.setMonsterId(monsterId);
        if (allBattlesPerson == null) {
            battle.setBattleNumber(1);
        } else {
            battle.setBattleNumber(allBattlesPerson.size() + 1);
        }
        Integer turn = 1;
        while (true) {
            Integer currentDamagePerson = currentDamage(person.getMinDamage(), person.getMaxDamage());
            monster.setHp(monster.getHp() - currentDamagePerson);
            if (monster.getHp() <= 0) {
                person.setExp(person.getExp() + monster.getExpForWin());
                if (checkLvlUp(person)) {
                    lvlUp(person);
                }
                personRepository.save(person);
                battle.setWinner(personId);
                battleRepository.save(battle);
                battleLog(battle.getId(), personId, turn,
                        person.getName() + " наносит " + currentDamagePerson + " урона. У " +
                                monster.getName() + " осталось " + monster.getHp() + " здоровья." + " Победил герой!");
                return "Победил герой!";
            }
            battleLog(battle.getId(), personId, turn,
                    person.getName() + " наносит " + currentDamagePerson + " урона. У " +
                            monster.getName() + " осталось " + monster.getHp() + " здоровья.");
            Integer currentDamageMonster = currentDamage(monster.getMinDamage(), monster.getMaxDamage());
            person.setHp(person.getHp() - currentDamageMonster);
            if (person.getHp() <= 0) {
                personRepository.save(person);
                battle.setWinner(monsterId);
                battleRepository.save(battle);
                battleLog(battle.getId(), personId, turn + 1,
                        monster.getName() + " наносит " + currentDamageMonster + " урона. У " +
                                person.getName() + " осталось " + person.getHp() + " здоровья." + " Победил монстр!");
                deathAndResurrectionLogRepository.save(new DeathAndResurrectionLog(personId, "Персонаж погиб."));
                return "Победил монстр!";
            }
            battleLog(battle.getId(), personId, turn + 1,
                    monster.getName() + " наносит " + currentDamageMonster + " урона. У " +
                            person.getName() + " осталось " + person.getHp() + " здоровья.");
            turn = turn + 2;
        }
    }

    private void battleLog(UUID battleId, UUID personId, Integer turn, String message) {
        BattleLog battleLog = new BattleLog();
        battleLog.setPersonId(personId);
        battleLog.setBattleId(battleId);
        battleLog.setTurn(turn);
        battleLog.setMessage(message);
        battleLogRepository.save(battleLog);
    }

    private void lvlUp(Person person) {
        person.setMaxDamage(person.getMaxDamage() + 1);
        person.setMaxHp(person.getMaxHp() + 10);
        person.setHp(person.getMaxHp());
        person.setLvl(person.getLvl() + 1);
        person.setExpForNextLvl(person.getExpForNextLvl() + 100);
        personRepository.save(person);
    }

    private boolean checkLvlUp(Person person) {
        if (person.getExp() >= person.getExpForNextLvl()) {
            return true;
        }
        return false;
    }

    private Integer currentDamage(Integer minDamage, Integer maxDamage) {
        if (minDamage < maxDamage) {
            return ThreadLocalRandom.current().nextInt(minDamage, maxDamage);
        } else if (maxDamage < minDamage) {
            return ThreadLocalRandom.current().nextInt(maxDamage, minDamage);
        }
        return minDamage;
    }

//    private void deathLog(UUID personId, String message) {
//        DeathAndResurrectionLog deathAndResurrectionLog = new DeathAndResurrectionLog();
//        deathAndResurrectionLog.setMessage(message);
//        deathAndResurrectionLog.setPersonId(personId);
//        deathAndResurrectionLogRepository.save(deathAndResurrectionLog);
//    }
}
