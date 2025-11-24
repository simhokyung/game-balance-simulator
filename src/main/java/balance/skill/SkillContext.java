package balance.skill;

import balance.battle.BattleCharacter;

/**
 * 스킬이 발동될 때 현재 전투 상황을 전달해 주는 컨텍스트.
 * - self: 스킬을 가진 캐릭터의 전투 상태
 * - opponent: 상대 캐릭터의 전투 상태
 * - firstAttackInBattle: 이 캐릭터의 이번 공격이
 *   "전투 시작 이후 첫 번째 공격"인지 여부
 */
public class SkillContext {

    private final BattleCharacter self;
    private final BattleCharacter opponent;
    private final boolean firstAttackInBattle;

    /**
     * 기본 생성자: firstAttackInBattle = false 로 간주.
     * (기존 코드 호환용)
     */
    public SkillContext(BattleCharacter self, BattleCharacter opponent) {
        this(self, opponent, false);
    }

    /**
     * 확장 생성자:
     * 전투 내 첫 번째 공격인지 여부를 함께 넘길 수 있다.
     */
    public SkillContext(BattleCharacter self,
                        BattleCharacter opponent,
                        boolean firstAttackInBattle) {
        if (self == null || opponent == null) {
            throw new IllegalArgumentException("SkillContext의 self/opponent는 null일 수 없습니다.");
        }
        this.self = self;
        this.opponent = opponent;
        this.firstAttackInBattle = firstAttackInBattle;
    }

    public BattleCharacter getSelf() {
        return self;
    }

    public BattleCharacter getOpponent() {
        return opponent;
    }

    /**
     * 이 공격이 전투 시작 이후 이 캐릭터의 첫 번째 공격인지 여부.
     * AssassinOpeningStrikeSkill 같은 스킬에서 사용한다.
     */
    public boolean isFirstAttackInBattle() {
        return firstAttackInBattle;
    }
}
