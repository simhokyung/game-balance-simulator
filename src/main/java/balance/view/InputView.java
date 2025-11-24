package balance.view;

import balance.domain.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputView {

    private final Scanner scanner = new Scanner(System.in);

    public List<Character> readCharacters() {
        System.out.print("시뮬레이션할 캐릭터 수를 입력하세요 (최소 2명): ");
        int count = readIntAtLeast(2, "캐릭터 수는 2명 이상이어야 합니다.");

        List<Character> characters = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            while (true) {
                System.out.printf("캐릭터 #%d 정보를 입력하세요 (형식: 이름 HP ATK DEF SPD CRIT):%n", i);
                System.out.println("예시: Warrior 120 40 20 15 0.2");
                String line = scanner.nextLine();
                try {
                    Character character = parseCharacterLine(line);
                    characters.add(character);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                    System.out.println("다시 입력해 주세요.");
                }
            }
        }

        return characters;
    }

    public int readScenarioMode() {
        System.out.println("모드를 선택하세요:");
        System.out.println("1: 미리 준비된 프리셋 시나리오 사용");
        System.out.println("2: 직접 캐릭터 스펙 입력");
        System.out.print("메뉴 번호를 입력하세요 (1-2): ");
        System.out.println();

        return readIntInRange(1, 2, "메뉴 번호는 1 또는 2여야 합니다.");
    }

    /**
     * 프리셋 시나리오 선택.
     * 현재는 1대1 밸런스형 전투 캐릭터 6종만 제공한다.
     */
    public int readPresetScenarioChoice() {
        System.out.println("프리셋 시나리오를 선택하세요:");
        System.out.println("1: 1대1 밸런스형 전투 캐릭터 6종 (Bruiser / Assassin / Tank / Speedster / Sustainer / Hybrid)");
        System.out.print("시나리오 번호를 입력하세요 (1): ");
        System.out.println();

        return readIntInRange(1, 1, "시나리오 번호는 1이어야 합니다.");
    }

    /**
     * 메타 6 아키타입 프리셋에서:
     * 1. 설명 보기
     * 2. 전투 시작하기
     */
    public int readPresetMetaAction() {
        System.out.println();
        System.out.println("무엇을 하시겠습니까?");
        System.out.println("1: 캐릭터별 스탯 / 스킬 설명 보기");
        System.out.println("2: 전투 시작하기");
        System.out.print("번호를 입력하세요 (1-2): ");

        return readIntInRange(1, 2, "메뉴 번호는 1 또는 2여야 합니다.");
    }

    /**
     * 캐릭터별 설명 보기용 선택 메뉴.
     * 0을 입력하면 이전 메뉴로 돌아간다.
     */
    public int readMetaCharacterInfoChoice() {
        System.out.println();
        System.out.println("설명을 보고 싶은 캐릭터를 선택하세요:");
        System.out.println("1: Bruiser");
        System.out.println("2: Assassin");
        System.out.println("3: Tank");
        System.out.println("4: Speedster");
        System.out.println("5: Sustainer");
        System.out.println("6: Hybrid");
        System.out.println("0: 이전 메뉴로 돌아가기");
        System.out.print("번호를 입력하세요 (0-6): ");

        return readIntInRange(0, 6, "번호는 0에서 6 사이여야 합니다.");
    }

    public int readRoundsPerPair() {
        System.out.print("각 캐릭터 조합당 전투 횟수(라운드 수)를 입력하세요 (예: 20): ");
        return readIntAtLeast(1, "전투 횟수는 1 이상이어야 합니다.");
    }

    public boolean askShowMatchupDetails() {
        System.out.println();
        System.out.println("캐릭터별 매치업 승률 상세를 확인하시겠습니까?");
        System.out.println("1: 예");
        System.out.println("2: 아니오");
        System.out.print("번호를 입력하세요 (1-2): ");

        int choice = readIntInRange(1, 2, "메뉴 번호는 1 또는 2여야 합니다.");
        return choice == 1;
    }

    private int readIntAtLeast(int min, String errorMessage) {
        while (true) {
            String line = scanner.nextLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value < min) {
                    System.out.println("[ERROR] " + errorMessage);
                    System.out.print("다시 입력해 주세요: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 정수를 입력해야 합니다.");
                System.out.print("다시 입력해 주세요: ");
            }
        }
    }

    private int readIntInRange(int min, int max, String errorMessage) {
        while (true) {
            String line = scanner.nextLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value < min || value > max) {
                    System.out.println("[ERROR] " + errorMessage);
                    System.out.print("다시 입력해 주세요: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 정수를 입력해야 합니다.");
                System.out.print("다시 입력해 주세요: ");
            }
        }
    }

    // 테스트를 위해 분리한 파싱 로직
    static Character parseCharacterLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("입력이 비어 있습니다.");
        }

        String[] tokens = line.trim().split("\\s+");
        if (tokens.length != 6) {
            throw new IllegalArgumentException("형식은 '이름 HP ATK DEF SPD CRIT' 여야 합니다. (총 6개 값)");
        }

        String name = tokens[0];

        try {
            int hp = Integer.parseInt(tokens[1]);
            int atk = Integer.parseInt(tokens[2]);
            int def = Integer.parseInt(tokens[3]);
            int spd = Integer.parseInt(tokens[4]);
            double crit = Double.parseDouble(tokens[5]);

            return new Character(name, hp, atk, def, spd, crit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("HP/ATK/DEF/SPD는 정수, CRIT는 실수(double)로 입력해야 합니다.");
        }
    }
}
