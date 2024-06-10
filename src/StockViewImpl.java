/**
 * Class representing the implementation
 * of a view for a stock calculation program.
 */
public class StockViewImpl implements StockView {

  /**
   * Constructor with no params, just used for dispatch.
   */
  public StockViewImpl() {
    //This constructor is purely for design.
  }

  /**
   * Creates the user menu with the different options of what they can do.
   */
  public void createMenu() {
    System.out.println("What would you like to do today?");
    System.out.println("1. Examine the gain or loss of a specific stock");
    System.out.println("2. Examine the x-day moving average of a stock");
    System.out.println("3. Find the x-day crossovers for a stock");
    System.out.println("4. Examine or create a portfolio");
    System.out.println("5. Quit the program");
  }

  /**
   * Creates the portfolio method for what they can do related to a portfolio.
   */
  public void portfolioMenu() {
    System.out.println("1. Create a new portfolio?");
    System.out.println("2. Add stock to a portfolio?");
    System.out.println("3. Take away stock from a portfolio?");
    System.out.println("4. Calculate the value of a portfolio?");
    System.out.println("5. View the portfolio distribution");
  }

  /**
   * Displays a given string when prompted.
   * @param result a String to be displayed.
   */
  public void displayResult(String result) {

    System.out.println(result);
  }
}