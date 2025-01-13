import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Class for holding my gui tests.
 */
public class GUITest {

  /**
   * Static class representing a mock for the view.
   */
  public static class MockGUI implements IStockViewGUI {
    private StringBuilder log = new StringBuilder();
    private String portfolioName;
    private String stockName;
    private String purchaseDate;
    private String numShares;
    private String portfolioNameBox;
    private String ridFutureSale;
    private String message;

    public MockGUI() {
    }

    @Override
    public void createMenu() {
      log.append("createMenu called\n");
    }

    @Override
    public void setFeatures(GUIFeatures features) {
      log.append("setFeatures called\n");
    }

    @Override
    public String getPortfolioName() {
      log.append("getPortfolioName called\n");
      return portfolioName;
    }

    @Override
    public String getPortfolioNameBox() {
      log.append("getPortfolioNameBox called\n");
      return portfolioNameBox;
    }

    @Override
    public String getRidFutureSale() {
      log.append("getRidFutureSale called\n");
      return ridFutureSale;
    }

    @Override
    public String getStockName() {
      log.append("getStockName called\n");
      return stockName;
    }

    @Override
    public String getPurchaseDate() {
      log.append("getPurchaseDate called\n");
      return purchaseDate;
    }

    @Override
    public void displayMessage(String message) {
      log.append("displayMessage called with message: ").append(message).append("\n");
      this.message = message;
    }

    @Override
    public void disposeCreateFrame() {
      log.append("disposeCreateFrame called\n");
    }

    @Override
    public String getNumShares() {
      log.append("getNumShares called\n");
      return numShares;
    }

    public void setPortfolioName(String portfolioName) {
      this.portfolioName = portfolioName;
    }

    public void setStockName(String stockName) {
      this.stockName = stockName;
    }

    public void setPurchaseDate(String purchaseDate) {
      this.purchaseDate = purchaseDate;
    }

    public void setNumShares(String numShares) {
      this.numShares = numShares;
    }

    public void setPortfolioNameBox(String portfolioNameBox) {
      this.portfolioNameBox = portfolioNameBox;
    }

    public void setRidFutureSale(String ridFutureSale) {
      this.ridFutureSale = ridFutureSale;
    }

    public StringBuilder getLog() {
      return log;
    }

    public String getMessage() {
      return message;
    }
  }

  @Test
  public void testCreatePortfolio() {
    StockModelTrader model = new StockModelNew();
    MockGUI view = new MockGUI();
    GUIFeatures controller = new StockControllerGUI(model, view);

    view.setPortfolioName("TestPortfolio");
    view.setStockName("AAPL");
    view.setPurchaseDate("2024-01-01");
    view.setNumShares("10");
    controller.createPortfolio();
    assertEquals(model.getPortfolios().get(0).name, "TestPortfolio");

  }
}
