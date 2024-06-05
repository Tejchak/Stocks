import java.util.ArrayList;

public class Portfolio {
  String name;
  ArrayList<String> stocks;

  public Portfolio(String name) {
    this.name = name;
    this.stocks = new ArrayList<String>();
  }
}
