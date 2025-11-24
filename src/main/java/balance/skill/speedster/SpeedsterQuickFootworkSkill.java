package balance.skill.speedster;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Speedster 패시브 - Quick Footwork
 *
 * - 속도로 이득을 보는 컨셉을, "평균 추가 피해 +5%"로 근사.
 * - 매 공격 후, 준 피해의 5%를 추가로 입힌다.
 */
public class SpeedsterQuickFootworkSkill implements Skill {

    private static final double BONUS_DAMAGE_RATIO = 0.05;

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

        int extraDamage = (int) Math.round(damageDealt * BONUS_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }
    }
}
