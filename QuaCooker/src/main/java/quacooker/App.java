package quacooker;

import quacooker.api.CoinbaseWebSocketClient;
import java.net.URI;
import java.net.URISyntaxException;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting QuaCooker...");

        try {
            // Coinbase WebSocket URL
            URI coinbaseUri = new URI("wss://ws-feed.exchange.coinbase.com");

            // Pass the URI to the WebSocket client
            CoinbaseWebSocketClient client = new CoinbaseWebSocketClient(coinbaseUri);
            client.connect();  // Make sure your client has a connect() method

            // Keep the app running
            Thread.sleep(60000);  // Keep it running for 60 seconds (adjust as needed)

            System.out.println("Shutting down...");
            client.close();  // Close the connection properly

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
