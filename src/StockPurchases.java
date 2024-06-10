/**
 * Class representing the purchase of a stock in a portfolio, it contains
 * the amount of shares purchaseDate and sellDate.
 */
public class StockPurchases {
  int shares;
  String purchaseDate;
  String sellDate;

  /**
   * Constructor for stock purchase that initializes the sellDate
   * as an empty string.
   * @param shares the amount of shares being purchased.
   * @param purchaseDate the date of the purchase.
   */
  StockPurchases(int shares, String purchaseDate) {
    this.shares = shares;
    this.purchaseDate = purchaseDate;
    this.sellDate = "";
  }
}