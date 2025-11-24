package balance.skill.speedster;

import static balance.preset.PresetCharacters.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Speedster 스킬1 - Quick Combo
 *
 * - 자기 공격 횟수를 기준으로 주기적으로 추가 타격을 주는 스킬.
 * - 구현:
 *   - 공격 전마다 actionCount를 증가시킨다.
 *   - 쿨타임이 0이고, actionCount가 2 이상이면 이번 공격에 대해
 *     준 피해의 50%를 추가 피해로 넣는다.
 *   - 발동 후 쿨타임 4턴.
 */
public class SpeedsterQuickComboSkill implements Skill {


    private static final double EXTRA_DAMAGE_RATIO = 0.50;
    private static final int COOLDOWN_TURNS = 4;

    private int actionCount = 0;
    private int cooldown = 0;
    private boolean activeThisTurn = false;

    @Override
    public void onTurnStart(SkillContext context) {
        if (cooldown > 0) {
            cooldown--;
        }
        activeThisTurn = false;
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        actionCount++;

        if (cooldown > 0) {
            return;
        }

        if (actionCount >= 2) {
            activeThisTurn = true;
        }
    }

    @Override
    public void onAfterAttack(SkillContext context, int damageDealt) {
        if (!activeThisTurn || damageDealt <= 0) {
            activeThisTurn = false;
            return;
        }

        BattleCharacter self = context.getSelf();
        BattleCharacter opponent = context.getOpponent();

        if (self.isDead() || opponent.isDead()) {
            activeThisTurn = false;
            cooldown = COOLDOWN_TURNS;
            return;
        }

        int extraDamage = (int) Math.round(damageDealt * EXTRA_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        cooldown = COOLDOWN_TURNS;
        activeThisTurn = false;
    }
}
