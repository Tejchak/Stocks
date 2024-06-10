

public class StockPurchases {
  int shares;
  String purchaseDate;
  String sellDate;
  Double closingValue;

  StockPurchases(int shares, String purchaseDate, Double closingValue) {
    this.shares = shares;
    this.purchaseDate = purchaseDate;
    this.sellDate = "";
    this.closingValue = closingValue;
  }
}