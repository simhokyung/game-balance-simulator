package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 분노 시스템 스킬
 *
 * - 피해를 받을 때마다 분노 스택이 쌓인다.
 * - 스택당 기본적으로 가한 피해가 2% 증가한다.
 * - 스택이 5 이상이고 쿨타임이 없을 때, 이번 공격에 한해 추가로
 *   스택당 3%의 추가 피해를 주고, 스택을 모두 소모하며 쿨타임에 들어간다.
 *
 * 즉, 항상 "맞을수록 조금씩 세지고",
 * 특정 타이밍에는 "분노 폭발"로 큰 한 방을 넣을 수 있다.
 */
public class BruiserRageSkill implements Skill {

    private static final int MAX_RAGE_STACKS = 10;
    private static final int RAGING_BLOW_REQUIRED_STACKS = 5;
    private static final int RAGING_BLOW_COOLDOWN_TURNS = 3;

    private static final double BASE_BONUS_PER_STACK = 0.02;      // 기본: 스택당 +2%
    private static final double RAGING_BLOW_BONUS_PER_STACK = 0.03; // 분노 폭발 시 추가: 스택당 +3%

    private int rageStacks = 0;
    private int ragingBlowCooldown = 0;
    private boolean ragingBlowActiveThisTurn = false;

    @Override
    public void onTurnStart(SkillContext context) {
        // 매 자신의 턴 시작 시 쿨타임 감소 & 플래그 초기화
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

        // 피해를 받을 때마다 분노 스택 +1 (최대 10)
        if (rageStacks < MAX_RAGE_STACKS) {
            rageStacks++;
        }
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        // 분노 폭발 발동 조건: 스택 5 이상 + 쿨타임 없음
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

        // 항상 적용되는 기본 분노 보너스: 스택당 2%
        double bonusPerStack = BASE_BONUS_PER_STACK;

        // 이번 턴에 분노 폭발이 켜져 있으면 추가로 스택당 3% 보너스
        if (ragingBlowActiveThisTurn) {
            bonusPerStack += RAGING_BLOW_BONUS_PER_STACK;
        }

        double bonusRatio = bonusPerStack * rageStacks;
        int extraDamage = (int) Math.round(damageDealt * bonusRatio);

        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        // 분노 폭발을 사용했다면 스택 초기화 + 쿨타임 진입
        if (ragingBlowActiveThisTurn) {
            rageStacks = 0;
            ragingBlowCooldown = RAGING_BLOW_COOLDOWN_TURNS;
            ragingBlowActiveThisTurn = false;
        }
    }
}
