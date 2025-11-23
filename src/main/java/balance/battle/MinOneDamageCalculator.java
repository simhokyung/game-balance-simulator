package balance.battle;

import balance.support.RandomProvider;

/**
 * 방어력이 아무리 높아도, 공격이 아예 막히지는 않도록
 * "최소 1딜"을 보장하는 데미지 공식.
 *
 * - 기본 공식: baseDamage = ATK - DEF
 * - baseDamage <= 0 이더라도, 공격이 들어갔다면 최소 1 피해를 준다고 가정한다.
 * - 치명타는 DefaultDamageCalculator와 동일하게 1.5배 적용 후, 최소 1을 보장한다.
 */
public class MinOneDamageCalculator implements DamageCalculator {

    private static final double CRIT_MULTIPLIER = 1.5;

    @Override
    public int calculateDamage(BattleCharacter attacker,
                               BattleCharacter defender,
                               RandomProvider randomProvider) {

        int baseDamage = attacker.getCharacter().getAttack()
                - defender.getCharacter().getDefense();

        // baseDamage가 0 이하라도 "공격 시도 자체"는 있었다고 보고, 기본값 1로 시작
        if (baseDamage <= 0) {
            baseDamage = 1;
        }

        double critChance = attacker.getCharacter().getCritChance();
        double roll = randomProvider.nextDouble();
        boolean isCritical = roll < critChance;

        double finalDamage = baseDamage;
        if (isCritical) {
            finalDamage = baseDamage * CRIT_MULTIPLIER;
        }

        int damage = (int) finalDamage;
        // 혹시 캐스팅으로 0이 되지 않게 최종적으로도 최소 1 보장
        if (damage < 1) {
            return 1;
        }
        return damage;
    }
}
