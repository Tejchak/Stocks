/**
 * Class representing the purchase of a stock in a portfolio, it contains
 * the amount of shares purchaseDate and sellDate.
 */
public class StockPurchases {
  int shares;
  String purchaseDate;
  String sellDate;
  Double closingValue;

  /**
   * Constructor for stock purchase that initializes the sellDate
   * as an empty string.
   * @param shares the amount of shares being purchased.
   * @param purchaseDate the date of the purchase.
   * @param closingValue the closingValue and the day it was purchased
   */
  StockPurchases(int shares, String purchaseDate, Double closingValue) {
    this.shares = shares;
    this.purchaseDate = purchaseDate;
    this.sellDate = "";
    this.closingValue = closingValue;
  }
}