/**
 * Interface for the user view of a stock calculation program.
 */
public interface StockView {

  /**
   * Creates the user menu with the different options of what they can do.
   */
  public void createMenu();

  /**
   * Creates the portfolio method for what they can do related to a portfolio.
   */
  public void portfolioMenu();

  /**
   * Displays a given string when prompted.
   *
   * @param result a String to be displayed.
   */
  public void displayResult(String result);
}