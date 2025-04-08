package quacooker.algorithm.stats;

/**
 * Stores alpha, beta, and standard error of beta from regression.
 */
public class RegressionResult {
    public final double alpha;
    public final double beta;
    public final double seBeta; // Standard error of beta

    public RegressionResult(double alpha, double beta, double seBeta) {
        this.alpha = alpha;
        this.beta = beta;
        this.seBeta = seBeta;
    }
}