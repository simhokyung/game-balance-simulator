package balance.simulation;

import balance.domain.Character;

import java.util.Collections;
import java.util.List;

/**
 * 캐릭터 간 1:1 매치업 승률 매트릭스를 표현하는 클래스.
 *
 * - characters: 행/열 순서를 나타내는 캐릭터 목록
 * - wins[i][j]: i번째 캐릭터가 j번째 캐릭터를 이긴 횟수
 * - draws[i][j]: i번째와 j번째의 대결에서 무승부 횟수 (i→j, j→i 동일 값)
 * - roundsPerPair: 한 조합당 시뮬레이션한 전투 수
 *
 * 관례:
 * - 대각선(i == j)은 사용하지 않는다. 출력 시 "-"로 표시.
 */
public class MatchupMatrix {

    private final List<Character> characters;
    private final int[][] wins;
    private final int[][] draws;
    private final int roundsPerPair;

    public MatchupMatrix(List<Character> characters,
                         int[][] wins,
                         int[][] draws,
                         int roundsPerPair) {
        this.characters = List.copyOf(characters);
        this.wins = wins;
        this.draws = draws;
        this.roundsPerPair = roundsPerPair;
    }

    public List<Character> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    public int[][] getWins() {
        return wins;
    }

    public int[][] getDraws() {
        return draws;
    }

    public int getRoundsPerPair() {
        return roundsPerPair;
    }

    /**
     * 행 i 캐릭터가 열 j 캐릭터를 이길 확률.
     * (무승부 포함해서 roundsPerPair로 나눈다)
     */
    public double getWinRate(int i, int j) {
        if (i == j) {
            return 0.0;
        }
        return (double) wins[i][j] / roundsPerPair;
    }
}
