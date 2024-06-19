public interface GUIFeatures extends StockController {

  public void createPortfolio();
  public void buyStock();
  public void sellStock();
  public void queryPortfolio();
  public void loadXml(String filepath);
  public void savePortfolio(String name, String filepath);
}
