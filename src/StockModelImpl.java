import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * The implementation of the Stock model.
 * Uses the AlphaVantage api to access stock data.
 * Also keeps track of stocks that have once been used while being able to pull
 * from a collections of CSV files.
 */
public class StockModelImpl implements StockModel {
  private final String apiKey;
  private final ArrayList<Portfolio> portfolios;
  private Map<String, String[]> stocks;

  /**
   * Constructor for the model, initializes the key as
   * the key from Alpha Vantage. Also creates the list of
   * portfolios that will be stored in the program.
   * Further the hashmap is the storage of stock info.
   */
  StockModelImpl() {
    this.apiKey = "F99D5A7QDFY52B58";
    this.portfolios = new ArrayList<Portfolio>();
    this.stocks = new HashMap<String, String[]>();
  }

  /**
   * Gets the information about a stock given its name.
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
    }
    catch (Exception e) {
        String[] stockData = this.getStockDataAPI(stockSymbol);
        this.stocks.put(stockSymbol, stockData);
        return stockData;
    }
  }

//gets the stock data from a csv file.
  protected String[] getStockDataCSV(String stocksymbol) {
    StringBuilder result = new StringBuilder();
    URL url = null;

    try {
      url = getClass().getClassLoader().getResource(stocksymbol + ".csv");
    }
    catch (NullPointerException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    try {
      InputStream is = url.openStream();
      int b;

      while ((b=is.read())!=-1) {
        result.append((char)b);
      }
    }
    catch (IOException e) {
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
              + "=" + stockSymbol + "&apikey="+this.apiKey+"&datatype=csv");
    }
    catch (MalformedURLException e) {
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

      while ((b=in.read())!=-1) {
        output.append((char)b);
      }
    }
    catch (IOException e) {
      throw new IllegalArgumentException("No price data found for "+stockSymbol);
    }
   // System.out.println(output.toString());
    return output.toString().split("\n");
  }

  /**
   * Compares the name of a stock to our sources to see if it exists.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return true if 
   */
  public boolean checkStockExists(String stockSymbol) {
    URL url = null;
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey="+this.apiKey+"&datatype=csv");
    }
    catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {

      in = url.openStream();
      int b;

      while ((b=in.read())!=-1) {
        output.append((char)b);
      }
    }
    catch (IOException e) {
      throw new IllegalArgumentException("No price data found for "+stockSymbol);
    }
    return !output.toString().contains("\"Error Message\":");
  }

  public double calculatePortfolio(String name, String date) {
    double result = 0.0;
    Portfolio portfolio = null;
    for (Portfolio p : this.portfolios) {
      while (portfolio == null) {
        if (p.name.equals(name)) {
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
    return result;
  }

  public void createPortfolio(String name, String stockSymbol, int shares) {
    Portfolio p = new Portfolio(name);
    p.stocks.put(stockSymbol, shares);
    this.portfolios.add(p);
  }

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

  public void addStockToPortfolio(String portfolioName, String stockSymbol, int shares) {
    for (Portfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        p.stocks.put(stockSymbol, p.stocks.getOrDefault(stockSymbol, 0) + shares);
      }
    }
  }

  public String[] getLine(String[] stockData, String date) {
    for (String line: stockData) {
      if (line.substring(0, 10).equals(date)) {
        String[] sections = line.split(",");
        return sections;
      }
    }
    throw new IllegalArgumentException("Date does not exist for stock");
  }

  public double stockGainLoss(String[] stockData, String[] startDateLine, String[] endDateLine) {
    double startPrice = Double.parseDouble(startDateLine[4]);
    double endPrice = Double.parseDouble(endDateLine[4]);
    double gainLoss = endPrice - startPrice;
    return gainLoss;
  }

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
    for (int j = startIndex; j < startIndex + xDays; j++) {
      if (j < stockData.length) {
        String line = stockData[j];
        String[] sections = line.split(",");
        movingAverage += Double.parseDouble(sections[4]);
      }
    }
    movingAverage /= xDays;
    return movingAverage;
  }

  public StringBuilder xDayCrossover(String[] stockData, String startDate, String endDate, int xDays) {
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

  private boolean isCrossover(String[] stockData, String startDate, int xDays, Double highestPrice) {
    return highestPrice - this.movingAverage(stockData, startDate, xDays) > 0;
  }

  public boolean existingPortfolio(String n) {
    for (Portfolio p : this.portfolios) {
      if (p.name.equals(n)) {
        return true;
      }
    }
    return false;
  }
}