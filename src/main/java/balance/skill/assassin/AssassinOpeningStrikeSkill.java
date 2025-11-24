package balance.skill.assassin;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * Assassin 스킬1 - 선제 일격(Opening Strike)
 *
 * - 전투 내 "첫 번째 공격"에 한해 발동.
 * - 그 공격으로 입힌 피해의 40%만큼 추가 피해를 준다.
 *
 */
public class AssassinOpeningStrikeSkill implements Skill {

    private static final double BONUS_DAMAGE_RATIO = 0.40;

    private boolean consumed = false;           // 전투당 1회
    private boolean activeThisTurn = false;     // 이번 공격에 적용 여부

    @Override
    public void onTurnStart(SkillContext context) {
        // 턴이 바뀔 때마다 플래그 리셋
        activeThisTurn = false;
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        if (consumed) {
            return;
        }

        // SkillContext의 플래그를 이용해 "전투 내 첫 공격"만 잡는다.
        if (context.isFirstAttackInBattle()) {
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
            consumed = true;
            return;
        }

        int extraDamage = (int) Math.round(damageDealt * BONUS_DAMAGE_RATIO);
        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        // 전투당 1회만
        consumed = true;
        activeThisTurn = false;
    }
}
