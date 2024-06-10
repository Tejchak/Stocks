//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class StockModelNew extends StockModelImpl {
//  private final String apiKey;
//  private final ArrayList<BetterPortfolio> portfolios;
//  private Map<String, String[]> stocks;
//
//  public StockModelNew() {
//    this.apiKey = "QCLLY08TISZBMXL9";
//    this.portfolios = new ArrayList<>();
//    this.stocks = new HashMap<String, String[]>();
//  }
//
//  /**
//   * Adds the given stock to the portfolio.
//   * @param portfolioName the name of the portfolio.
//   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
//   * @param shares the amount of shares pf the given stock.
//   */
//  @Override
//  public void addStockToPortfolio(String portfolioName, String stockSymbol, int shares) {
//    for (BetterPortfolio p : this.portfolios) {
//      if (p.name.equals(portfolioName)) {
//        p.stocks.put(stockSymbol, p.stocks.getOrDefault(stockSymbol, 0) + shares);
//      }
//    }
//  }
//}
//
