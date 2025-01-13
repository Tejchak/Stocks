/**
 * A new interface which is made for the use of a gui.
 */
public interface IStockViewGUI {

  /**
   * Creates the opening menu for the stock view with the GUI.
   */
  public void createMenu();

  /**
   * Sets the features, i.e. the controller so the view can access
   * some information for displaying.
   * @param features the gui controller.
   */
  public void setFeatures(GUIFeatures features);

  /**
   * Gets the name of a portfolio from a text field box.
   * @return the name of the portfolio entered.
   */
  public String getPortfolioName();

  /**
   * Gets the name of a portfolio from a selection box.
   * @return the name of the portfolio entered.
   */
  public String getPortfolioNameBox();

  /**
   * Combobox with only yes or no determining if the
   * user want to get rid of future sales.
   * @return yes or no as a string.
   */
  public String getRidFutureSale();

  /**
   * Gets the name of the stock inputted by the user.
   * @return the name of the stock inputted
   */
  public String getStockName();

  /**
   * Gets the purchase date inputted to the combo boxes
   * by the user.
   * @return the purchase date of the stock.
   */
  public String getPurchaseDate();

  /**
   * Displays a given message in the GUI.
   * @param message the message to be displayed.
   */
  public void displayMessage(String message);

  /**
   * Disposes of the current frame being used.
   */
  public void disposeCreateFrame();

  /**
   * Gets the number of shares inputted for sales or purchases by the user.
   * @return the number of shares as a string.
   */
  public String getNumShares();


}
