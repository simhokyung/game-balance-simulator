package balance.scenario;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public class PresetScenarios {

    /**
     * 기본 롤 조합:
     * - Warrior: 준수한 체력/공격 + 중간 방어/속도
     * - Assassin: 낮은 체력, 매우 높은 공격/속도, 높은 치명타
     * - Tank: 매우 높은 체력/방어, 낮은 공격/속도
     * - Mage: 중간 체력, 높은 공격, 낮은 방어, 중간 속도/치명타
     */
    public static List<Character> basicRoles() {
        Character warrior = new Character("Warrior", 120, 40, 20, 15, 0.2);
        Character assassin = new Character("Assassin", 80, 55, 10, 25, 0.4);
        Character tank = new Character("Tank", 180, 25, 35, 10, 0.1);
        Character mage = new Character("Mage", 90, 50, 8, 18, 0.3);

        return Arrays.asList(warrior, assassin, tank, mage);
    }

    /**
     * 극단 비교 시나리오:
     * - GlassCannon: 매우 높은 공격/치명, 매우 낮은 체력/방어
     * - IronWall: 매우 높은 체력/방어, 매우 낮은 공격/치명, 느린 속도
     * - Balanced: 전반적으로 균형 잡힌 스탯
     * - Speedster: 높은 속도/공격, 얇은 체력/방어
     */
    public static List<Character> extremeComparison() {
        Character glassCannon = new Character("GlassCannon", 70, 60, 5, 20, 0.5);
        Character ironWall = new Character("IronWall", 200, 20, 40, 8, 0.05);
        Character balanced = new Character("Balanced", 120, 35, 22, 15, 0.2);
        Character speedster = new Character("Speedster", 85, 40, 12, 30, 0.2);

        return Arrays.asList(glassCannon, ironWall, balanced, speedster);
    }

    private PresetScenarios() {
        // 유틸 클래스
    }
}
