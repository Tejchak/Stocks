import java.io.FileNotFoundException;
import java.util.Arrays;

public class StockControllerImpl implements StockController{
  private final StockModel model;
  private final StockView view;

  public StockControllerImpl(StockModel model, StockView view) {
    this.model = model;
    this.view = view;
  }

  public void startProgram() {
    view.displayResult("Welcome to the Stocks Program!");
    boolean endProgram = false;
    while(!endProgram) {
      int option = view.createMenu();
      switch (option) {
        case 1:
          handleGainLoss();
          break;
        case 2:
          handleMovingAverage();
          break;
        case 3:
          handleCrossover();
          break;
        case 4:
          this.handlePortfolioMenu();
          break;
        case 5:
          endProgram = true;
          break;
        default:
          view.displayResult("Invalid Number: Please pick between options 1 through 5");
      }
    }
  }

  private void handlePortfolioMenu() {
    int option = view.portfolioMenu();
    switch(option) {
      case 1:
        handleNewPortfolio();
      case 2:
        addStockToPortfolio();
    }
  }

  private void addStockToPortfolio() {

  }

  private void handleNewPortfolio() {
    String name = view.getStringInput("Type the name for your portfolio");
    view.displayResult("You must have atleast one stock in your portfolio");
    String stockSymbol = view.getStockSymbol();
    while (!model.checkStockExists(stockSymbol)) {
      view.displayResult("Sorry it appears your stock doesn't exist in out database");
      stockSymbol = view.getStockSymbol();
    }
    model.createPortfolio(name, stockSymbol);
    view.displayResult("Successfully created portfolio");
  }

  private String[] getValidTradingDay(String[] stockData, String date, String startEnd) {
    String[] line;
    while (true) {
      try {
        line = model.getLine(stockData, date);
        break;
      } catch (IllegalArgumentException e) {
        view.displayResult("Sorry the date you entered wasn't a trading day. Please make sure you "
                + "you have the correct format and try again: E.G 2024-05-09");
        date = view.getDate(startEnd);
      }
    }
    return line;
  }
  private String[] getValidStock(String stockSymbol) {
    String[] stockData;
    while (true) {
      try {
        stockData = model.getStockData(stockSymbol);
       // System.out.println(Arrays.toString(stockData));
        if (!stockData[1].contains("\"Error Message\":")) {
          break;
        }
        else {
          view.displayResult("Your symbol doesn't exist in our database. Please try again.");
          stockSymbol = view.getStockSymbol();
        }
      }
      catch (IllegalArgumentException e) {
        view.displayResult("Your symbol doesn't exist in our database. Please try again.");
        stockSymbol = view.getStockSymbol();
      }
    }
    return stockData;
  }

  private void handleGainLoss() {
    String stockSymbol = view.getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = view.getDate("start");
    String[] startDateLine = getValidTradingDay(stockData, startDate, "start");
    String endDate = view.getDate("end");
    String[] endDateLine = getValidTradingDay(stockData, endDate, "end");
    double gainLoss = model.stockGainLoss(model.getStockData(stockSymbol), startDateLine, endDateLine);
    view.displayResult("The gain/loss over that period of time is " + gainLoss);
  }

  private void handleMovingAverage() {
    String stockSymbol = view.getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = view.getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    int xDays = view.getValidPositiveNum("Type the number of days for moving average:");
    double movingAverage = model.movingAverage(model.getStockData(stockSymbol), startDate, xDays);
    view.displayResult("The " + xDays + "-day moving average is " + movingAverage);
  }

  private void handleCrossover() {
    String stockSymbol = view.getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = view.getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    String endDate = view.getDate("end");
    endDate = getValidTradingDay(stockData, endDate, "end")[0];
    int xDays =  view.getValidPositiveNum("Type the number of days for moving average:");
    StringBuilder crossovers = model.xDayCrossover(stockData, startDate, endDate, xDays);
    if (crossovers.length() >= 2) {
      crossovers.delete(crossovers.length() - 2, crossovers.length());
    }
    if (crossovers.length() == 0) {
      view.displayResult("There are no crossovers in the specified range");
    }
    else {
      view.displayResult("The following are x-day Crossovers: " + crossovers.toString());
    }
  }

}