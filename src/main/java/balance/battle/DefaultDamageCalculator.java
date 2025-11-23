package balance.battle;

import balance.support.RandomProvider;

/**
 * 기본 데미지 공식 (비율형):
 *  - base = ATK * (ATK / (ATK + DEF))
 *  - DEF가 높을수록 데미지가 줄어들지만, 완전히 0이 되지는 않도록 최소 1 보장
 *  - 치명타는 1.5배
 */
public class DefaultDamageCalculator implements DamageCalculator {

    private static final double CRIT_MULTIPLIER = 1.5;

    @Override
    public int calculateDamage(BattleCharacter attacker,
                               BattleCharacter defender,
                               RandomProvider randomProvider) {

        double attack = attacker.getCharacter().getAttack();
        double defense = defender.getCharacter().getDefense();

        // ATK / (ATK + DEF)를 곱해 방어력이 높을수록 데미지가 줄어들게 한다
        double baseDamageDouble = attack * (attack / (attack + defense));

        int baseDamage = (int) baseDamageDouble;
        if (baseDamage < 1) {
            baseDamage = 1;   // 최소 1딜 보장
        }

        double critChance = attacker.getCharacter().getCritChance();
        double roll = randomProvider.nextDouble();
        boolean isCritical = roll < critChance;

        double finalDamage = baseDamage;
        if (isCritical) {
            finalDamage = baseDamage * CRIT_MULTIPLIER;
        }

        int damage = (int) finalDamage;
        if (damage < 1) {
            return 1;
        }
        return damage;
    }
}
