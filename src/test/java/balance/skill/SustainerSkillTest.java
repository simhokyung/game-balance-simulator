package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.sustainer.SustainerBloodFrenzySkill;
import balance.skill.sustainer.SustainerLifestealSkill;
import balance.skill.sustainer.SustainerRegenSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SustainerSkillTest {

    @Test
    void 공격_후_기본_흡혈로_체력이_소량_회복된다() {
        Character sustainerChar = new Character("Sustainer", 100, 30, 15, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter sustainer = BattleCharacter.from(sustainerChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        // 일부러 체력을 깎아둔다.
        sustainer.takeDamage(50); // 100 → 50

        SustainerLifestealSkill lifesteal = new SustainerLifestealSkill();
        SkillContext context = new SkillContext(sustainer, dummy);

        // 이번 공격으로 80 피해를 줬다고 가정
        lifesteal.onAfterAttack(context, 80);

        // 패시브 흡혈 5% → 80 * 0.05 = 4
        assertEquals(54, sustainer.getCurrentHp(), "기본 흡혈로 4 만큼 회복해야 한다.");
    }

    @Test
    void 체력이_임계값_이하일_때_재생이_턴_시작에_회복을_적용한다() {
        Character sustainerChar = new Character("Sustainer", 100, 30, 15, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter sustainer = BattleCharacter.from(sustainerChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        // HP를 30으로 만들어서 (30%) 재생 조건을 만족시키자.
        sustainer.takeDamage(70); // 100 → 30

        SustainerRegenSkill regenSkill = new SustainerRegenSkill();
        SkillContext context = new SkillContext(sustainer, dummy);

        // 첫 턴: 재생 버프 시작(내부 카운터만 설정, 아직 회복 없음)
        regenSkill.onTurnStart(context);
        assertEquals(30, sustainer.getCurrentHp(), "재생 시작 턴에는 아직 회복이 일어나지 않는다.");

        // 두 번째 턴: 재생 효과로 회복 5 (최대 HP 100의 5%)
        regenSkill.onTurnStart(context);
        assertEquals(35, sustainer.getCurrentHp(), "두 번째 턴 시작 시 재생으로 HP가 35가 되어야 한다.");
    }

    @Test
    void 체력이_임계값_이하일_때_흡혈_강화로_추가_회복을_얻는다() {
        Character sustainerChar = new Character("Sustainer", 100, 30, 15, 10, 0.0);
        Character dummyChar = new Character("Dummy", 100, 10, 5, 5, 0.0);

        BattleCharacter sustainer = BattleCharacter.from(sustainerChar);
        BattleCharacter dummy = BattleCharacter.from(dummyChar);

        // HP를 20으로 낮춰서 (20%) 흡혈 강화 조건(BLOOD_FRENZY_THRESHOLD=30%) 만족
        sustainer.takeDamage(80); // 100 → 20

        SustainerBloodFrenzySkill bloodFrenzy = new SustainerBloodFrenzySkill();
        SkillContext context = new SkillContext(sustainer, dummy);

        // 이번 공격으로 100 피해를 줬다고 가정
        bloodFrenzy.onAfterAttack(context, 100);

        // 추가 흡혈 15% → 100 * 0.15 = 15
        assertEquals(35, sustainer.getCurrentHp(), "흡혈 강화로 15 만큼 추가 회복해야 한다.");

        int hpAfterFirst = sustainer.getCurrentHp();

        // 바로 한 번 더 써도, 쿨타임 때문에 두 번째는 발동하지 않아야 함
        bloodFrenzy.onAfterAttack(context, 100);
        assertEquals(hpAfterFirst, sustainer.getCurrentHp(),
                "쿨타임 동안에는 흡혈 강화가 다시 발동하면 안 된다.");
    }
}
