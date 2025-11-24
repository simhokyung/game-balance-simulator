package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.tank.TankFortifySkill;
import balance.skill.tank.TankHeavyArmorSkill;
import balance.skill.tank.TankSpikedCounterSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankSkillsTest {

    @Test
    void 철벽은_피해를받았을때_일정량을되돌려준다() {
        // given
        Character tankChar = new Character("Tank", 100, 35, 38, 19, 0.05);
        Character attackerChar = new Character("Attacker", 100, 50, 10, 20, 0.0);

        BattleCharacter tank = BattleCharacter.from(tankChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        TankHeavyArmorSkill skill = new TankHeavyArmorSkill();
        SkillContext context = new SkillContext(tank, attacker);

        int startHp = tank.getCurrentHp();
        int damage = 40;

        // when: 실제 피해를 먼저 입힌 후, 스킬이 발동해 일부를 되돌림
        tank.takeDamage(damage);
        skill.onDamaged(context, damage);

        // then
        int reductionByFlat = 5;
        int reductionByRatio = (int) Math.round(damage * 0.05); // 2
        int totalReduction = reductionByFlat + reductionByRatio; // 7
        int healAmount = Math.min(damage, totalReduction);       // 7

        int expectedHp = startHp - damage + healAmount;          // 100 - 40 + 7 = 67
        assertEquals(expectedHp, tank.getCurrentHp());
    }

    @Test
    void 방어자세가_발동중일때는_추가피해감쇄가적용된다() {
        // given
        Character tankChar = new Character("Tank", 100, 35, 38, 19, 0.05);
        Character attackerChar = new Character("Attacker", 100, 50, 10, 20, 0.0);

        BattleCharacter tank = BattleCharacter.from(tankChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        TankFortifySkill skill = new TankFortifySkill();
        SkillContext context = new SkillContext(tank, attacker);

        // 첫 턴 시작 → 방어 자세 발동 (2턴 지속)
        skill.onTurnStart(context);

        int startHp = tank.getCurrentHp();
        int damage = 40;

        // when
        tank.takeDamage(damage);
        skill.onDamaged(context, damage);

        // then: 10% 만큼 되돌려야 함
        int healAmount = (int) Math.round(damage * 0.10); // 4
        int expectedHp = startHp - damage + healAmount;
        assertEquals(expectedHp, tank.getCurrentHp());
    }

    @Test
    void 방어자세는_지속턴이끝나면_추가감쇄가없어진다() {
        // given
        Character tankChar = new Character("Tank", 100, 35, 38, 19, 0.05);
        Character attackerChar = new Character("Attacker", 100, 50, 10, 20, 0.0);

        BattleCharacter tank = BattleCharacter.from(tankChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        TankFortifySkill skill = new TankFortifySkill();
        SkillContext context = new SkillContext(tank, attacker);

        // 첫 턴: 버프 시작
        skill.onTurnStart(context);
        // 두 번 더 턴을 보낸다 → 지속 턴 소모
        skill.onTurnStart(context);
        skill.onTurnStart(context);

        int startHp = tank.getCurrentHp();
        int damage = 40;

        // when
        tank.takeDamage(damage);
        skill.onDamaged(context, damage);

        // then: 방어자세 버프가 끝났으므로 추가 힐 없음
        int expectedHp = startHp - damage;
        assertEquals(expectedHp, tank.getCurrentHp());
    }

    @Test
    void 철의반격은_HP50퍼이상이고_쿨타임이없을때_피해의일부를반사한다() {
        // given
        Character tankChar = new Character("Tank", 1000, 35, 38, 19, 0.05);
        Character attackerChar = new Character("Attacker", 1000, 50, 10, 20, 0.0);

        BattleCharacter tank = BattleCharacter.from(tankChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        TankSpikedCounterSkill skill = new TankSpikedCounterSkill();
        SkillContext context = new SkillContext(tank, attacker);

        int attackerStartHp = attacker.getCurrentHp();

        int damage = 200; // 1000 -> 800
        tank.takeDamage(damage);

        // when
        skill.onDamaged(context, damage);

        // then: 기대값 기준 15% 반사
        int expectedReflect = (int) Math.round(damage * 0.15); // 30
        assertEquals(attackerStartHp - expectedReflect, attacker.getCurrentHp());
    }

    @Test
    void 철의반격은_HP가50퍼미만이면발동하지않는다() {
        // given
        Character tankChar = new Character("Tank", 1000, 35, 38, 19, 0.05);
        Character attackerChar = new Character("Attacker", 1000, 50, 10, 20, 0.0);

        BattleCharacter tank = BattleCharacter.from(tankChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        TankSpikedCounterSkill skill = new TankSpikedCounterSkill();
        SkillContext context = new SkillContext(tank, attacker);

        // HP를 400까지 떨어뜨려서 50% 미만으로 만들어둔다.
        tank.takeDamage(600); // 1000 -> 400

        int attackerStartHp = attacker.getCurrentHp();

        int damage = 100;
        tank.takeDamage(damage);
        skill.onDamaged(context, damage);

        // then: 반사 없음
        assertEquals(attackerStartHp, attacker.getCurrentHp());
    }
}
