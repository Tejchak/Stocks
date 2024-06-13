import java.time.LocalDate;
import java.util.ArrayList;

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
   * @param shares        the amount of shares pf the given stock.
   */
  public void addStockToPortfolio(String portfolioName, String stockSymbol, int shares);

  /**
   * Gets the line of the given date.
   *
   * @param stockData the data about a stock.
   * @param date      the date as a String (YYYY-MM-DD).
   * @return the line on a given date split up into its parts.
   */
  public String[] getLine(String[] stockData, String date);

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
   * @param shares        the amount of shares pf the given stock.
   */
  public void removeStockFromPortfolio(String portfolioName, String stockSymbol, int shares);

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


  public LocalDate convertDate(String date);
}
