import java.time.LocalDate;
import java.util.List;
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
   * Checks if a portfolio exists in the model.
   * @param n the name of the portfolio we're looking for.
   * @return true if it exists in the model, false if not.
   */
  public boolean existingPortfolio(String n);

  /**
   * Calculates the total value of a portfolio.
   *
   * @param n the name of the portfolio.
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
   * Gets the closing value of a given stock or throws an exception
   * if the date or stock name does not exist.
   * @param stockSymbol the stock for which the closing value is being found.
   * @param date the date on which the value is being found.
   * @return the double closing value of the stock on the day.
   */
  public double getClosingValue(String stockSymbol, LocalDate date);
}
