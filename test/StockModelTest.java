//
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import org.junit.Test;
//import org.junit.BeforeClass;
//
//
//public class StockModelImplTest {
//  private static StockModelImpl stockModel;
//
//  @BeforeClass
//  public static void setUp() {
//    stockModel = new StockModelImpl();
//  }
//
//  @Test
//  public void testGetStockDataFromCSV() {
//    // This test assumes that a file named "AAPL.csv" is present in the classpath
//    String[] stockData = stockModel.getStockDataCSV("AAPL");
//    assertNotNull(stockData);
//    assertTrue(stockData.length > 0);
//  }
//
//  @Test
//  public void testGetStockDataFromAPI() {
//    // This test will make a real API call
//    String[] stockData = stockModel.getStockDataAPI("AAPL");
//    assertNotNull(stockData);
//    assertTrue(stockData.length > 0);
//  }
//
//  @Test
//  public void testCheckStockExists() {
//    // This test will make a real API call
//    assertTrue(stockModel.checkStockExists("AAPL"));
//  }
//
//  @Test
//  public void testCalculatePortfolio() {
//    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
//
//    String[] stockData = {
//            "2023-06-01,100,110,90,105,1000",
//            "2023-06-02,105,115,95,110,2000"
//    };
//    stockModel.stocks.put("AAPL", stockData);
//
//    double portfolioValue = stockModel.calculatePortfolio("TestPortfolio", "2023-06-01");
//    assertEquals(1050, portfolioValue);
//  }
//
//  @Test
//  public void testCreatePortfolio() {
//    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
//    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
//  }
//
//  @Test
//  public void testRemoveStockFromPortfolio() {
//    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
//    stockModel.removeStockFromPortfolio("TestPortfolio", "AAPL", 5);
//
//    Portfolio portfolio = stockModel.portfolios.stream()
//            .filter(p -> p.name.equals("TestPortfolio"))
//            .findFirst()
//            .orElse(null);
//    assertNotNull(portfolio);
//    assertEquals(5, (int) portfolio.stocks.get("AAPL"));
//  }
//
//  @Test
//  public void testAddStockToPortfolio() {
//    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
//    stockModel.addStockToPortfolio("TestPortfolio", "AAPL", 5);
//
//    Portfolio portfolio = stockModel.portfolios.stream()
//            .filter(p -> p.name.equals("TestPortfolio"))
//            .findFirst()
//            .orElse(null);
//    assertNotNull(portfolio);
//    assertEquals(15, (int) portfolio.stocks.get("AAPL"));
//  }
//
//  @Test
//  public void testGetLine() {
//    String[] stockData = {
//            "2023-06-01,100,110,90,105,1000",
//            "2023-06-02,105,115,95,110,2000"
//    };
//
//    String[] line = stockModel.getLine(stockData, "2023-06-01");
//    assertNotNull(line);
//    assertEquals("2023-06-01", line[0]);
//  }
//
//  @Test
//  public void testStockGainLoss() {
//    String[] startDateLine = {"2023-06-01", "100", "110", "90", "105", "1000"};
//    String[] endDateLine = {"2023-06-02", "105", "115", "95", "110", "2000"};
//
//    double gainLoss = stockModel.stockGainLoss(new String[0], startDateLine, endDateLine);
//    assertEquals(5, gainLoss);
//  }
//
//  @Test
//  public void testMovingAverage() {
//    String[] stockData = {
//            "2023-06-01,100,110,90,105,1000",
//            "2023-06-02,105,115,95,110,2000",
//            "2023-06-03,110,120,100,115,1500"
//    };
//
//    double movingAverage = stockModel.movingAverage(stockData, "2023-06-01", 2);
//    assertEquals(107.5, movingAverage, 0.01);
//  }
//
//  @Test
//  public void testXDayCrossover() {
//    String[] stockData = {
//            "2023-06-01,100,110,90,105,1000",
//            "2023-06-02,105,115,95,110,2000",
//            "2023-06-03,110,120,100,115,1500"
//    };
//
//    StringBuilder crossovers = stockModel.xDayCrossover(stockData, "2023-06-01", "2023-06-03", 2);
//    assertTrue(crossovers.toString().contains("2023-06-02"));
//  }
//
//  @Test
//  public void testExistingPortfolio() {
//    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
//    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
//  }
//}
