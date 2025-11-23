package balance.battle;

import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDamageCalculatorTest {

    @Test
    void 방어력이_높을수록_데미지가_줄어들지만_0이_되지는_않는다() {
        Character attackerChar = new Character("A", 100, 40, 5, 10, 0.0);

        Character lowDefChar = new Character("LowDef", 100, 10, 5, 10, 0.0);
        Character highDefChar = new Character("HighDef", 100, 10, 40, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter lowDef = BattleCharacter.from(lowDefChar);
        BattleCharacter highDef = BattleCharacter.from(highDefChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 크리 없음

        int damageToLowDef = calculator.calculateDamage(attacker, lowDef, random);
        int damageToHighDef = calculator.calculateDamage(attacker, highDef, random);

        assertTrue(damageToLowDef > damageToHighDef,
                "방어력이 높은 대상에게는 더 적은 데미지가 들어가야 한다.");
        assertTrue(damageToHighDef >= 1, "방어력이 높아도 최소 1딜은 들어가야 한다.");
    }

    @Test
    void 공격력이_높을수록_데미지가_커진다() {
        Character weakAttackerChar = new Character("Weak", 100, 20, 5, 10, 0.0);
        Character strongAttackerChar = new Character("Strong", 100, 50, 5, 10, 0.0);
        Character defenderChar = new Character("Def", 100, 10, 20, 10, 0.0);

        BattleCharacter weakAttacker = BattleCharacter.from(weakAttackerChar);
        BattleCharacter strongAttacker = BattleCharacter.from(strongAttackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 크리 없음

        int damageWeak = calculator.calculateDamage(weakAttacker, defender, random);
        int damageStrong = calculator.calculateDamage(strongAttacker, defender, random);

        assertTrue(damageStrong > damageWeak,
                "공격력이 높은 쪽이 더 큰 데미지를 줘야 한다.");
    }

    @Test
    void 치명타가_발생하면_데미지가_증가한다() {
        Character attackerChar = new Character("A", 100, 40, 5, 10, 1.0);
        Character defenderChar = new Character("D", 100, 10, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();

        // 크리 없음
        FixedRandomProvider noCritRandom = new FixedRandomProvider(1.0);
        int normalDamage = calculator.calculateDamage(attacker, defender, noCritRandom);

        // 항상 크리
        FixedRandomProvider critRandom = new FixedRandomProvider(0.0);
        int critDamage = calculator.calculateDamage(attacker, defender, critRandom);

        assertTrue(critDamage > normalDamage,
                "치명타 발생 시 데미지가 더 커야 한다.");
    }
}
