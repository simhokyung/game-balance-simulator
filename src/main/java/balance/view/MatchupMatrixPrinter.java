package balance.view;

import balance.domain.Character;
import balance.simulation.MatchupMatrix;

import java.util.List;

/**
 * 캐릭터 간 매치업 승률 매트릭스를 콘솔로 출력하는 뷰.
 *
 * - 행: 공격자(먼저 적을 보는 쪽)
 * - 열: 상대
 * - 셀 값: 행 캐릭터가 열 캐릭터를 이길 확률 (0.00 ~ 1.00)
 */
public class MatchupMatrixPrinter {

    public void print(MatchupMatrix matrix) {
        List<Character> characters = matrix.getCharacters();
        int size = characters.size();

        System.out.println("========================================");
        System.out.println("       Matchup Winrate Matrix");
        System.out.println(" (행 캐릭터가 열 캐릭터를 이길 확률)");
        System.out.println("========================================");

        // 헤더: 맨 왼쪽 빈 칸 + 캐릭터 이름들
        System.out.printf("%12s", "");
        for (Character character : characters) {
            System.out.printf("%12s", character.getName());
        }
        System.out.println();

        // 각 행
        for (int i = 0; i < size; i++) {
            Character rowChar = characters.get(i);
            System.out.printf("%12s", rowChar.getName());

            for (int j = 0; j < size; j++) {
                if (i == j) {
                    System.out.printf("%12s", "-");
                    continue;
                }

                double winRate = matrix.getWinRate(i, j);
                System.out.printf("%12.2f", winRate);
            }

            System.out.println();
        }

        System.out.println("※ 값은 " +
                "행 캐릭터가 열 캐릭터를 상대로 승리한 비율입니다. (무승부 포함)");
        System.out.println("========================================");
        System.out.println();
    }
}
