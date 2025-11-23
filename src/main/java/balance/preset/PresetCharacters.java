package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // 브루저: 기준 캐릭터 (너프 버전)
    private static final Character BRUISER = new Character(
            "Bruiser",
            128,   // HP: 130 -> 128 (살짝 감소)
            44,    // ATK: 48 -> 44 (딜 꽤 너프)
            24,    // DEF: 그대로
            20,    // SPD: 22 -> 20 (턴 주기 너프)
            0.10   // CRIT: 0.12 -> 0.10
    );

    // 어쌔신: 직전 패치 그대로 유지
    private static final Character ASSASSIN = new Character(
            "Assassin",
            90,
            50,
            10,
            29,
            0.22
    );

    // 탱커: 살짝 버프 (여전히 단단하지만, 너무 약하지는 않게)
    private static final Character TANK = new Character(
            "Tank",
            158,   // HP: 155 -> 158 (조금 더 버티게)
            35,    // ATK: 34 -> 35 (딜 미세 상향)
            38,    // DEF: 그대로
            19,    // SPD: 18 -> 19 (턴 주기 아주 조금 상향)
            0.05
    );

    // 스피드러너: 직전 패치 그대로 유지
    private static final Character SPEEDSTER = new Character(
            "Speedster",
            100,
            39,
            15,
            32,
            0.15
    );

    // 서스테이너: 직전 패치 그대로 유지
    private static final Character SUSTAINER = new Character(
            "Sustainer",
            128,
            37,
            23,
            24,
            0.10
    );

    // 하이브리드: 직전 패치 그대로 유지
    private static final Character HYBRID = new Character(
            "Hybrid",
            110,
            46,
            20,
            22,
            0.16
    );

    private PresetCharacters() {
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
