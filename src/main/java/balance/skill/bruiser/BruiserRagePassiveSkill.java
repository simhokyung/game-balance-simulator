package balance.skill.bruiser;

import balance.battle.BattleCharacter;
import balance.skill.Skill;
import balance.skill.SkillContext;

/**
 * 브루저 패시브 스킬 - 분노 축적(Rage Passive)
 *
 * - 피해를 받을 때마다 분노 스택이 1씩 증가한다. (최대 10스택)
 * - 실제 추가 피해 계산/발동은 BruiserRagingBlowSkill 이 담당한다.
 */
public class BruiserRagePassiveSkill implements Skill {

    private final BruiserRageState state;

    public BruiserRagePassiveSkill(BruiserRageState state) {
        if (state == null) {
            throw new IllegalArgumentException("BruiserRageState 는 null일 수 없습니다.");
        }
        this.state = state;
    }

    @Override
    public void onDamaged(SkillContext context, int damageTaken) {
        if (damageTaken <= 0) {
            return;
        }

        BattleCharacter self = context.getSelf();
        if (self.isDead()) {
            return;
        }

        state.addRageStack();
    }
}
