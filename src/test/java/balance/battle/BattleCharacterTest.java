package balance.battle;

import balance.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleCharacterTest {

    @Test
    void 캐릭터로부터_전투_캐릭터를_생성하면_현재_HP는_최대_HP와_같다() {
        Character character = new Character("Warrior", 1500, 80, 50, 30, 0.1);

        BattleCharacter battleCharacter = BattleCharacter.from(character);

        assertEquals(1500, battleCharacter.getCurrentHp());
        assertEquals(character, battleCharacter.getCharacter());
    }

    @Test
    void 피해를_입으면_HP가_감소하고_0_미만으로_내려가지_않는다() {
        Character character = new Character("Warrior", 100, 80, 50, 30, 0.1);
        BattleCharacter battleCharacter = BattleCharacter.from(character);

        battleCharacter.takeDamage(30);
        assertEquals(70, battleCharacter.getCurrentHp());

        battleCharacter.takeDamage(1000);
        assertEquals(0, battleCharacter.getCurrentHp());
    }

    @Test
    void 피해량이_0_이하이면_예외가_발생한다() {
        Character character = new Character("Warrior", 100, 80, 50, 30, 0.1);
        BattleCharacter battleCharacter = BattleCharacter.from(character);

        assertThrows(IllegalArgumentException.class, () -> battleCharacter.takeDamage(0));
        assertThrows(IllegalArgumentException.class, () -> battleCharacter.takeDamage(-10));
    }

    @Test
    void 회복을_하면_HP가_증가하고_최대_HP를_초과하지_않는다() {
        Character character = new Character("Warrior", 100, 80, 50, 30, 0.1);
        BattleCharacter battleCharacter = BattleCharacter.from(character);

        battleCharacter.takeDamage(60);
        assertEquals(40, battleCharacter.getCurrentHp());

        battleCharacter.heal(30);
        assertEquals(70, battleCharacter.getCurrentHp());

        // 최대 HP 초과 회복 시 최대 HP로 고정
        battleCharacter.heal(100);
        assertEquals(100, battleCharacter.getCurrentHp());
    }

    @Test
    void 회복량이_0_이하이면_예외가_발생한다() {
        Character character = new Character("Warrior", 100, 80, 50, 30, 0.1);
        BattleCharacter battleCharacter = BattleCharacter.from(character);

        assertThrows(IllegalArgumentException.class, () -> battleCharacter.heal(0));
        assertThrows(IllegalArgumentException.class, () -> battleCharacter.heal(-10));
    }

    @Test
    void HP가_0이면_죽은_상태로_판단한다() {
        Character character = new Character("Warrior", 50, 80, 50, 30, 0.1);
        BattleCharacter battleCharacter = BattleCharacter.from(character);

        assertFalse(battleCharacter.isDead());

        battleCharacter.takeDamage(50);
        assertTrue(battleCharacter.isDead());
    }
}
