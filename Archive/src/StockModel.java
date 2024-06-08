public interface StockModel {

  public String[] getStockData(String stockSymbol);

  public void addStockToPortfolio(String portfolioName, String stockSymbol, int shares);

  public String[] getLine(String[] stockData, String date);

  public void createPortfolio(String name, String stockSymbol, int shares);

  public boolean checkStockExists(String stockSymbol);

  public void removeStockFromPortfolio(String pName, String symbol, int share);

  public boolean existingPortfolio(String pName);

  public double calculatePortfolio(String n, String date);

  public double stockGainLoss(String[] stockData, String[] startDateLine, String[] endDateLine);

  public double movingAverage(String[] stockData, String startDate, int xDays);

  public StringBuilder xDayCrossover(String[] stockData, String startDate, String endDate, int xDays);
}
