package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.speedster.SpeedsterPreciseDodgeSkill;
import balance.skill.speedster.SpeedsterQuickComboSkill;
import balance.skill.speedster.SpeedsterQuickFootworkSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpeedsterSkillsTest {

    @Test
    void 가속은_공격후추가피해를준다() {
        Character spdChar = new Character("Speedster", 100, 39, 15, 32, 0.15);
        Character targetChar = new Character("Target", 200, 40, 10, 10, 0.0);

        BattleCharacter speedster = BattleCharacter.from(spdChar);
        BattleCharacter target = BattleCharacter.from(targetChar);

        SpeedsterQuickFootworkSkill skill = new SpeedsterQuickFootworkSkill();
        SkillContext context = new SkillContext(speedster, target);

        int baseDamage = 100;
        int startHp = target.getCurrentHp();

        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);

        int expectedExtra = (int) Math.round(baseDamage * 0.05);
        int expectedHp = startHp - baseDamage - expectedExtra;

        assertEquals(expectedHp, target.getCurrentHp());
    }

    @Test
    void 퀵콤보는_두번째공격에서_추가피해를준다() {
        Character spdChar = new Character("Speedster", 100, 39, 15, 32, 0.15);
        Character targetChar = new Character("Target", 300, 40, 10, 10, 0.0);

        BattleCharacter speedster = BattleCharacter.from(spdChar);
        BattleCharacter target = BattleCharacter.from(targetChar);

        SpeedsterQuickComboSkill skill = new SpeedsterQuickComboSkill();
        SkillContext context = new SkillContext(speedster, target);

        int baseDamage = 50;

        // 첫 번째 공격
        int hp1 = target.getCurrentHp();
        skill.onTurnStart(context);
        skill.onBeforeAttack(context);
        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);
        int afterFirst = target.getCurrentHp();
        // 첫 타에는 추가 피해 없음
        assertEquals(hp1 - baseDamage, afterFirst);

        // 두 번째 공격
        int hp2 = target.getCurrentHp();
        skill.onTurnStart(context);
        skill.onBeforeAttack(context);
        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);
        int expectedExtra = (int) Math.round(baseDamage * 0.10);
        int expectedHp2 = hp2 - baseDamage - expectedExtra;
        assertEquals(expectedHp2, target.getCurrentHp());
    }

    @Test
    void 예측회피는_큰피해를받을때_일부를되돌려준다() {
        Character spdChar = new Character("Speedster", 200, 39, 15, 32, 0.15);
        Character attackerChar = new Character("Attacker", 200, 50, 10, 10, 0.0);

        BattleCharacter speedster = BattleCharacter.from(spdChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        SpeedsterPreciseDodgeSkill skill = new SpeedsterPreciseDodgeSkill();
        SkillContext context = new SkillContext(speedster, attacker);

        int startHp = speedster.getCurrentHp();
        int damage = 60; // 60 / 200 = 0.3 → 25% 이상

        speedster.takeDamage(damage);
        skill.onDamaged(context, damage);

        int healNow = (int) Math.round(damage * 0.05); // 18
        int expectedHp = startHp - damage + healNow;

        assertEquals(expectedHp, speedster.getCurrentHp());
    }

    @Test
    void 예측회피는_그다음한번더맞을때추가감쇄를적용한다() {
        Character spdChar = new Character("Speedster", 200, 39, 15, 32, 0.15);
        Character attackerChar = new Character("Attacker", 200, 50, 10, 10, 0.0);

        BattleCharacter speedster = BattleCharacter.from(spdChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        SpeedsterPreciseDodgeSkill skill = new SpeedsterPreciseDodgeSkill();
        SkillContext context = new SkillContext(speedster, attacker);

        int startHp = speedster.getCurrentHp();

        // 첫 큰 피해
        int firstDamage = 60;
        speedster.takeDamage(firstDamage);
        skill.onDamaged(context, firstDamage);
        int healNow = (int) Math.round(firstDamage * 0.05);
        int afterFirst = startHp - firstDamage + healNow;

        // 다음 피해 (버프 1회 소모)
        int secondDamage = 40;
        speedster.takeDamage(secondDamage);
        skill.onDamaged(context, secondDamage);
        int healNext = (int) Math.round(secondDamage * 0.05);
        int expected = afterFirst - secondDamage + healNext;

        assertEquals(expected, speedster.getCurrentHp());
    }
}
