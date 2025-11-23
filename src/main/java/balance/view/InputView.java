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

    public int readPresetScenarioChoice() {
        System.out.println("프리셋 시나리오를 선택하세요:");
        System.out.println("1: 기본 전투 롤 4종 (Warrior / Assassin / Tank / Mage)");
        System.out.println("2: 극단적인 전투 캐릭터 4종 (GlassCannon / IronWall / Balanced / Speedster)");
        System.out.println("3: 1대1 밸런스형 전투 캐릭터 6종 (Bruiser / Assassin / Tank / Speedster / Sustainer / Hybrid)");
        System.out.print("시나리오 번호를 입력하세요 (1-3): ");
        System.out.println();

        return readIntInRange(1, 3, "시나리오 번호는 1, 2 또는 3이어야 합니다.");
    }

    public int readRoundsPerPair() {
        System.out.print("각 캐릭터 조합당 전투 횟수(라운드 수)를 입력하세요 (예: 20): ");
        return readIntAtLeast(1, "전투 횟수는 1 이상이어야 합니다.");
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
