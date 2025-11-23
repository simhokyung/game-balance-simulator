package balance.preset;

import balance.domain.Character;
import java.util.Arrays;
import java.util.List;

/**
 * 1대1 전투용 6개 캐릭터 아키타입 프리셋 모음.
 *
 * 설계 기준:
 *  - HP 90~150, ATK 30~65, DEF 5~40, SPD 10~35, CRIT 0~30%
 *  - 브루저를 기준점(Baseline)으로 두고 나머지 역할을 파생
 *  - 어쌔신/탱커/스피드/서스테이너/하이브리드는
 *    전투 곡선(초/중/후반), 승리 전략, 약점, 상성을 고려해 설계
 */
public final class PresetCharacters {

    private PresetCharacters() {
        // 인스턴스 생성 방지
    }

    /**
     * ⭐ 브루저(Bruiser) — 기준 캐릭터 (Baseline)
     * 역할: 안정성, 모든 밸런스 비교의 기준
     * 전투 곡선: 중반 강함
     *
     * HP 130 / ATK 45 / DEF 25 / SPD 18 / CRIT 0.10
     */
    private static final Character BRUISER = new Character(
            "Bruiser", 130, 45, 25, 18, 0.10
    );

    /**
     * ⭐ 어쌔신(Assassin) — 원턴킬 폭발형 (Burst)
     * 역할: 초반 압살
     * 전투 곡선: 초반 매우 강함, 후반 약함
     *
     * HP 90 / ATK 60 / DEF 10 / SPD 35 / CRIT 0.25
     */
    private static final Character ASSASSIN = new Character(
            "Assassin", 90, 60, 10, 35, 0.25
    );

    /**
     * ⭐ 탱커(Tank) — 방어벽 (Iron Wall)
     * 역할: 후반 압도
     * 전투 곡선: 후반 폭발적으로 강함
     *
     * HP 150 / ATK 30 / DEF 40 / SPD 10 / CRIT 0.05
     */
    private static final Character TANK = new Character(
            "Tank", 150, 30, 40, 10, 0.05
    );

    /**
     * ⭐ 스피드러너(Speedster) — 순수 속도형
     * 역할: 턴 이득, 다단 행동
     * 전투 곡선: 초반 약간 강함, 중후반 유지형
     *
     * HP 100 / ATK 40 / DEF 15 / SPD 32 / CRIT 0.15
     */
    private static final Character SPEEDSTER = new Character(
            "Speedster", 100, 40, 15, 32, 0.15
    );

    /**
     * ⭐ 서스테이너(Sustainer) — 지속전/흡혈형
     * 역할: 중장기전 최강 (향후 회복/흡혈 메커니즘과 연계 예정)
     * 전투 곡선: 초반 약함 → 중반 강함 → 후반 최강
     *
     * HP 120 / ATK 35 / DEF 20 / SPD 20 / CRIT 0.05
     *
     * ⚠ 아직 Character/BattleSimulator에 회복 로직은 없으므로
     *   현재는 "지속형 스탯만 잡아둔 상태"이며,
     *   나중에 패시브/스킬로 회복 효과를 붙일 예정.
     */
    private static final Character SUSTAINER = new Character(
            "Sustainer", 120, 35, 20, 20, 0.05
    );

    /**
     * ⭐ 하이브리드(Hybrid) — 메타 파괴형 조합 캐릭
     * 역할: 다재다능 + 위험한 빌드
     * 전투 곡선: 중반 최고 전성기
     *
     * HP 110 / ATK 50 / DEF 20 / SPD 25 / CRIT 0.20
     */

    private static final Character HYBRID = new Character(
            "Hybrid", 110, 50, 20, 25, 0.20
    );



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
     * 1대1 메타 분석용 6 아키타입 전체 반환.
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
