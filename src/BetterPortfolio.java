import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an improved portfolio, this tracks the purchases
 * and the stocks they are associated with.
 */
public class BetterPortfolio {
  String name;
  Map<String, ArrayList<StockPurchases>> purchases;

  /**
   * the constructor for the better portfolio that
   * initializes the map of purchases.
   * @param name the name of this portfolio.
   */
  BetterPortfolio(String name){
    this.name = name;
    this.purchases = new HashMap<>();
  }
}