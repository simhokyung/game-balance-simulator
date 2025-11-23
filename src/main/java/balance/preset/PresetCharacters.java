package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // Bruiser: 여전히 기준 캐릭, 하지만 승률 90%는 말이 안 되니 살짝 너프
    private static final Character BRUISER = new Character(
            "Bruiser",
            128,   // 130 -> 128
            44,    // 47 -> 44
            24,    // 25 -> 24
            20,    // 22 -> 20
            0.10
    );

    // Assassin: 너무 죽어서, 진짜 “짧게 강한 폭딜”로 상향
    private static final Character ASSASSIN = new Character(
            "Assassin",
            90,    // 80 -> 90 (조금 더 안 죽게)
            50,    // 48 -> 50
            10,
            29,    // 28 -> 29
            0.20   // 0.18 -> 0.20
    );

    // 탱커: 너무 못 싸우니, 딜/속도를 조금만 올림
    private static final Character TANK = new Character(
            "Tank",
            165,
            36,    // 32 -> 36
            40,
            20,    // 16 -> 20
            0.05
    );

    // 스피드러너: 속도로 먹고 사는 캐릭, 살짝 상향
    private static final Character SPEEDSTER = new Character(
            "Speedster",
            100,   // HP
            40,    // ATK: 38 -> 40 (조금 버스트 강화)
            15,    // DEF
            32,    // SPD
            0.15   // CRIT
    );

    // 서스테이너: 지금 거의 브루저 상위호환이라 확실히 너프
    private static final Character SUSTAINER = new Character(
            "Sustainer",
            128,   // 130 -> 128
            37,    // 40 -> 37
            23,    // 24 -> 23
            24,    // 26 -> 24
            0.10   // 0.12 -> 0.10
    );

    // 하이브리드: 만능형이지만, 지나치게 강하지 않도록 전체적으로 소폭 너프
    private static final Character HYBRID = new Character(
            "Hybrid",
            110,   // HP
            46,    // ATK: 48 -> 46
            20,    // DEF
            22,    // SPD: 24 -> 22
            0.16   // CRIT: 0.18 -> 0.16
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
