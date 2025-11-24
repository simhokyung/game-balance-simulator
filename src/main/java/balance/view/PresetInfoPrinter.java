package balance.view;

/**
 * 프리셋 3 (1대1 밸런스형 전투 캐릭터 6종)에 대한
 * 캐릭터별 스탯/스킬 컨셉 설명을 출력하는 뷰.
 *
 * Application / InputView 에서:
 * - 사용자가 선택한 번호에 따라
 *   아래의 메서드 중 하나를 호출하는 구조로 사용한다.
 */
public class PresetInfoPrinter {

    public void printBruiserDetails() {
        System.out.println("========================================");
        System.out.println("[Bruiser] – 맞을수록 강해지는 근접 브루저");
        System.out.println("========================================");
        System.out.println("▶ 역할: 맞으면서 분노 스택을 쌓고, 특정 타이밍에 큰 한 방을 넣는 브루저형 파이터");
        System.out.println();
        System.out.println("- 패시브: 분노 축적 (Rage Passive)");
        System.out.println("  · 피해를 받을 때마다 분노 스택이 1씩 증가한다. (최대 10스택)");
        System.out.println("  · 스택 자체는 바로 추가 피해를 주지 않고,");
        System.out.println("    '분노 폭발' 스킬 발동 시 추가 피해량을 결정하는 자원으로 사용된다.");
        System.out.println();
        System.out.println("- 스킬1: 분노 폭발 (Raging Blow)");
        System.out.println("  · 분노 스택이 7 이상이고, 쿨타임이 0일 때 공격 직전에 활성화된다.");
        System.out.println("  · 이번 공격으로 입힌 피해의 [현재 분노 스택 × 1%] 만큼 추가 피해를 준다.");
        System.out.println("    예) 10스택이면 기본 피해의 +10% 추가 피해.");
        System.out.println("  · 사용 후 스택을 모두 소모하고, 5턴 쿨타임에 들어간다.");
        System.out.println();
        System.out.println("- 스킬2: 근성 (Second Wind)");
        System.out.println("  · 전투 중 자신의 현재 HP가 처음으로 25% 이하로 떨어지는 순간 1회 발동한다.");
        System.out.println("    - 최대 체력의 15%를 즉시 회복한다.");
        System.out.println("    - 그때 받은 피해의 5%도 추가로 회복한다.");
        System.out.println("  · 위기 구간에서 한 번 버티고 역전 각을 만들 수 있는 생존기이다.");
        System.out.println("========================================");
    }

    public void printAssassinDetails() {
        System.out.println("========================================");
        System.out.println("[Assassin] – 짧은 턴 안에 승부 보는 폭딜 암살자");
        System.out.println("========================================");
        System.out.println("▶ 역할: 초반/중반 짧은 타이밍에 폭딜을 몰아 넣어 승부를 보는 캐릭터");
        System.out.println();
        System.out.println("- 패시브: 약점 포착 (Exposed Weakness)");
        System.out.println("  · 적의 현재 HP 비율이 30% 이하일 때,");
        System.out.println("    그 적을 공격하면 이번 공격으로 준 피해의 5%만큼 추가 피해를 한 번 더 준다.");
        System.out.println("  · 체력이 깎인 적을 마무리할 때 더 강해지는 마무리 전문가 느낌의 패시브다.");
        System.out.println();
        System.out.println("- 스킬1: 선제 일격 (Opening Strike)");
        System.out.println("  · 전투 전체를 통틀어, 이 암살자가 날리는 '첫 번째 공격'에만 발동한다.");
        System.out.println("  · 그 한 번의 공격에 대해, 준 피해의 40%만큼 추가 피해를 한 번 더 준다.");
        System.out.println("  · 전투당 1회만 발동하기 때문에, 첫 타이밍을 잡는 것이 매우 중요하다.");
        System.out.println();
        System.out.println("- 스킬2: 연막 (Shadow Evasion)");
        System.out.println("  · 자신이 피해를 받아 현재 HP 비율이 30% 이하로 떨어지는 순간,");
        System.out.println("    쿨타임이 0이라면 발동한다.");
        System.out.println("    - 그 공격으로 받은 피해의 30%를 즉시 회복하여 피해를 줄인다.");
        System.out.println("    - 이후 1턴 동안은, 맞을 때마다 받은 피해의 50%를 회복해 실질 피해를 크게 줄인다.");
        System.out.println("  · 발동 후 5턴 쿨타임에 들어가며, 완전 무적은 아니지만");
        System.out.println("    한 번은 위기를 버틸 수 있는 생존기 역할을 한다.");
        System.out.println("========================================");
    }

    public void printTankDetails() {
        System.out.println("========================================");
        System.out.println("[Tank] – 맞아도 안 죽는 방벽 + 반격");
        System.out.println("========================================");
        System.out.println("▶ 역할: 장기전, 피해 흡수와 반격에 특화된 방벽형 탱커");
        System.out.println();
        System.out.println("- 패시브: 철벽 (Heavy Armor)");
        System.out.println("  · 모든 피해에 대해 '고정 5 + 추가 5%' 만큼 피해를 감소시킨다.");
        System.out.println("    - 구현상: 받은 피해만큼 힐을 되돌려 주는 방식으로 근사.");
        System.out.println("    예) 40 피해를 받으면 5 + (40 × 5%) = 7만큼 회복 → 실질 피해 33.");
        System.out.println("  · 평타 위주의 딜러에게 특히 강한 구조다.");
        System.out.println();
        System.out.println("- 스킬1: 방어 자세 (Fortify)");
        System.out.println("  · 자기 턴 시작 시, 버프가 없고 쿨타임이 0이면 자동으로 발동한다.");
        System.out.println("    - 2턴 동안, 맞는 피해의 10%를 힐로 되돌려 받는다 (추가 피해 감소 효과).");
        System.out.println("  · 버프가 끝난 뒤에는 5턴 쿨타임에 들어간다.");
        System.out.println("  · 주기적으로 '딱딱해지는 타이밍'이 생겨, 이 구간에 딜을 넣기 까다롭다.");
        System.out.println();
        System.out.println("- 스킬2: 철의 반격 (Spiked Counter)");
        System.out.println("  · 현재 HP 비율이 50% 이상이고, 쿨타임이 0일 때 맞으면 발동한다.");
        System.out.println("    - 그때 받은 피해의 15%를 상대에게 고정 피해로 되돌려 준다.");
        System.out.println("  · 발동 후에는 3턴 쿨타임이 돈다.");
        System.out.println("  · 탱커를 초반에 마구 두들기면 오히려 손해를 볼 수 있는 구조를 만든다.");
        System.out.println("========================================");
    }

    public void printSpeedsterDetails() {
        System.out.println("========================================");
        System.out.println("[Speedster] – 턴 수 이득으로 압박하는 속도 메타 주도자");
        System.out.println("========================================");
        System.out.println("▶ 역할: 높은 SPD 덕분에 턴을 자주 가져가고,");
        System.out.println("         주기적인 추가 타격과 피해 감소로 압박하는 캐릭터");
        System.out.println();
        System.out.println("- 패시브: 가속 (Quick Footwork)");
        System.out.println("  · 속도를 실은 공격으로 공격이 명중할 때마다, 준 피해의 5%만큼 추가 피해를 한 번 더 준다.");
        System.out.println();
        System.out.println("- 스킬1: 더블 액션 (Quick Combo)");
        System.out.println("  · 이 캐릭터가 공격한 횟수를 세어, 조건을 만족하면 발동하는 추가 타격 스킬.");
        System.out.println("    - 공격할 때마다 내부적으로 공격 횟수가 1씩 증가한다.");
        System.out.println("    - 쿨타임이 0이고, 지금까지 두 번 이상 행동했다면 이번 공격에 대해");
        System.out.println("      준 피해의 10%를 추가 피해로 한 번 더 준다.");
        System.out.println("  · 발동 후에는 4턴 쿨타임이 돌아, '가끔씩 폭발하는' 느낌을 준다.");
        System.out.println();
        System.out.println("- 스킬2: 예측 회피 (Precise Dodge)");
        System.out.println("  · 한 번에 자신의 직전 HP의 25% 이상에 해당하는 큰 피해를 받았을 때,");
        System.out.println("    쿨타임이 0이면 발동한다.");
        System.out.println("    - 그 큰 피해의 5%를 즉시 회복하여 첫 타를 조금 줄이고,");
        System.out.println("    - 이후 '다음에 한 번 더 맞을 때' 그 피해의 5%를 다시 회복한다.");
        System.out.println("  · 발동 후에는 4턴 쿨타임에 들어가며,");
        System.out.println("    확률 회피 대신 '피해 일부를 되돌려 받는 형태'로 구현했다.");
        System.out.println("========================================");
    }

    public void printSustainerDetails() {
        System.out.println("========================================");
        System.out.println("[Sustainer] – 흡혈과 재생으로 오래 버티는 장기전형 파이터");
        System.out.println("========================================");
        System.out.println("▶ 역할: 지속적인 흡혈과 재생으로 교전이 길어질수록 이득을 보는 캐릭터");
        System.out.println();
        System.out.println("- 패시브: 생명 흡수 (Lifesteal)");
        System.out.println("  · 공격이 명중해 피해를 줄 때마다, 준 피해의 5%만큼 체력을 회복한다.");
        System.out.println("  · 기본 평타만 쳐도 시간이 지날수록 체력이 조금씩 올라가는 구조다.");
        System.out.println();
        System.out.println("- 스킬1: 재생 (Healing Aura)");
        System.out.println("  · 턴 시작 시, 다음 조건을 만족하면 재생 효과를 시작한다.");
        System.out.println("    - 현재 재생 버프가 없고, 재생 쿨타임이 0이며,");
        System.out.println("      현재 HP 비율이 30% 이하일 때.");
        System.out.println("    - 3턴 동안, 매 턴 시작 시 최대 체력의 5%만큼 회복한다.");
        System.out.println("  · 재생이 한 번 끝나면 5턴 쿨타임이 돈다.");
        System.out.println("  · 위기에서 천천히 체력을 끌어올리는 장기전형 힐 스킬이다.");
        System.out.println();
        System.out.println("- 스킬2: 흡혈 강화 (Blood Frenzy)");
        System.out.println("  · 공격 후, 현재 HP 비율이 30% 이하이고 쿨타임이 0이면 발동한다.");
        System.out.println("    - 이번 공격으로 준 피해의 15%를 추가로 회복한다.");
        System.out.println("  · 발동 후에는 4턴 쿨타임에 들어간다.");
        System.out.println("  · 재생(Healing Aura)와 함께 쓰이면, 체력이 낮을수록 '마지막 발악'을 할 수 있는 구조를 만든다.");
        System.out.println("========================================");
    }

    public void printHybridDetails() {
        System.out.println("========================================");
        System.out.println("[Hybrid] – 상황에 따라 공격/방어를 바꾸는 만능형");
        System.out.println("========================================");
        System.out.println("▶ 역할: 체력 상황에 따라 공격/방어 스탯이 자동으로 바뀌는 균형형 캐릭터");
        System.out.println();
        System.out.println("- 패시브: 균형 감각 (Balanced Instinct)");
        System.out.println("  · 현재 HP 비율이 50% 이상일 때 (건강한 상태):");
        System.out.println("    - 공격이 적중하면, 준 피해의 5%만큼 추가 피해를 한 번 더 준다. (공격 모드)");
        System.out.println("  · 현재 HP 비율이 50% 미만일 때 (위험 구간):");
        System.out.println("    - 피해를 받을 때마다, 받은 피해의 5%만큼 체력을 회복한다. (방어 모드)");
        System.out.println("  · 체력이 높을 때는 더 강하게 때리고, 낮을 때는 더 단단해지는 형태의 자동 전환 패시브다.");
        System.out.println();
        System.out.println("- 스킬1: 전술적 집중 (Tactical Focus)");
        System.out.println("  · 공격력/치명타를 올리는 버프형 스킬");
        System.out.println();
        System.out.println("- 스킬2: 약점 분석 (Expose Pattern)");
        System.out.println("  · 같은 적을 계속 공격할수록, 그 패턴을 읽어 추가 피해를 준다.");
        System.out.println("    -주기적으로 +10% 추가 피해를 준다'.");
        System.out.println("  · 구현상:");
        System.out.println("    - 자신의 공격 횟수를 누적해서 카운트한다.");
        System.out.println("    - 쿨타임이 0인 상태에서, 두 번째 공격 이후부터 조건이 충족되면 이번 공격에 대해");
        System.out.println("      준 피해의 10%만큼 추가 피해를 준다.");
        System.out.println("    - 발동 후에는 3턴 쿨타임이 돌아, 일정 주기마다 '방어 무시 느낌의 한 방'이 나온다.");
        System.out.println("========================================");
    }
}
