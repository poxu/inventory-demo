package takred.inventorydemo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Battle {
    private UUID personId;
    private UUID monsterId;
    private Integer battleNumber;
    private UUID winner;
    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    public Battle() {
    }

    //    public UUID getPersonId() {
//        return personId;
//    }
//
//    public void setPersonId(UUID personId) {
//        this.personId = personId;
//    }
//
//    public UUID getMonsterId() {
//        return monsterId;
//    }
//
//    public void setMonsterId(UUID monsterId) {
//        this.monsterId = monsterId;
//    }
//
//    public Integer getBattleNumber() {
//        return battleNumber;
//    }
//
//    public void setBattleNumber(Integer battleNumber) {
//        this.battleNumber = battleNumber;
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public UUID getWinner() {
//        return winner;
//    }
//
//    public void setWinner(UUID winner) {
//        this.winner = winner;
//    }
}
