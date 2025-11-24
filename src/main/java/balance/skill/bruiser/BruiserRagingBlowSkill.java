package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 액티브 스킬 - 분노 폭발(Raging Blow)
 *
 * - 턴 시작 시 쿨타임 감소 및 발동 플래그 초기화
 * - 공격 직전에:
 *      분노 스택이 7 이상이고 쿨타임이 0이면
 *      이번 공격에 한해 "분노 폭발"을 활성화
 * - 공격 후:
 *      분노 폭발이 활성화된 상태라면
 *      스택당 +1% 추가 피해를 가하고,
 *      스택을 모두 소모하며 5턴 쿨타임에 들어간다.
 */
public class BruiserRagingBlowSkill implements Skill {

    // 분노 폭발 시: 스택당 +1% → 풀스택(10스택)이면 +10%
    private static final double RAGING_BLOW_BONUS_PER_STACK = 0.01;

    private final BruiserRageState state;

    public BruiserRagingBlowSkill(BruiserRageState state) {
        if (state == null) {
            throw new IllegalArgumentException("BruiserRageState 는 null일 수 없습니다.");
        }
        this.state = state;
    }

    @Override
    public void onTurnStart(SkillContext context) {
        // 매 턴 시작 시 쿨타임 감소 + 발동 플래그 초기화
        state.decreaseCooldown();
        state.setRagingBlowActiveThisTurn(false);
    }

    @Override
    public void onBeforeAttack(SkillContext context) {
        // 분노 스택이 충분하고 쿨타임이 없으면 이번 공격에 한해 발동
        if (state.getRageStacks() >= BruiserRageState.RAGING_BLOW_REQUIRED_STACKS
                && state.getRagingBlowCooldown() == 0) {
            state.setRagingBlowActiveThisTurn(true);
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

        if (!state.isRagingBlowActiveThisTurn()) {
            return;
        }

        int stacks = state.getRageStacks();
        if (stacks <= 0) {
            return;
        }

        double bonusRatio = RAGING_BLOW_BONUS_PER_STACK * stacks;
        int extraDamage = (int) Math.round(damageDealt * bonusRatio);

        if (extraDamage > 0) {
            opponent.takeDamage(extraDamage);
        }

        // 사용 후 스택 초기화 + 쿨타임 시작 + 플래그 해제
        state.resetRageStacks();
        state.startCooldown();
        state.setRagingBlowActiveThisTurn(false);
    }
}
