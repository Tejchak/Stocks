

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collections;

/**
 * Class representing the tests for the model.
 */
public class StockModelTest {
  private static StockModelImpl stockModel;

  @Before
  public void setUp() {
    stockModel = new StockModelImpl();
  }

  /**
   * Tests that the model is able to access the csv and take information.
   */
  @Test
  public void testGetStockDataFromCSV() {
    // This test assumes that a file named "AAPL.csv" is present in the classpath
    String[] stockData = stockModel.getStockDataCSV("AAPL");
    assertNotNull(stockData);
    assertTrue(stockData.length > 0);
  }

  /**
   * Tests that the model is able to access the API and take information.
   */
  @Test
  public void testGetStockDataFromAPI() {
    // This test will make a real API call
    String[] stockData = stockModel.getStockDataAPI("AAPL");
    assertNotNull(stockData);
    assertTrue(stockData.length > 0);
  }

  /**
   * Tests that checkStockExists will return true and false
   * at the correct times.
   */
  @Test
  public void testCheckStockExists() {
    // This test will make a real CSV call
    assertTrue(stockModel.checkStockExists("AAPL"));
  }

  /**
   * Tests that the model is able to create a portfolio.
   */
  @Test
  public void testCreatePortfolio() {
    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
    assertEquals(10.0,
            stockModel.getPortfolios().get(0).stocks.get("AAPL"), 0.01);

  }
  /**
   * Tests that getPortfolio does not allow the user to mutate the field.
   */
  @Test
  public void testGetPortfolio() {
    stockModel.getPortfolios().add(new Portfolio("test"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
    stockModel.getPortfolios().get(0).name = "yo";
    assertFalse(stockModel.existingPortfolio("test"));
    assertFalse(stockModel.existingPortfolio("yo"));
  }

  /**
   * Tests that the value of a portfolio is correctly calculated.
   */
  @Test
  public void testCalculatePortfolios() {
    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
    assertEquals(1902.89993,
            stockModel.calculatePortfolio("TestPortfolio", "2024-05-29"), 0.0001);
    stockModel.addStockToPortfolio("TestPortfolio", "GOOG", 10);
    assertEquals(3676.89987,
            stockModel.calculatePortfolio("TestPortfolio", "2024-05-29"), 0.0001);
  }

  /**
   * Tests that get line can get the information from a given day.
   */
  @Test
  public void testGetLine() {
    String[] stockData = {
            "2023-06-01,100,110,90,105,1000",
            "2023-06-02,105,115,95,110,2000"
    };

    String[] line = stockModel.getLine(stockData, "2023-06-01");
    assertNotNull(line);
    assertEquals("2023-06-01", line[0]);
  }

  /**
   * Tests that gain loss will return the right value.
   */
  @Test
  public void testStockGainLoss() {
    String[] startDateLine = {"2023-06-01", "100", "110", "90", "105", "1000"};
    String[] endDateLine = {"2023-06-02", "105", "115", "95", "110", "2000"};
    String[] endDateLine2 = {"2023-06-02", "105", "115", "95", "104", "2000"};

    double gainLoss = stockModel.stockGainLoss(new String[0], startDateLine, endDateLine);
    double gainLoss2 = stockModel.stockGainLoss(new String[0], startDateLine, endDateLine2);
    assertEquals(5.0, gainLoss, 0.1);
    assertEquals(-1.0, gainLoss2, 0.1);
  }

  /**
   * Tests that the program will return the correct moving average
   */
  @Test
  public void testMovingAverage() {
    String[] stockData = {
            "2023-06-01,100,110,90,105,1000",
            "2023-06-02,105,115,95,110,2000",
            "2023-06-03,110,120,100,115,1500"
    };

    double movingAverage = stockModel.movingAverage(stockData, "2023-06-01", 2);
    assertEquals(107.5, movingAverage, 0.01);
  }

  @Test
  public void testXDayCrossover() {
    String[] stockData = {
            "2013-08-19,21.362677,21.718594,21.356201,21.560436,21.560436,72707508",
            "2013-08-20,21.627684,21.721333,21.507883,21.554708,21.554708,49504863",
            "2013-08-21,21.684969,21.840885,21.581608,21.652094,21.652094,70555472",
            "2013-08-22,21.736029,21.787086,21.675007,21.761185,21.761185,34926424",
            "2013-08-23,21.863798,21.868032,21.662554,21.674009,21.674009,43245489",
            "2013-08-26,21.668779,21.790823,21.570398,21.578867,21.578867,42257801",
            "2013-08-27,21.410250,21.512615,21.118343,21.174383,21.174383,69623993",
            "2013-08-28,21.176874,21.305391,21.115105,21.134533,21.134533,53395392",
            "2013-08-29,21.147484,21.429178,21.135529,21.305889,21.305889,59361671",
            "2013-08-30,21.314110,21.370897,21.060062,21.093437,21.093437,74743109",
            "2013-09-03,21.279240,21.573887,21.269028,21.429178,21.429178,82210996"
    };
    Collections.reverse(Arrays.asList(stockData));
    System.out.println(stockModel.movingAverage(stockData,"2013-08-19", 4));
    StringBuilder crossovers = stockModel.xDayCrossover(stockData, "2013-08-19", "2013-08-22", 4);
    assertTrue(crossovers.toString().contains("2013-08-20"));
  }

  /**
   * Tests that existing portfolio works properly.
   */
  @Test
  public void testExistingPortfolio() {
    stockModel.createPortfolio("TestPortfolio", "AAPL", 10);
    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
  }
}
