package balance.skill.tank;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Tank 스킬2 - 철의 반격(Spiked Counter)
 *
 * 설계:
 *  - HP 50% 이상일 때, 50% 확률로 받은 피해의 30%를 반사, 쿨타임 3턴
 *
 * 구현:
 *  - 확률 대신 "기댓값"으로 단순화:
 *    → HP 50% 이상이고, 쿨타임 0이면 받은 피해의 15%를 확정 반사
 */
public class TankSpikedCounterSkill implements Skill {

    private static final double MIN_HP_RATIO = 0.50;
    private static final double REFLECT_EXPECTED_RATIO = 0.15;
    private static final int COOLDOWN_TURNS = 3;

    private int cooldown = 0;

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
        BattleCharacter attacker = context.getOpponent();

        if (self.isDead() || attacker.isDead()) {
            return;
        }

        int maxHp = self.getCharacter().getMaxHp();
        if (maxHp <= 0) {
            return;
        }

        double hpRatio = (double) self.getCurrentHp() / maxHp;

        // HP 50% 이상 + 쿨타임 0일 때만 발동
        if (hpRatio < MIN_HP_RATIO || cooldown > 0) {
            return;
        }

        int reflectDamage = (int) Math.round(damageTaken * REFLECT_EXPECTED_RATIO);
        if (reflectDamage > 0) {
            attacker.takeDamage(reflectDamage);
        }

        cooldown = COOLDOWN_TURNS;
    }
}
