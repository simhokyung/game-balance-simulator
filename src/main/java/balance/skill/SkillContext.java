package balance.skill;

import balance.battle.BattleCharacter;

/**
 * 스킬이 발동될 때 현재 전투 상황을 전달해 주는 컨텍스트.
 * - self: 스킬을 가진 캐릭터의 전투 상태
 * - opponent: 상대 캐릭터의 전투 상태
 */
public class SkillContext {

    private final BattleCharacter self;
    private final BattleCharacter opponent;

    public SkillContext(BattleCharacter self, BattleCharacter opponent) {
        this.self = self;
        this.opponent = opponent;
    }

    public BattleCharacter getSelf() {
        return self;
    }

    public BattleCharacter getOpponent() {
        return opponent;
    }
}
