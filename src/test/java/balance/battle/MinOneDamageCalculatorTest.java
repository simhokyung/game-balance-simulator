package balance.battle;

import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinOneDamageCalculatorTest {

    @Test
    void 방어력이_공격력보다_높아도_최소_1의_데미지는_들어간다() {
        // ATK 10, DEF 20 → 기존 공식이면 0
        Character attackerChar = new Character("A", 100, 10, 5, 10, 0.0);
        Character defenderChar = new Character("D", 100, 10, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        MinOneDamageCalculator calculator = new MinOneDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 크리 안 터지게

        int damage = calculator.calculateDamage(attacker, defender, random);

        assertEquals(1, damage, "방어력이 높아도 최소 1딜은 들어가야 한다.");
    }

    @Test
    void 치명타가_발생하지_않으면_ATK_DEF_차이에_기반하면서도_최소_1딜을_보장한다() {
        Character attackerChar = new Character("A", 100, 40, 5, 10, 0.5);
        Character defenderChar = new Character("D", 100, 10, 20, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        MinOneDamageCalculator calculator = new MinOneDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(1.0); // 크리 X

        int damage = calculator.calculateDamage(attacker, defender, random);

        // 기본 데미지: 40 - 20 = 20
        assertEquals(20, damage);
    }

    @Test
    void 치명타가_발생하면_데미지는_증가하지만_최소_1딜을_보장한다() {
        Character attackerChar = new Character("A", 100, 40, 5, 100, 1.0);
        Character defenderChar = new Character("D", 100, 50, 60, 10, 0.0);

        BattleCharacter attacker = BattleCharacter.from(attackerChar);
        BattleCharacter defender = BattleCharacter.from(defenderChar);

        MinOneDamageCalculator calculator = new MinOneDamageCalculator();
        FixedRandomProvider random = new FixedRandomProvider(0.0); // 항상 크리

        int damage = calculator.calculateDamage(attacker, defender, random);

        // baseDamage = 40 - 60 = -20 → 최소 1로 올림 → 크리티컬 배율 적용 후에도 최소 1 보장
        assertTrue(damage >= 1);
    }
}
