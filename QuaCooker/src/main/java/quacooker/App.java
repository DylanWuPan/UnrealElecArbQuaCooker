package quacooker;

import java.time.LocalDate;

import quacooker.trading.PairsTrade;
import quacooker.trading.PairsTradeLedger;
import quacooker.trading.ShortTrade;
import quacooker.trading.Trade;

public class App {
    public static void main(String[] args) {
        System.out.println("QUA COOKIN");

        // Example usage of the PairsTrade and PairsTradeLedger classes
        PairsTradeLedger pairsTradeLedger = new PairsTradeLedger();

        LocalDate tradeDate1 = LocalDate.of(2023, 1, 1);
        PairsTrade trade1 = new PairsTrade(
                new Trade("AAPL", 10, 150.0, tradeDate1),
                new ShortTrade("MSFT", 5, 300.0, tradeDate1));

        LocalDate tradeDate2 = LocalDate.of(2023, 1, 2);
        PairsTrade trade2 = new PairsTrade(
                new Trade("GOOGL", 20, 2500.0, tradeDate2),
                new ShortTrade("AMZN", 10, 3500.0, tradeDate2));

        pairsTradeLedger.add(trade1);
        pairsTradeLedger.add(trade2);

        trade1.sell(200, 200, tradeDate1);
        trade2.sell(3000, 3000, tradeDate2);

        System.out.println("Total Revenue: " + pairsTradeLedger.getTotalRevenue());
        System.out.println("Unsold Trades: " + pairsTradeLedger.getUnsoldTrades().size());
    }
}