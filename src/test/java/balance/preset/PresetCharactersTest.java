package balance.preset;

import balance.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PresetCharactersTest {

    @Test
    void 브루저_프리셋이_기대한_스탯으로_생성된다() {
        Character bruiser = PresetCharacters.bruiser();

        assertEquals("Bruiser", bruiser.getName());
        assertEquals(128, bruiser.getMaxHp());
        assertEquals(44, bruiser.getAttack());
        assertEquals(24, bruiser.getDefense());
        assertEquals(20, bruiser.getSpeed());
        assertEquals(0.10, bruiser.getCritChance(), 1e-9);
    }

    @Test
    void 모든_프리셋은_양수_스탯과_0에서_1사이_크리확률을_가진다() {
        Character[] presets = {
                PresetCharacters.bruiser(),
                PresetCharacters.assassin(),
                PresetCharacters.tank(),
                PresetCharacters.speedster(),
                PresetCharacters.sustainer(),
                PresetCharacters.hybrid()
        };

        for (Character c : presets) {
            assertTrue(c.getMaxHp() > 0, c.getName() + " HP는 양수여야 한다.");
            assertTrue(c.getAttack() > 0, c.getName() + " ATK는 양수여야 한다.");
            assertTrue(c.getDefense() >= 0, c.getName() + " DEF는 0 이상이어야 한다.");
            assertTrue(c.getSpeed() > 0, c.getName() + " SPD는 양수여야 한다.");
            assertTrue(c.getCritChance() >= 0.0 && c.getCritChance() <= 1.0,
                    c.getName() + " CRIT 확률은 0~1 사이여야 한다.");
        }
    }
}
