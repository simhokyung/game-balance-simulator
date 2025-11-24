package balance.skill.assassin;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Assassin 스킬2 - 연막(Shadow Evasion)
 *
 * 설계:
 *  - 자신 HP가 30% 이하로 떨어지는 피해를 입었을 때:
 *      - 해당 공격 피해 30% 감소
 *      - 다음 1턴 동안 적 공격을 50% 확률로 회피
 *      - 쿨타임 5턴
 *
 * 구현:
 *  - onDamaged에서 "피해 30%만큼 즉시 회복" → 해당 공격 30% 감소 효과
 *  - 다음 1턴 동안은 onDamaged에서 "피해 50%만큼 회복" → 기대값 기준 회피 효과를 근사
 *  - 쿨타임 턴 단위 관리
 */
public class AssassinShadowEvasionSkill implements Skill {

    private static final double HP_THRESHOLD = 0.30;                 // HP 30% 이하
    private static final double FIRST_HIT_REDUCE = 0.30;             // 첫 발동시 30% 감소
    private static final double NEXT_TURN_REDUCE = 0.50;             // 다음 1턴 동안 기대값 50% 감소
    private static final int NEXT_TURN_DURATION_TURNS = 1;           // 1턴 지속
    private static final int COOLDOWN_TURNS = 5;                     // 쿨타임 5턴

    private int evasiveTurnsRemaining = 0;
    private int cooldown = 0;

    @Override
    public void onTurnStart(SkillContext context) {
        if (cooldown > 0) {
            cooldown--;
        }
        if (evasiveTurnsRemaining > 0) {
            evasiveTurnsRemaining--;
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
        if (maxHp <= 0) {
            return;
        }

        double hpRatio = (double) self.getCurrentHp() / maxHp;

        // 1) 트리거 조건 충족 + 쿨타임 0 → 이번 공격 30% 감쇄 + 다음 1턴 동안 회피 버프
        if (cooldown == 0 && hpRatio <= HP_THRESHOLD) {
            int healNow = (int) Math.round(damageTaken * FIRST_HIT_REDUCE);
            if (healNow > 0) {
                self.heal(healNow);
            }

            evasiveTurnsRemaining = NEXT_TURN_DURATION_TURNS;
            cooldown = COOLDOWN_TURNS;
            return; // 이번 히트에 대해서는 여기까지
        }

        // 2) 회피 버프가 살아 있는 동안은 맞을 때마다 50% 기대감쇄
        if (evasiveTurnsRemaining > 0) {
            int heal = (int) Math.round(damageTaken * NEXT_TURN_REDUCE);
            if (heal > 0) {
                self.heal(heal);
            }
        }
    }
}
