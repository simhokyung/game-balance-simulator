package balance;

import balance.analysis.BalanceAnalyzer;
import balance.analysis.BalanceAssessment;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.preset.PresetCharacters;
import balance.simulation.CharacterStats;
import balance.simulation.SimulationService;
import balance.view.BalanceReportPrinter;
import balance.view.InputView;
import balance.scenario.PresetScenarios;
import balance.skill.PresetSkillSetProvider;

import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        InputView inputView = new InputView();

        System.out.println("=== Game Balance Simulator ===");
        System.out.println();

        int mode = inputView.readScenarioMode();

        List<Character> characters;
        int roundsPerPair;

        if (mode == 1) {
            int scenarioChoice = inputView.readPresetScenarioChoice();

            if (scenarioChoice == 1) {
                characters = PresetScenarios.basicRoles();
            } else if (scenarioChoice == 2) {
                characters = PresetScenarios.extremeComparison();
            } else {
                // 3번: 1대1 메타 6 아키타입
                characters = PresetCharacters.metaArchetypes();
            }

            roundsPerPair = inputView.readRoundsPerPair();
        } else {
            System.out.println();
            System.out.println("[직접 입력 모드] 시뮬레이션할 캐릭터를 입력해 주세요.");
            System.out.println();
            characters = inputView.readCharacters();
            roundsPerPair = inputView.readRoundsPerPair();
        }

// 2. 전투/시뮬레이션/분석 구성
        BattleSimulator battleSimulator =
                new BattleSimulator(new PresetSkillSetProvider());      // 프리셋 스킬 적용 전투 엔진
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
