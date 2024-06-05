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

public class StockModel {
  private final String apiKey;
  private final ArrayList<Portfolio> portfolios;

  StockModel() {
    this.apiKey = "F99D5A7QDFY52B58";
    this.portfolios = new ArrayList<Portfolio>();
  }

  protected String[] getStockDataCSV(String stocksymbol) {
    StringBuilder result = new StringBuilder();
    URL url = null;

    try {
      url = getClass().getClassLoader().getResource(stocksymbol + ".csv");
    }
    catch (NullPointerException e) {
      throw new RuntimeException("The stock you entered is not in our database " +
              stocksymbol);
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
    String[] data = result.toString().split("\n");
    Collections.reverse(Arrays.asList(data));
    return data;
  }

  protected String[] getStockData(String stockSymbol) {
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

  protected boolean checkStockExists(String stockSymbol) {
    try {
      this.getStockDataCSV(stockSymbol);
    }
    catch (RuntimeException e) {
      try {
        this.getStockData(stockSymbol);
      }
      catch (RuntimeException e2) {
        return false;
      }
    }
    return true;
  }

  protected void createPortfolio(String name, String stockSymbol) {
    Portfolio p = new Portfolio(name);
    p.stocks.add(stockSymbol);
    this.portfolios.add(p);
  }

  protected Double stockGainLoss(String[] stockData, String startDate, String endDate) {
    Double startPrice = 0.0;
    Double endPrice = 0.0;
    for (String line: stockData) {
      if (line.substring(0,10).equals(startDate)) {
        String[] sections = line.split(",");
        startPrice = Double.parseDouble(sections[4]);
      }
      if (line.substring(0,10).equals(endDate)) {
        String[] sections = line.split(",");
        endPrice = Double.parseDouble(sections[4]);
      }
    }
    Double gainLoss = endPrice - startPrice;
    return gainLoss;
  }

  protected Double movingAverage(String[] stockData, String startDate, int xDays) {
    Double movingAverage = 0.0;
    int startIndex = -1;
    for (int i = 0; i < stockData.length; i++) {
      String line = stockData[i];
      if (line.substring(0, 10).equals(startDate)) {
        String[] sections = line.split(",");
        startIndex = i;
      }
    }
    for (int j = startIndex; j < startIndex + xDays; j++) {
      String line = stockData[j];
      String[] sections = line.split(",");
      movingAverage += Double.parseDouble(sections[4]);
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

}