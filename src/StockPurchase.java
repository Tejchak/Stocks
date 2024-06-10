import java.util.Date;

/**
 * Class representing the purchase of a stock in a portfolio, it contains
 * the amount of shares purchaseDate.
 */
public class StockPurchase {
  double shares;
  Date purchaseDate;

  /**
   * Constructor for stock purchase that initializes the sellDate
   * as an empty string.
   * @param shares the amount of shares being purchased.
   * @param purchaseDate the date of the purchase.
   */
  StockPurchase(double shares, Date purchaseDate) {
    this.shares = shares;
    this.purchaseDate = purchaseDate;
  }
}