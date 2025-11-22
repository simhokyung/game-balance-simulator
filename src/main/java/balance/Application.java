package balance;

import balance.analysis.BalanceAnalyzer;
import balance.analysis.BalanceAssessment;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.simulation.CharacterStats;
import balance.simulation.SimulationService;
import balance.view.BalanceReportPrinter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        // 1. 캐릭터 정의 (예시 시나리오)
        Character warrior = new Character("Warrior", 120, 40, 20, 15, 0.2);
        Character assassin = new Character("Assassin", 80, 55, 10, 25, 0.4);
        Character tank = new Character("Tank", 180, 25, 30, 10, 0.1);
        Character mage = new Character("Mage", 90, 50, 8, 18, 0.3);

        List<Character> characters = Arrays.asList(warrior, assassin, tank, mage);

        int roundsPerPair = 20; // 캐릭터 쌍마다 20판씩 돌려본다.

        // 2. 전투/시뮬레이션/분석 구성
        BattleSimulator battleSimulator = new BattleSimulator();        // 랜덤 기반 전투 엔진
        SimulationService simulationService = new SimulationService(battleSimulator);
        BalanceAnalyzer balanceAnalyzer = new BalanceAnalyzer();        // 기본 임계값 사용
        BalanceReportPrinter printer = new BalanceReportPrinter();

        // 3. 전체 리그 시뮬레이션 실행
        Map<Character, CharacterStats> statsByCharacter =
                simulationService.simulateAll(characters, roundsPerPair);

        // 4. 밸런스 분석
        List<BalanceAssessment> assessments = balanceAnalyzer.analyze(statsByCharacter);

        // 5. 리포트 출력
        printer.print(statsByCharacter, assessments);
    }
}
