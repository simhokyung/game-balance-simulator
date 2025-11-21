package balance.battle;

import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleSimulatorCritTest {

    @Test
    void 치명타가_적용되면_더_적은_턴_안에_적을_처치할_수_있다() {
        // ATK 40 vs DEF 1 → 기본 데미지 39
        // HP 100
        // - 크리 없음: 39, 39, 22 (3번 맞음) → 우리 로직상 턴 수는 5 (공격자/수비자 번갈아)
        // - 항상 크리: 58, 58 (2번 맞음) → 우리 로직상 턴 수는 3

        Character critAttacker = new Character("Critter", 100, 40, 1, 10, 0.5);
        Character defender = new Character("Dummy", 100, 1, 1, 5, 0.0);

        // 항상 크리티컬 발생 (0.0 < 0.5)
        BattleSimulator simulatorAlwaysCrit = new BattleSimulator(new FixedRandomProvider(0.0));
        BattleResult resultCrit = simulatorAlwaysCrit.simulate(critAttacker, defender);

        assertEquals("Critter", resultCrit.getWinner().getName());
        assertEquals(3, resultCrit.getTurnCount(), "치명타가 적용되면 3턴 안에 끝나야 한다.");

        // 절대 크리티컬 발생 안 함 (1.0 >= 0.5)
        BattleSimulator simulatorNoCrit = new BattleSimulator(new FixedRandomProvider(1.0));
        BattleResult resultNoCrit = simulatorNoCrit.simulate(critAttacker, defender);

        assertEquals("Critter", resultNoCrit.getWinner().getName());
        assertEquals(5, resultNoCrit.getTurnCount(), "치명타가 없으면 5턴이 걸려야 한다.");

        // 그리고 중요한 핵심: 치명타 있는 쪽이 더 빠르다
        assertTrue(resultCrit.getTurnCount() < resultNoCrit.getTurnCount());
    }
}
