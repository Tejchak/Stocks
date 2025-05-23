import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation of the Stock model.
 * Uses the AlphaVantage api to access stock data.
 * Also keeps track of stocks that have once been used while being able to pull
 * from a collections of CSV files.
 */
public class StockModelImpl implements StockModel {
  private final String apiKey;
  private final List<Portfolio> portfolios;
  private Map<String, String[]> stocks;

  /**
   * Constructor for the model, initializes the key as
   * the key from Alpha Vantage. Also creates the list of
   * portfolios that will be stored in the program.
   * Further the hashmap is the storage of stock info.
   */
  StockModelImpl() {
    this.apiKey = "QCLLY08TISZBMXL9";
    this.portfolios = new ArrayList<Portfolio>();
    this.stocks = new HashMap<String, String[]>();
  }

  /**
   * Gets the information about a stock given its name.
   *
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return The information of the stock as a string array.
   */
  @Override
  public String[] getStockData(String stockSymbol) {
    if (this.stocks.containsKey(stockSymbol)) {
      return this.stocks.get(stockSymbol);
    }
    try {
      String[] stockData = this.getStockDataCSV(stockSymbol);
      this.stocks.put(stockSymbol, stockData);
      return stockData;
    } catch (Exception e) {
      String[] stockData = this.getStockDataAPI(stockSymbol);
      this.stocks.put(stockSymbol, stockData);
      return stockData;
    }
  }

  //gets the stock data from a csv file in the resources root folder.
  protected String[] getStockDataCSV(String stocksymbol) {
    StringBuilder result = new StringBuilder();
    URL url = null;

    try {
      url = getClass().getClassLoader().getResource(stocksymbol + ".csv");
    } catch (NullPointerException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    try {
      InputStream is = url.openStream();
      int b;

      while ((b = is.read()) != -1) {
        result.append((char) b);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not find file " + stocksymbol + ".csv");
    }
    String[] stockData = result.toString().split("\n");
    Collections.reverse(Arrays.asList(stockData));
    return stockData;
  }

  //gets the stock data from an online API.
  protected String[] getStockDataAPI(String stockSymbol) {
    URL url = null;
    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey=" + this.apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
    //System.out.println(output.toString());
    return output.toString().split("\n");
  }

  /**
   * Compares the name of a stock to our sources to see if it exists.
   *
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return true if the stock exists, false if not.
   */
  @Override
  public boolean checkStockExists(String stockSymbol) {
    String[] stockData;
    try {
      stockData = getStockData(stockSymbol);
      if (stockData[1].contains("\"Error Message\":") || stockData[1].contains("Thank you for "
              + "using Alpha Vantage!")) {
        return false;
      }
      if (stockData.length < 10) {
        return false;
      }
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Calculates the total value of a portfolio.
   *
   * @param n    the name of the portfolio.
   * @param date the date on which the value is being calculated.
   * @return the total value in USD.
   */
  @Override
  public double calculatePortfolio(String n, LocalDate date) {
    double result = 0.0;
    Portfolio portfolio = null;
    for (Portfolio p : this.portfolios) {
      while (portfolio == null) {
        if (p.name.equals(n)) {
          portfolio = p;
        }
      }
    }
    if (portfolio != null) {
      for (String stockSymbol : portfolio.stocks.keySet()) {
        String[] stockData = getStockData(stockSymbol);
        Double value = 0.0;
        for (int i = 0; i < stockData.length; i++) {
          String line = stockData[i];
          if (line.substring(0, 10).equals(date)) {
            String[] sections = line.split(",");
            value = Double.parseDouble(sections[4]);
          }
        }
        result += (value * portfolio.stocks.getOrDefault(stockSymbol, 0));
      }
    }
    return Math.round(result * 100) / 100.0;
  }


  /**
   * Adds the given stock to the given portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param shares        the amount of shares pf the given stock.
   */
  @Override
  public void addStockToPortfolio(String portfolioName, String stockSymbol, int shares) {
    for (Portfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        p.stocks.put(stockSymbol, p.stocks.getOrDefault(stockSymbol, 0) + shares);
      }
    }
  }

  /**
   * Gets the line of the given date.
   *
   * @param stockData the data about a stock.
   * @param date      the date as a String (YYYY-MM-DD).
   * @return the line on a given date split up into its parts.
   */
  @Override
  public String[] getLine(String[] stockData, String date) {
    for (String line : stockData) {
      if (line.length() > 9) {
        if (line.substring(0, 10).equals(date)) {
          String[] sections = line.split(",");
          return sections;
        }
      }
    }
    throw new IllegalArgumentException("Date does not exist for stock");
  }

  /**
   * Calculates the gain or loss of a stock from the startdate to the enddate.
   *
   * @param stockData     the data about a stock.
   * @param startDateLine the starting date line.
   * @param endDateLine   the end date line.
   * @return the gain or loss of the stock as a double.
   */
  @Override
  public double stockGainLoss(String[] stockData, String[] startDateLine, String[] endDateLine) {
    double startPrice = Double.parseDouble(startDateLine[4]);
    double endPrice = Double.parseDouble(endDateLine[4]);
    double gainLoss = endPrice - startPrice;
    return Math.round(gainLoss * 100) / 100.0;
  }

  /**
   * Calculates the x-day moving average of a stock on a given day and with a
   * given x.
   *
   * @param stockData the data about a stock.
   * @param startDate the starting date as a String (YYYY-MM-DD).
   * @param xDays     the amount of days the moving average is being calculated for.
   * @return the x-day moving average.
   */
  @Override
  public double movingAverage(String[] stockData, String startDate, int xDays) {
    double movingAverage = 0.0;
    int startIndex = -1;
    for (int i = 0; i < stockData.length; i++) {
      String line = stockData[i];
      if (line.substring(0, 10).equals(startDate)) {
        String[] sections = line.split(",");
        startIndex = i;
      }
    }
    int notCounted = -1;
    for (int j = startIndex; j < startIndex + xDays; j++) {
      if (j < stockData.length - 1) {
        String line = stockData[j];
        String[] sections = line.split(",");
        movingAverage += Double.parseDouble(sections[4]);
      } else {
        notCounted = j;
      }
    }
    if (notCounted > xDays) {
      xDays = xDays - (notCounted - xDays);
    }
    movingAverage /= xDays;
    return Math.round(movingAverage * 100) / 100.0;
  }

  /**
   * Creates a string builder with all of the days that are crossovers.
   *
   * @param stockData the data about a stock.
   * @param startDate the starting date as a String (YYYY-MM-DD).
   * @param endDate   the ending date as a String (YYYY-MM-DD).
   * @param xDays     the amount of days the moving average is being calculated
   *                  for to find the moving average.
   * @return the crossover dates.
   */
  @Override
  public StringBuilder xDayCrossover(String[] stockData,
                                     String startDate, String endDate, int xDays) {
    StringBuilder crossovers = new StringBuilder();
    int startIndex = -1;
    int endIndex = -2;
    for (int i = 0; i < stockData.length; i++) {
      String line = stockData[i];
      if (line.substring(0, 10).equals(startDate)) {
        endIndex = i;
      }
      if (line.substring(0, 10).equals(endDate)) {
        startIndex = i;
      }
    }
    for (int j = startIndex; j < endIndex + 1; j++) {
      String line = stockData[j];
      String[] sections = line.split(",");
      if (this.isCrossover(stockData, sections[0], xDays, Double.parseDouble(sections[4]))) {
        crossovers.append(sections[0]).append(", ");
      }
    }
    return crossovers;

  }

  /**
   * Removes the given stock from a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param shares        the amount of shares pf the given stock.
   */
  @Override
  public void removeStockFromPortfolio(String portfolioName, String stockSymbol, int shares) {
    for (Portfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        p.stocks.put(stockSymbol, p.stocks.getOrDefault(stockSymbol, 0) - shares);
      }
      if (p.stocks.get(stockSymbol) <= 0) {
        p.stocks.remove(stockSymbol);
      }
    }
  }

  //Checks if the highest price is above the moving average.
  private boolean isCrossover(String[] stockData,
                              String startDate, int xDays, Double highestPrice) {
    return highestPrice - this.movingAverage(stockData, startDate, xDays) > 0;
  }

  /**
   * Checks if a portfolio exists in the model.
   *
   * @param n the name of the portfolio we're looking for.
   * @return true if it exists in the model, false if not.
   */
  @Override
  public boolean existingPortfolio(String n) {
    for (Portfolio p : this.portfolios) {
      if (p.name.equals(n)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Gets the closing value of a given stock or throws an exception
   * if the date or stock name does not exist.
   *
   * @param stockSymbol the stock for which the closing value is being found.
   * @param date        the date on which the value is being found.
   * @return the double closing value of the stock on the day.
   */
  @Override
  public ArrayList<BetterPortfolio> getPortfolios() {
    ArrayList<BetterPortfolio> result = new ArrayList<>();
    for (BetterPortfolio p : this.portfolios) {
      BetterPortfolio port = new BetterPortfolio(p.name);
      port.purchases = new HashMap<>(p.purchases);
      port.sales = new HashMap<>(p.sales);
      result.add(port);
    }
    return result;
  }

  @Override
  public String portfolioAsDistribution(String pName, Date date) {
    HashMap<String, Double> result = new HashMap<String, Double>();
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(pName)) {
        for (String symbol :p.purchases.keySet()) {
          if (!result.containsKey(symbol)) {
            result.put(symbol, (this.getBoughtShares(pName, symbol, date) -
                    this.getSoldShares(pName, symbol, date)) * getClosingValue(symbol, date));
          }
        }
      }
    }
    return Arrays.toString(result.toString().split(","));
  }

  //gets the closing value of a stock on a given day
  public double getClosingValue(String stockSymbol, LocalDate date) {
    String[] stockData = getStockData(stockSymbol);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (String line : stockData) {
      String[] sections = line.split(",");
      if (sections[0].equals(date.format(formatter))) {
        return Double.parseDouble(sections[4]);
      }
    }
    throw new IllegalArgumentException("Date does not exist for stock");
  }
}



