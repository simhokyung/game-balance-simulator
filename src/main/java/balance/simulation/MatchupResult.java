package balance.simulation;

import balance.domain.Character;

public class MatchupResult {

    private final Character first;
    private final Character second;
    private final int rounds;
    private final int firstWins;
    private final int secondWins;
    private final int draws;
    private final double averageTurnCount;

    public MatchupResult(Character first,
                         Character second,
                         int rounds,
                         int firstWins,
                         int secondWins,
                         int draws,
                         double averageTurnCount) {
        this.first = first;
        this.second = second;
        this.rounds = rounds;
        this.firstWins = firstWins;
        this.secondWins = secondWins;
        this.draws = draws;
        this.averageTurnCount = averageTurnCount;
    }

    public Character getFirst() {
        return first;
    }

    public Character getSecond() {
        return second;
    }

    public int getRounds() {
        return rounds;
    }

    public int getFirstWins() {
        return firstWins;
    }

    public int getSecondWins() {
        return secondWins;
    }

    public int getDraws() {
        return draws;
    }

    public double getAverageTurnCount() {
        return averageTurnCount;
    }

    public double getFirstWinRate() {
        if (rounds == 0) {
            return 0.0;
        }
        return (double) firstWins / rounds;
    }

    public double getSecondWinRate() {
        if (rounds == 0) {
            return 0.0;
        }
        return (double) secondWins / rounds;
    }

    public double getDrawRate() {
        if (rounds == 0) {
            return 0.0;
        }
        return (double) draws / rounds;
    }
}
