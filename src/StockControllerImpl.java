
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the stock model that gets inputs and calls the model at certain points.
 */
public class StockControllerImpl implements StockController {
  private final StockModelTrader model;
  private final StockView view;
  private final Scanner scanner;

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
  private String getStringInput(String prompt) {
    view.displayResult(prompt);
    return this.scanner.nextLine();
  }

  /**
   * Prompts the user to enter a stock symbol and returns the input.
   *
   * @return the stock symbol entered by the user.
   */
  private String getStockSymbol() {
    return getStringInput("Type the stock symbol (case insensitive, e.g., GOOG or goog):")
            .toUpperCase();
  }

  /**
   * Prompts the user to enter a date and returns the input.
   *
   * @param prompt a String prompt that specifies the type of date (start/end).
   * @return the date entered by the user.
   */
  private String getDate(String prompt) {
    view.displayResult("What is the " + prompt + " date");
    int year = getValidPositiveNum("Type the year (e.g., 2017):");
    int month = getValidPositiveNum("Type the month (e.g., 1):");
    int day = getValidPositiveNum("Type the day (e.g., 1):");
    String formattedDate = String.format("%04d-%02d-%02d", year, month, day);
    return formattedDate;
  }

  private LocalDate getValidLocalDate(String prompt, String date) {
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

  private double getValidPositiveDouble(String prompt) {
    double input = 0.0;
    boolean validInput = false;
    while (!validInput) {
      view.displayResult(prompt);
      if (scanner.hasNextDouble()) {
        input = scanner.nextDouble();
        scanner.nextLine();
        if (input > 0) {
          validInput = true;
        } else {
          view.displayResult("Invalid input. Please enter a positive number.");
        }
      } else if (scanner.hasNextInt()) {
        input = scanner.nextInt();
        scanner.nextLine();
        if (input > 0) {
          validInput = true;
        } else {
          view.displayResult("Invalid input. Please enter a positive number.");
        }
      } else {
        view.displayResult("Invalid input. Please enter a positive number.");
        scanner.next();
      }
    }
    return input;
  }


  /**
   * Handles the portfolio menu options, including creating, adding,
   * removing, and calculating portfolio values.
   */
  private void handlePortfolioMenu() {
    view.portfolioMenu();
    int option = getValidPositiveNum("Please enter a number between 1 and 9");
    while (option != 1 && option != 9 && model.getPortfolios().isEmpty()) {
      view.displayResult("Must have an existing portfolio " +
              "before you can add, remove, or calculate.");
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
          portfolioName = getStringInput("Portfolio " + portfolioName +
                  " does not exist. Please enter another name.");
        }
        String stockSymbol = getStockSymbol();
        String[] stockData = getValidStock(stockSymbol);
        shares = getValidPositiveNum("How many shares would you like to get" +
                "(you can only purchase whole shares):");
        String purchaseDate = getDate("Enter the date you would like to purchase: ");
        String[] purchaseDateLine = getValidTradingDay(stockData, purchaseDate, "purchase");
        LocalDate correctDate = model.convertDate(purchaseDateLine[0]);
        StockPurchase currentPurchase = new StockPurchase(shares, correctDate);
        model.buyStock(portfolioName, stockSymbol, currentPurchase);
        break;
      case 3:
        double sellShares;
        String pName = getStringInput(
                "Enter the name of the portfolio you would like to sell stock from: ");
        while (!model.existingPortfolio(pName)) {
          pName = getStringInput("Portfolio " + pName +
                  " does not exist. Please enter another name.");
        }
        String symbol = getStockSymbol();
        while (!model.portfolioContainsStock(pName, symbol)) {
          view.displayResult("Sorry it appears you haven't bought any shares of that stock");
          symbol = getStockSymbol();
        }
        stockData = getValidStock(symbol);
        String sellDate = getDate("date you would like to sell (you must sell shares in "
                + "Chronological order!): ");
        String[] sellDateLine = getValidTradingDay(stockData, sellDate, "sell");
        LocalDate currentSellDate = model.convertDate(sellDateLine[0]);
        LocalDate finalDate = getValidSellDate(pName, symbol, currentSellDate, stockData);
        sellShares = getValidPositiveDouble("How many shares would you like to sell:");
        double availableShares = model.getBoughtShares(pName, symbol, finalDate) -
                model.getSoldShares(pName, symbol, finalDate);
        if (availableShares > 0) {
          while (availableShares < sellShares) {
            view.displayResult("Invalid number: you only have "
                    + availableShares + " shares available");
            sellShares = getValidPositiveDouble("How many shares would you like to sell:");
          }
          StockSale sale = new StockSale(sellShares, finalDate);
          model.sellStock(pName, symbol, sale);
        } else {
          view.displayResult("Invalid number, you have no shares available at this time.");
        }
        break;
      case 4:
        String n = getStringInput("Enter the name of the portfolio you " +
                "would like to calculate the value of: ");
        while (!model.existingPortfolio(n)) {
          n = getStringInput("Portfolio " + n +
                  " does not exist. Please enter another name.");
        }
        String date = getDate("date you would like " +
                "to calculate the value on: ");
        view.displayResult(n + " is worth $" + model.calculatePortfolio(n, model.convertDate(date)));
        break;
      case 5:
          String name = getStringInput("Enter the name of the portfolio you " +
                  "would like to see as a distribution: ");
          while (!model.existingPortfolio(name)) {
            name = getStringInput("Portfolio " + name +
                    " does not exist. Please enter another name.");
          }
          String valDate = getDate("date you would like to view this portfolio on (if "
                  + "it as a weekend, we will use the closing time on Friday: ");
          LocalDate day = model.convertDate(valDate);
          view.displayResult("The distribution in USD is:");
          for (String s : model.portfolioAsDistribution(name, day)) {
            view.displayResult(s);
          }
          break;
      case 6:
        pName = getStringInput("Enter the name of the portfolio you " +
                "would like to rebalance: ");
        while (!model.existingPortfolio(pName)) {
          pName = getStringInput("Portfolio " + pName +
                  " does not exist. Please enter another name.");
        }
        LocalDate localDate = model.convertDate(getDate("rebalance"));
        List<String> stocks = model.getListStocks(pName, localDate);
        view.displayResult("You will be asked to enter the weights of each stock in your portfolio. \n" +
                "Here is a list of all the stocks in your portfolio: \n" +
                stocks.toString() + "\n" +
                "The total of your weights should add to 1");
        HashMap<String, Double> weights = new HashMap<>();
        double totalweight = 0.0;
        while (totalweight != 1) {
          for (int i = 0; i < stocks.size(); i++) {
            double val = 0;
            val = getWeight("Enter a weight value between 0 and 1 for the stock "
                    + stocks.get(i));
            totalweight += val;
            weights.put(stocks.get(i), val);
            if (totalweight > 1 || (totalweight < 1 && i == stocks.size() - 1)) {
              weights = new HashMap<>();
              totalweight = 0.0;
              view.displayResult("Weights total must be equal to 1, please try again.");
            }
          }
        }
        model.rebalancePortfolio(weights, pName, localDate);
        break;
      case 7:
        handleBarChart();
        break;
      case 8:
        pName = getStringInput("Enter the name of the portfolio you " +
                "would like to store: ");
        String filePath = getStringInput("Type the filepath (E.G. Resources/portfolios.xml): ");
        while (!model.existingPortfolio(pName)) {
          pName = getStringInput("Portfolio " + pName +
                  " does not exist. Please enter another name.");
        }
        model.portfolioToXML(filePath);
        break;
      case 9:
         filePath = getStringInput("Type the filepath (E.G. Resources/portfolios.xml): ");
        model.loadPortfolioFromXML(filePath);
        break;
      default:
        view.displayResult("Invalid input. Please enter a valid number.");
    }
  }

  private void handleBarChart() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String pName = getStringInput(
            "Enter the name of the portfolio you would like chart: ");
    while (!model.existingPortfolio(pName)) {
      pName = getStringInput("Portfolio " + pName +
              " does not exist. Please enter another name.");
    }
    view.displayResult("Weekend dates will be calculated using the closing time on Friday");
    String startDate = getDate("start");
    LocalDate start = getValidLocalDate("start", startDate);
    String endDate = getDate("end");
    LocalDate end = getValidLocalDate("end", endDate);
    while (!end.isAfter(start)) {
      view.displayResult("End must be after start.");
      end = getValidLocalDate("end", "jk");
    }
    String timeStamp = model.getTimeStamp(start, end);
    Map<String, Double> data = model.getPortfolioData(pName, start, end, timeStamp);
    data.put(end.format(formatter), model.calculatePortfolio(pName, model.convertDate(endDate)));
    double highestValue = Collections.max(data.values());
    double lowestValue = Collections.min(data.values());
    double difference = highestValue - lowestValue;
    int maxStars = 10;
    double scale = (difference / maxStars);
    while (scale > 1000 && maxStars != 50) {
      maxStars += 10;
      scale = (difference / maxStars);
    }
    if (scale >= 10) {
      scale = Math.ceil(scale / 10) * 10;
    } else {
      scale = Math.ceil(scale * 10) / 10.0;
    }
    view.displayResult("Performance of portfolio " + pName + " from " +
            startDate + " to " +
            endDate);
    for (Map.Entry<String, Double> entry : data.entrySet()) {
      String date = entry.getKey();
      Double value = entry.getValue();
      StringBuilder line = new StringBuilder();
      line.append(date).append(":").append(" ");
      for (int i = 1; i <= (value - lowestValue) / scale; i++) {
        line.append("*");
      }
      view.displayResult(line.toString());
    }
    String baseOutput = "";
    if (lowestValue != 0) {
      baseOutput = " Onto base amount of $" + Math.round(lowestValue);
    }
    view.displayResult("Scale: * =  $" + scale + baseOutput);
  }

  /**
   * Handles the creation of a new portfolio
   * by prompting the user for details and updating the model.
   */
  private void handleNewPortfolio() {
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
  private String[] getValidTradingDay(String[] stockData, String date, String startEnd) {
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

  private LocalDate getValidSellDate(String pName, String stockSymbol, LocalDate sellDate, String[] stockData) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate latestSellDate = model.getLatestSellDate(pName, stockSymbol);
    if (latestSellDate != null) {
      while (latestSellDate.isAfter(sellDate)) {
        view.displayResult("Sell date cannot be before most recent sell date for this stock. "
                + "You sold this stock on " + dateFormat.format(latestSellDate));
        if (askRemoveSale(pName, stockSymbol, sellDate)) {
          break;
        } else {
          sellDate = model.convertDate(getValidTradingDay(stockData, getDate("sell"), "sell")[0]);
        }
      }
    }
    return sellDate;
  }

  private boolean askRemoveSale(String pName, String stockSymbol, LocalDate sellDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    view.displayResult("Would you like to remove your sales that are after the date " +
            dateFormat.format(sellDate) + "?" + " Type y for yes and n for no");
    String response = scanner.nextLine();
    while (!response.equals("y") && !response.equals("n")) {
      view.displayResult("Invalid input: Please type y/n.");
      response = scanner.nextLine();
    }
    if (response.equals("y")) {
      model.removeSales(pName, stockSymbol, sellDate);
      return true;
    }
    return false;
  }

  /**
   * Validates the stock symbol to ensure it exists in the database.
   *
   * @param stockSymbol the stock symbol to validate.
   * @return the stock data for the valid stock symbol.
   */
  private String[] getValidStock(String stockSymbol) {
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

  //gets a valid weight value.
  private double getWeight(String prompt) {
    double input = 0.0;
    boolean validInput = false;
    while (!validInput) {
      view.displayResult(prompt);
      if (scanner.hasNextDouble()) {
        input = scanner.nextDouble();
        scanner.nextLine();
        if (input >= 0 && input <= 1) {
          validInput = true;
        } else {
          view.displayResult("Invalid weight. Please enter a value between 0 and 1.");
        }
      }
    }
    return input;
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