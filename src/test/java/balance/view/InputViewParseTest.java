package balance.view;

import balance.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputViewParseTest {

    @Test
    void 한줄_입력에서_캐릭터를_정상적으로_파싱할_수_있다() {
        String line = "Warrior 120 40 20 15 0.2";

        Character character = InputView.parseCharacterLine(line);

        assertEquals("Warrior", character.getName());
        assertEquals(120, character.getMaxHp());
        assertEquals(40, character.getAttack());
        assertEquals(20, character.getDefense());
        assertEquals(15, character.getSpeed());
        assertEquals(0.2, character.getCritChance(), 1e-9);
    }

    @Test
    void 토큰_개수가_6개가_아니면_예외가_발생한다() {
        String line = "Warrior 120 40 20 15"; // 5개

        assertThrows(IllegalArgumentException.class,
                () -> InputView.parseCharacterLine(line));
    }

    @Test
    void 숫자_형식이_잘못되면_예외가_발생한다() {
        String line = "Warrior 120 aa 20 15 0.2";

        assertThrows(IllegalArgumentException.class,
                () -> InputView.parseCharacterLine(line));
    }
}
