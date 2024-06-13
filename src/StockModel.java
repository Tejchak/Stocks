import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Interface for a model that can do certain calculations about a stock or,
 * add a stock and shares to a portfolio.
 */
public interface StockModel {

  /**
   * Gets the information about a stock given its name.
   *
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return The information of the stock as a string array.
   */
  public String[] getStockData(String stockSymbol);

  /**
   * Adds the given stock to the portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param stockPurchase        the amount of shares pf the given stock.
   */
  public void addStockToPortfolio(String portfolioName, String stockSymbol,
                                  StockPurchase stockPurchase);

  /**
   * Gets the amount of bought shares of a given stock in a portfolio
   * only gets the shares that have been bought before the given date.
   * @param name the name of the portfolio.
   * @param stockSymbol the symbol for the stock.
   * @param currentDate the date that the shares are being found on.
   * @return the amount of bought shares.
   */
  public double getBoughtShares(String name, String stockSymbol, LocalDate currentDate);

  /**
   * Checks if a portfolio contains a given stock.
   * @param pName the name of the portfolio.
   * @param stockSymbol the ticker of the stock.
   * @return true if the portfolio contains the stock.
   */
  public boolean portfolioContainsStock(String pName, String stockSymbol);

  /**
   * Gets the data for a portfolio as a map, tej can you please.
   * @param pName
   * @param start
   * @param end
   * @param timeStamp
   * @return
   */
  public Map<String, Double> getPortfolioData(String pName,
                                              LocalDate start,
                                              LocalDate end, String timeStamp);

  /**
   * Gets the units that will be used for a barchart based
   * on the amount of time between the start date
   * and the end date.
   * @param start the starting date.
   * @param end the ending date.
   * @return the String representing a time stamp between these times.
   */
  public String getTimeStamp(LocalDate start, LocalDate end);

  /**
   *
   * @param portfolioName
   * @param stockSymbol
   * @return
   */
  public LocalDate getLatestSellDate(String portfolioName, String stockSymbol);

  /**
   * Gets the line of the given date.
   *
   * @param stockData the data about a stock.
   * @param date      the date as a String (YYYY-MM-DD).
   * @return the line on a given date split up into its parts.
   */
  public String[] getLine(String[] stockData, String date);

  /**
   * Creates a portfolio, and adds the shares of the given stock.
   *
   * @param name        the name of the portfolio.
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @param shares      the amount of shares pf the given stock.
   */
  public void createPortfolio(String name, String stockSymbol, StockPurchase shares);

  /**
   * Compares the name of a stock to our sources to see if it exists.
   *
   * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
   * @return true if the stock exists, false if not.
   */
  public boolean checkStockExists(String stockSymbol);

  /**
   * Removes the given stock from a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
   * @param sale        the stockSale being added.
   */
  public void removeStockFromPortfolio(String portfolioName,
                                       String stockSymbol, StockSale sale);

  /**
   * Method that converts the date from a string to a LocalDate.
   * @param date the string fromat of a date.
   * @return the Local date from the given string.
   */
  public LocalDate convertDate(String date);

  /**
   * Removes all of the sales of a given stock in a portfolio after
   * a given date, this is so users can go back on sales they
   * previously made.
   * @param portfolioName the name of the portfolio.
   * @param stockSymbol the stock being removed.
   * @param sellDate the date of which the sales are after.
   */
  public void removeSales(String portfolioName, String stockSymbol, LocalDate sellDate);

  /**
   * Checks if a portfolio exists in the model.
   * @param n the name of the portfolio we're looking for.
   * @return true if it exists in the model, false if not.
   */
  public boolean existingPortfolio(String n);

  /**
   * Calculates the total value of a portfolio.
   *
   * @param n    the name of the portfolio.
   * @param date the date on which the value is being calculated.
   * @return the total value in USD.
   */
  public double calculatePortfolio(String n, LocalDate date);

  /**
   * Calculates the gain or loss of a stock from the startdate to the enddate.
   *
   * @param stockData     the data about a stock.
   * @param startDateLine the starting date line.
   * @param endDateLine   the end date line.
   * @return the gain or loss of the stock as a double.
   */
  public double stockGainLoss(String[] stockData, String[] startDateLine, String[] endDateLine);

  /**
   * Calculates the x-day moving average of a stock on a given day and with a
   * given x.
   *
   * @param stockData the data about a stock.
   * @param startDate the starting date as a String (YYYY-MM-DD).
   * @param xDays     the amount of days the moving average is being calculated for.
   * @return the x-day moving average.
   */
  public double movingAverage(String[] stockData, String startDate, int xDays);

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
  public StringBuilder xDayCrossover(String[] stockData,
                                     String startDate, String endDate, int xDays);

  /**
   * Gets a copy of the portfolios field that does not
   * modify the original.
   */
  public List<BetterPortfolio> getPortfolios();

  /**
   * Gets the amount of sold shares of a given stock in a portfolio
   * only gets the shares that have been sold before the given date.
   * @param name the name of the portfolio.
   * @param StockSymbol the symbol for the stock.
   * @param currentDate the date that the shares are being found on.
   * @return the amount of sold shares.
   */
  public double getSoldShares(String name, String StockSymbol, LocalDate currentDate);

  /**
   * Gets the portfolio as a distribution. Gets each stock in a portfolio finds it's value
   * and matches them together then displays them in the form {stock=value}.
   * @param pName the name of the portfolio.
   * @param date the date for the distribution.
   * @return the distribution as an array.
   */
  public String[] portfolioAsDistribution(String pName, LocalDate date);

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
  public void rebalancePortfolio(HashMap<String, Double> weights, String name, LocalDate date);

  /**
   * Gets a String list of stocks that are currently available to be sold,
   * i.e there purchase date is before ethe given date and there sale date
   * is not before.
   * @param name the name of the portfolio where the stocks are being pulled from.
   * @param date the date of which these stocks exist.
   * @return the list of stocks that currently exist.
   */
  public List<String> getListStocks(String name, LocalDate date);

  /**
   * Moves a date from the weekend to a weekday.
   * There are some drawbacks to this method
   * because it does not account for holidays unfortunately.
   * @param date the date being moved.
   * @return the edited date.
   */
  public LocalDate moveToRecentTradingDay(LocalDate date);

  /**
   * Saves the current state of the portfolios to an XML file.
   * Will create a new file if the file doesn't exist, if the
   * file does exist it will add to it with a new portfolio.
   * @param filePath the name of the file to be saved.
   */
  public void portfolioToXML(String filePath);

  /**
   * Loads the portfolios from an XML file.
   * Will iterate through each portfolio in an xml file and pull the correct data
   * for each one.
   * @param xmlFilePath the string representing the path to the desired xml.
   */
  public void loadPortfolioFromXML(String xmlFilePath);

  /**
   * Gets the closing value of a given stock or throws an exception
   * if the date or stock name does not exist.
   * @param stockSymbol the stock for which the closing value is being found.
   * @param date the date on which the value is being found.
   * @return the double closing value of the stock on the day.
   */
  public double getClosingValue(String stockSymbol, LocalDate date);
}
