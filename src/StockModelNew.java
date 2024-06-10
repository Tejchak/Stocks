import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockModelNew extends StockModelImpl {
  private final String apiKey;
  private final ArrayList<BetterPortfolio> portfolios;
  private Map<String, String[]> stocks;

  public StockModelNew() {
    this.apiKey = "QCLLY08TISZBMXL9";
    this.portfolios = new ArrayList<>();
    this.stocks = new HashMap<String, String[]>();
  }

}
