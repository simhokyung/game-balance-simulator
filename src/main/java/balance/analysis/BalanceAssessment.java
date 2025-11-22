package balance.analysis;

import balance.domain.Character;
import balance.simulation.CharacterStats;

public class BalanceAssessment {

    private final Character character;
    private final CharacterStats stats;
    private final BalanceTier tier;
    private final String reason;

    public BalanceAssessment(Character character,
                             CharacterStats stats,
                             BalanceTier tier,
                             String reason) {
        this.character = character;
        this.stats = stats;
        this.tier = tier;
        this.reason = reason;
    }

    public Character getCharacter() {
        return character;
    }

    public CharacterStats getStats() {
        return stats;
    }

    public BalanceTier getTier() {
        return tier;
    }

    public String getReason() {
        return reason;
    }
}
