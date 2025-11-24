package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.SkillContext;
import balance.skill.assassin.AssassinExposedWeaknessSkill;
import balance.skill.assassin.AssassinOpeningStrikeSkill;
import balance.skill.assassin.AssassinShadowEvasionSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssassinSkillsTest {

    @Test
    void 약점포착은상대HP가50퍼이하일때추가피해를준다() {
        // given
        Character assassin = new Character("Assassin", 100, 50, 10, 20, 0.2);
        Character dummy = new Character("Dummy", 200, 1, 1, 1, 0.0); // 전부 1 이상

        BattleCharacter self = BattleCharacter.from(assassin);
        BattleCharacter opponent = BattleCharacter.from(dummy);

        AssassinExposedWeaknessSkill skill = new AssassinExposedWeaknessSkill();

        // 상대 HP를 50%까지 깎아놓는다 (200 → 100)
        opponent.takeDamage(100);
        int hpBefore = opponent.getCurrentHp();
        assertEquals(100, hpBefore);

        SkillContext context = new SkillContext(self, opponent);

        // when
        int baseDamage = 100;
        skill.onAfterAttack(context, baseDamage);

        // then: 추가 피해 10% → 10
        int expectedExtra = (int) Math.round(baseDamage * 0.10);
        assertEquals(hpBefore - expectedExtra, opponent.getCurrentHp());
    }

    @Test
    void 약점포착은상대HP가50퍼초과일땐발동하지않는다() {
        // given
        Character assassin = new Character("Assassin", 100, 50, 10, 20, 0.2);
        Character dummy = new Character("Dummy", 200, 1, 1, 1, 0.0);

        BattleCharacter self = BattleCharacter.from(assassin);
        BattleCharacter opponent = BattleCharacter.from(dummy);

        AssassinExposedWeaknessSkill skill = new AssassinExposedWeaknessSkill();

        // 상대 HP를 60% 근처로 맞춰놓기 (200 → 120)
        opponent.takeDamage(80);
        int hpBefore = opponent.getCurrentHp();
        assertEquals(120, hpBefore);

        SkillContext context = new SkillContext(self, opponent);

        // when
        skill.onAfterAttack(context, 100);

        // then: 추가 피해 없음
        assertEquals(hpBefore, opponent.getCurrentHp());
    }

    @Test
    void 선제일격은전투첫공격에만추가피해를준다() {
        // given
        Character assassin = new Character("Assassin", 100, 50, 10, 20, 0.2);
        Character dummy = new Character("Dummy", 500, 1, 1, 1, 0.0);

        BattleCharacter self = BattleCharacter.from(assassin);
        BattleCharacter opponent = BattleCharacter.from(dummy);

        AssassinOpeningStrikeSkill skill = new AssassinOpeningStrikeSkill();
        int baseDamage = 100;
        int startHp = opponent.getCurrentHp(); // 500

        // --- 첫 공격 ---
        SkillContext firstContext = new SkillContext(self, opponent, true);
        skill.onTurnStart(firstContext);
        skill.onBeforeAttack(firstContext);

        opponent.takeDamage(baseDamage);
        skill.onAfterAttack(firstContext, baseDamage);

        int expectedAfterFirst =
                startHp - baseDamage - (int) Math.round(baseDamage * 0.40);
        assertEquals(expectedAfterFirst, opponent.getCurrentHp(),
                "첫 공격에는 +40% 추가 피해가 적용되어야 한다.");

        // --- 두 번째 공격 ---
        SkillContext secondContext = new SkillContext(self, opponent, false);
        skill.onTurnStart(secondContext);
        skill.onBeforeAttack(secondContext);

        int hpBeforeSecond = opponent.getCurrentHp();
        opponent.takeDamage(baseDamage);
        skill.onAfterAttack(secondContext, baseDamage);

        assertEquals(hpBeforeSecond - baseDamage, opponent.getCurrentHp(),
                "두 번째 공격부터는 추가 피해가 없어야 한다.");
    }

    @Test
    void 연막은HP가30퍼이하로떨어지는피해를받으면발동해피해일부를되돌린다() {
        // given
        Character assassinChar = new Character("Assassin", 1000, 50, 10, 20, 0.2);
        Character attackerChar = new Character("Attacker", 1000, 50, 10, 20, 0.0);

        BattleCharacter assassin = BattleCharacter.from(assassinChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        AssassinShadowEvasionSkill skill = new AssassinShadowEvasionSkill();

        // 1000 → 400
        assassin.takeDamage(600);
        SkillContext context = new SkillContext(assassin, attacker);

        int damageTaken = 200; // 400 → 200 (20%)
        assassin.takeDamage(damageTaken);
        int hpAfterRawDamage = assassin.getCurrentHp(); // 200

        // when
        skill.onDamaged(context, damageTaken);

        // then: 30% 회복
        int expectedAfterSkill =
                hpAfterRawDamage + (int) Math.round(damageTaken * 0.30);
        assertEquals(expectedAfterSkill, assassin.getCurrentHp(),
                "연막 발동 시 첫 히트에서 30% 피해를 되돌려야 한다.");
    }

    @Test
    void 연막버프가유지되는동안추가피해감쇄가적용된다() {
        // given
        Character assassinChar = new Character("Assassin", 1000, 50, 10, 20, 0.2);
        Character attackerChar = new Character("Attacker", 1000, 50, 10, 20, 0.0);

        BattleCharacter assassin = BattleCharacter.from(assassinChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        AssassinShadowEvasionSkill skill = new AssassinShadowEvasionSkill();

        // 1) 트리거 구간까지 HP 감소
        assassin.takeDamage(600); // 1000 → 400
        SkillContext context = new SkillContext(assassin, attacker);

        // 2) 첫 히트로 연막 발동
        int firstHit = 200; // 400 → 200
        assassin.takeDamage(firstHit);
        skill.onDamaged(context, firstHit);

        int hpAfterFirstSkill = assassin.getCurrentHp();
        assertTrue(hpAfterFirstSkill > 200);

        // 3) 자기 턴 오기 전에 한 번 더 맞음 (onTurnStart 호출 X)
        int secondHit = 100;
        assassin.takeDamage(secondHit);
        int hpAfterRawSecond = assassin.getCurrentHp();

        // when
        skill.onDamaged(context, secondHit);

        // then: 50% 회복
        int expectedAfterSecond =
                hpAfterRawSecond + (int) Math.round(secondHit * 0.50);
        assertEquals(expectedAfterSecond, assassin.getCurrentHp(),
                "연막 버프가 유지되는 동안은 추가로 50% 피해를 되돌려야 한다.");
    }

    @Test
    void 연막버프는자신턴이한번돌아오면사라진다() {
        // given
        Character assassinChar = new Character("Assassin", 1000, 50, 10, 20, 0.2);
        Character attackerChar = new Character("Attacker", 1000, 50, 10, 20, 0.0);

        BattleCharacter assassin = BattleCharacter.from(assassinChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        AssassinShadowEvasionSkill skill = new AssassinShadowEvasionSkill();

        // 1) 트리거 구간까지 HP 감소
        assassin.takeDamage(600); // 1000 → 400
        SkillContext context = new SkillContext(assassin, attacker);

        // 2) 첫 히트로 연막 발동
        int firstHit = 200; // 400 → 200
        assassin.takeDamage(firstHit);
        skill.onDamaged(context, firstHit);

        // 3) 자신의 턴이 한 번 지나감
        skill.onTurnStart(context);

        // 4) 다시 피해
        int secondHit = 100;
        assassin.takeDamage(secondHit);
        int hpAfterRawSecond = assassin.getCurrentHp();

        // when
        skill.onDamaged(context, secondHit);

        // then: 추가 회복 없어야 함
        assertEquals(hpAfterRawSecond, assassin.getCurrentHp(),
                "자신의 턴이 한 번 지난 이후에는 연막 버프가 사라져야 한다.");
    }
}
