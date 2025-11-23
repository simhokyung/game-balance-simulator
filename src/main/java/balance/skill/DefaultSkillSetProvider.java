package balance.skill;

import balance.domain.Character;

import java.util.Collections;
import java.util.List;

/**
 * 기본 스킬 제공자.
 * - 현재는 모든 캐릭터에 대해 "스킬 없음" (빈 리스트)을 반환한다.
 * - 이렇게 하면 스킬 시스템을 도입해도 기존 전투 결과가 전혀 바뀌지 않는다.
 */
public class DefaultSkillSetProvider implements SkillSetProvider {

    @Override
    public List<Skill> getSkillsFor(Character character) {
        return Collections.emptyList();
    }
}
