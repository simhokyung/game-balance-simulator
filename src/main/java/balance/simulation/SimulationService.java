package balance.simulation;

import balance.battle.BattleResult;
import balance.battle.BattleSimulator;
import balance.domain.Character;

public class SimulationService {

    private final BattleSimulator battleSimulator;

    public SimulationService(BattleSimulator battleSimulator) {
        if (battleSimulator == null) {
            throw new IllegalArgumentException("BattleSimulator는 null일 수 없습니다.");
        }
        this.battleSimulator = battleSimulator;
    }

    public MatchupResult simulateMatchup(Character first,
                                         Character second,
                                         int rounds) {
        validateRounds(rounds);

        int firstWins = 0;
        int secondWins = 0;
        int draws = 0;
        int totalTurns = 0;

        for (int i = 0; i < rounds; i++) {
            BattleResult result = battleSimulator.simulate(first, second);
            totalTurns += result.getTurnCount();

            if (result.isDraw()) {
                draws++;
                continue;
            }

            if (result.getWinner().getName().equals(first.getName())) {
                firstWins++;
                continue;
            }

            secondWins++;
        }

        double averageTurnCount = (double) totalTurns / rounds;

        return new MatchupResult(
                first,
                second,
                rounds,
                firstWins,
                secondWins,
                draws,
                averageTurnCount
        );
    }

    private void validateRounds(int rounds) {
        if (rounds < 1) {
            throw new IllegalArgumentException("시뮬레이션 횟수는 1 이상이어야 합니다.");
        }
    }
}
