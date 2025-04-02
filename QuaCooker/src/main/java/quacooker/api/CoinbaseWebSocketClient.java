package quacooker.api;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class CoinbaseWebSocketClient extends WebSocketClient {

  public CoinbaseWebSocketClient(URI serverURI) {
    super(serverURI);
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    System.out.println("Connected to Coinbase WebSocket!");

    // Subscribe to BTC-USD ticker updates
    JSONObject subscribeMessage = new JSONObject();
    subscribeMessage.put("type", "subscribe");
    subscribeMessage.put("channels", new JSONObject[] {
        new JSONObject().put("name", "ticker").put("product_ids", new String[] { "BTC-USD" })
    });

    send(subscribeMessage.toString());
  }

  @Override
  public void onMessage(String message) {
    JSONObject jsonMessage = new JSONObject(message);
    System.out.println("Received: " + jsonMessage.toString(2));

    // Extract price if available
    if (jsonMessage.has("price")) {
      double price = jsonMessage.getDouble("price");
      System.out.println("BTC Price: $" + price);
    }
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("WebSocket closed: " + reason);
  }

  @Override
  public void onError(Exception ex) {
    System.err.println("WebSocket error: " + ex.getMessage());
  }

  public static void main(String[] args) {
    try {
      URI uri = new URI("wss://ws-feed.exchange.coinbase.com");
      CoinbaseWebSocketClient client = new CoinbaseWebSocketClient(uri);
      client.connectBlocking(); // Blocks until the connection is established
    } catch (URISyntaxException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
