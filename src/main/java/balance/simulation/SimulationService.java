package balance.simulation;

import balance.battle.BattleResult;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Character, CharacterStats> simulateAll(List<Character> characters,
                                                      int roundsPerPair) {
        if (characters == null || characters.size() < 2) {
            throw new IllegalArgumentException("시뮬레이션할 캐릭터는 최소 2명 이상이어야 합니다.");
        }
        validateRounds(roundsPerPair);

        Map<Character, MutableStats> statsMap = new HashMap<>();
        for (Character character : characters) {
            statsMap.put(character, new MutableStats());
        }

        int size = characters.size();
        for (int i = 0; i < size; i++) {
            Character first = characters.get(i);
            for (int j = i + 1; j < size; j++) {
                Character second = characters.get(j);

                MatchupResult matchupResult = simulateMatchup(first, second, roundsPerPair);

                MutableStats firstStats = statsMap.get(first);
                MutableStats secondStats = statsMap.get(second);

                firstStats.addWins(matchupResult.getFirstWins());
                firstStats.addLosses(matchupResult.getSecondWins());
                firstStats.addDraws(matchupResult.getDraws());
                firstStats.addMatches(roundsPerPair);

                secondStats.addWins(matchupResult.getSecondWins());
                secondStats.addLosses(matchupResult.getFirstWins());
                secondStats.addDraws(matchupResult.getDraws());
                secondStats.addMatches(roundsPerPair);
            }
        }

        Map<Character, CharacterStats> result = new HashMap<>();
        for (Character character : characters) {
            MutableStats mutable = statsMap.get(character);
            result.put(character, new CharacterStats(
                    character,
                    mutable.wins,
                    mutable.losses,
                    mutable.draws,
                    mutable.totalMatches
            ));
        }

        return result;
    }

    private static class MutableStats {
        private int wins;
        private int losses;
        private int draws;
        private int totalMatches;

        private void addWins(int value) {
            wins += value;
        }

        private void addLosses(int value) {
            losses += value;
        }

        private void addDraws(int value) {
            draws += value;
        }

        private void addMatches(int value) {
            totalMatches += value;
        }
    }



    private void validateRounds(int rounds) {
        if (rounds < 1) {
            throw new IllegalArgumentException("시뮬레이션 횟수는 1 이상이어야 합니다.");
        }
    }
}
