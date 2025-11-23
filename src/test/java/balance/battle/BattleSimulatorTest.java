package balance.battle;

import balance.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleSimulatorTest {

    @Test
    void 더_빠른_캐릭터가_동일_스탯에서_전투에서_이길_수_있다() {
        // ATK / DEF / HP는 같고, SPD만 다르게 설정
        Character fastStrong = new Character("FastStrong", 100, 50, 10, 30, 0.0);
        Character slowWeak = new Character("SlowWeak", 100, 50, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(fastStrong, slowWeak);

        assertFalse(result.isDraw());
        assertEquals("FastStrong", result.getWinner().getName(), "속도가 더 빠른 캐릭터가 승리해야 한다.");
        assertEquals("SlowWeak", result.getLoser().getName());
        assertTrue(result.getTurnCount() > 0 && result.getTurnCount() <= 100);
    }

    @Test
    void 속도가_같으면_첫번째_인자로_들어온_캐릭터가_선공_우위를_가지고_전투에서_이길_수_있다() {
        Character first = new Character("First", 100, 40, 10, 20, 0.0);
        Character second = new Character("Second", 100, 40, 10, 20, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(first, second);

        // 공격력/방어력이 같으면, 선공을 잡은 첫 번째 캐릭터가 유리하다고 가정
        assertFalse(result.isDraw());
        assertEquals("First", result.getWinner().getName());
    }

    @Test
    void 서로_데미지를_줄_수_없으면_최대_턴_이후_무승부가_된다() {
        // DEF가 ATK보다 높아서 데미지가 0인 상황
        Character tank1 = new Character("Tank1", 100, 10, 20, 10, 0.0);
        Character tank2 = new Character("Tank2", 100, 10, 20, 15, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(tank1, tank2);

        assertTrue(result.isDraw());
        assertNull(result.getWinner());
        assertNull(result.getLoser());
        assertTrue(result.getTurnCount() > 0);
    }
}
