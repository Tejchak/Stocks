import java.time.LocalDate;

/**
 * A class representing the sale of a stock,
 * this stores the date of the sale and the amount of shares sold
 * so that it can be easily tracked.
 */
public class StockSale {
  private double shares;
  private LocalDate saledate;

  /**
   * Creates a stock sale.
   * @param shares represent the number of shares sold.
   * @param saledate represents the date they were sold.
   */
  public StockSale(double shares, LocalDate saledate) {
    this.shares = shares;
    this.saledate = saledate;
  }

  /**
   * Gets the stocksales shares.
   * @return as a double.
   */
  public double getShares() {
    return shares;
  }

  /**
   * Gets the date the stockSale occured.
   * @return as a local date.
   */
  public LocalDate getSaledate() {
    return saledate;
  }
}