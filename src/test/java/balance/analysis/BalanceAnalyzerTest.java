package balance.analysis;

import balance.domain.Character;
import balance.simulation.CharacterStats;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BalanceAnalyzerTest {

    @Test
    void 승률_기준으로_OP_밸런스_약캐를_분류할_수_있다() {
        Character op = new Character("OP", 100, 50, 10, 20, 0.0);
        Character fair = new Character("Fair", 100, 30, 10, 15, 0.0);
        Character weak = new Character("Weak", 100, 10, 10, 10, 0.0);

        // 총 경기 수는 모두 20으로 동일, 승률만 다르게 설정
        CharacterStats opStats = new CharacterStats(op, 16, 4, 0, 20);     // 승률 0.8
        CharacterStats fairStats = new CharacterStats(fair, 10, 10, 0, 20); // 승률 0.5
        CharacterStats weakStats = new CharacterStats(weak, 4, 16, 0, 20);  // 승률 0.2

        Map<Character, CharacterStats> statsMap = Map.of(
                op, opStats,
                fair, fairStats,
                weak, weakStats
        );

        BalanceAnalyzer analyzer = new BalanceAnalyzer(); // 기본 임계값 사용
        List<BalanceAssessment> assessments = analyzer.analyze(statsMap);

        BalanceAssessment opAssessment = findByName(assessments, "OP");
        BalanceAssessment fairAssessment = findByName(assessments, "Fair");
        BalanceAssessment weakAssessment = findByName(assessments, "Weak");

        assertEquals(BalanceTier.OVERPOWERED, opAssessment.getTier());
        assertEquals(BalanceTier.BALANCED, fairAssessment.getTier());
        assertEquals(BalanceTier.UNDERPOWERED, weakAssessment.getTier());
    }

    @Test
    void 경기수가_너무_적으면_승률이_극단적이어도_일단_밸런스로_본다() {
        Character lucky = new Character("Lucky", 100, 40, 10, 20, 0.0);

        // 2경기 2승 → 승률 1.0 이지만 표본이 너무 적음
        CharacterStats luckyStats = new CharacterStats(lucky, 2, 0, 0, 2);

        Map<Character, CharacterStats> statsMap = Map.of(lucky, luckyStats);

        BalanceAnalyzer analyzer = new BalanceAnalyzer(); // 기본 임계값 + 최소 경기 수
        List<BalanceAssessment> assessments = analyzer.analyze(statsMap);

        BalanceAssessment assessment = assessments.get(0);

        assertEquals(BalanceTier.BALANCED, assessment.getTier());
        assertTrue(assessment.getReason().contains("표본"));
    }

    private BalanceAssessment findByName(List<BalanceAssessment> list, String name) {
        return list.stream()
                .filter(a -> a.getCharacter().getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
