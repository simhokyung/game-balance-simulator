package balance.skill.hybrid;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Hybrid 패시브 - Balanced Instinct
 *
 * - HP 50% 이상: 공격 특화 → 공격 시 피해 +5% (추가 피해)
 * - HP 50% 미만: 방어 특화 → 피격 시 피해의 5%를 되돌려 받음 (실질 피감)
 */
public class HybridBalancedInstinctSkill implements Skill {

    private static final double THRESHOLD = 0.5;
    private static final double BONUS_RATIO = 0.05 ; // 10% -> 5%로 너프

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (damageDealt <= 0) return;

        BattleCharacter self = context.getSelf();
        BattleCharacter opponent = context.getOpponent();

        if (self.isDead() || opponent.isDead()) return;

        int maxHp = self.getCharacter().getMaxHp();
        if (maxHp <= 0) return;

        double hpRatio = (double) self.getCurrentHp() / maxHp;
        if (hpRatio < THRESHOLD) {
            return; // 방어 모드 구간에서는 공격 버프 없음
        }

        int extraDamage = (int) Math.round(damageDealt * BONUS_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }
    }

    @Override
    public void onDamaged(SkillContext context, int damageTaken) {
        if (damageTaken <= 0) return;

        BattleCharacter self = context.getSelf();
        if (self.isDead()) return;

        int maxHp = self.getCharacter().getMaxHp();
        if (maxHp <= 0) return;

        double hpRatio = (double) self.getCurrentHp() / maxHp;
        if (hpRatio >= THRESHOLD) {
            return; // 공격 모드 구간에서는 방어 버프 없음
        }

        int healAmount = (int) Math.round(damageTaken * BONUS_RATIO);
        if (healAmount > 0) {
            self.heal(healAmount);
        }
    }
}
