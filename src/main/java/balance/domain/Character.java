package balance.domain;

public class Character {

    private final String name;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final int speed;
    private final double critChance;

    public Character(String name,
                     int maxHp,
                     int attack,
                     int defense,
                     int speed,
                     double critChance) {

        validateName(name);
        validatePositive("HP", maxHp);
        validatePositive("ATK", attack);
        validatePositive("DEF", defense);
        validatePositive("SPD", speed);
        validateCritChance(critChance);

        this.name = name.trim();
        this.maxHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.critChance = critChance;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
    }

    private void validatePositive(String label, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(label + "는 1 이상이어야 합니다.");
        }
    }

    private void validateCritChance(double critChance) {
        if (critChance < 0.0 || critChance > 1.0) {
            throw new IllegalArgumentException("치명타 확률은 0 이상 1 이하여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public double getCritChance() {
        return critChance;
    }
}
