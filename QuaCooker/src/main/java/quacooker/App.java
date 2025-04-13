package quacooker;

import quacooker.algorithm.model.PairsTrader;
import quacooker.algorithm.strategy.MeanReversionStrategy;
import quacooker.trading.PairsTradeLedger;

public class App {
    public static void main(String[] args) {
        System.out.println("QUA COOKIN");

        PairsTrader trader = new PairsTrader("ethereum-classic", "litecoin",
                new MeanReversionStrategy(1000000));
        trader.backtest(300, 25);
        PairsTradeLedger ledger = trader.getLedger();
        System.out.println("Total Revenue: " + ledger.getTotalRevenue());
        System.out.println("Unsold Trades: " + ledger.getUnsoldTrades().size());
        System.out.println("Total Trades: " + ledger.size());
    }
}