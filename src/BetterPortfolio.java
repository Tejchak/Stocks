import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BetterPortfolio {
  String name;
  Map<String, ArrayList<StockPurchases>> portfolio;

  BetterPortfolio(String name){
    this.name = name;
    this.portfolio = new HashMap<>();
  }
}