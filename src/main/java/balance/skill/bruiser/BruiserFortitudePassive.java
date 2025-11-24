package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 패시브 - Fortitude
 * - 피해를 받을 때마다 최대 체력의 일부를 회복한다.
 * - 효과는 매우 작게(약 2%) 설정해서 과도하게 세지 않도록 한다.
 */
public class BruiserFortitudePassive implements Skill {

    private static final double MAX_HP_HEAL_RATIO = 0.01; // 2% → 1% 로 너프

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
        int healAmount = (int) Math.max(1, Math.round(maxHp * MAX_HP_HEAL_RATIO));
        self.heal(healAmount);
    }
}
