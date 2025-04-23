package quacooker.api;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

/**
 * A WebSocket client for subscribing to real-time ticker data from Coinbase's
 * WebSocket API.
 * This client listens for price updates for specified product IDs and updates
 * the local
 * ticker data accordingly.
 */
public class CoinbaseWebSocketClient extends WebSocketClient {

  private final String[] product_ids; // The product IDs to subscribe to
  private final TickerData tickerData; // Stores the live ticker data

  /**
   * Constructs a new {@link CoinbaseWebSocketClient} for subscribing to a list of
   * product IDs.
   *
   * @param serverURI   The URI of the WebSocket server.
   * @param product_ids The list of product IDs (e.g., ["BTC-USD", "ETH-USD"]) to
   *                    subscribe to.
   */
  public CoinbaseWebSocketClient(URI serverURI, String[] product_ids) {
    super(serverURI);
    this.product_ids = product_ids;
    this.tickerData = new TickerData();

    // Initialize ProductData for each product_id
    for (String productId : product_ids) {
      tickerData.add(new ProductData(productId, 0.0));
    }
  }

  /**
   * Called when the WebSocket connection is established. Sends a subscription
   * message
   * to the server to subscribe to the ticker data for the specified product IDs.
   *
   * @param handshakedata Data from the handshake after opening the connection.
   */
  @Override
  public void onOpen(ServerHandshake handshakedata) {
    JSONObject subscribeMessage = new JSONObject();
    subscribeMessage.put("type", "subscribe");
    subscribeMessage.put("channels", new JSONObject[] {
        new JSONObject().put("name", "ticker").put("product_ids", product_ids)
    });

    send(subscribeMessage.toString());
  }

  /**
   * Called when a message is received from the WebSocket server. This method
   * processes
   * the message, extracts the price and product ID, and updates the local ticker
   * data.
   *
   * @param message The message received from the WebSocket server in JSON format.
   */
  @Override
  public void onMessage(String message) {
    JSONObject jsonMessage = new JSONObject(message);

    // Extract price and product_id from the message
    double price = jsonMessage.has("price") ? jsonMessage.getDouble("price") : 0.0;
    String product = jsonMessage.has("product_id") ? jsonMessage.getString("product_id") : "N/A";

    // Update the product data in tickerData
    ProductData productData = tickerData.getProductData(product);
    if (productData != null) {
      productData.setPrice(price);
    }
  }

  /**
   * Called when the WebSocket connection is closed.
   *
   * @param code   The status code indicating the reason for the closure.
   * @param reason The reason for the closure.
   * @param remote A boolean indicating whether the closure was initiated by the
   *               remote peer.
   */
  @Override
  public void onClose(int code, String reason, boolean remote) {
    // Optional: Handle WebSocket closure if needed
  }

  /**
   * Called when an error occurs during the WebSocket communication.
   *
   * @param ex The exception that occurred during the communication.
   */
  @Override
  public void onError(Exception ex) {
    System.err.println("WebSocket error: " + ex.getMessage());
  }

  /**
   * Returns the live ticker data that has been received from the WebSocket
   * server.
   *
   * @return The {@link TickerData} object containing the live ticker data.
   */
  public TickerData getLiveData() {
    return (tickerData != null) ? tickerData : new TickerData();
  }
}