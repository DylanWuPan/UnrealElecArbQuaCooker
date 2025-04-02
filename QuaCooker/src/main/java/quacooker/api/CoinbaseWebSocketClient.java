package quacooker.api;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CoinbaseWebSocketClient extends WebSocketClient {
  private final String[] product_ids;
  private ArrayList<ProductData> liveProductData;

  public CoinbaseWebSocketClient(URI serverURI, String[] product_ids) {
    super(serverURI);
    this.product_ids = product_ids;
    this.liveProductData = new ArrayList<>();

    for (String productId : product_ids) {
      liveProductData.add(new ProductData(productId, 0.0));
    }
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("Connected to Coinbase WebSocket!");

    // Subscribe to ticker updates
    JSONObject subscribeMessage = new JSONObject();
    subscribeMessage.put("type", "subscribe");
    subscribeMessage.put("channels", new JSONObject[] {
        new JSONObject().put("name", "ticker").put("product_ids", product_ids) // Fixed missing parenthesis
    });

    send(subscribeMessage.toString());
  }

  @Override
  public void onMessage(String message) {
    JSONObject jsonMessage = new JSONObject(message);

    System.out.println(jsonMessage);
    // double price = jsonMessage.has("price") ? jsonMessage.getDouble("price") : 0.0;
    // String product = jsonMessage.has("product_id") ? jsonMessage.getString("product_id") : "N/A";
    // System.out.println(product + ": $" + price);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("WebSocket closed. " + reason);
  }

  @Override
  public void onError(Exception ex) {
    System.err.println("WebSocket error: " + ex.getMessage());
  }

  // public static void main(String[] args) {
  //   try {
  //     URI uri = new URI("wss://ws-feed.exchange.coinbase.com");
  //     CoinbaseWebSocketClient client = new CoinbaseWebSocketClient(uri);
  //     client.connectBlocking(); // Blocks until the connection is established
  //   } catch (URISyntaxException | InterruptedException e) {
  //     e.printStackTrace();
  //   }
  // }

  public ArrayList<ProductData> getLiveData() {
    return (liveProductData != null) ? liveProductData : new ArrayList<>();
  }
}
