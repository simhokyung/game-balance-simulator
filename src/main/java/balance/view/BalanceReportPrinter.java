package balance.view;

import balance.analysis.BalanceAssessment;
import balance.analysis.BalanceTier;
import balance.domain.Character;
import balance.simulation.CharacterStats;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BalanceReportPrinter {

    public void print(Map<Character, CharacterStats> statsByCharacter,
                      List<BalanceAssessment> assessments) {

        Map<Character, BalanceAssessment> assessmentByCharacter = assessments.stream()
                .collect(Collectors.toMap(
                        BalanceAssessment::getCharacter,
                        a -> a
                ));

        System.out.println("========================================");
        System.out.println("         Game Balance Report");
        System.out.println("========================================");

        System.out.println("이름 | 티어 | 승률 | 전적(W/L/D) | 경기 수");
        System.out.println("----------------------------------------");

        statsByCharacter.entrySet().stream()
                .sorted(Comparator.comparingDouble(
                        entry -> -entry.getValue().getWinRate()
                ))
                .forEach(entry -> {
                    Character character = entry.getKey();
                    CharacterStats stats = entry.getValue();
                    BalanceAssessment assessment = assessmentByCharacter.get(character);
                    BalanceTier tier = assessment != null ? assessment.getTier() : BalanceTier.BALANCED;

                    String line = String.format(
                            "%s | %s | %.2f | %d/%d/%d | %d",
                            character.getName(),
                            tier.name(),
                            stats.getWinRate(),
                            stats.getWins(),
                            stats.getLosses(),
                            stats.getDraws(),
                            stats.getTotalMatches()
                    );
                    System.out.println(line);
                });

        System.out.println();
        System.out.println("----- 상세 사유 -----");
        assessments.stream()
                .sorted(Comparator.comparingDouble(
                        a -> -a.getStats().getWinRate()
                ))
                .forEach(assessment -> {
                    Character character = assessment.getCharacter();
                    System.out.printf("[%s] %s%n", character.getName(), assessment.getReason());
                });
        System.out.println();
    }
}
