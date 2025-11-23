package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 스킬 - Last Stand
 * - 체력이 최대 체력의 30% 이하로 떨어지면
 *   자신의 턴 시작 시 최대 체력의 5%를 회복한다.
 * - 위기 상황에서만 발동하는 생존용 스킬.
 */
public class BruiserLastStandSkill implements Skill {

    private static final double HP_THRESHOLD_RATIO = 0.3; // 30% 이하일 때 발동
    private static final double HEAL_RATIO = 0.05;        // 최대 체력의 5% 회복

    @Override
    public void onTurnStart(SkillContext context) {
        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        int maxHp = self.getCharacter().getMaxHp();
        int currentHp = self.getCurrentHp();

        double hpRatio = (double) currentHp / maxHp;
        if (hpRatio > HP_THRESHOLD_RATIO) {
            return;
        }

        int healAmount = (int) Math.max(1, Math.round(maxHp * HEAL_RATIO));
        self.heal(healAmount);
    }
}
