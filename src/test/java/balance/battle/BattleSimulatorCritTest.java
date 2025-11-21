package balance.battle;

import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleSimulatorCritTest {

    @Test
    void 치명타가_적용되면_더_적은_턴_안에_적을_처치할_수_있다() {
        // ATK 40 vs DEF 0 → 기본 데미지 40
        // HP 100 → 크리 없으면 40,40,20 = 3턴
        // 크리(1.5배)면 60 → 60,40 = 2턴

        Character critAttacker = new Character("Critter", 100, 40, 0, 10, 0.5);
        Character defender = new Character("Dummy", 100, 0, 0, 5, 0.0);

        // 항상 크리티컬 발생 (0.0 < 0.5)
        BattleSimulator simulatorAlwaysCrit = new BattleSimulator(new FixedRandomProvider(0.0));
        BattleResult resultCrit = simulatorAlwaysCrit.simulate(critAttacker, defender);

        assertEquals("Critter", resultCrit.getWinner().getName());
        assertEquals(2, resultCrit.getTurnCount(), "치명타가 적용되면 2턴 안에 끝나야 한다.");

        // 절대 크리티컬 발생 안 함 (1.0 >= 0.5)
        BattleSimulator simulatorNoCrit = new BattleSimulator(new FixedRandomProvider(1.0));
        BattleResult resultNoCrit = simulatorNoCrit.simulate(critAttacker, defender);

        assertEquals("Critter", resultNoCrit.getWinner().getName());
        assertEquals(3, resultNoCrit.getTurnCount(), "치명타가 없으면 3턴이 걸려야 한다.");
    }
}
