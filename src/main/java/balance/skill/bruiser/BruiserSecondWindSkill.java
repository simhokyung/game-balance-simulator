package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 스킬 - 근성(Second Wind)
 *
 * - 전투 중 자신의 HP가 처음으로 30% 이하로 떨어질 때 1회 발동한다.
 *   - 즉시 최대 체력의 20%를 회복하고,
 *   - 이후 2턴 동안 받은 피해의 10%를 다시 회복(= 실질적인 피해 감소 효과).
 */
public class BruiserSecondWindSkill implements Skill {

    private static final double HP_THRESHOLD_RATIO = 0.30;   // 30%
    private static final double HEAL_RATIO = 0.20;           // 최대 체력의 20% 회복
    private static final double DAMAGE_REDUCTION_HEAL = 0.10; // 받은 피해의 10% 회복

    private boolean triggered = false;
    private int damageReductionTurnsRemaining = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        if (damageReductionTurnsRemaining > 0) {
            damageReductionTurnsRemaining--;
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

        int maxHp = self.getCharacter().getMaxHp();
        int currentHp = self.getCurrentHp();

        // 1) 아직 한 번도 발동하지 않았고, 현재 HP가 30% 이하로 떨어졌다면 발동
        if (!triggered && currentHp <= maxHp * HP_THRESHOLD_RATIO) {
            triggered = true;

            int healAmount = (int) Math.max(1, Math.round(maxHp * HEAL_RATIO));
            self.heal(healAmount);

            damageReductionTurnsRemaining = 2;
        }

        // 2) 피해 감소 효과가 남아 있을 때는 받은 피해의 일부를 회복 (= 실질적인 피해 감소)
        if (damageReductionTurnsRemaining > 0) {
            int healFromDamage = (int) Math.max(1, Math.round(damageTaken * DAMAGE_REDUCTION_HEAL));
            self.heal(healFromDamage);
        }
    }
}
