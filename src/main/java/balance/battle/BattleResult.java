package balance.battle;

import balance.domain.Character;

public class BattleResult {

    private final Character winner;
    private final Character loser;
    private final int turnCount;
    private final double winnerRemainingHpRatio;
    private final boolean draw;

    private BattleResult(Character winner,
                         Character loser,
                         int turnCount,
                         double winnerRemainingHpRatio,
                         boolean draw) {
        this.winner = winner;
        this.loser = loser;
        this.turnCount = turnCount;
        this.winnerRemainingHpRatio = winnerRemainingHpRatio;
        this.draw = draw;
    }

    public static BattleResult win(Character winner,
                                   Character loser,
                                   int turnCount,
                                   double winnerRemainingHpRatio) {
        return new BattleResult(winner, loser, turnCount, winnerRemainingHpRatio, false);
    }

    public static BattleResult draw(int turnCount) {
        return new BattleResult(null, null, turnCount, 0.0, true);
    }

    public boolean isDraw() {
        return draw;
    }

    public Character getWinner() {
        return winner;
    }

    public Character getLoser() {
        return loser;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public double getWinnerRemainingHpRatio() {
        return winnerRemainingHpRatio;
    }
}
