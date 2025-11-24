package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // 브루저: 기준 캐릭터 (너프 버전)
    private static final Character BRUISER = new Character(
            "Bruiser",
            128,   // HP: 130 -> 128 (살짝 감소)
            40,    // ATK: 48 -> 40 (딜 꽤 너프)
            24,    // DEF: 그대로
            20,    // SPD: 22 -> 20 (턴 주기 너프)
            0.10   // CRIT: 0.12 -> 0.10
    );

    // 어쌔신: (너프)
    private static final Character ASSASSIN = new Character(
            "Assassin",
            87, // HP 90->87로 너프
            48, //ATK 50->48로 너프
            10,
            28, //SPD 29->28로 너프
            0.22
    );

    // 탱커: 너프
    private static final Character TANK = new Character(
            "Tank",
            150,   // HP: 158 -> 150 (HP 너프)
            30,    // ATK: 35 -> 30 (딜 너프)
            38,    // DEF: 그대로
            17,    // SPD: 19 -> 17 (스피드 미세 너프)
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

    // 하이브리드:
    private static final Character HYBRID = new Character(
            "Hybrid",
            110,
            44, // 46 -> 44너프
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
