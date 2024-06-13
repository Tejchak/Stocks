import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface StockModelTrader extends StockModel {


    public double getBoughtShares(String portfolioName, String stockSymbol, LocalDate currentDate);

    public boolean portfolioContainsStock(String portfolioName, String stockSymbol);

    public Map<String, Double> getPortfolioData(String portfolioName,
                                                LocalDate startDate,
                                                LocalDate endDate, String timeStamp);
    /**
     * Gets a copy of the portfolios field.
     */
    public ArrayList<BetterPortfolio> getPortfolios();

    public String getTimeStamp(LocalDate start, LocalDate end);

    public LocalDate getLatestSellDate(String portfolioName, String stockSymbol);

    public LocalDate convertDate(String date);

    /**
     * Creates a portfolio, and adds the shares of the given stock.
     *
     * @param name        the name of the portfolio.
     * @param stockSymbol the symbol of a stock as a string (Ex, AMC).
     * @param purchase      the purchase being added.
     */
    public void createPortfolio(String name, String stockSymbol, StockPurchase purchase);

    public void removeSales(String portfolioName, String stockSymbol, LocalDate sellDate);

    public double getSoldShares(String name, String StockSymbol, LocalDate currentDate);

    /**
     *
     * @param date
     * @return
     */
    public String[] portfolioAsDistribution(String pName, LocalDate date);

    /**
     *
     */
    public void rebalancePortfolio(HashMap<String, Double> weights, String name, LocalDate date);

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
     *
     */
    public ArrayList<String> getListStocks(String name, LocalDate date);

    public LocalDate moveToRecentTradingDay(LocalDate date);

    public void portfolioToXML(String filePath);

    public void loadPortfolioFromXML(String xmlFilePath);
  }
