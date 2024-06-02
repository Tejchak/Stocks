import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ModelStock {
  private final String apiKey;

  ModelStock() {
    this.apiKey = "F99D5A7QDFY52B58";
  }


  public void startProgram() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the stocks program\n Here you can practice trading stocks!");
    System.out.println("What would you like to do today?");
    System.out.println("Would you like to examine the gain or loss of a specific stock? Type 1");
    System.out.println("Would you like to examine the x-day moving average of a stock for a "
            + "specified date and a specified value of x? Type 2");
    System.out.println("Would you like to find the x-day crossovers for a stock"
            + " over a specified date range and a specified value of x? Type 3");
    System.out.println("Would you like to examine or create a portfolio? Type 4");
    int path = scanner.nextInt();
    scanner.nextLine();

    switch (path) {
      case 1:
        this.stockGainLoss(scanner);
        break;
      case 2:
        System.out.println("x-day moving average");
        break;
      case 3:
        System.out.println("x-day crossovers");
        break;
      case 4:
        System.out.println("Examine or create portfolio");
        break;
    }

  }

  private void stockGainLoss(Scanner scanner) {
    System.out.println("Type the stock symbol. E.G, Google would be GOOG:");
    String stockSymbol = scanner.nextLine();
    System.out.println("Type the start date in the format: YYYY-MM-DD");
    String startDate = scanner.nextLine();
    System.out.println("Type the end date in the format: YYYY-MM-DD");
    String endDate = scanner.nextLine();
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
    String[] outputInLines = output.toString().split("\n");
    Double startprice = 0.0;
    Double endprice = 0.0;
    for (String line: outputInLines) {
      if (line.substring(0,10).equals(startDate)) {
        String[] sections = line.split(",");
        startprice = Double.parseDouble(sections[1]);
      }
      if (line.substring(0,10).equals(startDate)) {
        String[] sections = line.split(",");
        endprice = Double.parseDouble(sections[1]);
      }
    }
  //  System.out.println("Return value: ");
    System.out.println(output.toString());
    System.out.println(startprice);
  }

}