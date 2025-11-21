package balance.simulation;

import balance.battle.BattleResult;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationServiceTest {

    @Test
    void 강한_캐릭터는_여러_판_시뮬레이션에서_항상_이길_수_있다() {
        Character strong = new Character("Strong", 100, 50, 10, 20, 0.0);
        Character weak = new Character("Weak", 100, 10, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator(new FixedRandomProvider(1.0)); // 크리 없음
        SimulationService service = new SimulationService(simulator);

        int rounds = 10;
        MatchupResult result = service.simulateMatchup(strong, weak, rounds);

        assertEquals(rounds, result.getRounds());
        assertEquals(rounds, result.getFirstWins());  // strong이 첫 번째 인자
        assertEquals(0, result.getSecondWins());
        assertEquals(0, result.getDraws());
        assertTrue(result.getAverageTurnCount() > 0.0);
    }

    @Test
    void 시뮬레이션_횟수가_1미만이면_예외가_발생한다() {
        Character a = new Character("A", 100, 20, 10, 10, 0.0);
        Character b = new Character("B", 100, 20, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator(new FixedRandomProvider(1.0));
        SimulationService service = new SimulationService(simulator);

        assertThrows(IllegalArgumentException.class,
                () -> service.simulateMatchup(a, b, 0));

        assertThrows(IllegalArgumentException.class,
                () -> service.simulateMatchup(a, b, -5));
    }
}
