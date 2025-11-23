package balance.skill;

/**
 * 모든 스킬이 공통으로 구현해야 할 인터페이스.
 * 기본 구현은 전부 "아무 것도 하지 않음"으로 두어서
 * 스킬이 없으면 전투 결과가 기존과 똑같이 나오도록 한다.
 */
public interface Skill {

    /**
     * 내 턴이 시작될 때 호출.
     */
    default void onTurnStart(SkillContext context) {
        // 기본 구현: 아무 것도 하지 않음
    }

    /**
     * 내가 공격하기 직전에 호출.
     */
    default void onBeforeAttack(SkillContext context) {
        // 기본 구현: 아무 것도 하지 않음
    }

    /**
     * 내가 공격을 끝낸 후 호출.
     *
     * @param damageDealt 이번 공격으로 실제로 들어간 피해량
     */
    default void onAfterAttack(SkillContext context, int damageDealt) {
        // 기본 구현: 아무 것도 하지 않음
    }

    /**
     * 내가 피해를 입었을 때 호출.
     *
     * @param damageTaken 이번에 실제로 입은 피해량
     */
    default void onDamaged(SkillContext context, int damageTaken) {
        // 기본 구현: 아무 것도 하지 않음
    }
}
