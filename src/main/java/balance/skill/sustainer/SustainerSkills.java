package balance.skill.sustainer;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Sustainer 전용 스킬:
 * - 패시브: Lifesteal (준 피해의 10% 회복)
 * - 스킬1(재생): HP 70% 이하일 때 3턴 동안 매 턴 시작 시 최대 HP의 5% 회복 (쿨타임 5턴)
 * - 스킬2(흡혈 강화): HP 50% 이하일 때 이번 공격만 추가로 15% 흡혈 (쿨타임 4턴)
 */
public class SustainerSkills implements Skill {

    // 재생(Healing Aura) 관련 상태
    private int regenTurnsRemaining = 0;   // 재생 효과 남은 턴 수
    private int regenCooldown = 0;         // 재생 쿨타임 (턴 기준)

    // 흡혈 강화(Blood Frenzy) 관련 상태
    private int bloodFrenzyCooldown = 0;   // 흡혈 강화 쿨타임

    // 설정 값들
    private static final double PASSIVE_LIFESTEAL = 0.05;          // 기본 흡혈 10% -> 5%로 너프
    private static final double BLOOD_FRENZY_EXTRA_LIFESTEAL = 0.15; // 추가 15% → 총 20%
    private static final double REGEN_THRESHOLD = 0.30;            // HP 70% 이하에서 재생 시작 -> 30%로 너프
    private static final double BLOOD_FRENZY_THRESHOLD = 0.30;     // HP 50% 이하에서 흡혈 강화 -> 30%로 너프
    private static final double REGEN_HEAL_PERCENT = 0.05;         // 턴당 최대 HP의 5% 회복
    private static final int REGEN_DURATION_TURNS = 3;             // 재생 3턴 지속
    private static final int REGEN_COOLDOWN_TURNS = 5;             // 재생 쿨타임 5턴
    private static final int BLOOD_FRENZY_COOLDOWN_TURNS = 4;      // 흡혈 강화 쿨타임 4턴

    @Override
    public void onTurnStart(SkillContext context) {
        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        // 쿨타임 감소
        if (regenCooldown > 0) {
            regenCooldown--;
        }
        if (bloodFrenzyCooldown > 0) {
            bloodFrenzyCooldown--;
        }

        // 이미 활성화된 재생 효과가 있다면, 턴 시작 시 회복
        if (regenTurnsRemaining > 0) {
            int maxHp = self.getCharacter().getMaxHp();
            int healAmount = Math.max(1, (int) Math.round(maxHp * REGEN_HEAL_PERCENT));
            self.heal(healAmount);
            regenTurnsRemaining--;
        }

        // 재생 효과를 새로 시작할지 판단
        // - 현재 재생 효과 없음
        // - 재생 쿨타임 0
        // - HP 비율이 70% 이하
        if (regenTurnsRemaining == 0 && regenCooldown == 0) {
            double hpRatio = hpRatio(self);
            if (hpRatio <= REGEN_THRESHOLD) {
                regenTurnsRemaining = REGEN_DURATION_TURNS;
                regenCooldown = REGEN_COOLDOWN_TURNS;
            }
        }
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        // Sustainer는 데미지 배수를 직접 건드리지 않고,
        // onAfterAttack에서 흡혈량으로 효과를 준다.
    }

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        BattleCharacter self = context.getSelf();

        if (self.isDead() || damageDealt <= 0) {
            return;
        }

        // 1) 패시브 Lifesteal: 준 피해의 10% 회복
        int baseLifesteal = (int) Math.round(damageDealt * PASSIVE_LIFESTEAL);
        if (baseLifesteal > 0) {
            self.heal(baseLifesteal);
        }

        // 2) Blood Frenzy:
        //    HP 50% 이하 + 쿨타임 0 → 이번 공격만 추가 15% 흡혈
        double hpRatio = hpRatio(self);
        if (hpRatio <= BLOOD_FRENZY_THRESHOLD && bloodFrenzyCooldown == 0) {
            int extraLifesteal = (int) Math.round(damageDealt * BLOOD_FRENZY_EXTRA_LIFESTEAL);
            if (extraLifesteal > 0) {
                self.heal(extraLifesteal);
            }
            bloodFrenzyCooldown = BLOOD_FRENZY_COOLDOWN_TURNS;
        }
    }

    @Override
    public void onDamaged(SkillContext context, int damageTaken) {
        // 지금 설계에서는 피격 시 추가 효과 없음
        // (나중에 필요하면 여기서 "피격 횟수 기반 추가 근성" 같은 것 넣을 수 있음)
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
