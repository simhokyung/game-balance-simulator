package balance.battle;

import balance.domain.Character;
import balance.skill.Skill;
import balance.skill.SkillContext;
import balance.support.DefaultRandomProvider;
import balance.support.RandomProvider;

public class BattleSimulator {

    private static final int MAX_TURNS = 100;
    private static final double ACTION_THRESHOLD = 100.0;

    private final RandomProvider randomProvider;
    private final DamageCalculator damageCalculator;

    public BattleSimulator() {
        this(new DefaultRandomProvider(), new DefaultDamageCalculator());
    }

    public BattleSimulator(RandomProvider randomProvider) {
        this(randomProvider, new DefaultDamageCalculator());
    }

    public BattleSimulator(RandomProvider randomProvider,
                           DamageCalculator damageCalculator) {
        this.randomProvider = randomProvider;
        this.damageCalculator = damageCalculator;
    }

    public BattleResult simulate(Character first, Character second) {
        BattleCharacter firstBattle = BattleCharacter.from(first);
        BattleCharacter secondBattle = BattleCharacter.from(second);

        double firstGauge = 0.0;
        double secondGauge = 0.0;

        int turn = 0;

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

            // ===== 스킬 훅: 턴 시작 / 공격 전 =====
            triggerOnTurnStart(attacker, defender);
            triggerOnBeforeAttack(attacker, defender);

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

    private void triggerOnBeforeAttack(BattleCharacter acting, BattleCharacter opponent) {
        SkillContext context = new SkillContext(acting, opponent);
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
