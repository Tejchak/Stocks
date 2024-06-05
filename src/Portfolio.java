import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {
  String name;
  Map<String, Integer> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<String, Integer>();
  }
}
