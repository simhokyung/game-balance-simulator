package balance.skill;

import balance.battle.BattleCharacter;
import balance.domain.Character;
import balance.skill.hybrid.HybridBalancedInstinctSkill;
import balance.skill.hybrid.HybridExposePatternSkill;
import balance.skill.hybrid.HybridTacticalFocusSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HybridSkillsTest {

    @Test
    void 균형감각은_HP50퍼이상일때_공격추가피해를준다() {
        Character hybridChar = new Character("Hybrid", 100, 46, 20, 22, 0.16);
        Character targetChar = new Character("Target", 200, 40, 10, 10, 0.0);

        BattleCharacter hybrid = BattleCharacter.from(hybridChar);
        BattleCharacter target = BattleCharacter.from(targetChar);

        HybridBalancedInstinctSkill skill = new HybridBalancedInstinctSkill();
        SkillContext context = new SkillContext(hybrid, target);

        int baseDamage = 50;
        int startHp = target.getCurrentHp();

        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);

        int extra = (int) Math.round(baseDamage * 0.05);
        int expectedHp = startHp - baseDamage - extra;
        assertEquals(expectedHp, target.getCurrentHp());
    }

    @Test
    void 균형감각은_HP50퍼미만일때_피해일부를되돌려준다() {
        Character hybridChar = new Character("Hybrid", 100, 46, 20, 22, 0.16);
        Character attackerChar = new Character("Attacker", 200, 40, 10, 10, 0.0);

        BattleCharacter hybrid = BattleCharacter.from(hybridChar);
        BattleCharacter attacker = BattleCharacter.from(attackerChar);

        HybridBalancedInstinctSkill skill = new HybridBalancedInstinctSkill();
        SkillContext context = new SkillContext(hybrid, attacker);

        // 먼저 체력을 45으로 만들어 HP<50%
        hybrid.takeDamage(55);

        int startHp = hybrid.getCurrentHp();
        int damage = 50;

        hybrid.takeDamage(damage);
        skill.onDamaged(context, damage);

        int heal = (int) Math.round(damage * 0.10);
        int expectedHp = startHp - damage + heal;
        assertEquals(expectedHp, hybrid.getCurrentHp());
    }

    @Test
    void 전술적집중이활성화되면_공격추가피해가적용된다() {
        Character hybridChar = new Character("Hybrid", 100, 46, 20, 22, 0.16);
        Character targetChar = new Character("Target", 200, 40, 10, 10, 0.0);

        BattleCharacter hybrid = BattleCharacter.from(hybridChar);
        BattleCharacter target = BattleCharacter.from(targetChar);

        HybridTacticalFocusSkill skill = new HybridTacticalFocusSkill();
        SkillContext context = new SkillContext(hybrid, target);

        // 첫 턴 시작 → 버프 발동
        skill.onTurnStart(context);

        int baseDamage = 50;
        int startHp = target.getCurrentHp();

        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);

        int extra = (int) Math.round(baseDamage * 0.20);
        int expectedHp = startHp - baseDamage - extra;
        assertEquals(expectedHp, target.getCurrentHp());
    }

    @Test
    void 약점분석은_두번째연속공격에서_추가피해를준다() {
        Character hybridChar = new Character("Hybrid", 100, 46, 20, 22, 0.16);
        Character targetChar = new Character("Target", 300, 40, 10, 10, 0.0);

        BattleCharacter hybrid = BattleCharacter.from(hybridChar);
        BattleCharacter target = BattleCharacter.from(targetChar);

        HybridExposePatternSkill skill = new HybridExposePatternSkill();
        SkillContext context = new SkillContext(hybrid, target);

        int baseDamage = 40;

        // 첫 번째 공격
        int hp1 = target.getCurrentHp();
        skill.onTurnStart(context);
        skill.onBeforeAttack(context);
        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);
        assertEquals(hp1 - baseDamage, target.getCurrentHp());

        // 두 번째 공격
        int hp2 = target.getCurrentHp();
        skill.onTurnStart(context);
        skill.onBeforeAttack(context);
        target.takeDamage(baseDamage);
        skill.onAfterAttack(context, baseDamage);

        int extra = (int) Math.round(baseDamage * 0.10);
        int expectedHp2 = hp2 - baseDamage - extra;
        assertEquals(expectedHp2, target.getCurrentHp());
    }
}
