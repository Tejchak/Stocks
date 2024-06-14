import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * New controller that adds the new features to our portfolio code.
 * This allows the user to have accurate purchase and sale information
 * for the stock portfolios. This also allows for, rebalancing, the
 * view of the portfolio as a bar chart or a distribution, and the
 * storage and importing of portfolios into a seperate file.
 */
public class StockControllerNew extends StockControllerImpl {

  /**
   * Constructor that takes in a model and view so it can send to both
   * and a readable.
   *
   * @param model    the model for the stock program.
   * @param view     the view for the stock progrqm.
   * @param readable the information that the scanner will be taking.
   */
  public StockControllerNew(StockModelTrader model, StockView view, Readable readable) {
    super(model, view, readable);
  }

  //gets a valid positive double from the user input.
  protected double getValidPositiveDouble(String prompt) {
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
  @Override
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
        buyStock();
        break;
      case 3:
        sellStock();
        break;
      case 4:
        calculatePortfolio();
        break;
      case 5:
        viewDistribution();
        break;
      case 6:
        rebalance();
        break;
      case 7:
        handleBarChart();
        break;
      case 8:
        storePortfolio();
        break;
      case 9:
        loadFile();
        break;
      default:
        view.displayResult("Invalid input. Please enter a valid number.");
    }
  }

  //Case for storing a portfolio. Will store to the file input
  protected void storePortfolio() {
    String pName = getStringInput("Enter the name of the portfolio you " +
            "would like to store: ");
    String filePath = getStringInput("Type the filepath (E.G. Resources/portfolios.xml): ");
    while (!model.existingPortfolio(pName)) {
      pName = getStringInput("Portfolio " + pName
              + " does not exist. Please enter another name.");
    }
      try {
        model.portfolioToXML(filePath);
      } catch (Exception e) {
        view.displayResult("Error writing to file");
      }
  }

  //Case for loading in files. Will not work
  protected void loadFile() {
    String filePath = getStringInput("Type the filepath (E.G. Resources/portfolios.xml): ");
    try {
      model.loadPortfolioFromXML(filePath);
    }
    catch (Exception e) {
      view.displayResult("Could not find file in xml format, please make sure the filepath is "
              + "correct and you have followed our format.");
    }
  }

  /**
   * Handles the creation of a new portfolio
   * by prompting the user for details and updating the model.
   */
  @Override
  protected void handleNewPortfolio() {
    String name = getStringInput("Type the name for your portfolio");
    view.displayResult("You must have atleast one stock in your portfolio");
    String stockSymbol = getStockSymbol();
    while (!model.checkStockExists(stockSymbol)) {
      view.displayResult("Sorry it appears your stock doesn't exist in out database");
      stockSymbol = getStockSymbol();
    }
    String[] stockData = model.getStockData(stockSymbol);
    int shares = getValidPositiveNum("How many shares would you like to get"
            + "(you can only purchase whole shares):");
    String purchaseDate = getDate("purchase: ");
    String[] purchaseDateLine = getValidTradingDay(stockData, purchaseDate, "purchase");
    LocalDate correctDate = model.convertDate(purchaseDateLine[0]);
    StockPurchase currentPurchase = new StockPurchase(shares, correctDate);
    model.createPortfolio(name, stockSymbol, currentPurchase);
    view.displayResult("Successfully created portfolio");
  }


  //case for buying stock (Only whole shares).
  protected void buyStock() {
    int shares;
    String portfolioName = getStringInput(
            "Enter the name of the portfolio you would like to add to: ");
    while (!model.existingPortfolio(portfolioName)) {
      portfolioName = getStringInput("Portfolio " + portfolioName
              + " does not exist. Please enter another name.");
    }
    String stockSymbol = getStockSymbol();
    String[] stockData = getValidStock(stockSymbol);
    shares = getValidPositiveNum("How many shares would you like to get"
            + "(you can only purchase whole shares):");
    String purchaseDate = getDate("Enter the date you would like to purchase: ");
    String[] purchaseDateLine = getValidTradingDay(stockData, purchaseDate, "purchase");
    LocalDate correctDate = model.convertDate(purchaseDateLine[0]);
    StockPurchase currentPurchase = new StockPurchase(shares, correctDate);
    model.buyStock(portfolioName, stockSymbol, currentPurchase);
  }

  // case for selling stock (can be fractional or whole shares)
  protected void sellStock() {
    double sellShares;
    String pName = getStringInput(
            "Enter the name of the portfolio you would like to sell stock from: ");
    while (!model.existingPortfolio(pName)) {
      pName = getStringInput("Portfolio " + pName
              + " does not exist. Please enter another name.");
    }
    String symbol = getStockSymbol();
    while (!model.portfolioContainsStock(pName, symbol)) {
      view.displayResult("Sorry it appears you haven't bought any shares of that stock");
      symbol = getStockSymbol();
    }
    String[] stockData = getValidStock(symbol);
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
  }

  //Handles the calculation of a portfolio in the controller.
  protected void calculatePortfolio() {
    String n = getStringInput("Enter the name of the portfolio you " +
            "would like to calculate the value of: ");
    while (!model.existingPortfolio(n)) {
      n = getStringInput("Portfolio " + n
              + " does not exist. Please enter another name.");
    }
    String date = getDate("desired");
    LocalDate finalDate = getValidLocalDate("desired", date);
    view.displayResult(n + " is worth $" + model.calculatePortfolio(n, finalDate));
  }

  //case for viewing the distribution of a portfolio on an input dat
  protected void viewDistribution() {
    String name = getStringInput("Enter the name of the portfolio you "
            + "would like to see as a distribution: ");
    while (!model.existingPortfolio(name)) {
      name = getStringInput("Portfolio " + name +
              " does not exist. Please enter another name.");
    }
    view.displayResult("Weekends will be calculated using the closing time on Friday");
    String date = getDate("desired");
    LocalDate finalDate = getValidLocalDate("desired", date);
    view.displayResult("The distribution in USD is:");
    for (String s : model.portfolioAsDistribution(name, finalDate)) {
      if (s.contains("=")) {
      s = s.replace("=", " = $");
      }
      view.displayResult(s);
    }
  }

  //Handles the rebalancing of a portfolio in the controller.
  protected void rebalance() {
    String pName = getStringInput("Enter the name of the portfolio you " +
            "would like to rebalance: ");
    while (!model.existingPortfolio(pName)) {
      pName = getStringInput("Portfolio " + pName
              + " does not exist. Please enter another name.");
    }
    LocalDate localDate = getValidLocalDate("rebalance", getDate("rebalance"));
    List<String> stocks = model.getListStocks(pName, localDate);
    view.displayResult("You will be asked to enter the weights of each stock in your portfolio. \n"
            + "Here is a list of all the stocks in your portfolio: \n" +
            stocks.toString() + "\n" +
            "The total of your weights should add to 1");
    Map<String, Double> weights = new HashMap<>();
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
    view.displayResult("Succesfully rebalanced");
  }

  //gets a valid date that a stock can be sold on.
  protected LocalDate getValidSellDate(String pName, String stockSymbol, LocalDate sellDate, String[] stockData) {
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

  //Gets a valid weight for the rebalancing.
  protected double getWeight(String prompt) {
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

  //Asks if the user wants to remove all of their sales after a date.
  private boolean askRemoveSale(String pName, String stockSymbol, LocalDate sellDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    view.displayResult("Would you like to remove your sales that are after the date "
            + dateFormat.format(sellDate) + "?" + " Type y for yes and n for no");
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

  //Handles the creation of a bar chart for a user.
  protected void handleBarChart() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String pName = getStringInput(
            "Enter the name of the portfolio you would like chart: ");
    while (!model.existingPortfolio(pName)) {
      pName = getStringInput("Portfolio " + pName
              + " does not exist. Please enter another name.");
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
    data.put(end.format(formatter), model.calculatePortfolio(pName, end));
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
    view.displayResult("Performance of portfolio " + pName + " from "
            + startDate + " to " +
            end.format(formatter));
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
}
