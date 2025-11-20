package balance.battle;

import balance.domain.Character;

public class BattleCharacter {

    private final Character character;
    private int currentHp;

    private BattleCharacter(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("전투 캐릭터의 원본 캐릭터는 null일 수 없습니다.");
        }
        this.character = character;
        this.currentHp = character.getMaxHp();
    }

    public static BattleCharacter from(Character character) {
        return new BattleCharacter(character);
    }

    public Character getCharacter() {
        return character;
    }

    public int getCurrentHp() {
        return currentHp;
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
