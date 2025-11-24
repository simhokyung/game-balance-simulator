package balance.skill.hybrid;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Hybrid 스킬1 - Tactical Focus
 *
 * - onTurnStart에서, 쿨타임이 0이고 버프가 없으면 자동 발동.
 * - 2턴 동안 공격 피해 +20% (ATK+크리 기대값을 합산한 근사치).
 * - 쿨타임: 4턴.
 */
public class HybridTacticalFocusSkill implements Skill {

    private static final int DURATION_TURNS = 2;
    private static final int COOLDOWN_TURNS = 4;
    private static final double BONUS_DAMAGE_RATIO = 0.20;

    private int buffTurnsRemaining = 0;
    private int cooldown = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        if (cooldown > 0) {
            cooldown--;
        }
        if (buffTurnsRemaining > 0) {
            buffTurnsRemaining--;
        }

        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        if (buffTurnsRemaining == 0 && cooldown == 0) {
            buffTurnsRemaining = DURATION_TURNS;
            cooldown = COOLDOWN_TURNS;
        }
    }

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (damageDealt <= 0) return;

        if (buffTurnsRemaining <= 0) return;

        BattleCharacter self = context.getSelf();
        BattleCharacter opponent = context.getOpponent();

        if (self.isDead() || opponent.isDead()) return;

        int extraDamage = (int) Math.round(damageDealt * BONUS_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }
    }
}
