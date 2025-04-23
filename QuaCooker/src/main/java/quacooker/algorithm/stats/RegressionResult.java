package quacooker.algorithm.stats;

/**
 * Holds the result of a simple linear regression.
 * Includes alpha (intercept), beta (slope), and the standard error of beta.
 * Also provides utility methods for calculating trading units based on the
 * regression.
 */
public class RegressionResult {
    private final double alpha;
    private final double beta;
    private final double seBeta;

    /**
     * Constructs a {@code RegressionResult} with the specified regression
     * parameters.
     *
     * @param alpha  The intercept of the regression line.
     * @param beta   The slope of the regression line.
     * @param seBeta The standard error of the slope.
     */
    public RegressionResult(double alpha, double beta, double seBeta) {
        this.alpha = alpha;
        this.beta = beta;
        this.seBeta = seBeta;
    }

    /**
     * Returns the intercept (alpha) of the regression line.
     *
     * @return The alpha value.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Returns the slope (beta) of the regression line.
     *
     * @return The beta value.
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Returns the standard error of the beta (slope) estimate.
     *
     * @return The standard error of beta.
     */
    public double getSeBeta() {
        return seBeta;
    }

    /**
     * Calculates the number of units of the first coin to trade based on the given
     * notional value.
     *
     * @param notional   The total capital to allocate.
     * @param coin1Price The current price of the first coin.
     * @return The number of units of coin 1 to trade.
     */
    public double getCoin1Units(double notional, double coin1Price) {
        return notional / coin1Price;
    }

    /**
     * Calculates the number of units of the second coin to trade based on the
     * regression beta.
     *
     * @param notional   The total capital to allocate.
     * @param coin2Price The current price of the second coin.
     * @return The number of units of coin 2 to trade.
     */
    public double getCoin2Units(double notional, double coin2Price) {
        return (notional * beta) / coin2Price;
    }
}