import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a stock portfolio.
 */
public class Portfolio {
  String name;
  Map<String, Integer> stocks;

  /**
   * Creates the portfolio and initializes the hashmap
   * with no stocks and no shares.
   * @param name a String name of the portfolio.
   */
  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<String, Integer>();
  }
}
