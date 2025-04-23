package quacooker.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The {@link DataFetcher} class is responsible for fetching live market data
 * from the Coinbase WebSocket API.
 * It connects to the WebSocket, retrieves the live data for specified product
 * IDs, and then returns it.
 */
public class DataFetcher {

  // WebSocket URI for the Coinbase feed
  private static final String COINBASE_URI = "wss://ws-feed.exchange.coinbase.com";
  private CoinbaseWebSocketClient client; // WebSocket client to connect to Coinbase API

  /**
   * Constructs a new {@link DataFetcher} object that connects to the Coinbase
   * WebSocket API for the
   * specified product IDs.
   *
   * @param product_ids The product IDs to fetch market data for (e.g.,
   *                    ["BTC-USD", "ETH-USD"]).
   */
  public DataFetcher(String[] product_ids) {
    try {
      // Create a new WebSocket client
      client = new CoinbaseWebSocketClient(new URI(COINBASE_URI), product_ids);
    } catch (URISyntaxException e) {
      // Handle URI syntax error
      e.printStackTrace();
    }
  }

  /**
   * Fetches the live market data for the specified product IDs from Coinbase
   * WebSocket API.
   * The client will connect, fetch data, and close the connection after a brief
   * wait.
   *
   * @return A list of {@link ProductData} objects containing the live data for
   *         each product.
   */
  public ArrayList<ProductData> fetchLiveData() {
    if (client != null) {
      try {
        // Connect to the WebSocket and wait for 1 second to receive data
        client.connect();
        Thread.sleep(1000);
        // Close the WebSocket connection after fetching data
        client.close();
      } catch (InterruptedException e) {
        // Handle interruption during sleep or connection
        e.printStackTrace();
      }
    } else {
      System.err.println("Failed to create WebSocket client.");
    }

    // Return the live data from the WebSocket client
    return client.getLiveData();
  }
}