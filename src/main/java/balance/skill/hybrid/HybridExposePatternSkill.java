package balance.skill.hybrid;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Hybrid 스킬2 - Expose Pattern
 *
 * - 같은 대상에게 2회 이상 연속 공격 시, 이번 공격에 한해
 *   적 방어력 10% 무시 → 피해 +10%로 근사.
 * - 쿨타임: 3턴.
 *
 */
public class HybridExposePatternSkill implements Skill {

    private static final double BONUS_DAMAGE_RATIO = 0.10;
    private static final int COOLDOWN_TURNS = 3;

    private int consecutiveAttacks = 0;
    private int cooldown = 0;
    private boolean activeThisTurn = false;

    @Override
    public void onTurnStart(SkillContext context) {
        if (cooldown > 0) {
            cooldown--;
        }
        activeThisTurn = false;
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        consecutiveAttacks++;

        if (cooldown > 0) {
            return;
        }

        if (consecutiveAttacks >= 2) {
            activeThisTurn = true;
        }
    }

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (!activeThisTurn || damageDealt <= 0) {
            activeThisTurn = false;
            return;
        }

        BattleCharacter self = context.getSelf();
        BattleCharacter opponent = context.getOpponent();

        if (self.isDead() || opponent.isDead()) {
            activeThisTurn = false;
            cooldown = COOLDOWN_TURNS;
            return;
        }

        int extraDamage = (int) Math.round(damageDealt * BONUS_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        cooldown = COOLDOWN_TURNS;
        activeThisTurn = false;
    }
}
