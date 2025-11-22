package balance.analysis;

import balance.domain.Character;
import balance.simulation.CharacterStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BalanceAnalyzer {

    private static final int DEFAULT_MIN_MATCHES = 5;
    private static final double DEFAULT_OVERPOWERED_THRESHOLD = 0.6;  // 60% 이상이면 OP 후보
    private static final double DEFAULT_UNDERPOWERED_THRESHOLD = 0.4; // 40% 이하면 약캐 후보

    private final int minMatches;
    private final double overpoweredThreshold;
    private final double underpoweredThreshold;

    public BalanceAnalyzer() {
        this(DEFAULT_MIN_MATCHES, DEFAULT_OVERPOWERED_THRESHOLD, DEFAULT_UNDERPOWERED_THRESHOLD);
    }

    public BalanceAnalyzer(int minMatches,
                           double overpoweredThreshold,
                           double underpoweredThreshold) {
        if (minMatches < 1) {
            throw new IllegalArgumentException("최소 경기 수는 1 이상이어야 합니다.");
        }
        if (underpoweredThreshold < 0.0 || underpoweredThreshold > 1.0) {
            throw new IllegalArgumentException("언더파워 기준 승률은 0~1 사이여야 합니다.");
        }
        if (overpoweredThreshold < 0.0 || overpoweredThreshold > 1.0) {
            throw new IllegalArgumentException("오버파워 기준 승률은 0~1 사이여야 합니다.");
        }
        if (underpoweredThreshold > overpoweredThreshold) {
            throw new IllegalArgumentException("언더파워 기준은 오버파워 기준보다 작거나 같아야 합니다.");
        }

        this.minMatches = minMatches;
        this.overpoweredThreshold = overpoweredThreshold;
        this.underpoweredThreshold = underpoweredThreshold;
    }

    public List<BalanceAssessment> analyze(Map<Character, CharacterStats> statsByCharacter) {
        List<BalanceAssessment> result = new ArrayList<>();

        for (Map.Entry<Character, CharacterStats> entry : statsByCharacter.entrySet()) {
            Character character = entry.getKey();
            CharacterStats stats = entry.getValue();

            BalanceAssessment assessment = assess(character, stats);
            result.add(assessment);
        }

        return result;
    }

    private BalanceAssessment assess(Character character, CharacterStats stats) {
        int totalMatches = stats.getTotalMatches();
        double winRate = stats.getWinRate();

        // 표본이 너무 적으면 극단적인 승률이어도 '일단 보류' → BALANCED 처리
        if (totalMatches < minMatches) {
            String reason = String.format(
                    "경기 수가 %d회로 최소 표본(%d회) 미만이므로 일단 BALANCED로 분류합니다. (승률=%.2f)",
                    totalMatches, minMatches, winRate
            );
            return new BalanceAssessment(character, stats, BalanceTier.BALANCED, reason);
        }

        if (winRate >= overpoweredThreshold) {
            String reason = String.format(
                    "승률이 %.0f%% 이상(%.2f)으로 높아 OVERPOWERED로 분류합니다.",
                    overpoweredThreshold * 100, winRate
            );
            return new BalanceAssessment(character, stats, BalanceTier.OVERPOWERED, reason);
        }

        if (winRate <= underpoweredThreshold) {
            String reason = String.format(
                    "승률이 %.0f%% 이하(%.2f)로 낮아 UNDERPOWERED로 분류합니다.",
                    underpoweredThreshold * 100, winRate
            );
            return new BalanceAssessment(character, stats, BalanceTier.UNDERPOWERED, reason);
        }

        String reason = String.format(
                "승률이 중간 구간(%.0f%%~%.0f%%)에 위치하여 BALANCED로 분류합니다. (승률=%.2f)",
                underpoweredThreshold * 100, overpoweredThreshold * 100, winRate
        );
        return new BalanceAssessment(character, stats, BalanceTier.BALANCED, reason);
    }
}
