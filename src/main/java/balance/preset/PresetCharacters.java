package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // 브루저: 기준 캐릭터 (살짝 상향해서 진짜 "평균" 축)
    private static final Character BRUISER = new Character(
            "Bruiser",
            130,   // HP
            47,    // ATK: 45 -> 47 (조금 더 때리게)
            25,    // DEF
            22,    // SPD: 20 -> 22 (턴 주기 살짝 개선)
            0.10   // CRIT
    );

    // 어쌔신: 여전히 폭딜 + 고속, 하지만 너무 압도적이지 않게 너프
    private static final Character ASSASSIN = new Character(
            "Assassin",
            80,    // HP: 85 -> 80 (진짜 유리몸 느낌 강화)
            48,    // ATK: 52 -> 48
            10,    // DEF
            28,    // SPD: 30 -> 28
            0.18   // CRIT: 0.20 -> 0.18
    );

    // 탱커: "잘 버티면서 이길 수는 있지만, 항상 이기진 않는다" 쪽으로 조정
    private static final Character TANK = new Character(
            "Tank",
            165,   // HP: 그대로
            32,    // ATK: 36 -> 32 (딜 약간 너프)
            40,    // DEF
            16,    // SPD: 18 -> 16 (조금 더 느리게)
            0.05   // CRIT
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

    // 서스테이너: 나중에 회복/흡혈 스킬 붙을 예정이라, 지금 기본 스펙 확실히 보정
    private static final Character SUSTAINER = new Character(
            "Sustainer",
            130,   // HP: 125 -> 130
            40,    // ATK: 36 -> 40 (딜 꽤 올려줌)
            24,    // DEF: 22 -> 24
            26,    // SPD: 24 -> 26
            0.12   // CRIT: 0.08 -> 0.12
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
