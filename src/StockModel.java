import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class StockModel {
  private final String apiKey;

  StockModel() {
    this.apiKey = "UKFRFIDTUCC65L7C.";
  }

  private String[] getStockData(String stockSymbol) {
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
    System.out.println(output.toString());
    return output.toString().split("\n");
  }

  protected Double stockGainLoss(String stockSymbol, String startDate, String endDate) {
    String[] outputInLines = this.getStockData(stockSymbol);
    System.out.println(Arrays.toString(outputInLines));
    Double startPrice = 0.0;
    Double endPrice = 0.0;
    for (String line: outputInLines) {
      if (line.substring(0,10).equals(startDate)) {
        String[] sections = line.split(",");
        startPrice = Double.parseDouble(sections[4]);
      }
      if (line.substring(0,10).equals(endDate)) {
        String[] sections = line.split(",");
        endPrice = Double.parseDouble(sections[4]);
      }
    }
    return endPrice - startPrice;
  }

  protected Double movingAverage(String stockSymbol, String startDate, int xDays) {
    String[] outputInLines = getStockData(stockSymbol);
    Double movingAverage = 0.0;
    int startIndex = -1;
    for (int i = 0; i < outputInLines.length; i++) {
      String line = outputInLines[i];
      if (line.substring(0, 10).equals(startDate)) {
        String[] sections = line.split(",");
        startIndex = i;
      }
    }
    for (int j = startIndex; j < startIndex + xDays; j++) {
      String line = outputInLines[j];
      String[] sections = line.split(",");
      movingAverage += Double.parseDouble(sections[4]);
    }
    movingAverage /= xDays;
    return movingAverage;
  }

  protected String xdayCrossover(String stockSymbol,
                                        String startDate, String endDate, int xDays) {
    String[] outputInLines = getStockData(stockSymbol);
    StringBuilder xdayCrossover = new StringBuilder();
    boolean inRange = false;
    for (int i = 0; i < outputInLines.length; i++) {
      String line = outputInLines[i];
      if (line.substring(0, 10).equals(startDate)) {
        inRange = true;
      }
      if (inRange) {
        String[] sections = line.split(",");
        if (Double.parseDouble(sections[4]) > this.movingAverage(stockSymbol, startDate, xDays)) {
          xdayCrossover.append(sections[0]).append(",");
        }
      }
      if (line.substring(0, 10).equals(startDate)) {
        inRange = false;
      }
    }
    return xdayCrossover.toString();
  }
}