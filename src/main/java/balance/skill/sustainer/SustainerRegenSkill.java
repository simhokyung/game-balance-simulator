package balance.skill.sustainer;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 서스테이너 액티브 스킬 - 재생(Healing Aura)
 *
 * - 자신의 HP 비율이 30% 이하일 때:
 *   - 재생 효과가 없고, 쿨타임이 0이면
 *     3턴 동안 매 턴 시작 시 최대 HP의 5%를 회복한다.
 *   - 재사용 대기시간: 5턴
 */
public class SustainerRegenSkill implements Skill {

    private static final double REGEN_THRESHOLD = 0.30;       // HP 30% 이하일 때 발동
    private static final double REGEN_HEAL_PERCENT = 0.05;    // 턴당 최대 HP의 5%
    private static final int REGEN_DURATION_TURNS = 3;        // 3턴 지속
    private static final int REGEN_COOLDOWN_TURNS = 5;        // 쿨타임 5턴

    private int regenTurnsRemaining = 0;
    private int regenCooldown = 0;

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

        // 활성화된 재생 효과 처리
        if (regenTurnsRemaining > 0) {
            int maxHp = self.getCharacter().getMaxHp();
            int healAmount = Math.max(1, (int) Math.round(maxHp * REGEN_HEAL_PERCENT));
            self.heal(healAmount);
            regenTurnsRemaining--;
        }

        // 새로 재생 효과 시작 여부 판단
        if (regenTurnsRemaining == 0 && regenCooldown == 0) {
            double hpRatio = hpRatio(self);
            if (hpRatio <= REGEN_THRESHOLD) {
                regenTurnsRemaining = REGEN_DURATION_TURNS;
                regenCooldown = REGEN_COOLDOWN_TURNS;
            }
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
