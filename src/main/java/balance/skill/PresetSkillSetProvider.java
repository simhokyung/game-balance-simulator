package balance.skill;

import balance.domain.Character;
import balance.skill.assassin.AssassinExposedWeaknessSkill;
import balance.skill.assassin.AssassinOpeningStrikeSkill;
import balance.skill.assassin.AssassinShadowEvasionSkill;
import balance.skill.bruiser.BruiserRagePassiveSkill;
import balance.skill.bruiser.BruiserRageState;
import balance.skill.bruiser.BruiserRagingBlowSkill;
import balance.skill.bruiser.BruiserSecondWindSkill;
import balance.skill.hybrid.HybridBalancedInstinctSkill;
import balance.skill.hybrid.HybridExposePatternSkill;
import balance.skill.hybrid.HybridTacticalFocusSkill;
import balance.skill.speedster.SpeedsterPreciseDodgeSkill;
import balance.skill.speedster.SpeedsterQuickComboSkill;
import balance.skill.speedster.SpeedsterQuickFootworkSkill;
import balance.skill.sustainer.SustainerBloodFrenzySkill;
import balance.skill.sustainer.SustainerLifestealSkill;
import balance.skill.sustainer.SustainerRegenSkill;

import balance.skill.tank.TankFortifySkill;
import balance.skill.tank.TankHeavyArmorSkill;
import balance.skill.tank.TankSpikedCounterSkill;
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

        // ✅ Assassin: 약점포착 패시브 + 연막 + 선제일격
        if ("Assassin".equalsIgnoreCase(name)) {
            return List.of(
                    new AssassinExposedWeaknessSkill(),
                    new AssassinShadowEvasionSkill(),
                    new AssassinOpeningStrikeSkill()
            );
        }

        // ✅ Tank: 철벽 + 방어 자세 + 철의 반격
        if ("Tank".equalsIgnoreCase(name)) {
            return List.of(
                    new TankHeavyArmorSkill(),
                    new TankFortifySkill(),
                    new TankSpikedCounterSkill()
            );
        }


        // ✅ Speedster: 가속 + 더블 액션 + 예측 회피
        if ("Speedster".equalsIgnoreCase(name)) {
            return List.of(
                    new SpeedsterQuickFootworkSkill(),
                    new SpeedsterQuickComboSkill(),
                    new SpeedsterPreciseDodgeSkill()
            );
        }

        // ✅ Hybrid: 균형 감각 + 전술적 집중 + 약점 분석
        if ("Hybrid".equalsIgnoreCase(name)) {
            return List.of(
                    new HybridBalancedInstinctSkill(),
                    new HybridTacticalFocusSkill(),
                    new HybridExposePatternSkill()
            );
        }

        return Collections.emptyList();
    }
}
