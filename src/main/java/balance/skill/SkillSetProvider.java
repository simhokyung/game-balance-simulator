package balance.skill;

import balance.domain.Character;

import java.util.List;

/**
 * 주어진 캐릭터가 전투에서 사용할 스킬 목록을 제공하는 역할.
 *
 * - 기본 구현(DefaultSkillSetProvider)은 항상 빈 리스트를 반환한다.
 * - 나중에 Preset 전용 구현을 만들어서
 *   이름/타입에 따라 각 캐릭터의 스킬 세트를 돌려주도록 확장할 예정이다.
 */
public interface SkillSetProvider {

    /**
     * 주어진 캐릭터가 전투에서 사용할 스킬 목록을 반환한다.
     * 절대 null을 반환하지 말고, 스킬이 없으면 빈 리스트를 반환해야 한다.
     */
    List<Skill> getSkillsFor(Character character);
}
