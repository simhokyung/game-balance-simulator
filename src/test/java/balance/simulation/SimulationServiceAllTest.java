package balance.simulation;

import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.support.FixedRandomProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimulationServiceAllTest {

    @Test
    void 강한_캐릭터는_리그_시뮬레이션에서도_모든_상대에게_이긴다() {
        Character strong = new Character("Strong", 100, 50, 10, 20, 0.0);
        Character mid = new Character("Mid", 100, 30, 10, 15, 0.0);
        Character weak = new Character("Weak", 100, 10, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator(new FixedRandomProvider(1.0)); // 크리 없음
        SimulationService service = new SimulationService(simulator);

        int roundsPerPair = 5;
        Map<Character, CharacterStats> statsByCharacter =
                service.simulateAll(List.of(strong, mid, weak), roundsPerPair);

        CharacterStats strongStats = statsByCharacter.get(strong);
        CharacterStats midStats = statsByCharacter.get(mid);
        CharacterStats weakStats = statsByCharacter.get(weak);

        // Strong은 Mid, Weak 각각 5판씩 → 총 10경기, 전승 기대
        assertEquals(10, strongStats.getTotalMatches());
        assertEquals(10, strongStats.getWins());
        assertEquals(0, strongStats.getLosses());
        assertEquals(0, strongStats.getDraws());
        assertEquals(1.0, strongStats.getWinRate(), 1e-9);

        // Weak는 Strong, Mid에게 모두 짐 → 총 10경기, 전패 기대
        assertEquals(10, weakStats.getTotalMatches());
        assertEquals(0, weakStats.getWins());
        assertEquals(10, weakStats.getLosses());
        assertEquals(0, weakStats.getDraws());
        assertEquals(0.0, weakStats.getWinRate(), 1e-9);

        // Mid는 Strong에게는 지고, Weak에게는 이김 → 총 10경기, 5승 5패 정도 기대
        assertEquals(10, midStats.getTotalMatches());
        assertEquals(5, midStats.getWins());
        assertEquals(5, midStats.getLosses());
        assertEquals(0, midStats.getDraws());
        assertEquals(0.5, midStats.getWinRate(), 1e-9);
    }

    @Test
    void 캐릭터가_1명뿐이면_예외가_발생한다() {
        Character only = new Character("Only", 100, 20, 10, 10, 0.0);

        BattleSimulator simulator = new BattleSimulator(new FixedRandomProvider(1.0));
        SimulationService service = new SimulationService(simulator);

        assertThrows(IllegalArgumentException.class,
                () -> service.simulateAll(List.of(only), 5));
    }
}
