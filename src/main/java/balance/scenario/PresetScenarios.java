package balance.scenario;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

/**
 * 밸런스 실험/테스트용 시나리오 프리셋.
 *
 * - main 메타는 balance.preset.PresetCharacters 의 6 아키타입을 사용하고,
 * - 이 클래스는 극단값 비교, 초기 설계안, 실험용 조합들을 담는다.
 */
public class PresetScenarios {

    /**
     * [초기 롤 조합 시나리오]
     * - Warrior, Assassin, Tank, Mage 등
     * - 6 아키타입 도입 전, 기본 구조를 잡을 때 사용했던 스냅샷.
     */
    public static List<Character> basicRoles() {
        Character warrior = new Character("Warrior", 120, 40, 20, 15, 0.2);
        Character assassin = new Character("Assassin", 80, 55, 10, 25, 0.4);
        Character tank = new Character("Tank", 180, 25, 35, 10, 0.1);
        Character mage = new Character("Mage", 90, 50, 8, 18, 0.3);

        return Arrays.asList(warrior, assassin, tank, mage);
    }

    /**
     * [극단 비교 시나리오]
     * - GlassCannon vs IronWall vs Balanced vs Speedster
     * - "극단적인 유리대포/철벽/균형형/속도형" 조합이
     *   비율형 데미지 공식에서 어떻게 동작하는지 보기 위한 실험용.
     */
    public static List<Character> extremeComparison() {
        Character glassCannon = new Character("GlassCannon", 70, 60, 5, 20, 0.5);
        Character ironWall = new Character("IronWall", 200, 20, 40, 8, 0.05);
        Character balanced = new Character("Balanced", 120, 35, 22, 15, 0.2);
        Character speedster = new Character("Speedster", 85, 40, 12, 30, 0.2);

        return Arrays.asList(glassCannon, ironWall, balanced, speedster);
    }

    private PresetScenarios() {
    }
}
