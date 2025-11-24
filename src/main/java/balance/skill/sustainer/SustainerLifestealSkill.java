package balance.skill.sustainer;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 서스테이너 패시브 스킬 - Lifesteal
 *
 * - 자신이 준 피해의 5%만큼 체력을 회복한다.
 */
public class SustainerLifestealSkill implements Skill {

    private static final double PASSIVE_LIFESTEAL = 0.05; // 5%

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        BattleCharacter self = context.getSelf();
        if (self.isDead() || damageDealt <= 0) {
            return;
        }

        int healAmount = (int) Math.round(damageDealt * PASSIVE_LIFESTEAL);
        if (healAmount > 0) {
            self.heal(healAmount);
        }
    }
}
