package balance.skill;

import balance.domain.Character;
import balance.skill.bruiser.BruiserFortitudePassive;
import balance.skill.bruiser.BruiserLastStandSkill;
import balance.skill.bruiser.BruiserRallyStrikeSkill;

import java.util.Collections;
import java.util.List;

/**
 * 프리셋 캐릭터 이름에 따라 스킬 세트를 부여하는 구현.
 *
 * 현재는:
 * - 이름이 "Bruiser"인 캐릭터에게만 브루저 전용 스킬 3개를 부여한다.
 * - 나머지 캐릭터는 스킬 없음(빈 리스트).
 *
 * 이후 Assassin / Tank / Speedster / Sustainer / Hybrid용 스킬을
 * 순차적으로 추가할 예정이다.
 */
public class PresetSkillSetProvider implements SkillSetProvider {

    @Override
    public List<Skill> getSkillsFor(Character character) {
        if (character == null) {
            return Collections.emptyList();
        }

        String name = character.getName();
        if (name == null) {
            return Collections.emptyList();
        }

        // 이름으로 매칭 (대소문자 구분 없이)
        if ("bruiser".equalsIgnoreCase(name)) {
            return List.of(
                    new BruiserFortitudePassive(),
                    new BruiserRallyStrikeSkill(),
                    new BruiserLastStandSkill()
            );
        }

        // TODO: Assassin / Tank / Speedster / Sustainer / Hybrid 스킬 연결 예정
        return Collections.emptyList();
    }
}
