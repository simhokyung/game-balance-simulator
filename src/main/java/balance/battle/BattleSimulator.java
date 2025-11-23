package balance.battle;

import balance.domain.Character;
import balance.support.DefaultRandomProvider;
import balance.support.RandomProvider;

public class BattleSimulator {

    private static final int MAX_TURNS = 100;
    private static final double CRIT_MULTIPLIER = 1.5;
    private static final double ACTION_THRESHOLD = 100.0;

    private final RandomProvider randomProvider;

    public BattleSimulator() {
        this(new DefaultRandomProvider());
    }

    public BattleSimulator(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public BattleResult simulate(Character first, Character second) {
        BattleCharacter firstBattle = BattleCharacter.from(first);
        BattleCharacter secondBattle = BattleCharacter.from(second);

        // SPD 게이지 (처음엔 0)
        double firstGauge = 0.0;
        double secondGauge = 0.0;

        int turn = 0;

        while (turn < MAX_TURNS) {
            turn++;

            // 1) 이번 행동을 할 캐릭터를 SPD 기반 게이지로 결정
            BattleCharacter attacker;
            BattleCharacter defender;

            // 게이지가 둘 다 ACTION_THRESHOLD 미만이면 SPD만큼 누적
            while (firstGauge < ACTION_THRESHOLD && secondGauge < ACTION_THRESHOLD) {
                firstGauge += first.getSpeed();
                secondGauge += second.getSpeed();
            }

            // 게이지가 더 큰 쪽이 이번 공격자
            if (firstGauge > secondGauge) {
                attacker = firstBattle;
                defender = secondBattle;
                firstGauge -= ACTION_THRESHOLD;
            } else if (secondGauge > firstGauge) {
                attacker = secondBattle;
                defender = firstBattle;
                secondGauge -= ACTION_THRESHOLD;
            } else {
                // 게이지가 같다면 SPD로 우선권, SPD도 같으면 first가 우선
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

            // 2) 데미지 계산 및 적용
            int damage = calculateDamage(attacker, defender);
            if (damage > 0) {
                defender.takeDamage(damage);
            }

            // 3) 종료 조건 체크
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

        // 턴 제한까지 승부 안 나면 무승부
        return BattleResult.draw(turn);
    }

    private int calculateDamage(BattleCharacter attacker, BattleCharacter defender) {
        int baseDamage = attacker.getCharacter().getAttack() - defender.getCharacter().getDefense();
        if (baseDamage <= 0) {
            return 0;
        }

        double critChance = attacker.getCharacter().getCritChance();
        double roll = randomProvider.nextDouble();
        boolean isCritical = roll < critChance;

        double finalDamage = baseDamage;
        if (isCritical) {
            finalDamage = baseDamage * CRIT_MULTIPLIER;
        }

        return (int) finalDamage;
    }

    private double calculateHpRatio(BattleCharacter battleCharacter) {
        int currentHp = battleCharacter.getCurrentHp();
        int maxHp = battleCharacter.getCharacter().getMaxHp();
        return (double) currentHp / maxHp;
    }
}
