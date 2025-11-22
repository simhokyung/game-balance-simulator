package balance;

import balance.analysis.BalanceAnalyzer;
import balance.analysis.BalanceAssessment;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.simulation.CharacterStats;
import balance.simulation.SimulationService;
import balance.view.BalanceReportPrinter;
import balance.view.InputView;

import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        InputView inputView = new InputView();

        System.out.println("=== Game Balance Simulator ===");
        System.out.println("시뮬레이션할 캐릭터를 입력해 주세요.");
        System.out.println();

        // 1. 사용자로부터 캐릭터 목록과 라운드 수를 입력받기
        List<Character> characters = inputView.readCharacters();
        int roundsPerPair = inputView.readRoundsPerPair();

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
