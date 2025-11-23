package balance.battle;

import balance.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleSimulatorTest {

    @Test
    void 더_빠른_캐릭터가_선공을_잡고_전투에서_이길_수_있다() {
        Character fastStrong = new Character("FastStrong", 100, 50, 10, 30, 0.0);
        Character slowWeak = new Character("SlowWeak", 100, 30, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(fastStrong, slowWeak);

        assertFalse(result.isDraw());
        assertEquals("FastStrong", result.getWinner().getName());
        assertEquals("SlowWeak", result.getLoser().getName());
        assertTrue(result.getTurnCount() >= 1);
    }

    @Test
    void 속도가_같으면_첫번째_인자로_들어온_캐릭터가_선공을_잡는다() {
        Character first = new Character("First", 100, 40, 10, 20, 0.0);
        Character second = new Character("Second", 100, 40, 10, 20, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(first, second);

        // 공격력/방어력이 같으면 선공이 유리하다고 가정
        assertFalse(result.isDraw());
        assertEquals("First", result.getWinner().getName());
    }

    @Test
    void 방어력이_매우_높으면_전투는_길어지지만_결국_종료된다() {
        // 예전에는 ATK < DEF → 0딜 → 무승부를 기대했지만,
        // 지금은 비율형 + 최소 1딜 보장이라 아주 오래 걸리더라도 결국 끝난다.
        Character tank1 = new Character("Tank1", 120, 20, 35, 10, 0.0);
        Character tank2 = new Character("Tank2", 120, 20, 35, 12, 0.0);

        BattleSimulator simulator = new BattleSimulator();
        BattleResult result = simulator.simulate(tank1, tank2);

        assertFalse(result.isDraw(), "최소 1딜이 보장되므로 무승부가 되어서는 안 된다.");
        assertNotNull(result.getWinner());
        assertNotNull(result.getLoser());
        assertTrue(result.getTurnCount() > 5,
                "방어력이 높으면 전투는 어느 정도 길어지는 것이 자연스럽다.");
    }
}
