package balance.skill;

import balance.domain.Character;
import balance.skill.bruiser.BruiserRagePassiveSkill;
import balance.skill.bruiser.BruiserRageState;
import balance.skill.bruiser.BruiserRagingBlowSkill;
import balance.skill.bruiser.BruiserSecondWindSkill;
import balance.skill.sustainer.SustainerBloodFrenzySkill;
import balance.skill.sustainer.SustainerLifestealSkill;
import balance.skill.sustainer.SustainerRegenSkill;

import java.util.Collections;
import java.util.List;

/**
 * 프리셋 캐릭터 이름에 따라 스킬 세트를 부여하는 구현.
 *
 * 현재는:
 * - "Bruiser" : 분노 패시브 + 분노 폭발 + 근성(Second Wind)
 * - "Sustainer" : 기본 흡혈 + 재생 + 흡혈 강화
 * - 그 외 캐릭터 : 스킬 없음(빈 리스트)
 *
 * 이후 Assassin / Tank / Speedster / Hybrid 용 스킬을
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

        // ✅ Bruiser: 분노 스택 패시브 + 분노 폭발 액티브 + 근성(Second Wind)
        if ("Bruiser".equalsIgnoreCase(name)) {
            BruiserRageState rageState = new BruiserRageState();
            return List.of(
                    new BruiserRagePassiveSkill(rageState),
                    new BruiserRagingBlowSkill(rageState),
                    new BruiserSecondWindSkill()
            );
        }

        // ✅ Sustainer: Lifesteal 패시브 + 재생 + 흡혈 강화
        if ("Sustainer".equalsIgnoreCase(name)) {
            return List.of(
                    new SustainerLifestealSkill(),
                    new SustainerRegenSkill(),
                    new SustainerBloodFrenzySkill()
            );
        }

        // TODO: Assassin / Tank / Speedster / Hybrid 스킬은 이후 단계에서 추가
        return Collections.emptyList();
    }
}
