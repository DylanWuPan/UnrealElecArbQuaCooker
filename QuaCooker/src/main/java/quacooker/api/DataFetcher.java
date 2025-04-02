package quacooker.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DataFetcher {
  private static final String COINBASE_URI = "wss://ws-feed.exchange.coinbase.com";
  private CoinbaseWebSocketClient client;

  public DataFetcher(String[] product_ids) {
    try {
      client = new CoinbaseWebSocketClient(new URI(COINBASE_URI), product_ids);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<ProductData> fetchLiveData() {
    if (client != null) {
      try {
        client.connect();
        Thread.sleep(1000);
        client.close();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      System.err.println("Failed to create WebSocket client.");
    }
    return client.getLiveData();
  }
}
