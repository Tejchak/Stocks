import java.util.List;

public interface GUIFeatures extends StockController {

  public void createPortfolio();
  public void buyStock();
  public void sellStock(String getRid);
  public void queryPortfolio();
  public void loadXml(String filepath);
  public void savePortfolio(String name, String filepath);
  public List<String> portfolioList();
}
