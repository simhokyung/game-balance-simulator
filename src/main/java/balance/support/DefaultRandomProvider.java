package balance.support;

import java.util.Random;

public class DefaultRandomProvider implements RandomProvider {

    private final Random random = new Random();

    @Override
    public double nextDouble() {
        return random.nextDouble(); // 0.0 이상 1.0 미만
    }
}
