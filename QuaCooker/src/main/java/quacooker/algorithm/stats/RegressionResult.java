package quacooker.algorithm.stats;

public class RegressionResult {
    private final double alpha;
    private final double beta;
    private final double seBeta;

    public RegressionResult(double alpha, double beta, double seBeta) {
        this.alpha = alpha;
        this.beta = beta;
        this.seBeta = seBeta;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getSeBeta() {
        return seBeta;
    }

    public double getCoin1Units(double notional, double coin1Price) {
        return notional / coin1Price;
    }

    public double getCoin2Units(double notional, double coin2Price) {
        return (notional * beta) / coin2Price;
    }

}