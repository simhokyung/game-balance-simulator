package balance.battle;

import balance.support.RandomProvider;

public interface DamageCalculator {

    /**
     * 공격자와 방어자, 랜덤 공급자를 받아 이번 공격에서 줄 데미지를 계산한다.
     */
    int calculateDamage(BattleCharacter attacker,
                        BattleCharacter defender,
                        RandomProvider randomProvider);
}
