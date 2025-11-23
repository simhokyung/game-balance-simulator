package balance.battle;

import balance.domain.Character;
import balance.skill.Skill;

import java.util.Collections;
import java.util.List;

public class BattleCharacter {

    private final Character character;
    private int currentHp;
    private final List<Skill> skills;

    private BattleCharacter(Character character, List<Skill> skills) {
        if (character == null) {
            throw new IllegalArgumentException("전투 캐릭터의 원본 캐릭터는 null일 수 없습니다.");
        }
        this.character = character;
        this.currentHp = character.getMaxHp();
        this.skills = skills == null ? Collections.emptyList() : List.copyOf(skills);
    }

    /**
     * 기본 생성: 스킬 없는 전투 캐릭터.
     * (기존 코드와의 호환성을 위해 유지)
     */
    public static BattleCharacter from(Character character) {
        return new BattleCharacter(character, Collections.emptyList());
    }

    /**
     * 스킬 목록을 가진 전투 캐릭터 생성용 팩토리 메서드.
     * 나중에 Preset 캐릭터에 스킬을 붙일 때 사용할 예정.
     */
    public static BattleCharacter from(Character character, List<Skill> skills) {
        return new BattleCharacter(character, skills);
    }

    public Character getCharacter() {
        return character;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void takeDamage(int damage) {
        validatePositive("피해량", damage);
        if (isDead()) {
            return;
        }
        int nextHp = currentHp - damage;
        if (nextHp < 0) {
            nextHp = 0;
        }
        currentHp = nextHp;
    }

    public void heal(int amount) {
        validatePositive("회복량", amount);
        if (isDead()) {
            return;
        }
        int nextHp = currentHp + amount;
        int maxHp = character.getMaxHp();
        if (nextHp > maxHp) {
            nextHp = maxHp;
        }
        currentHp = nextHp;
    }

    public boolean isDead() {
        return currentHp <= 0;
    }

    private void validatePositive(String label, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(label + "은(는) 1 이상이어야 합니다.");
        }
    }
}
