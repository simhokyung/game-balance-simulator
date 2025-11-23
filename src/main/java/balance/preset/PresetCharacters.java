package balance.preset;

import balance.domain.Character;

import java.util.Arrays;
import java.util.List;

public final class PresetCharacters {

    // 브루저: 기준 캐릭터 (이번 패치에서 확실히 버프)
    private static final Character BRUISER = new Character(
            "Bruiser",
            130,   // HP: 조금 튼튼하게 유지
            48,    // ATK: 44 -> 48 (딜 확실히 상향)
            24,    // DEF
            22,    // SPD: 20 -> 22 (턴 주기도 상향)
            0.12   // CRIT: 0.10 -> 0.12
    );

    // 어쌔신: 암살자 맛만 살짝 강화 (승률 0.44라 크게 건들 필요 X)
    private static final Character ASSASSIN = new Character(
            "Assassin",
            90,    // HP
            50,    // ATK
            10,    // DEF
            29,    // SPD
            0.22   // CRIT: 0.20 -> 0.22 (폭딜 변동성 조금 강화)
    );

    // 탱커: 너무 사기라 확실하게 너프 (그래도 '벽' 정체성은 유지)
    private static final Character TANK = new Character(
            "Tank",
            155,   // HP: 165 -> 155
            34,    // ATK: 36 -> 34
            38,    // DEF: 40 -> 38 (그래도 제일 단단)
            18,    // SPD: 20 -> 18 (턴 수 손해를 더 보게)
            0.05   // CRIT
    );

    // 스피드러너: 속도로 이득 보는 캐릭, 딜만 살짝 너프
    private static final Character SPEEDSTER = new Character(
            "Speedster",
            100,   // HP
            39,    // ATK: 40 -> 39
            15,    // DEF
            32,    // SPD: 그대로, 정체성 유지
            0.15   // CRIT
    );

    // 서스테이너: 현재 0.43 근처라 유지 (나중에 스킬 붙으면 자연히 올라갈 예정)
    private static final Character SUSTAINER = new Character(
            "Sustainer",
            128,   // HP
            37,    // ATK
            23,    // DEF
            24,    // SPD
            0.10   // CRIT
    );

    // 하이브리드: 이것저것 조금씩 괜찮은 '만능형', 현재 승률 안정적이라 유지
    private static final Character HYBRID = new Character(
            "Hybrid",
            110,   // HP
            46,    // ATK
            20,    // DEF
            22,    // SPD
            0.16   // CRIT
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
