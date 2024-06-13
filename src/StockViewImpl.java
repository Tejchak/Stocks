import java.io.IOException;

/**
 * Class representing the implementation
 * of a view for a stock calculation program.
 */
public class StockViewImpl implements StockView {
  private final Appendable appendable;
  /**
   * Constructor with no params, just used for dispatch.
   */
  public StockViewImpl(Appendable appendable) {
    this.appendable = appendable;
  }

  /**
   * Creates the user menu with the different options of what they can do.
   */
  public void createMenu() {
    try {
      appendable.append("What would you like to do today?\n");
      appendable.append("1. Examine the gain or loss of a specific stock\n");
      appendable.append("2. Examine the x-day moving average of a stock\n");
      appendable.append("3. Find the x-day crossovers for a stock\n");
      appendable.append("4. Examine, edit, or create a portfolio\n");
      appendable.append("5. Quit the program\n");
    }
    catch (IOException e) {
      throw new RuntimeException("error adding to appendable");
    }
  }

  /**
   * Creates the portfolio method for what they can do related to a portfolio.
   */
  public void portfolioMenu() {
    try {
      appendable.append("1. Create a new portfolio?\n");
      appendable.append("2. Add stock to a portfolio?\n");
      appendable.append("3. Take away stock from a portfolio?\n");
      appendable.append("4. Calculate the value of a portfolio?\n");
      appendable.append("5. View the portfolio distribution\n");
      appendable.append("6. Rebalance a portfolio\n");
      appendable.append("7. View a portfolio performance as a bar chart\n");
      appendable.append("8. Store a portfolio\n");
      appendable.append("9. Recover a portfolio\n");
    }
    catch (IOException e) {
      throw new RuntimeException("error adding to appendable");
    }
  }

  /**
   * Displays a given string when prompted.
   * @param result a String to be displayed.
   */
  public void displayResult(String result) {
    try {
      appendable.append(result + "\n");
    }
    catch (IOException e) {
      throw new RuntimeException("error adding to appendable");
    }
  }
}