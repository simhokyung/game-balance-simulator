package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.bruiser.BruiserRagePassiveSkill;
import balance.skill.bruiser.BruiserRageState;
import balance.skill.bruiser.BruiserRagingBlowSkill;
import balance.skill.bruiser.BruiserSecondWindSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BruiserSkillTest {

    @Test
    void 피해를_받을_때마다_분노_스택이_최대치까지_증가한다() {
        Character bruiserChar = new Character("Bruiser", 100, 40, 20, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter bruiser = BattleCharacter.from(bruiserChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        BruiserRageState rageState = new BruiserRageState();
        BruiserRagePassiveSkill ragePassive = new BruiserRagePassiveSkill(rageState);

        SkillContext context = new SkillContext(bruiser, dummy);

        // 15번 맞아도 스택은 최대 10까지
        for (int i = 0; i < 15; i++) {
            bruiser.takeDamage(1);
            ragePassive.onDamaged(context, 1);
        }

        assertEquals(10, rageState.getRageStacks(), "분노 스택은 최대 10까지 쌓여야 한다.");
    }

    @Test
    void 분노_폭발이_발동되면_추가_피해가_들어간다() {
        Character bruiserChar = new Character("Bruiser", 100, 40, 20, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter bruiser = BattleCharacter.from(bruiserChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        BruiserRageState rageState = new BruiserRageState();
        BruiserRagePassiveSkill ragePassive = new BruiserRagePassiveSkill(rageState);
        BruiserRagingBlowSkill ragingBlow = new BruiserRagingBlowSkill(rageState);

        SkillContext context = new SkillContext(bruiser, dummy);

        // 피해를 8번 입어서 스택 8까지 쌓기 (요구 스택 7 이상)
        for (int i = 0; i < 8; i++) {
            bruiser.takeDamage(1);
            ragePassive.onDamaged(context, 1);
        }
        assertEquals(8, rageState.getRageStacks(), "테스트 준비: 분노 스택이 8이어야 한다.");

        // 턴 시작 처리 (쿨타임 감소/플래그 리셋)
        ragingBlow.onTurnStart(context);

        // 공격 직전: 분노 폭발 발동 여부 결정
        ragingBlow.onBeforeAttack(context);

        int opponentHpBefore = dummy.getCurrentHp();

        // 이번 공격으로 기본 피해가 50 들어갔다고 가정하고, 스킬에 전달
        // 분노 폭발: 스택당 1% → 8스택이면 8% → 추가 피해 ≒ 4
        int baseDamage = 50;
        ragingBlow.onAfterAttack(context, baseDamage);

        int opponentHpAfter = dummy.getCurrentHp();
        int extraDamage = opponentHpBefore - opponentHpAfter;

        // 우리가 onAfterAttack에서 준 건 '추가 피해'뿐이므로,
        // 추가 피해가 0보다 크고, 대략 4 근처여야 정상.
        assertTrue(extraDamage > 0, "분노 폭발로 추가 피해가 들어가야 한다.");
        assertEquals(4, extraDamage, "8스택일 때 추가 피해는 4(≈ 50 * 0.08)여야 한다.");

        // 사용 후에는 스택이 초기화되어야 한다.
        assertEquals(0, rageState.getRageStacks(), "분노 폭발 사용 후 스택은 0이 되어야 한다.");
    }

    @Test
    void 체력이_25퍼센트_이하로_처음_떨어지면_근성이_발동한다() {
        Character bruiserChar = new Character("Bruiser", 100, 40, 20, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter bruiser = BattleCharacter.from(bruiserChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        BruiserSecondWindSkill secondWind = new BruiserSecondWindSkill();
        SkillContext context = new SkillContext(bruiser, dummy);

        // 시작 HP 100 → 80 피해 입혀서 20 (20% ≤ 25% 임계값)
        bruiser.takeDamage(80);
        assertEquals(20, bruiser.getCurrentHp());

        secondWind.onDamaged(context, 80);

        // 근성:
        // - 최대 HP 100의 15% = 15 회복
        // - 받은 피해 80의 5% = 4 회복
        // -> 20 + 15 + 4 = 39
        assertEquals(39, bruiser.getCurrentHp(), "근성이 발동되어 HP가 39가 되어야 한다.");

        int hpAfterFirstTrigger = bruiser.getCurrentHp();

        // 다음 턴 시작: 피해 감소 효과 턴 수 감소
        secondWind.onTurnStart(context);

        // 다시 큰 피해를 받아도, 이미 한 번 발동했으므로
        // 추가 큰 힐은 없어야 한다.
        bruiser.takeDamage(30); // 39 → 9
        secondWind.onDamaged(context, 30);

        int hpAfterSecondHit = bruiser.getCurrentHp();

        // 두 번째 큰 피해 이후에는 그냥 30 깎인 상태(혹은 아주 약간의 회복)여야 함
        // 여기서는 onTurnStart에서 피해감소 턴을 0으로 만들었으므로
        // 추가 회복 없이 9가 되어야 한다.
        assertEquals(hpAfterFirstTrigger - 30, hpAfterSecondHit,
                "근성은 한 번만 크게 발동해야 한다.");
    }
}
