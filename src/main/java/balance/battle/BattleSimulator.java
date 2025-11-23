package balance.battle;

import balance.domain.Character;
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

            while (firstGauge < ACTION_THRESHOLD && secondGauge < ACTION_THRESHOLD) {
                firstGauge += first.getSpeed();
                secondGauge += second.getSpeed();
            }

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

            int damage = damageCalculator.calculateDamage(attacker, defender, randomProvider);
            if (damage > 0) {
                defender.takeDamage(damage);
            }

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

        return BattleResult.draw(turn);
    }

    private double calculateHpRatio(BattleCharacter battleCharacter) {
        int currentHp = battleCharacter.getCurrentHp();
        int maxHp = battleCharacter.getCharacter().getMaxHp();
        return (double) currentHp / maxHp;
    }
}
