import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * A new that extends the previous StockModel interface. Since it extends the old interface and
 * implements its own public methods, any classes of that implement StockModelTrader will be
 * guaranteed to have all of the functions from both StockModel and StockModelTrader, allowing
 * all of those functions to be called from the controller. We chose to make this new interface
 * so as to adhere to SOLID principles and minimize our changes to the existing StockModel as much
 * as possible.
 */
public interface StockModelTrader extends StockModel {


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
     * Gets the data for a portfolio over a given period as a map. Each value in the map is a date.
     * as a string and the corresponding value is the portfolio' value at that date.
     * @param pName determines the portfolio we're getting data for.
     * @param start determines the start date (first key-value pair in map).
     * @param end determines the end date (last key-value pair in map).
     * @param timeStamp determines how we.
     * @return A map of the key value pairs.
     */
    public Map<String, Double> getPortfolioData(String pName,
                                                LocalDate start,
                                                LocalDate end, String timeStamp);

    /**
     * Gets a copy of the portfolios field that does not
     * modify the original.
     */
    public List<BetterPortfolio> getPortfolios();

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
     * Gets the most recent date of a sale from a given portfolio of the
     * given stock.
     * @param pName the name of the portfolio.
     * @param stockSymbol the stock being found.
     * @return the date of the most recent sale.
     */
    public LocalDate getLatestSellDate(String pName, String stockSymbol);

    /**
     * Method that converts the date from a string to a LocalDate.
     * @param date the string fromat of a date.
     * @return the Local date from the given string.
     */
    public LocalDate convertDate(String date);

    /**
     * Creates a portfolio, and adds the shares of the given stock.
     *
     * @param name        the name of the portfolio.
     * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
     * @param purchase      the purchase being added.
     */
    public void createPortfolio(String name, String stockSymbol, StockPurchase purchase);

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
     * Gets the amount of sold shares of a given stock in a portfolio
     * only gets the shares that have been sold before the given date.
     * @param name the name of the portfolio.
     * @param stockSymbol the symbol for the stock.
     * @param currentDate the date that the shares are being found on.
     * @return the amount of sold shares.
     */
    public double getSoldShares(String name, String stockSymbol, LocalDate currentDate);

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
    public void rebalancePortfolio(Map<String, Double> weights, String name, LocalDate date);

    /**
     * Removes the given stock from a portfolio.
     *
     * @param portfolioName the name of the portfolio.
     * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
     * @param stockSale        the sale being added.
     */
    public void sellStock(String portfolioName, String stockSymbol, StockSale stockSale);

    /**
     * Adds the given stock to the portfolio.
     *
     * @param portfolioName the name of the portfolio.
     * @param stockSymbol   the symbol of a stock as a string (Ex, AMC).
     * @param stockPurchase        the purchase being added.
     */
    public void buyStock(String portfolioName, String stockSymbol, StockPurchase stockPurchase);

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
  }
