import java.time.LocalDate;
import java.util.Date;

/**
 * A class representing the sale of a stock,
 * this stores the date of the sale and the amount of shares sold
 * so that it can be easily tracked.
 */
public class StockSale {
  double shares;
  LocalDate saledate;

  StockSale(double shares, LocalDate saledate) {
    this.shares = shares;
    this.saledate = saledate;
  }
}