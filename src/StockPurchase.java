import java.time.LocalDate;

/**
 * Class representing the purchase of a stock in a portfolio, it contains
 * the amount of shares purchaseDate.
 */
public class StockPurchase {
  private double shares;
  private LocalDate purchaseDate;

  /**
   * Constructor for stock purchase that initializes the sellDate
   * as an empty string.
   * @param shares the amount of shares being purchased.
   * @param purchaseDate the date of the purchase.
   */
  StockPurchase(double shares, LocalDate purchaseDate) {
    this.shares = shares;
    this.purchaseDate = purchaseDate;
  }

  /**
   * Gets the shares bought with the stockPurchase.
   * @return as a double.
   */
  public double getShares() {
    return shares;
  }

  /**
   * Gets the date the purchase occured.
   * @return as a LocalDate.
   */
  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }
}