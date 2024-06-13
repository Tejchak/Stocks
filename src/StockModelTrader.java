import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface StockModelTrader extends StockModel {


    @Override
    public double getBoughtShares(String portfolioName, String stockSymbol, LocalDate currentDate);

    public boolean portfolioContainsStock(String portfolioName, String stockSymbol);

    public Map<String, Double> getPortfolioData(String portfolioName,
                                                LocalDate startDate,
                                                LocalDate endDate, String timeStamp);

    public String getTimeStamp(LocalDate start, LocalDate end);

    public LocalDate getLatestSellDate(String portfolioName, String stockSymbol);

    public LocalDate convertDate(String date);

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
     *
     */
    public ArrayList<String> getListStocks(String name, LocalDate date);

    public LocalDate moveToRecentTradingDay(LocalDate date);

    public void portfolioToXML(String filePath);

    public void loadPortfolioFromXML(String xmlFilePath);
  }
