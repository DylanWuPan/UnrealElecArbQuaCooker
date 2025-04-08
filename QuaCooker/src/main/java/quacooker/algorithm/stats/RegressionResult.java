package quacooker.algorithm.stats;

/**
 * Stores alpha and beta from regression.
 */
public class RegressionResult {
    public final double alpha;
    public final double beta;

    public RegressionResult(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }
}
