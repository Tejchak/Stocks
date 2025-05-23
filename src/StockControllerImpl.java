import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the stock model that gets inputs and calls the model at certain points.
 */
public class StockControllerImpl implements StockController {
  protected final StockModelTrader model;
  protected final StockView view;
  protected final Scanner scanner;

  /**
   * Constructor that takes in a model and view so it can send to both
   * and a readable.
   *
   * @param model    the model for the stock program.
   * @param view     the view for the stock progrqm.
   * @param readable the information that the scanner will be taking.
   */
  public StockControllerImpl(StockModelTrader model, StockView view, Readable readable) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(readable);
  }

  /**
   * Starts the main program loop, displaying the menu and handling user input.
   */
  @Override
  public void startProgram() {
    view.displayResult("Welcome to the Stocks Program!");
    boolean endProgram = false;
    while (!endProgram) {
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

  /**
   * Gets a string response from the user after a message prompt.
   *
   * @param prompt a String prompt that gets printed.
   * @return the string response of the user.
   */
  protected String getStringInput(String prompt) {
    view.displayResult(prompt);
    return this.scanner.nextLine();
  }

  /**
   * Prompts the user to enter a stock symbol and returns the input.
   *
   * @return the stock symbol entered by the user.
   */
  protected String getStockSymbol() {
    return getStringInput("Type the stock symbol (case insensitive, e.g., GOOG or goog):")
            .toUpperCase();
  }

  /**
   * Prompts the user to enter a date and returns the input.
   *
   * @param prompt a String prompt that specifies the type of date (start/end).
   * @return the date entered by the user.
   */
  protected String getDate(String prompt) {
    view.displayResult("What is the " + prompt + " date");
    int year = getValidPositiveNum("Type the year (e.g., 2017):");
    int month = getValidPositiveNum("Type the month (e.g., 1):");
    int day = getValidPositiveNum("Type the day (e.g., 1):");
    String formattedDate = String.format("%04d-%02d-%02d", year, month, day);
    return formattedDate;
  }

  protected LocalDate getValidLocalDate(String prompt, String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = null;
    while (true) {
      try {
        localDate = LocalDate.parse(date, formatter);
        break;
      } catch (Exception e) {
        view.displayResult("Invalid date, please try again (year, then month, then day)");
        date = getDate(prompt);
      }
    }
    return localDate;
  }

  /**
   * Prompts the user to enter a positive number and validates the input.
   *
   * @param prompt a String prompt that specifies what the user should enter.
   * @return a valid positive number entered by the user.
   */
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
        } else {
          view.displayResult("Invalid input. Please enter a positive integer.");
        }

      } else {
        view.displayResult("Invalid input. Please enter a positive integer.");
        scanner.next();
      }
    }
    return input;
  }

  /**
   * Handles the portfolio menu options, including creating, adding,
   * removing, and calculating portfolio values.
   */
  protected void handlePortfolioMenu() {
    view.portfolioMenu();
    int option = getValidPositiveNum("Please enter a number between 1 and 9");
    while (option != 1 && option != 9 && model.getPortfolios().isEmpty()) {
      view.displayResult("Must have an existing portfolio "
              + "before you can add, remove, or calculate.");
      option = getValidPositiveNum("Please enter 1 or 9");
    }
    switch (option) {
      case 1:
        handleNewPortfolio();
        break;
      case 2:
        int shares;
        String portfolioName = getStringInput(
                "Enter the name of the portfolio you would like to add to: ");
        while (!model.existingPortfolio(portfolioName)) {
          portfolioName = getStringInput("Portfolio " + portfolioName
                  + " does not exist. Please enter another name.");
        }
        String stockSymbol = getStockSymbol();
        shares = getValidPositiveNum("How many shares would you like to get"
                + "(you can only purchase whole shares):");
        model.addStockToPortfolio(portfolioName, stockSymbol, shares);
        break;
      case 3:
        int share;
        String pName = getStringInput(
                "Enter the name of the portfolio you would like to take away from: ");
        while (!model.existingPortfolio(pName)) {
          pName = getStringInput("Portfolio " + pName
                  + " does not exist. Please enter another name.");
        }
        String symbol = getStockSymbol();
        share = getValidPositiveNum("How many shares would you like to remove" +
                "(you can only purchase whole shares):");
        model.removeStockFromPortfolio(pName, symbol, share);
        break;
      case 4:
        String n = getStringInput("Enter the name of the portfolio you "
                + "would like to calculate the value of: ");
        while (!model.existingPortfolio(n)) {
          n = getStringInput("Portfolio " + n +
                  " does not exist. Please enter another name.");
        }
        String date = getDate("date you would like "
                + "to calculate the value on: ");
        view.displayResult(n + " is worth $" + model.calculatePortfolio(n,
                model.convertDate(date)));
        break;
      default:
        view.displayResult("Invalid input. Please enter a valid number.");
    }
  }

  /**
   * Handles the creation of a new portfolio
   * by prompting the user for details and updating the model.
   */
  protected void handleNewPortfolio() {
    String name = getStringInput("Type the name for your portfolio");
    view.displayResult("You must have atleast one stock in your portfolio");
    String stockSymbol = getStockSymbol();
    while (!model.checkStockExists(stockSymbol)) {
      view.displayResult("Sorry it appears your stock doesn't exist in out database");
      stockSymbol = getStockSymbol();
    }
    String[] stockData = model.getStockData(stockSymbol);
    int shares = getValidPositiveNum("How many shares would you like to get" +
            "(you can only purchase whole shares):");
    String purchaseDate = getDate("purchase: ");
    String[] purchaseDateLine = getValidTradingDay(stockData, purchaseDate, "purchase");
    LocalDate correctDate = model.convertDate(purchaseDateLine[0]);
    StockPurchase currentPurchase = new StockPurchase(shares, correctDate);
    model.createPortfolio(name, stockSymbol, currentPurchase);
    view.displayResult("Successfully created portfolio");
  }

  /**
   * Validates the end date to ensure it is after the start date and is a trading day.
   *
   * @param stockData the stock data.
   * @param startDate the start date.
   * @param endLine   the end line.
   * @return a valid end line.
   */
  private String[] getValidEndDate(String[] stockData, String startDate,
                                   String[] endLine) {
    while (true) {
      String endDate = endLine[0];
      int startYear = Integer.parseInt(startDate.substring(0, 4));
      int endYear = Integer.parseInt(endDate.substring(0, 4));
      int startMonth = Integer.parseInt(startDate.substring(5, 7));
      int endMonth = Integer.parseInt(endDate.substring(5, 7));
      int startDay = Integer.parseInt(startDate.substring(8));
      int endDay = Integer.parseInt(endDate.substring(8));
      if (startYear > endYear || (startYear == endYear && startMonth > endMonth) ||
              (startYear == endYear && startMonth == endMonth && startDay >= endDay)) {
        view.displayResult("End date must be after start date");
        endLine = getValidTradingDay(stockData, getDate("end"), "end");
      } else {
        break;
      }
    }
    return endLine;
  }

  /**
   * Validates the given date to ensure it is a trading day.
   *
   * @param stockData the stock data.
   * @param date      the date to validate.
   * @param startEnd  indicates if the date is start or end.
   * @return a valid trading day line.
   */
  protected String[] getValidTradingDay(String[] stockData, String date, String startEnd) {
    String[] line;
    while (true) {
      try {
        line = model.getLine(stockData, date);
        break;
      } catch (IllegalArgumentException e) {
        view.displayResult("Sorry the date you entered wasn't a trading day. Please make sure you "
                + "you have the correct format and try again.");
        date = getDate(startEnd);
      }
    }
    return line;
  }

  /**
   * Validates the stock symbol to ensure it exists in the database.
   *
   * @param stockSymbol the stock symbol to validate.
   * @return the stock data for the valid stock symbol.
   */
  protected String[] getValidStock(String stockSymbol) {
    String[] stockData;
    while (true) {
      try {
        stockData = model.getStockData(stockSymbol);
        if (!stockData[1].contains("\"Error Message\":") && !stockData[1].contains("Thank you for "
                + "using Alpha Vantage!")) {
          break;
        }
        else {
          if (stockData[1].contains("\"Error Message\":")) {
            view.displayResult("Your symbol doesn't exist in our database. Please try again.");
          } else {
            view.displayResult("Your symbol does not exist in our local folder and cannot query API"
                    + "right now so please try again later or input a different symbol in our local"
                    + " database");
          }
          stockSymbol = getStockSymbol();
        }
      } catch (IllegalArgumentException e) {
        view.displayResult("Your symbol doesn't exist in our database. Please try again.");
        stockSymbol = getStockSymbol();
      }
    }
    return stockData;
  }

  /**
   * Handles the gain/loss calculation for a specified stock and date range.
   */
  private void handleGainLoss() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    String[] startDateLine = getValidTradingDay(stockData, startDate, "start");
    String endDate = getDate("end");
    String[] endLine = getValidTradingDay(stockData, endDate, "end");
    String[] endDateLine = getValidEndDate(stockData, startDateLine[0], endLine);
    double gainLoss = model.stockGainLoss(stockData, startDateLine, endDateLine);
    view.displayResult("The gain/loss over that period of time is $" + gainLoss);
  }

  //Handles the moving average and assigns the variables for the model.
  private void handleMovingAverage() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    String earliestDate = stockData[stockData.length - 2].substring(0, 11);
    view.displayResult("The moving average will be calulated over the days we have, and days " +
            "prior to " + earliestDate + " will not be included in calculating the moving average");
    int xDays = getValidPositiveNum("Type the number of days for moving average:");
    double movingAverage = model.movingAverage(stockData, startDate, xDays);
    view.displayResult("The " + xDays + "-day moving average is $" + movingAverage);
  }

  //Handles the crossover
  private void handleCrossover() {
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    String startDate = getDate("start");
    startDate = getValidTradingDay(stockData, startDate, "start")[0];
    String endDate = getDate("end");
    String[] endLine = getValidTradingDay(stockData, endDate, "end");
    endDate = getValidEndDate(stockData, startDate, endLine)[0];
    String earliestDate = stockData[stockData.length - 2].substring(0, 11);
    view.displayResult("The moving average will be calulated over the days we have, and days " +
            "prior to " + earliestDate + " will not be included in calculating the moving average");
    int xDays = getValidPositiveNum("Type the number of days for moving average:");
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