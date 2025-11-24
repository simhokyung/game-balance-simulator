package balance.skill.assassin;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Assassin 패시브 - 약점 포착(Exposed Weakness)
 *
 * - 적의 현재 HP가 50% 이하일 때,
 *   이번 공격으로 입힌 피해의 10%만큼 추가 피해를 준다.
 *
 * 설계상은 "치명타 확률 +10%"이지만,
 * 현재 크리 로직에 직접 개입하기 어렵기 때문에
 * 기대값에 해당하는 고정 추가 피해(+10%)로 구현했다.
 */
public class AssassinExposedWeaknessSkill implements Skill {

    private static final double HP_THRESHOLD = 0.5;        // 상대 HP 50% 이하일 때 발동
    private static final double BONUS_DAMAGE_RATIO = 0.10; // 추가 피해 10%

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (damageDealt <= 0) {
            return;
        }

        BattleCharacter self = context.getSelf();
        BattleCharacter opponent = context.getOpponent();

        if (self.isDead() || opponent.isDead()) {
            return;
        }

        int maxHp = opponent.getCharacter().getMaxHp();
        if (maxHp <= 0) {
            return;
        }

        double hpRatio = (double) opponent.getCurrentHp() / maxHp;
        if (hpRatio > HP_THRESHOLD) {
            return;
        }

        int extraDamage = (int) Math.round(damageDealt * BONUS_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }
    }
}
