import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class StockModelNew extends StockModelImpl implements StockModelTrader {
  private final List<BetterPortfolio> portfolios;

  /**
   * Constructor for the model, initializes the key as
   * the key from Alpha Vantage. Also creates the list of
   * portfolios that will be stored in the program.
   * Further the hashmap is the storage of stock info.
   */
  StockModelNew() {
    String apiKey = "QCLLY08TISZBMXL9";
    this.portfolios = new ArrayList<>();
    Map<String, String[]> stocks = new HashMap<String, String[]>();
  }

  /**
   * Checks if a portfolio contains a given stock.
   *
   * @param pName       the name of the portfolio.
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

  /**
   * Calculates the total value of a portfolio on the given date.
   *
   * @param n    the name of the portfolio.
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
        List<StockPurchase> l = portfolio.purchases.getOrDefault(stockSymbol,
                new ArrayList<StockPurchase>());
        for (StockPurchase p : l) {
          if (!p.getPurchaseDate().isAfter(date)) {
            result += (value * p.getShares());
          }
        }
        List<StockSale> l2 = portfolio.sales.getOrDefault(stockSymbol,
                new ArrayList<StockSale>());
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
   *
   * @param start the starting date.
   * @param end   the ending date.
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
   *
   * @param date the string fromat of a date.
   * @return the Local date from the given string.
   */
  public LocalDate convertDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate newDate = LocalDate.parse(date, formatter);
    return newDate;
  }

  /**
   * Gets the data for a portfolio over a given period as a map. Each value in the map is a date
   * as a string and the corresponding value is the portfolio' value at that date.
   *
   * @param pName     determines the portfolio we're getting data for
   * @param start     determines the start date (first key-value pair in map)
   * @param end       determines the end date (last key-value pair in map)
   * @param timeStamp determines how we
   * @return
   */
  @Override
  public Map<String, Double> getPortfolioData(String pName, LocalDate start,
                                              LocalDate end, String timeStamp) {
    Map<String, Double> data = new LinkedHashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate currentDate = start;
    double currentValue = 0.0;
    while (currentDate.isBefore(end)) {
      currentValue = calculatePortfolio(pName, moveToRecentTradingDay(currentDate));
      data.put(currentDate.format(formatter), currentValue);
      switch (timeStamp) {
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
   *
   * @param name        the name of the portfolio.
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
   *
   * @param pName       the name of the portfolio.
   * @param stockSymbol the stock being found.
   * @return the date of the most recent sale.
   */
  public LocalDate getLatestSellDate(String pName, String stockSymbol) {
    LocalDate latestDate = LocalDate.of(0000, 1, 1);
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

  @Override
  public double getSoldShares(String name, String stockSymbol, LocalDate currentDate) {
    double totalShares = 0;
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(name)) {
        for (StockSale sale : p.sales.getOrDefault(stockSymbol, new ArrayList<>())) {
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
   *
   * @param name        the name of the portfolio.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @param purchase    the StockPurchases being made with the
   */
  @Override
  public void createPortfolio(String name, String stockSymbol, StockPurchase purchase) {
    BetterPortfolio p = new BetterPortfolio(name);
    List<StockPurchase> addition = new ArrayList<StockPurchase>();
    addition.add(purchase);
    p.purchases.put(stockSymbol, addition);
    this.portfolios.add(p);
  }

  /**
   * Removes the given stock from a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param stockSale     the stockSale being added
   */
  @Override
  public void sellStock(String portfolioName, String stockSymbol, StockSale stockSale) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        List<StockSale> soldList = p.sales.getOrDefault(stockSymbol, new ArrayList<>());
        soldList.add(stockSale);
        p.sales.put(stockSymbol, soldList);
      }
    }
  }

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
   * Adds the given stock to the portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param stockPurchase the stockPurchase
   */
  @Override
  public void buyStock(String portfolioName, String stockSymbol,
                       StockPurchase stockPurchase) {
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(portfolioName)) {
        List<StockPurchase> purchasesList = p.purchases.getOrDefault(stockSymbol,
                new ArrayList<>());
        purchasesList.add(stockPurchase);
        p.purchases.put(stockSymbol, purchasesList);
      }
    }
  }

  /**
   * Checks if a portfolio exists in the model.
   *
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
    List<BetterPortfolio> result = new ArrayList<>();
    for (BetterPortfolio p : this.portfolios) {
      BetterPortfolio port = new BetterPortfolio(p.name);
      port.purchases = new HashMap<>(p.purchases);
      port.sales = new HashMap<>(p.sales);
      result.add(port);
    }
    return result;
  }

  /**
   * Gets the portfolio as a composition. Gets each stock in a portfolio finds the number of shares
   * and matches them together then displays them in the form {stock=shares}.
   *
   * @param pName the name of the portfolio.
   * @param date  the date for the composition.
   * @return the distribution as an array.
   */
  @Override
  public String[] portfolioComposition(String pName, LocalDate date) {
    Map<String, Double> result = new HashMap<String, Double>();
    date = moveToRecentTradingDay(date);
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(pName)) {
        for (String symbol : p.purchases.keySet()) {
          if (!result.containsKey(symbol)) {
            result.put(symbol, Math.round((this.getBoughtShares(pName, symbol, date)
                    - this.getSoldShares(pName, symbol, date)) * 100) / 100.0);
          }
        }
      }
    }
    return result.toString().split(",");
  }

  /**
   * Gets the portfolio as a distribution. Gets each stock in a portfolio finds it's value
   * and matches them together then displays them in the form {stock=value}.
   *
   * @param pName the name of the portfolio.
   * @param date  the date for the distribution.
   * @return the distribution as an array.
   */
  @Override
  public String[] portfolioAsDistribution(String pName, LocalDate date) {
    Map<String, Double> result = new HashMap<String, Double>();
    date = moveToRecentTradingDay(date);
    for (BetterPortfolio p : this.portfolios) {
      if (p.name.equals(pName)) {
        for (String symbol : p.purchases.keySet()) {
          if (!result.containsKey(symbol)) {
            result.put(symbol, Math.round((this.getBoughtShares(pName, symbol, date)
                    - this.getSoldShares(pName, symbol, date)) * getClosingValue(symbol, date)
                    * 100) / 100.0);
          }
        }
      }
    }
    return result.toString().split(",");
  }

  /**
   * Gets the closing value on given date.
   *
   * @param stockSymbol the stock for which the closing value is being found.
   * @param date        the date on which the value is being found.
   * @return the closing value as a double.
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

  //gets a specific portfolio.
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
   *
   * @param weights a hashmap of stocksymbols(String) to their weights as decimals(double).
   * @param name    the name of the portfolio being rebalanced.
   * @param date    the date that the rebalancing will take place on.
   */
  @Override
  public void rebalancePortfolio(Map<String, Double> weights, String name, LocalDate date) {
    double total = this.calculatePortfolio(name, date);
    date = moveToRecentTradingDay(date);
    for (String stocksymbol : weights.keySet()) {
      double currentVal = (this.getBoughtShares(name, stocksymbol, date)
              - this.getSoldShares(name, stocksymbol, date))
              * getClosingValue(stocksymbol, date);
      double goalVal = total * weights.get(stocksymbol);
      if (goalVal > currentVal) {
        double shares = (goalVal - currentVal) / getClosingValue(stocksymbol, date);
        StockPurchase purchase = new StockPurchase(shares, date);
        List<StockPurchase> newPurchase = this.getPortfolio(name).purchases.get(stocksymbol);
        newPurchase.add(purchase);
        this.getPortfolio(name).purchases.put(stocksymbol, newPurchase);
      }
      if (goalVal < currentVal) {
        double shares2 = (currentVal - goalVal) / getClosingValue(stocksymbol, date);
        StockSale sale = new StockSale(shares2, date);
        List<StockSale> newSale = this.getPortfolio(name)
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
   *
   * @param name the name of the portfolio where the stocks are being pulled from.
   * @param date the date of which these stocks exist.
   * @return the list of stocks that currently exist.
   */
  @Override
  public List<String> getListStocks(String name, LocalDate date) {
    List<String> result = new ArrayList<>();
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
   *
   * @param date the date being moved.
   * @return the edited date.
   */
  @Override
  public LocalDate moveToRecentTradingDay(LocalDate date) {
    while (true) {
      try {
        this.getClosingValue("GOOG", date);
        break;
      } catch (Exception e) {
        System.out.println("here");
        if (date.isBefore(LocalDate.of(2013, 6, 8))) {
          date = LocalDate.of(2013, 8, 19);
        }
        else {
          date = date.minusDays(1);
        }
      }
    }
    return date;
  }

  /**
   * Saves the current state of the portfolios to an XML file.
   * Will create a new file if the file doesn't exist, if the
   * file does exist it will add to it with a new portfolio.
   *
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
   *
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
              String stockSymbol = purchaseElement.getElementsByTagName("symbol").item(0)
                      .getTextContent();
              double shares = Double.parseDouble(purchaseElement.getElementsByTagName("shares")
                      .item(0).getTextContent());
              LocalDate purchaseDate = LocalDate.parse(purchaseElement.getElementsByTagName("date")
                      .item(0).getTextContent());

              StockPurchase stockPurchase = new StockPurchase(shares, purchaseDate);
              portfolio.purchases.computeIfAbsent(stockSymbol, k -> new ArrayList<>())
                      .add(stockPurchase);
            }
          }

          NodeList saleList = portfolioElement.getElementsByTagName("sale");
          for (int j = 0; j < saleList.getLength(); j++) {
            Node saleNode = saleList.item(j);
            if (saleNode.getNodeType() == Node.ELEMENT_NODE) {
              Element saleElement = (Element) saleNode;
              String stockSymbol = saleElement.getElementsByTagName("symbol").item(0)
                      .getTextContent();
              double shares = Double.parseDouble(saleElement.getElementsByTagName("shares")
                      .item(0).getTextContent());
              LocalDate saleDate = LocalDate.parse(saleElement.getElementsByTagName("date")
                      .item(0).getTextContent());

              StockSale stockSale = new StockSale(shares, saleDate);
              portfolio.sales.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(stockSale);
            }
          }

          this.portfolios.add(portfolio);
        }
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("File not found");
    }
  }
}