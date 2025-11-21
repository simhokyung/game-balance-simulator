package balance.support;

public class FixedRandomProvider implements RandomProvider {

    private final double value;

    public FixedRandomProvider(double value) {
        this.value=value;
    }

    @Override
    public double nextDouble() {
        return value;
    }
}
