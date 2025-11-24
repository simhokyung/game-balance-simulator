package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 분노 시스템 스킬 (2차 너프 버전)
 *
 * - 피해를 받을 때마다 분노 스택이 쌓인다. (최대 10스택)
 * - 평소에는 스택이 있어도 추가 피해 없음.
 * - 스택이 7 이상이고 쿨타임이 없을 때, 이번 공격에 한해
 *   스택당 1%의 추가 피해를 주고(분노 폭발),
 *   사용 후 스택을 모두 소모하며 5턴 쿨타임에 들어간다.
 *
 * 즉, "맞을수록 세진다"는 느낌은 있지만
 * 항상 OP 딜이 아니라, 가끔 한 번 터지는 정도로 제한했다.
 */
public class BruiserRageSkill implements Skill {

    private static final int MAX_RAGE_STACKS = 10;
    private static final int RAGING_BLOW_REQUIRED_STACKS = 7;
    private static final int RAGING_BLOW_COOLDOWN_TURNS = 5;

    // 평소 추가 피해는 없음
    private static final double BASE_BONUS_PER_STACK = 0.0;

    // 분노 폭발 시: 스택당 +1% → 풀스택이면 +10%
    private static final double RAGING_BLOW_BONUS_PER_STACK = 0.01;

    private int rageStacks = 0;
    private int ragingBlowCooldown = 0;
    private boolean ragingBlowActiveThisTurn = false;

    @Override
    public void onTurnStart(SkillContext context) {
        if (ragingBlowCooldown > 0) {
            ragingBlowCooldown--;
        }
        ragingBlowActiveThisTurn = false;
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

        if (rageStacks < MAX_RAGE_STACKS) {
            rageStacks++;
        }
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        if (rageStacks >= RAGING_BLOW_REQUIRED_STACKS && ragingBlowCooldown == 0) {
            ragingBlowActiveThisTurn = true;
        }
    }

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

        if (rageStacks <= 0) {
            return;
        }

        double bonusPerStack = BASE_BONUS_PER_STACK;

        if (ragingBlowActiveThisTurn) {
            bonusPerStack += RAGING_BLOW_BONUS_PER_STACK;
        }

        double bonusRatio = bonusPerStack * rageStacks;
        int extraDamage = (int) Math.round(damageDealt * bonusRatio);

        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        if (ragingBlowActiveThisTurn) {
            rageStacks = 0;
            ragingBlowCooldown = RAGING_BLOW_COOLDOWN_TURNS;
            ragingBlowActiveThisTurn = false;
        }
    }
}
