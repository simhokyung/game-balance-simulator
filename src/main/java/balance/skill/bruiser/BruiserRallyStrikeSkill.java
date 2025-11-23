package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 스킬 - Rally Strike
 * - 공격으로 준 피해의 일부(약 5%)를 즉시 회복한다.
 * - 고정 쿨다운은 없고, 매 공격 시 발동하지만 비율을 작게 해서 밸런스를 맞춘다.
 */
public class BruiserRallyStrikeSkill implements Skill {

    private static final double LIFE_STEAL_RATIO = 0.05; // 가한 피해의 5%

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (damageDealt <= 0) {
            return;
        }
        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        int healAmount = (int) Math.max(1, Math.round(damageDealt * LIFE_STEAL_RATIO));
        self.heal(healAmount);
    }
}
