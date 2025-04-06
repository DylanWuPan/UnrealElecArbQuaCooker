package quacooker.api;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

public class CoinbaseWebSocketClient extends WebSocketClient {
  private final String[] product_ids;
  private final TickerData tickerData;

  public CoinbaseWebSocketClient(URI serverURI, String[] product_ids) {
    super(serverURI);
    this.product_ids = product_ids;
    this.tickerData = new TickerData();

    for (String productId : product_ids) {
      tickerData.add(new ProductData(productId, 0.0));
    }
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
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

    // System.out.println(jsonMessage);
    double price = jsonMessage.has("price") ? jsonMessage.getDouble("price") : 0.0;
    String product = jsonMessage.has("product_id") ? jsonMessage.getString("product_id") : "N/A";
    // System.out.println(product + ": $" + price);

    ProductData productData = tickerData.getProductData(product);
    if (productData != null) {
      productData.setPrice(price);
    }
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    // System.out.println("WebSocket closed. " + reason);
  }

  @Override
  public void onError(Exception ex) {
    System.err.println("WebSocket error: " + ex.getMessage());
  }

  public TickerData getLiveData() {
    return (tickerData != null) ? tickerData : new TickerData();
  }
}
