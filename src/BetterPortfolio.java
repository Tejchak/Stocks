import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class representing an improved portfolio, this tracks the purchases
 * and the stocks they are associated with.
 */
public class BetterPortfolio {
  String name;
  Map<String, List<StockPurchase>> purchases;
  Map<String, List<StockSale>> sales;

  /**
   * the constructor for the better portfolio that
   * initializes the map of purchases.
   * @param name the name of this portfolio.
   */
  BetterPortfolio(String name) {
    this.name = name;
    this.purchases = new HashMap<>();
    this.sales = new HashMap<>();
  }
}