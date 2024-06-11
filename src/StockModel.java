import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

  public double getBoughtShares(String portfolioName, String stockSymbol, Date currentDate);

  public boolean portfolioContainsStock(String portfolioName, String stockSymbol);

  public Map<String, Double> getPortfolioData(String portfolioName,
                                              LocalDate startDate,
                                              LocalDate endDate, String timeStamp);

  public String getTimeStamp(Period period);

  public Date getLatestSellDate(String portfolioName, String stockSymbol);

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

  public Date convertDate(String date);

  public void removeSales(String portfolioName, String stockSymbol, Date sellDate);

  /**
   * Checks if a portfolio exists in the model.
   *
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
  public double calculatePortfolio(String n, Date date);

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
   * Gets a copy of the portfolios field.
   */
  public ArrayList<BetterPortfolio> getPortfolios();

  public double getSoldShares(String name, String StockSymbol, Date currentDate);

  /**
   *
   * @param date
   * @return
   */
  public String portfolioAsDistribution(String pName, Date date);
}
