package balance.skill.tank;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Tank 스킬1 - 방어 자세(Fortify)
 *
 * - 자기 턴 시작 시, 쿨타임이 0이면 자동 발동.
 *   2턴 동안 "받는 피해의 10%를 추가로 되돌려주는" 효과를 준다.
 * - 쿨타임: 5턴 (자신 기준)
 */
public class TankFortifySkill implements Skill {

    private static final int FORTIFY_DURATION_TURNS = 2;
    private static final int FORTIFY_COOLDOWN_TURNS = 5;
    private static final double EXTRA_REDUCTION_RATIO = 0.10;

    private int fortifyTurnsRemaining = 0;
    private int cooldown = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        // 쿨타임/지속 턴 감소
        if (cooldown > 0) {
            cooldown--;
        }
        if (fortifyTurnsRemaining > 0) {
            fortifyTurnsRemaining--;
        }

        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        // 버프가 없는 상태에서 쿨타임이 0이면 방어 자세 발동
        if (fortifyTurnsRemaining == 0 && cooldown == 0) {
            fortifyTurnsRemaining = FORTIFY_DURATION_TURNS;
            cooldown = FORTIFY_COOLDOWN_TURNS;
        }
    }

    @Override
    public void onDamaged(SkillContext context, int damageTaken) {
        if (damageTaken <= 0) {
            return;
        }

        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        if (fortifyTurnsRemaining > 0) {
            int healAmount = (int) Math.round(damageTaken * EXTRA_REDUCTION_RATIO);
            if (healAmount > 0) {
                self.heal(healAmount);
            }
        }
    }
}
