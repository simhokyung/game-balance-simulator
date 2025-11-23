package balance.battle;

import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDamageCalculatorTest {

    @Test
    void 방어력이_공격력보다_높으면_데미지는_0이다() {
        Character attackerChar = new Character("A", 100, 10, 5, 10, 0.0);
        Character defenderChar = new Character("D", 100, 15, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 크리 안 터지게

        int damage = calculator.calculateDamage(attacker, defender, random);

        assertEquals(0, damage);
    }

    @Test
    void 치명타가_발생하지_않으면_기본_데미지는_ATK_마이너스_DEF이다() {
        Character attackerChar = new Character("A", 100, 40, 5, 10, 0.5);
        Character defenderChar = new Character("D", 100, 10, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 1.0 >= critChance → 크리 X

        int damage = calculator.calculateDamage(attacker, defender, random);

        // ATK(40) - DEF(20) = 20
        assertEquals(20, damage);
    }

    @Test
    void 치명타가_발생하면_데미지는_1점대배로_증가한다() {
        Character attackerChar = new Character("A", 100, 40, 5, 10, 1.0);
        Character defenderChar = new Character("D", 100, 10, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        DefaultDamageCalculator calculator = new DefaultDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(0.0); // 0.0 < critChance(1.0) → 항상 크리

        int damage = calculator.calculateDamage(attacker, defender, random);

        // 기본 데미지: 40 - 20 = 20
        // 크리티컬: 20 * 1.5 = 30 → int 캐스팅으로 30
        assertEquals(30, damage);
    }
}
