package balance.skill.speedster;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Speedster 스킬2 - Precise Dodge
 *
 * - 큰 피해(직전 HP의 25% 이상)를 받으면:
 *    - 해당 피해의 30%를 즉시 회복 (첫 히트 감소)
 *    - 다음 한 번 더 맞을 때, 그 피해의 50%를 회복 (기댓값 회피)
 *    - 쿨타임 4턴
 *
 * - nextHitMitigationCharges를 이용해 "다음 한 번"만 추가 감소.
 */
public class SpeedsterPreciseDodgeSkill implements Skill {

    private static final double BIG_HIT_RATIO = 0.25;
    private static final double FIRST_HIT_REDUCE = 0.30;
    private static final double NEXT_HIT_REDUCE = 0.50;
    private static final int COOLDOWN_TURNS = 4;

    private int cooldown = 0;
    private int nextHitMitigationCharges = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        if (cooldown > 0) {
            cooldown--;
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

        int currentHp = self.getCurrentHp();
        int prevHp = currentHp + damageTaken;
        if (prevHp <= 0) {
            return;
        }

        double hitRatio = (double) damageTaken / prevHp;

        // 1) 큰 피해 + 쿨타임 0 → 첫 히트 감소 + 다음 히트 감소 준비
        if (cooldown == 0 && hitRatio >= BIG_HIT_RATIO) {
            int healNow = (int) Math.round(damageTaken * FIRST_HIT_REDUCE);
            if (healNow > 0) {
                self.heal(healNow);
            }

            nextHitMitigationCharges = 1;
            cooldown = COOLDOWN_TURNS;
            return;
        }

        // 2) 다음 히트에 대해 50% 기대 감소
        if (nextHitMitigationCharges > 0) {
            int heal = (int) Math.round(damageTaken * NEXT_HIT_REDUCE);
            if (heal > 0) {
                self.heal(heal);
            }
            nextHitMitigationCharges--;
        }
    }
}
