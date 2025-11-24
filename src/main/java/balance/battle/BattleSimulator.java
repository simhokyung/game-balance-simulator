package balance.battle;

import balance.domain.Character;
import balance.skill.DefaultSkillSetProvider;
import balance.skill.Skill;
import balance.skill.SkillContext;
import balance.skill.SkillSetProvider;
import balance.support.DefaultRandomProvider;
import balance.support.RandomProvider;

import java.util.Objects;

public class BattleSimulator {

    private static final int MAX_TURNS = 100;
    private static final double ACTION_THRESHOLD = 100.0;

    private final RandomProvider randomProvider;
    private final DamageCalculator damageCalculator;
    private final SkillSetProvider skillSetProvider;

    public BattleSimulator() {
        this(new DefaultRandomProvider(), new DefaultDamageCalculator(), new DefaultSkillSetProvider());
    }

    public BattleSimulator(RandomProvider randomProvider) {
        this(randomProvider, new DefaultDamageCalculator(), new DefaultSkillSetProvider());
    }

    public BattleSimulator(RandomProvider randomProvider,
                           DamageCalculator damageCalculator) {
        this(randomProvider, damageCalculator, new DefaultSkillSetProvider());
    }

    public BattleSimulator(RandomProvider randomProvider,
                           DamageCalculator damageCalculator,
                           SkillSetProvider skillSetProvider) {
        this.randomProvider = Objects.requireNonNull(randomProvider, "randomProvider는 null일 수 없습니다.");
        this.damageCalculator = Objects.requireNonNull(damageCalculator, "damageCalculator는 null일 수 없습니다.");
        this.skillSetProvider = Objects.requireNonNull(skillSetProvider, "skillSetProvider는 null일 수 없습니다.");
    }

    // 스킬셋 프로바이더만 넘기는 생성자
    public BattleSimulator(SkillSetProvider skillSetProvider) {
        this(new DefaultRandomProvider(), new DefaultDamageCalculator(), skillSetProvider);
    }

    public BattleResult simulate(Character first, Character second) {
        // 캐릭터별 스킬 세트 주입
        BattleCharacter firstBattle =
                BattleCharacter.from(first, skillSetProvider.getSkillsFor(first));
        BattleCharacter secondBattle =
                BattleCharacter.from(second, skillSetProvider.getSkillsFor(second));

        double firstGauge = 0.0;
        double secondGauge = 0.0;

        int turn = 0;

        // 🔥 전투 내 "첫 공격 여부" 추적용 플래그
        boolean firstHasAttackedOnce = false;
        boolean secondHasAttackedOnce = false;

        while (turn < MAX_TURNS) {
            turn++;

            BattleCharacter attacker;
            BattleCharacter defender;

            // 속도 게이지 누적
            while (firstGauge < ACTION_THRESHOLD && secondGauge < ACTION_THRESHOLD) {
                firstGauge += first.getSpeed();
                secondGauge += second.getSpeed();
            }

            // 행동자 결정
            if (firstGauge > secondGauge) {
                attacker = firstBattle;
                defender = secondBattle;
                firstGauge -= ACTION_THRESHOLD;
            } else if (secondGauge > firstGauge) {
                attacker = secondBattle;
                defender = firstBattle;
                secondGauge -= ACTION_THRESHOLD;
            } else {
                // 게이지 동률이면 SPD가 더 높은 쪽, 같으면 first 우선
                if (first.getSpeed() >= second.getSpeed()) {
                    attacker = firstBattle;
                    defender = secondBattle;
                    firstGauge -= ACTION_THRESHOLD;
                } else {
                    attacker = secondBattle;
                    defender = firstBattle;
                    secondGauge -= ACTION_THRESHOLD;
                }
            }

            // ✅ 이 공격이 "해당 캐릭터의 전투 첫 공격"인지 계산
            boolean isFirstAttackInBattle;
            if (attacker == firstBattle) {
                isFirstAttackInBattle = !firstHasAttackedOnce;
            } else {
                isFirstAttackInBattle = !secondHasAttackedOnce;
            }

            // ===== 스킬 훅: 턴 시작 / 공격 전 =====
            triggerOnTurnStart(attacker, defender);
            triggerOnBeforeAttack(attacker, defender, isFirstAttackInBattle);

            // 데미지 계산
            int damage = damageCalculator.calculateDamage(attacker, defender, randomProvider);

            if (damage > 0) {
                // 피해 적용
                defender.takeDamage(damage);

                // ===== 스킬 훅: 피해 후 / 공격 후 =====
                triggerOnDamaged(defender, attacker, damage);
                triggerOnAfterAttack(attacker, defender, damage);

                if (defender.isDead()) {
                    double winnerHpRatio = calculateHpRatio(attacker);
                    return BattleResult.win(
                            attacker.getCharacter(),
                            defender.getCharacter(),
                            turn,
                            winnerHpRatio
                    );
                }
            }

            // ✅ 공격이 끝난 뒤, 해당 캐릭터가 "한 번 이상 공격함" 표시
            if (attacker == firstBattle) {
                firstHasAttackedOnce = true;
            } else {
                secondHasAttackedOnce = true;
            }
        }

        return BattleResult.draw(turn);
    }

    private double calculateHpRatio(BattleCharacter battleCharacter) {
        int currentHp = battleCharacter.getCurrentHp();
        int maxHp = battleCharacter.getCharacter().getMaxHp();
        return (double) currentHp / maxHp;
    }

    private void triggerOnTurnStart(BattleCharacter acting, BattleCharacter opponent) {
        SkillContext context = new SkillContext(acting, opponent);
        for (Skill skill : acting.getSkills()) {
            skill.onTurnStart(context);
        }
    }

    /**
     * 이제 onBeforeAttack 에서는
     * "이 공격이 전투 내 첫 공격인지" 여부를 함께 넘겨준다.
     */
    private void triggerOnBeforeAttack(BattleCharacter acting,
                                       BattleCharacter opponent,
                                       boolean firstAttackInBattle) {
        SkillContext context = new SkillContext(acting, opponent, firstAttackInBattle);
        for (Skill skill : acting.getSkills()) {
            skill.onBeforeAttack(context);
        }
    }

    private void triggerOnAfterAttack(BattleCharacter acting,
                                      BattleCharacter opponent,
                                      int damageDealt) {
        SkillContext context = new SkillContext(acting, opponent);
        for (Skill skill : acting.getSkills()) {
            skill.onAfterAttack(context, damageDealt);
        }
    }

    private void triggerOnDamaged(BattleCharacter damaged,
                                  BattleCharacter attacker,
                                  int damageTaken) {
        SkillContext context = new SkillContext(damaged, attacker);
        for (Skill skill : damaged.getSkills()) {
            skill.onDamaged(context, damageTaken);
        }
    }
}
