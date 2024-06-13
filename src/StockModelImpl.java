import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The implementation of the Stock model. This specific implementation uses the alphavantage api
 * and a resources root folder to obtain its stocks. Furthermore we also gave it a stocks field
 * to store all the stocks that have thus far been called. We also enforced the constraint that you
 * cannot have a portfolio with without a stock in the portfolio, and when calculating any kind
 * of moving average, if the x days ends up going further back than what we have data for,
 * those days will be not be used when calculating the moving average. The portfolios list is a
 * list of portfolios, portfolios is a class we created to create and manage stock portfolios. The
 * ApiKey is used to access Alphavantage, it is a free one so there are a limited number of
 * querries (which is why we have the csv files and stocks field)
 */
public class StockModelImpl implements StockModel {
  private final String apiKey;
  private final List<BetterPortfolio> portfolios;
  private Map<String, String[]> stocks;

  /**
   * Constructor for the model, initializes the key as
   * the key from Alpha Vantage. Also creates the list of
   * portfolios that will be stored in the program.
   * Further the hashmap is the storage of stock info.
   */
  StockModelImpl() {
    this.apiKey = "QCLLY08TISZBMXL9";
    this.portfolios = new ArrayList<>();
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

  /**
   * Checks if a portfolio contains a given stock.
   * @param pName the name of the portfolio.
   * @param stockSymbol the ticker of the stock.
   * @return true if the portfolio contains the stock.
   */
  @Override
  public boolean portfolioContainsStock(String pName, String stockSymbol) {
    for (BetterPortfolio portfolio : this.portfolios) {
      if (portfolio.name.equals(pName)) {
        return portfolio.purchases.containsKey(stockSymbol);
      }
    }
    throw new RuntimeException("Could not find portfolio with name " + pName);
  }

//gets the stock data from a csv file in the resources root folder.
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

  //gets the stock data from an online API (Alphavantage)
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
   //System.out.println(output.toString());
    return output.toString().split("\n");
  }

  /**
   * Compares the name of a stock to our sources to see if it exists.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return true if the stock exists, false if not.
   */
  @Override
  public boolean checkStockExists(String stockSymbol) {
    try {
      String[] data = getStockData(stockSymbol);
      if (data.length < 5) {
        return false;
      }
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Calculates the total value of a portfolio on the given date.
   * @param n the name of the portfolio.
   * @param date the date on which the value is being calculated.
   * @return the total value in USD.
   */
  @Override
  public double calculatePortfolio(String n, LocalDate date) {
    date = moveToRecentTradingDay(date);
    double result = 0.0;
    BetterPortfolio portfolio = null;
    for (BetterPortfolio p : this.portfolios) {
        if (p.name.equals(n)) {
          portfolio = p;
      }
    }
    if (portfolio != null) {
      for (String stockSymbol : portfolio.purchases.keySet()) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] stockData = getStockData(stockSymbol);
        Double value = 0.0;
        for (int i = 0; i < stockData.length; i++) {
          String line = stockData[i];
          if (line.substring(0, 10).equals(date.format(formatter))) {
            String[] sections = line.split(",");
            value = Double.parseDouble(sections[4]);
          }
        }
        ArrayList<StockPurchase> l = portfolio.purchases.getOrDefault(stockSymbol, new ArrayList<StockPurchase>());
        for (StockPurchase p : l) {
          if (!p.getPurchaseDate().isAfter(date)) {
            result += (value * p.getShares());
          }
        }
        ArrayList<StockSale> l2 = portfolio.sales.getOrDefault(stockSymbol, new ArrayList<StockSale>());
        for (StockSale s : l2) {
          if (!s.getSaledate().isAfter(date)) {
            result -= (value * s.getShares());
          }
        }
      }
    }
    return Math.round(result * 100) / 100.0;
  }

  /**
   * Gets the units that will be used for a barchart based
   * on the amount of time between the start date
   * and the end date.
   * @param start the starting date.
   * @param end the ending date.
   * @return the String representing a time stamp between these times.
   */
  @Override
  public String getTimeStamp(LocalDate start, LocalDate end) {
    if (ChronoUnit.YEARS.between(start, end) >= 5) {
      return "Years";
    }
    if (ChronoUnit.MONTHS.between(start, end) >= 5) {
      if (ChronoUnit.MONTHS.between(start, end) <= 29) {
        return "Months";
      }
      return "Two months";
    }
    if (ChronoUnit.DAYS.between(start, end) >= 1) {
      if (ChronoUnit.DAYS.between(start, end) <= 29) {
        return "Days";
      }
      return "Weeks";
    }
    return "";
  }

  /**
   * Method that converts the date from a string to a LocalDate.
   * @param date the string format of a date.
   * @return the Local date from the given string.
   */
  @Override
  public LocalDate convertDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate newDate = LocalDate.parse(date, formatter);
    return newDate;
  }

  /**
   * Gets the data for a portfolio as a map, tej can you please.
   * @param pName
   * @param start
   * @param end
   * @param timeStamp
   * @return
   */
  @Override
  public Map<String, Double> getPortfolioData(String pName, LocalDate start, LocalDate end, String timeStamp) {
    Map<String, Double> data = new LinkedHashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate currentDate = start;
    double currentValue = 0.0;
    while (currentDate.isBefore(end)) {
      currentValue = calculatePortfolio(pName, moveToRecentTradingDay(currentDate));
      data.put(currentDate.format(formatter), currentValue);
      switch(timeStamp) {
        case "Years":
          currentDate = currentDate.plusYears(1);
          break;
        case "Months":
          currentDate = currentDate.plusMonths(1);
          break;
        case "Two months":
          currentDate = currentDate.plusMonths(2);
          break;
        case "Weeks":
          currentDate = currentDate.plusWeeks(1);
          break;
        case "Days":
          currentDate = currentDate.plusDays(1);
          break;
        default:
          throw new RuntimeException("Unknown time stamp: " + timeStamp);
      }
    }
    return data;
  }

  /**
   * Gets the amount of bought shares of a given stock in a portfolio
   * only gets the shares that have been bought before the given date.
   * @param name the name of the portfolio.
   * @param stockSymbol the symbol for the stock.
   * @param currentDate the date that the shares are being found on.
   * @return the amount of bought shares.
   */
  @Override
  public double getBoughtShares(String name, String stockSymbol, LocalDate currentDate) {
    double totalShares = 0;
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(name)) {
        for (StockPurchase purchase : p.purchases.getOrDefault(stockSymbol, new ArrayList<>())) {
          if (!purchase.getPurchaseDate().isAfter(currentDate)) {
            totalShares += purchase.getShares();
          }
        }
      }
    }
    return totalShares;
  }


  /**
   * Gets the most recent date of a sale from a given portfolio of the
   * given stock.
   * @param pName the name of the portfolio.
   * @param stockSymbol the stock being found.
   * @return the date of the most recent sale.
   */
  @Override
  public LocalDate getLatestSellDate(String pName, String stockSymbol) {
    LocalDate latestDate = null;
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(pName)) {
        for (StockSale sale : p.sales.getOrDefault(stockSymbol, new ArrayList<>())) {
          if (latestDate == null || sale.getSaledate().isAfter(latestDate)) {
            latestDate = sale.getSaledate();
          }
        }
      }
    }
    return latestDate;
  }

  /**
   * Gets the amount of sold shares of a given stock in a portfolio
   * only gets the shares that have been sold before the given date.
   * @param name the name of the portfolio.
   * @param StockSymbol the symbolf for the stock.
   * @param currentDate the date that the shares are being found on.
   * @return the amount of sold shares.
   */
  @Override
  public double getSoldShares(String name, String StockSymbol, LocalDate currentDate) {
    double totalShares = 0;
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(name)) {
        for (StockSale sale : p.sales.getOrDefault(StockSymbol, new ArrayList<>())) {
          if (!sale.getSaledate().isAfter(currentDate)) {
            totalShares += sale.getShares();
          }
        }
      }
    }
    return totalShares;
  }

  /**
   * Creates a portfolio, and adds the given amount shares of the given stock.
   * @param name the name of the portfolio.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @param purchase the StockPurchases being made with the
   */
  @Override
  public void createPortfolio(String name, String stockSymbol, StockPurchase purchase) {
    BetterPortfolio p = new BetterPortfolio(name);
    ArrayList<StockPurchase> addition = new ArrayList<StockPurchase>();
    addition.add(purchase);
    p.purchases.put(stockSymbol, addition);
    this.portfolios.add(p);
  }

  /**
   * Removes the given stock from a portfolio.
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @param stockSale the stockSale being added
   */
  @Override
  public void removeStockFromPortfolio(String portfolioName, String stockSymbol, StockSale stockSale) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        ArrayList<StockSale> soldList = p.sales.getOrDefault(stockSymbol, new ArrayList<>());
        soldList.add(stockSale);
        p.sales.put(stockSymbol, soldList);
      }
    }
  }

  /**
   * Removes all of the sales of a given stock in a portfolio after
   * a given date, this is so users can go back on sales they
   * previously made.
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol the stock being removed.
   * @param sellDate the date of which the sales are after.
   */
  @Override
  public void removeSales(String portfolioName, String stockSymbol, LocalDate sellDate) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        for (int i = 0; i < p.sales.get(stockSymbol).size(); i++) {
          StockSale sale = p.sales.get(stockSymbol).get(i);
          if (sale.getSaledate().isAfter(sellDate)) {
            p.sales.get(stockSymbol).remove(sale);
          }
        }
      }
      }
  }

  /**
   * Adds the given stock to the given portfolio.
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @param stockPurchase the stockPurchase containing the shares and the date.
   */
  @Override
  public void addStockToPortfolio(String portfolioName, String stockSymbol,
                                  StockPurchase stockPurchase) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        ArrayList<StockPurchase> purchasesList = p.purchases.getOrDefault(stockSymbol, new ArrayList<>());
        purchasesList.add(stockPurchase);
        p.purchases.put(stockSymbol, purchasesList);
      }
    }
  }

  /**
   * Gets the line of the given date.
   * @param stockData the data about a stock.
   * @param date the date as a String (YYYY-MM-DD).
   * @return the line on a given date split up into its parts.
   */
  @Override
  public String[] getLine(String[] stockData, String date) {
    for (String line: stockData) {
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
   * @param stockData the data about a stock.
   * @param startDateLine the starting date line.
   * @param endDateLine the end date line.
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
   * @param stockData the data about a stock.
   * @param startDate the starting date as a String (YYYY-MM-DD).
   * @param xDays the amount of days the moving average is being calculated for.
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
      }
      else {
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
   * @param stockData the data about a stock.
   * @param startDate the starting date as a String (YYYY-MM-DD).
   * @param endDate the ending date as a String (YYYY-MM-DD).
   * @param xDays the amount of days the moving average is being calculated
   *              for to find the moving average.
   * @return the crossover dates.
   */
  @Override
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

  //Checks if the highest price is above the moving average.
  private boolean isCrossover(String[] stockData, String startDate, int xDays, Double highestPrice) {
    return highestPrice - this.movingAverage(stockData, startDate, xDays) > 0;
  }

  /**
   * Checks if a portfolio exists in the model.
   * @param n the name of the portfolio we're looking for.
   * @return true if it exists in the model, false if not.
   */
  @Override
  public boolean existingPortfolio(String n) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(n)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets a copy of the portfolios field that does not
   * modify the original.
   */
  @Override
  public List<BetterPortfolio> getPortfolios() {
    ArrayList<BetterPortfolio> result = new ArrayList<>();
    for (BetterPortfolio p : this.portfolios) {
      BetterPortfolio port = new BetterPortfolio(p.name);
      port.purchases = new HashMap<>(p.purchases);
      port.sales = new HashMap<>(p.sales);
      result.add(port);
    }
    return result;
  }

  /**
   * Gets the portfolio as a distribution. Gets each stock in a portfolio finds it's value
   * and matches them together then displays them in the form {stock=value}.
   * @param pName the name of the portfolio.
   * @param date the date for the distribution.
   * @return the distribution as an array.
   */
  @Override
  public String[] portfolioAsDistribution(String pName, LocalDate date) {
    HashMap<String, Double> result = new HashMap<String, Double>();
    date = moveToRecentTradingDay(date);
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(pName)) {
        for (String symbol :p.purchases.keySet()) {
          if (!result.containsKey(symbol)) {
            result.put(symbol, Math.round((this.getBoughtShares(pName, symbol, date) -
                    this.getSoldShares(pName, symbol, date)) * getClosingValue(symbol, date)
                    * 100) / 100.0);
          }
        }
      }
    }
    return result.toString().split(",");
  }

  /**
   * Gets the closing value of a given stock or throws an exception
   * if the date or stock name does not exist.
   * @param stockSymbol the stock for which the closing value is being found.
   * @param date the date on which the value is being found.
   * @return the double closing value of the stock on the day.
   */
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

  //gets a portfolio from a name
  private BetterPortfolio getPortfolio(String name) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(name)) {
        return p;
      }
    }
    throw new IllegalArgumentException("Portfolio does not exist");
  }

  /**
   * This method rebalances a portfolio to contain the correct value of each stock.
   * It takes in the weights as a hashmap with the stock they represent, the name
   * of the portfolio and the date that the rebalancing is taking place on.
   * The method finds the current value of the stock in the portfolio and
   * compares it to the goal value, based on this comparison it will
   * either buy or sell stock.
   * @param weights a hashmap of stocksymbols(String) to their weights as decimals(double).
   * @param name the name of the portfolio being rebalanced.
   * @param date the date that the rebalancing will take place on.
   */
  @Override
  public void rebalancePortfolio(HashMap<String, Double> weights, String name, LocalDate date) {
    double total = this.calculatePortfolio(name, date);
    date = moveToRecentTradingDay(date);
     for (String stocksymbol : weights.keySet()) {
       double currentVal = (this.getBoughtShares(name, stocksymbol, date) - this.getSoldShares(name, stocksymbol, date))
               * getClosingValue(stocksymbol, date);
       double goalVal = total * weights.get(stocksymbol);
       if (goalVal > currentVal) {
         double shares = (goalVal - currentVal) / getClosingValue(stocksymbol, date);
         StockPurchase purchase = new StockPurchase(shares, date);
         ArrayList<StockPurchase> newPurchase = this.getPortfolio(name)
                 .purchases.getOrDefault(stocksymbol, new ArrayList<>());
         newPurchase.add(purchase);
         this.getPortfolio(name).purchases.put(stocksymbol, newPurchase);
       }
       if (goalVal < currentVal) {
           double shares2 = (currentVal - goalVal)/getClosingValue(stocksymbol, date);
           StockSale sale = new StockSale(shares2, date);
           ArrayList<StockSale> newSale = this.getPortfolio(name)
                   .sales.getOrDefault(stocksymbol, new ArrayList<>());
           newSale.add(sale);
           this.getPortfolio(name).sales.put(stocksymbol, newSale);
       }
     }
  }

  /**
   * Gets a String list of stocks that are currently available to be sold,
   * i.e there purchase date is before ethe given date and there sale date
   * is not before.
   * @param name the name of the portfolio where the stocks are being pulled from.
   * @param date the date of which these stocks exist.
   * @return the list of stocks that currently exist.
   */
  @Override
  public List<String> getListStocks(String name, LocalDate date) {
    ArrayList<String> result = new ArrayList<>();
    BetterPortfolio p = getPortfolio(name);
    for (String symbol : p.purchases.keySet()) {
      if ((this.getBoughtShares(name, symbol, date) - this.getSoldShares(name, symbol, date)) > 0) {
        result.add(symbol);
      }
    }
    return result;
  }

  /**
   * Moves a date from the weekend to a weekday.
   * There are some drawbacks to this method
   * because it does not account for holidays unfortunately.
   * @param date the date being moved.
   * @return the edited date.
   */
  @Override
  public LocalDate moveToRecentTradingDay(LocalDate date) {
    if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
      return date.minusDays(1);
    }
    if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      return date.minusDays(2);
    }
    return date;
  }


  /**
   * Saves the current state of the portfolios to an XML file.
   * Will create a new file if the file doesn't exist, if the
   * file does exist it will add to it with a new portfolio.
   * @param filePath the name of the file to be saved.
   */
  @Override
  public void portfolioToXML(String filePath) {
    try {
      File xmlFile = new File(filePath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc;

      // Check if the file exists and has content
      if (xmlFile.exists() && xmlFile.length() > 0) {
        doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
      } else {
        doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("portfolios");
        doc.appendChild(rootElement);
      }

      Element rootElement = doc.getDocumentElement();

      for (BetterPortfolio portfolio : portfolios) {
        Element portfolioElement = doc.createElement("portfolio");
        portfolioElement.setAttribute("name", portfolio.name);
        rootElement.appendChild(portfolioElement);

        for (String stockSymbol : portfolio.purchases.keySet()) {
          List<StockPurchase> stockPurchases = portfolio.purchases.get(stockSymbol);
          for (StockPurchase purchase : stockPurchases) {
            Element purchaseElement = doc.createElement("purchase");
            portfolioElement.appendChild(purchaseElement);

            Element symbolElement = doc.createElement("symbol");
            symbolElement.appendChild(doc.createTextNode(stockSymbol));
            purchaseElement.appendChild(symbolElement);

            Element sharesElement = doc.createElement("shares");
            sharesElement.appendChild(doc.createTextNode(Double.toString(purchase.getShares())));
            purchaseElement.appendChild(sharesElement);

            Element dateElement = doc.createElement("date");
            dateElement.appendChild(doc.createTextNode(purchase.getPurchaseDate().toString()));
            purchaseElement.appendChild(dateElement);
          }
        }

        for (String stockSymbol : portfolio.sales.keySet()) {
          List<StockSale> stockSales = portfolio.sales.get(stockSymbol);
          for (StockSale sale : stockSales) {
            Element saleElement = doc.createElement("sale");
            portfolioElement.appendChild(saleElement);

            Element symbolElement = doc.createElement("symbol");
            symbolElement.appendChild(doc.createTextNode(stockSymbol));
            saleElement.appendChild(symbolElement);

            Element sharesElement = doc.createElement("shares");
            sharesElement.appendChild(doc.createTextNode(Double.toString(sale.getShares())));
            saleElement.appendChild(sharesElement);

            Element dateElement = doc.createElement("date");
            dateElement.appendChild(doc.createTextNode(sale.getSaledate().toString()));
            saleElement.appendChild(dateElement);
          }
        }
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(xmlFile);
      transformer.transform(source, result);

      System.out.println("Portfolio data successfully appended to the XML file.");
    } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
      e.printStackTrace();
    }
  }


  /**
   * Loads the portfolios from an XML file.
   * Will iterate through each portfolio in an xml file and pull the correct data
   * for each one.
   * @param xmlFilePath the string representing the path to the desired xml.
   */
  @Override
  public void loadPortfolioFromXML(String xmlFilePath) {
    try {
      File xmlFile = new File(xmlFilePath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();


      NodeList portfolioList = doc.getElementsByTagName("portfolio");
      for (int i = 0; i < portfolioList.getLength(); i++) {
        Node portfolioNode = portfolioList.item(i);
        if (portfolioNode.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolioNode;
          String portfolioName = portfolioElement.getAttribute("name");

          BetterPortfolio portfolio = new BetterPortfolio(portfolioName);

          NodeList purchaseList = portfolioElement.getElementsByTagName("purchase");
          for (int j = 0; j < purchaseList.getLength(); j++) {
            Node purchaseNode = purchaseList.item(j);
            if (purchaseNode.getNodeType() == Node.ELEMENT_NODE) {
              Element purchaseElement = (Element) purchaseNode;
              String stockSymbol = purchaseElement.getElementsByTagName("symbol").item(0).getTextContent();
              double shares = Double.parseDouble(purchaseElement.getElementsByTagName("shares").item(0).getTextContent());
              LocalDate purchaseDate = LocalDate.parse(purchaseElement.getElementsByTagName("date").item(0).getTextContent());

              StockPurchase stockPurchase = new StockPurchase(shares, purchaseDate);
              portfolio.purchases.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(stockPurchase);
            }
          }

          NodeList saleList = portfolioElement.getElementsByTagName("sale");
          for (int j = 0; j < saleList.getLength(); j++) {
            Node saleNode = saleList.item(j);
            if (saleNode.getNodeType() == Node.ELEMENT_NODE) {
              Element saleElement = (Element) saleNode;
              String stockSymbol = saleElement.getElementsByTagName("symbol").item(0).getTextContent();
              double shares = Double.parseDouble(saleElement.getElementsByTagName("shares").item(0).getTextContent());
              LocalDate saleDate = LocalDate.parse(saleElement.getElementsByTagName("date").item(0).getTextContent());

              StockSale stockSale = new StockSale(shares, saleDate);
              portfolio.sales.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(stockSale);
            }
          }

          this.portfolios.add(portfolio);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error loading portfolio from the XML file.");
    }
  }
}