
public interface IStockViewGUI {

  public void createMenu();

  public void setFeatures(GUIFeatures features);

  public void displayCreatePortfolio();

  public String getPortfolioName();

  public String getStockName();

  public String getPurchaseDate();

  public void displayMessage(String message);

  public void disposeCreateFrame();

  public String getNumShares();
}
