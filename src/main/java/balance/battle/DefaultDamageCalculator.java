package balance.battle;

import balance.support.RandomProvider;

public class DefaultDamageCalculator implements DamageCalculator {

    private static final double CRIT_MULTIPLIER = 1.5;

    @Override
    public int calculateDamage(BattleCharacter attacker,
                               BattleCharacter defender,
                               RandomProvider randomProvider) {

        int baseDamage = attacker.getCharacter().getAttack()
                - defender.getCharacter().getDefense();

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
}
