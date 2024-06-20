import java.util.List;

public interface GUIFeatures extends StockController {

  /**
   * Manages the creation of the portfolio between the user interface and
   * the model.
   */
  public void createPortfolio();

  /**
   * Manages the purchase of a stock through the GUI
   * to the madel.
   */
  public void buyStock();

  /**
   * Manages the sale of a portfolio in the gui.
   * Checks for errors in the inputs and allows the user
   * to get rid of the older inputs.
   * @param getRid the user selection for getting rid of sales.
   */
  public void sellStock(String getRid);

  /**
   * Allows the user to view the total value of a portfolio
   * and its distribution in a gui.
   */
  public void queryPortfolio();

  /**
   * Manages the loading of a portfolio from a
   * selected file path.
   * @param filepath the path to the file.
   */
  public void loadXml(String filepath);

  /**
   * Manages the saving of a portfolio through the gui.
   * Handles miss inputs and errors that could possibly arise.
   * @param name the name of the portfolio.
   * @param filepath the file path for the storage of the portfolio.
   */
  public void savePortfolio(String name, String filepath);

  /**
   * A list of portfolios from the model so the gui allows
   * the user to select from existing portfolios.
   * @return a list of the portfolio names.
   */
  public List<String> portfolioList();
}
