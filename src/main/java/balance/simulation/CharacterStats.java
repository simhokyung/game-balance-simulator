package balance.simulation;

import balance.domain.Character;

public class CharacterStats {

    private final Character character;
    private final int wins;
    private final int losses;
    private final int draws;
    private final int totalMatches;

    public CharacterStats(Character character,
                          int wins,
                          int losses,
                          int draws,
                          int totalMatches) {
        this.character = character;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.totalMatches = totalMatches;
    }

    public Character getCharacter() {
        return character;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public double getWinRate() {
        if (totalMatches == 0) {
            return 0.0;
        }
        return (double) wins / totalMatches;
    }

    public double getDrawRate() {
        if (totalMatches == 0) {
            return 0.0;
        }
        return (double) draws / totalMatches;
    }

    public double getLossRate() {
        if (totalMatches == 0) {
            return 0.0;
        }
        return (double) losses / totalMatches;
    }
}
