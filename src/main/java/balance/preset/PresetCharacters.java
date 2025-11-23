package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // 브루저: 기준 캐릭터 (조금 상향해서 중심축 역할)
    private static final Character BRUISER = new Character(
            "Bruiser",
            130,   // HP: 여전히 튼튼
            45,    // ATK: 기준 딜
            25,    // DEF
            20,    // SPD: 기존 18 → 20 (조금 더 민첩하게)
            0.10   // CRIT
    );

    // 어쌔신: 여전히 폭딜 + 고속, 하지만 너무 압도적이지 않게 너프
    private static final Character ASSASSIN = new Character(
            "Assassin",
            85,    // HP: 살짝 더 유리몸
            52,    // ATK: 60 → 52 (비율 공식이라 이 정도만 내려도 체감 큼)
            10,    // DEF: 그대로, 종이 방패
            30,    // SPD: 35 → 30 (여전히 빠르지만 압도적이진 않게)
            0.20   // CRIT: 0.25 → 0.20
    );

    // 탱커: 진짜로 "버티기만 하는 바보"가 아니라 "버티면서 역킬도 가능한 벽"으로
    private static final Character TANK = new Character(
            "Tank",
            165,   // HP: 150 → 165 (탱커 정체성 강화)
            36,    // ATK: 30 → 36 (맞기만 하는 게 아니라 어느 정도 패는 딜)
            40,    // DEF: 그대로, 방어벽
            18,    // SPD: 10 → 18 (여전히 느리지만, 아예 턴을 못 가져가진 않게)
            0.05   // CRIT
    );

    // 스피드러너: 속도로 이득 보는 캐릭, 딜은 살짝 조정
    private static final Character SPEEDSTER = new Character(
            "Speedster",
            100,   // HP
            38,    // ATK: 40 → 38 (조금 너프)
            15,    // DEF
            32,    // SPD: 여전히 매우 빠름 (정체성 유지)
            0.15   // CRIT
    );

    // 서스테이너: 나중에 회복/흡혈 스킬 붙을 예정이라, 지금은 기초 스펙 보정
    private static final Character SUSTAINER = new Character(
            "Sustainer",
            125,   // HP: 120 → 125
            36,    // ATK: 35 → 36 (딜 조금 상향)
            22,    // DEF: 20 → 22
            24,    // SPD: 20 → 24 (너무 굼뜨지 않게)
            0.08   // CRIT: 0.05 → 0.08
    );

    // 하이브리드: 종합 능력치가 너무 높았으니, 전체적으로 살짝 너프
    private static final Character HYBRID = new Character(
            "Hybrid",
            110,   // HP: 그대로
            48,    // ATK: 50 → 48
            20,    // DEF
            24,    // SPD: 25 → 24
            0.18   // CRIT: 0.20 → 0.18
    );

    private PresetCharacters() {
        // 인스턴스 생성 방지
    }

    public static Character bruiser() {
        return BRUISER;
    }

    public static Character assassin() {
        return ASSASSIN;
    }

    public static Character tank() {
        return TANK;
    }

    public static Character speedster() {
        return SPEEDSTER;
    }

    public static Character sustainer() {
        return SUSTAINER;
    }

    public static Character hybrid() {
        return HYBRID;
    }

    /**
     * 1대1 메타 밸런스 분석용 6 아키타입 전체 목록.
     */
    public static List<Character> metaArchetypes() {
        return Arrays.asList(
                BRUISER,
                ASSASSIN,
                TANK,
                SPEEDSTER,
                SUSTAINER,
                HYBRID
        );
    }
}
