package balance.battle;

import balance.domain.Character;
import balance.support.DefaultRandomProvider;
import balance.support.RandomProvider;

public class BattleSimulator {

    private static final int MAX_TURNS = 100;
    private static final double CRIT_MULTIPLIER = 1.5;

    private final RandomProvider randomProvider;

    public BattleSimulator(){
        this(new DefaultRandomProvider());
    }

    public BattleSimulator(RandomProvider randomProvider){
        this.randomProvider = randomProvider;
    }


    public BattleResult simulate(Character first, Character second) {
        BattleCharacter firstBattle = BattleCharacter.from(first);
        BattleCharacter secondBattle = BattleCharacter.from(second);

        BattleCharacter attacker = decideFirstAttacker(firstBattle, secondBattle, first, second);
        BattleCharacter defender = (attacker == firstBattle) ? secondBattle : firstBattle;

        int turn = 0;

        while (turn < MAX_TURNS) {
            turn++;

            int damage = calculateDamage(attacker, defender);
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

            // 공격자/수비자 교체
            BattleCharacter temp = attacker;
            attacker = defender;
            defender = temp;
        }

        return BattleResult.draw(turn);
    }

    private BattleCharacter decideFirstAttacker(BattleCharacter firstBattle,
                                                BattleCharacter secondBattle,
                                                Character first,
                                                Character second) {
        int firstSpeed = first.getSpeed();
        int secondSpeed = second.getSpeed();

        if (firstSpeed > secondSpeed) {
            return firstBattle;
        }
        if (secondSpeed > firstSpeed) {
            return secondBattle;
        }
        // 속도가 같으면 첫 번째 인자가 선공
        return firstBattle;
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
        if(isCritical) {
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
