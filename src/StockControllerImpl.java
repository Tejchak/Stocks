import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockControllerImpl implements StockController {
  private final StockModelImpl model;
  private final StockView view;
  private final Scanner scanner;

  public StockControllerImpl(StockModelImpl model, StockView view, Scanner scanner) {
    this.model = model;
    this.view = view;
    this.scanner = scanner;
  }

  public String getStringInput(String prompt) {
    view.displayResult(prompt);
    return this.scanner.nextLine();
  }

  public String getStockSymbol() {
    return getStringInput("Type the stock symbol (e.g., GOOG):");
  }

  public String getDate(String prompt) {

    return getStringInput("Type the " + prompt + " date (YYYY-MM-DD):");
  }

  protected int getValidPositiveNum(String prompt) {
    int input = 0;
    boolean validInput = false;
    while (!validInput) {
      view.displayResult(prompt);
      if (scanner.hasNextInt()) {
        input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (input > 0) {
          validInput = true;
        }
        else {
          view.displayResult("Invalid input. Please enter a positive number.");
        }

      } else {
        view.displayResult("Invalid input. Please enter a positive number.");
        scanner.next();
      }
    }
    return input;
  }

  public void startProgram() {
    view.displayResult("Welcome to the Stocks Program!");
    boolean endProgram = false;
    while(!endProgram) {
      view.createMenu();
      int option = getValidPositiveNum("Please enter a number between 1 and 5");
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
    view.portfolioMenu();
    int option = getValidPositiveNum("Please enter a number between 1 and 4");
    switch(option) {
      case 1:
        handleNewPortfolio();
        break;
      case 2:
        int shares;
        String portfolioName = getStringInput("Enter the name of the portfolio you would like to add to: ");
        while (!model.existingPortfolio(portfolioName)) {
          portfolioName = getStringInput("Portfolio " + portfolioName +
                  " does not exist. Please enter another name.");
        }
        String stockSymbol = getStockSymbol();
        shares = getValidPositiveNum("How many shares would you like to get" +
                "(you can only purchase whole shares):");
        model.addStockToPortfolio(portfolioName, stockSymbol, shares);
        break;
      case 3:
        int share;
        String pName = getStringInput("Enter the name of the portfolio you would like to take away from: ");
        while (!model.existingPortfolio(pName)) {
          pName = getStringInput("Portfolio " + pName +
                  " does not exist. Please enter a name.");
        }
        String symbol = getStockSymbol();
        share = getValidPositiveNum("How many shares would you like to remove" +
                "(you can only purchase whole shares):");
        model.removeStockFromPortfolio(pName, symbol, share);
        break;
        case 4:
          String n = getStringInput("Enter the name of the portfolio you " +
                  "would like to calculate the value of: ");
          while (!model.existingPortfolio(n)) {
            n = getStringInput("Portfolio " + n +
                    " does not exist. Please enter a name.");
          }
          String date = getDate("date you would like " +
                  "to calculate the value on: ");
          view.displayResult(n + " is worth " + model.calculatePortfolio(n, date) + " USD");
    }
  }

  private void handleNewPortfolio() {
    String name = getStringInput("Type the name for your portfolio");
    view.displayResult("You must have atleast one stock in your portfolio");
    String stockSymbol = getStockSymbol();
    while (!model.checkStockExists(stockSymbol)) {
      view.displayResult("Sorry it appears your stock doesn't exist in out database");
      stockSymbol = getStockSymbol();
    }
    int shares = getValidPositiveNum("How many shares would you like to share get" +
            "(you can only purchase whole shares):");
    model.createPortfolio(name, stockSymbol, shares);
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
        date = getDate(startEnd);
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
        } else {
          view.displayResult("Your symbol doesn't exist in our database. Please try again.");
          stockSymbol = getStockSymbol();
        }
      } catch (IllegalArgumentException e) {
        view.displayResult("Your symbol doesn't exist in our database. Please try again.");
        stockSymbol = getStockSymbol();
      }
    }
    return stockData;
  }


  private void handleGainLoss() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    String[] startDateLine = getValidTradingDay(stockData, startDate, "start");
    String endDate = getDate("end");
    String[] endDateLine = getValidTradingDay(stockData, endDate, "end");
    double gainLoss = model.stockGainLoss(stockData, startDateLine, endDateLine);
    view.displayResult("The gain/loss over that period of time is " + gainLoss);
  }

  private void handleMovingAverage() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    int xDays = getValidPositiveNum("Type the number of days for moving average:");
    double movingAverage = model.movingAverage(stockData, startDate, xDays);
    view.displayResult("The " + xDays + "-day moving average is " + movingAverage);
  }

  private void handleCrossover() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    String endDate = getDate("end");
    endDate = getValidTradingDay(stockData, endDate, "end")[0];
    int xDays =  getValidPositiveNum("Type the number of days for moving average:");
    StringBuilder crossovers = model.xDayCrossover(stockData, startDate, endDate, xDays);
    if (crossovers.length() >= 2) {
      crossovers.delete(crossovers.length() - 2, crossovers.length());
    }
    if (crossovers.length() == 0) {
      view.displayResult("There are no crossovers in the specified range");
    } else {
      view.displayResult("The following are x-day Crossovers: " + crossovers.toString());
    }
  }

}