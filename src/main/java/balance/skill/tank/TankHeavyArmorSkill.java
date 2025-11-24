package balance.skill.tank;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Tank 패시브 - 철벽(Heavy Armor)
 *
 * - 모든 피해에 대해 "고정 5 + 5%"만큼 피해를 줄여주는 효과를
 *   힐(heal)로 근사한다.
 *   실제 적용: healAmount = min(받은 피해, 5 + damage * 0.05)
 */
public class TankHeavyArmorSkill implements Skill {

    private static final int FLAT_REDUCTION = 5;
    private static final double RATIO_REDUCTION = 0.05;

    @Override
    public void onDamaged(SkillContext context, int damageTaken) {
        if (damageTaken <= 0) {
            return;
        }

        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        int reductionByFlat = FLAT_REDUCTION;
        int reductionByRatio = (int) Math.round(damageTaken * RATIO_REDUCTION);
        int totalReduction = reductionByFlat + reductionByRatio;

        // 실제 받은 피해보다 많이 되돌려주지 않는다.
        int healAmount = Math.min(damageTaken, totalReduction);
        if (healAmount <= 0) {
            return;
        }

        self.heal(healAmount);
    }
}
