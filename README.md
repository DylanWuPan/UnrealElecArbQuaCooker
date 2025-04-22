# ElecArbQuaCooker

Thomson, Baumal-Bardy, Pan

**UnrealElecArbQuaCooker** is a Java-based cryptocurrency pairs trading platform that uses statistical arbitrage techniques. The application supports historical backtesting using mean reversion strategies and cointegration tests, with a modern JavaFX GUI for visualization.

If none of that made any sense, watch this video: https://www.youtube.com/watch?v=YDMSqal-RZ4!

## Features

- 📈 **Cointegration Testing**

  - Test if two crypto assets are cointegrated.
  - Visualize price series and spread charts.

- 🤖 **Pairs Trading Backtest**

  - Simulate trading between two cointegrated assets.
  - Use a Mean Reversion strategy with customizable parameters.
  - View portfolio performance, net returns, trading fees, and more.

- 🎨 **Interactive UI**
  - JavaFX interface styled with dark mode aesthetics.
  - Real-time chart generation and results display.

## Technologies

- Java 17+
- JavaFX
- Maven
- Coinbase API (for live crypto ticker data)
- Coingecko API (for historical crypto data)

## Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+
- Internet connection (for API data fetches)

### Clone the Repo

```bash
git clone https://github.com/yourusername/quacooker.git
cd quacooker
mvn javafx:run
```
