package balance.battle;

import balance.domain.Character;

public class BattleSimulator {

    private static final int MAX_TURNS = 100;

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
        int rawDamage = attacker.getCharacter().getAttack() - defender.getCharacter().getDefense();
        if (rawDamage < 0) {
            return 0;
        }
        return rawDamage;
    }

    private double calculateHpRatio(BattleCharacter battleCharacter) {
        int currentHp = battleCharacter.getCurrentHp();
        int maxHp = battleCharacter.getCharacter().getMaxHp();
        return (double) currentHp / maxHp;
    }
}
