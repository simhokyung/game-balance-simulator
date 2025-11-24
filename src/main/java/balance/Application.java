package balance;

import balance.analysis.BalanceAnalyzer;
import balance.analysis.BalanceAssessment;
import balance.battle.BattleSimulator;
import balance.domain.Character;
import balance.preset.PresetCharacters;
import balance.simulation.CharacterStats;
import balance.simulation.MatchupMatrix;
import balance.simulation.SimulationService;
import balance.skill.PresetSkillSetProvider;
import balance.view.BalanceReportPrinter;
import balance.view.InputView;
import balance.view.MatchupMatrixPrinter;
import balance.view.PresetInfoPrinter;

import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        InputView inputView = new InputView();
        PresetInfoPrinter presetInfoPrinter = new PresetInfoPrinter();

        System.out.println("=== Game Balance Simulator ===");
        System.out.println();

        int mode = inputView.readScenarioMode();

        List<Character> characters;
        int roundsPerPair;

        // ==========================
        // 1. 프리셋 모드
        // ==========================
        if (mode == 1) {
            // 프리셋 시나리오: 현재는 메타 6 아키타입 하나만 사용
            int scenarioChoice = inputView.readPresetScenarioChoice();
            // 실제로는 choice가 1밖에 없지만, 구조를 위해 변수로 받아둠
            if (scenarioChoice != 1) {
                System.out.println("[ERROR] 지원하지 않는 시나리오입니다.");
                return;
            }

            // 메타 6 아키타입 캐릭터 목록
            characters = PresetCharacters.metaArchetypes();

            // 설명 보기 / 전투 시작 선택 루프
            while (true) {
                int action = inputView.readPresetMetaAction();
                if (action == 2) {
                    // 전투 시작하기
                    break;
                }

                // action == 1 → 캐릭터별 설명 보기
                while (true) {
                    int charChoice = inputView.readMetaCharacterInfoChoice();
                    if (charChoice == 0) {
                        // 뒤로가기
                        break;
                    }

                    switch (charChoice) {
                        case 1 -> presetInfoPrinter.printBruiserDetails();
                        case 2 -> presetInfoPrinter.printAssassinDetails();
                        case 3 -> presetInfoPrinter.printTankDetails();
                        case 4 -> presetInfoPrinter.printSpeedsterDetails();
                        case 5 -> presetInfoPrinter.printSustainerDetails();
                        case 6 -> presetInfoPrinter.printHybridDetails();
                        default -> System.out.println("[ERROR] 잘못된 번호입니다.");
                    }
                }
            }

            // 설명 모드에서 빠져나오면 → 실제 시뮬레이션 설정
            roundsPerPair = inputView.readRoundsPerPair();
        }
        // ==========================
        // 2. 직접 입력 모드
        // ==========================
        else {
            System.out.println();
            System.out.println("[직접 입력 모드] 시뮬레이션할 캐릭터를 입력해 주세요.");
            System.out.println();
            characters = inputView.readCharacters();
            roundsPerPair = inputView.readRoundsPerPair();
        }

        // ==========================
        // 3. 전투/시뮬레이션/분석 구성
        // ==========================
        BattleSimulator battleSimulator =
                new BattleSimulator(new PresetSkillSetProvider()); // 프리셋 스킬 적용 전투 엔진

        SimulationService simulationService = new SimulationService(battleSimulator);
        BalanceAnalyzer balanceAnalyzer = new BalanceAnalyzer();        // 기본 임계값 사용
        BalanceReportPrinter printer = new BalanceReportPrinter();
        MatchupMatrixPrinter matrixPrinter = new MatchupMatrixPrinter();

        // 4. 전체 리그 시뮬레이션 실행
        Map<Character, CharacterStats> statsByCharacter =
                simulationService.simulateAll(characters, roundsPerPair);

        // 5. 밸런스 분석
        List<BalanceAssessment> assessments = balanceAnalyzer.analyze(statsByCharacter);

        // 6. 리포트 출력
        printer.print(statsByCharacter, assessments);

        // 7. 매치업 승률 매트릭스는 사용자가 원할 때만 출력
        boolean showMatchup = inputView.askShowMatchupDetails();
        if (showMatchup) {
            MatchupMatrix matchupMatrix =
                    simulationService.simulateMatrix(characters, roundsPerPair);
            matrixPrinter.print(matchupMatrix);
        }
    }
}
