package balance.skill.sustainer;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 서스테이너 액티브 스킬 - 흡혈 강화(Blood Frenzy)
 *
 * - 자신의 HP가 30% 이하이고, 쿨타임이 0일 때:
 *   이번 공격으로 준 피해의 15%를 추가로 회복한다.
 *   이후 4턴 쿨타임에 들어간다.
 */
public class SustainerBloodFrenzySkill implements Skill {

    private static final double BLOOD_FRENZY_THRESHOLD = 0.30;          // HP 30% 이하일 때 발동
    private static final double BLOOD_FRENZY_EXTRA_LIFESTEAL = 0.15;    // 추가 15% 흡혈
    private static final int BLOOD_FRENZY_COOLDOWN_TURNS = 4;           // 쿨타임 4턴

    private int bloodFrenzyCooldown = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        if (bloodFrenzyCooldown > 0) {
            bloodFrenzyCooldown--;
        }
    }

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        BattleCharacter self = context.getSelf();

        if (self.isDead() || damageDealt <= 0) {
            return;
        }

        double hpRatio = hpRatio(self);
        if (hpRatio <= BLOOD_FRENZY_THRESHOLD && bloodFrenzyCooldown == 0) {
            int extraLifesteal = (int) Math.round(damageDealt * BLOOD_FRENZY_EXTRA_LIFESTEAL);
            if (extraLifesteal > 0) {
                self.heal(extraLifesteal);
            }
            bloodFrenzyCooldown = BLOOD_FRENZY_COOLDOWN_TURNS;
        }
    }

    private double hpRatio(BattleCharacter self) {
        int current = self.getCurrentHp();
        int max = self.getCharacter().getMaxHp();
        if (max <= 0) {
            return 0.0;
        }
        return (double) current / max;
    }
}
