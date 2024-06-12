import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.Before;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the tests for the model.
 */
public class StockModelTest {
  private static StockModelImpl stockModel;


  public LocalDate convertDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate newDate = LocalDate.parse(date, formatter);
    return newDate;
  }

  /**
   * Sets up the tests.
   */
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
    StockPurchase purchase = new StockPurchase(10, this.convertDate("2024-05-09"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", purchase);
    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
    assertEquals(10.0,
            stockModel.getPortfolios().get(0).purchases.get("AAPL").get(0).getShares(), 0.01);
  }

  /**
   * Tests that getPortfolio does not allow the user to mutate the field.
   */
  @Test
  public void testGetPortfolio() {
    StockPurchase purchase = new StockPurchase(10, this.convertDate("2024-05-09"));
    stockModel.getPortfolios().add(new BetterPortfolio("test"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", purchase);
    stockModel.getPortfolios().get(0).name = "yo";
    assertFalse(stockModel.existingPortfolio("test"));
    assertFalse(stockModel.existingPortfolio("yo"));
  }

  /**
   * Tests that the value of a portfolio is correctly calculated.
   */
  @Test
  public void testCalculatePortfolios() {
    StockPurchase purchase = new StockPurchase(10, this.convertDate("2024-05-09"));
    StockPurchase purchase2 = new StockPurchase(10, this.convertDate("2024-05-24"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", purchase);
    assertEquals(1902.89993,
            stockModel.calculatePortfolio("TestPortfolio", this.convertDate("2024-05-29")), 0.0001);
    stockModel.addStockToPortfolio("TestPortfolio", "GOOG", purchase2);
    assertEquals(3676.9,
            stockModel.calculatePortfolio("TestPortfolio", this.convertDate("2024-05-29")), 0.0001);
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
   * Tests that the program will return the correct moving average.
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

  /**
   * Tests that the x-day crossover returns the correct dates.
   */
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
    System.out.println(stockModel.movingAverage(stockData, "2013-08-19", 4));
    StringBuilder crossovers = stockModel.xDayCrossover(stockData, "2013-08-19", "2013-08-22", 4);
    assertEquals("2013-08-22, 2013-08-21, 2013-08-20, 2013-08-19, ", crossovers.toString());
  }

  /**
   * Tests that existing portfolio works properly.
   */
  @Test
  public void testExistingPortfolio() {
    StockPurchase purchase = new StockPurchase(10, this.convertDate("2024-05-09"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", purchase);

    assertTrue(stockModel.existingPortfolio("TestPortfolio"));
  }

  /**
   * Tests that the value of a portfolio will default to zero
   * if the given date was not a trading day.
   */
  @Test
  public void testAddStockToPortfolioDefaultToZero() {
    StockPurchase purchase = new StockPurchase(10, this.convertDate("2024-05-09"));
    stockModel.createPortfolio("TestPortfolio", "AAPL", purchase);
    assertEquals(0.0, stockModel.calculatePortfolio("TestPortfolio",
            this.convertDate("2025-05-29")), 0.0001);
  }

  @Test
  public void testimportPortfolios() {
    assertEquals(0, stockModel.getPortfolios().size());
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    assertEquals(2, stockModel.getPortfolios().size());
    assertEquals("Jake", stockModel.getPortfolios().get(0).name);
    assertEquals(1, stockModel.getPortfolios().get(0).purchases.size());
    assertEquals(1, stockModel.getPortfolios().get(0).purchases.get("L").size());
    assertEquals(this.convertDate("2016-08-03"),
            stockModel.getPortfolios().get(0).purchases.get("L").get(0).getPurchaseDate());
    assertEquals(70,
            stockModel.getPortfolios().get(0).purchases.get("L").get(0).getShares(), .01);
  }

  @Test
  public void testBarChartYears() {
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    Map<String, Double> expectedValue = new HashMap<String, Double>();
    expectedValue.put("2015-05-09", 0.0);
    expectedValue.put("2016-05-09", 0.0);
    expectedValue.put("2017-05-09", 0.0);
    expectedValue.put("2018-05-09", 8785.0);
    expectedValue.put("2019-05-09", 6676.0);
    expectedValue.put("2020-05-09", 8517.48);
    expectedValue.put("2021-05-09", 11118.76);
    expectedValue.put("2022-05-09", 5820.7);
    expectedValue.put("2023-05-09", 5642.0);
    assertEquals(expectedValue, stockModel.getPortfolioData("Tej",
            this.convertDate("2015-05-09"), this.convertDate("2024-05-09"), "Years"));
  }

  @Test
  public void testBarChartMonths() {
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    Map<String, Double> expectedValue = new HashMap<String, Double>();
    expectedValue.put("2024-01-09", 4969.3);
    expectedValue.put("2024-02-09", 5092.5);
    expectedValue.put("2024-03-09", 5238.8);
    expectedValue.put("2024-04-09", 5263.3);
    assertEquals(expectedValue, stockModel.getPortfolioData("Jake",
            this.convertDate("2024-01-09"), this.convertDate("2024-05-09"), "Months"));
  }

  @Test
  public void testBarChartTwoMonths() {
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    Map<String, Double> expectedValue = new HashMap<String, Double>();
    expectedValue.put("2022-01-09", 4254.6);
    expectedValue.put("2022-03-09", 4272.1);
    expectedValue.put("2022-05-09", 4415.6);
    expectedValue.put("2022-07-09", 4055.1);
    expectedValue.put("2022-09-09", 3980.2);
    expectedValue.put("2022-11-09", 3812.2);
    expectedValue.put("2023-01-09", 4127.9);
    expectedValue.put("2023-03-09", 4071.9);
    expectedValue.put("2023-05-09", 4103.4);
    expectedValue.put("2023-07-09", 4231.5);
    expectedValue.put("2023-09-09", 4370.1);
    expectedValue.put("2023-11-09", 4567.5);
    expectedValue.put("2024-01-09", 4969.3);
    expectedValue.put("2024-03-09", 5238.8);
    assertEquals(expectedValue, stockModel.getPortfolioData("Jake",
            this.convertDate("2022-01-09"), this.convertDate("2024-05-09"), "Two months"));
  }

  @Test
  public void testBarChartDays() {
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    Map<String, Double> expectedValue = new HashMap<String, Double>();
    expectedValue.put("2024-05-09", 5439.0);
    expectedValue.put("2024-05-10", 5458.6);
    expectedValue.put("2024-05-11", 5458.6);
    expectedValue.put("2024-05-12", 5458.6);
    expectedValue.put("2024-05-13", 5413.8);
    expectedValue.put("2024-05-14", 5413.8);
    expectedValue.put("2024-05-15", 5363.4);
    expectedValue.put("2024-05-16", 5404.0);
    expectedValue.put("2024-05-17", 5441.8);
    expectedValue.put("2024-05-18", 5441.8);
    expectedValue.put("2024-05-19", 5441.8);
    expectedValue.put("2024-05-20", 5329.1);
    expectedValue.put("2024-05-21", 5334.7);
    expectedValue.put("2024-05-22", 5312.3);
    expectedValue.put("2024-05-23", 5191.2);
    assertEquals(expectedValue, stockModel.getPortfolioData("Jake",
            this.convertDate("2024-05-09"), this.convertDate("2024-05-24"), "Days"));
  }

  @Test
  public void testBarChartWeeks() {
    stockModel.loadPortfolioFromXML("Resources/Jake.xml");
    Map<String, Double> expectedValue = new HashMap<String, Double>();
    expectedValue.put("2024-03-09", 5238.8);
    expectedValue.put("2024-03-16", 5387.9);
    expectedValue.put("2024-03-23", 5351.5);
    expectedValue.put("2024-03-30", 0.0);
    expectedValue.put("2024-04-06", 5366.2);
    expectedValue.put("2024-04-13", 5173.0);
    expectedValue.put("2024-04-20", 5294.8);
    expectedValue.put("2024-04-27", 5282.2);
    expectedValue.put("2024-05-04", 5348.0);
    assertEquals(expectedValue, stockModel.getPortfolioData("Jake",
            this.convertDate("2024-03-09"), this.convertDate("2024-05-09"), "Weeks"));
  }

  @Test
  public void testTimeStampDays() {
    LocalDate start = LocalDate.of(2015, 1, 1);
    LocalDate end = LocalDate.of(2015, 1, 29);
    assertEquals("Days", stockModel.getTimeStamp(start, end));
  }

  @Test
  public void testTimeStampWeeks() {
    LocalDate start = LocalDate.of(2015, 1, 1);
    LocalDate end = LocalDate.of(2015, 1, 31);
    assertEquals("Weeks", stockModel.getTimeStamp(start, end));
  }

  @Test
  public void testTimeStampMonths() {
    LocalDate start = LocalDate.of(2016, 1, 1);
    LocalDate end = LocalDate.of(2017, 2, 28);
    assertEquals("Months", stockModel.getTimeStamp(start, end));
  }

  @Test
  public void testTimeStamp2Months() {
    LocalDate start = LocalDate.of(2015, 1, 1);
    LocalDate end = LocalDate.of(2019, 1, 31);
    assertEquals("Two months", stockModel.getTimeStamp(start, end));
  }

  @Test
  public void testTimeStampYears() {
    LocalDate start = LocalDate.of(2015, 1, 1);
    LocalDate end = LocalDate.of(2024, 5, 24);
    assertEquals("Years", stockModel.getTimeStamp(start, end));
  }

}
