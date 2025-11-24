package balance.skill.bruiser;

/**
 * 브루저의 분노/쿨타임 상태를 공유하기 위한 전용 상태 클래스.
 * - BruiserRagePassiveSkill 과 BruiserRagingBlowSkill 이 함께 사용한다.
 */
public class BruiserRageState {

    static final int MAX_RAGE_STACKS = 10;
    static final int RAGING_BLOW_REQUIRED_STACKS = 7;
    static final int RAGING_BLOW_COOLDOWN_TURNS = 5;

    // 스택/쿨타임/이번 턴 발동 여부
    int rageStacks = 0;
    int ragingBlowCooldown = 0;
    boolean ragingBlowActiveThisTurn = false;

    public int getRageStacks() {
        return rageStacks;
    }

    public void addRageStack() {
        if (rageStacks < MAX_RAGE_STACKS) {
            rageStacks++;
        }
    }

    public void resetRageStacks() {
        rageStacks = 0;
    }

    public int getRagingBlowCooldown() {
        return ragingBlowCooldown;
    }

    public void decreaseCooldown() {
        if (ragingBlowCooldown > 0) {
            ragingBlowCooldown--;
        }
    }

    public void startCooldown() {
        this.ragingBlowCooldown = RAGING_BLOW_COOLDOWN_TURNS;
    }

    public boolean isRagingBlowActiveThisTurn() {
        return ragingBlowActiveThisTurn;
    }

    public void setRagingBlowActiveThisTurn(boolean ragingBlowActiveThisTurn) {
        this.ragingBlowActiveThisTurn = ragingBlowActiveThisTurn;
    }
}
