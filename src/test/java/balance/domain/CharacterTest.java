package balance.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void 캐릭터를_정상적으로_생성할_수_있다() {
        Character character = new Character(
                "Warrior",
                1500,   // HP
                80,     // ATK
                50,     // DEF
                30,     // SPD
                0.1     // CRIT
        );

        assertEquals("Warrior", character.getName());
        assertEquals(1500, character.getMaxHp());
        assertEquals(80, character.getAttack());
        assertEquals(50, character.getDefense());
        assertEquals(30, character.getSpeed());
        assertEquals(0.1, character.getCritChance());
    }

    @Test
    void HP_ATK_DEF_SPD는_1_이상이_아니면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 0, 80, 50, 30, 0.1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 1500, -10, 50, 30, 0.1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 1500, 80, 0, 30, 0.1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 1500, 80, 50, 0, 0.1)
        );
    }

    @Test
    void 치명타_확률은_0이상_1이하가_아니면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 1500, 80, 50, 30, -0.1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Character("Warrior", 1500, 80, 50, 30, 1.1)
        );
    }

    @Test
    void 이름이_비어있거나_공백이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class, () ->
                new Character("", 1500, 80, 50, 30, 0.1)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Character("   ", 1500, 80, 50, 30, 0.1)
        );
    }
}
