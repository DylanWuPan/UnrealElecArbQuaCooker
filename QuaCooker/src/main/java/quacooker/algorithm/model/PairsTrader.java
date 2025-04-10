package quacooker.algorithm.model;

/**
 * Represents a trade: open date, close date, entry/exit z-score, PnL, etc.
 */
public class PairsTrader {
  /*
   * PAIRS TRADER LOGIC:
   * 1 trade per day
   * 
   * Parameters:
   * coin ids
   * start date
   * cointegration testing length (how many days in the past to cointegrate and
   * calculate alpha/beta values)
   * 
   * Logic:
   * 1. cointegrate on specified time length, calculate alpha/beta values
   * 2. fetch today's prices, determine actions, take actions
   * 3. repeat
   */
}
