import java.util.ArrayList;
import java.util.List;

public class StockControllerGUI implements GUIFeatures {
  private StockModelTrader model;
  private IStockViewGUI view;

  /**
   * Constructor that takes in a model and view so it can send to both
   * and a readable.
   *
   * @param model the model for the stock program.
   * @param view  the view for the stock progrqm.
   */
  public StockControllerGUI(StockModelTrader model, IStockViewGUI view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Starts the main program loop, displaying the user interface and handling user input.
   */
  @Override
  public void startProgram() {
    view.setFeatures(this);
    view.createMenu();
  }

  //Checks if the date can be converted.
  private boolean checkDate(String date) {
    try {
      model.convertDate(date);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Manages the creation of the portfolio between the user interface and
   * the model.
   */
  @Override
  public void createPortfolio() {
    String portfolioName = view.getPortfolioName();
    String stockName = view.getStockName().toUpperCase();
    String purchaseDate = view.getPurchaseDate();
    String numberOfShares = view.getNumShares();
    int shares = 0;
    if (portfolioName.isEmpty() || portfolioName == null) {
      view.displayMessage("Portfolio name cannot be empty.");
    } else if (!model.checkStockExists(stockName)) {
      view.displayMessage("We do not have your stock in our database. Sorry for the inconvenience");
    } else if (numberOfShares == null || numberOfShares.isEmpty()) {
      view.displayMessage("Number of shares cannot be empty.");
    } else if (purchaseDate.isEmpty() || !checkDate(purchaseDate)) {
      view.displayMessage("Check the inputted date (ensure your inputted month has the correct number of days" +
              "e.g. February only has 28 days");
    } else {
      try {
        shares = Integer.parseInt(numberOfShares);
        if (shares < 0) {
          view.displayMessage("Number of shares must be greater than 0.");
          view.disposeCreateFrame();
        } else {
          StockPurchase purchase = new StockPurchase(shares, model.convertDate(purchaseDate));
          model.createPortfolio(portfolioName, stockName, purchase);
          view.displayMessage("New portfolio created: " + portfolioName + " " + shares + " shares" +
                  " with stock " + stockName + " bought on " + purchaseDate);
        }
      } catch (NumberFormatException e) {
        view.displayMessage("Number of shares must be an integer.");
      }
    }
    view.disposeCreateFrame();
  }

  /**
   * Manages the purchase of a stock through the GUI
   * to the madel.
   */
  @Override
  public void buyStock() {
    String portfolioName = view.getPortfolioNameBox();
    String stockName = view.getStockName().toUpperCase();
    String purchaseDate = view.getPurchaseDate();
    String numberOfShares = view.getNumShares();
    int shares = 0;
    if (!model.existingPortfolio(portfolioName)) {
      view.displayMessage("Portfolio does not exist yet.");
    } else if (!model.checkStockExists(stockName)) {
      view.displayMessage("We do not have your stock in our database. Sorry for the inconvenience");
    } else if (numberOfShares == null || numberOfShares.isEmpty()) {
      view.displayMessage("Number of shares cannot be empty.");
    } else if (purchaseDate.isEmpty() || !checkDate(purchaseDate)) {
      view.displayMessage("Check the inputted date (ensure your inputted month has the correct number of days" +
              "e.g. February only has 28 days");
    } else {
      try {
        shares = Integer.parseInt(numberOfShares);
        if (shares < 0) {
          view.displayMessage("Number of shares must be greater than 0.");
          view.disposeCreateFrame();
        } else {
          StockPurchase purchase = new StockPurchase(shares, model.convertDate(purchaseDate));
          model.buyStock(portfolioName, stockName, purchase);
          view.displayMessage("Stock successfully bought: " + portfolioName + " " + shares + " shares" +
                  " with stock " + stockName + " bought on " + purchaseDate);
        }
      } catch (NumberFormatException e) {
        view.displayMessage("Number of shares must be an integer.");
      }
    }
    view.disposeCreateFrame();
  }


  /**
   * Manages the sale of a portfolio in the gui.
   * Checks for errors in the inputs and allows the user
   * to get rid of the older inputs.
   * @param getRid the user selection for getting rid of sales.
   */
  @Override
  public void sellStock(String getRid) {
    String portfolioName = view.getPortfolioNameBox();
    String stockName = view.getStockName().toUpperCase();
    String sellDate = view.getPurchaseDate();
    String numberOfShares = view.getNumShares();
    int shares = 0;
    if (!model.existingPortfolio(portfolioName)) {
      view.displayMessage("Portfolio does not exist yet.");
    } else if (!model.checkStockExists(stockName)) {
      view.displayMessage("We do not have your stock in our database. Sorry for the inconvenience");
    } else if (numberOfShares == null || numberOfShares.isEmpty()) {
      view.displayMessage("Number of shares cannot be empty.");
    } else if (sellDate.isEmpty() || !checkDate(sellDate)) {
      view.displayMessage("Check the inputted date (ensure your inputted month has the correct number of days" +
              "e.g. February only has 28 days");
    } else if (!model.convertDate(sellDate).isAfter(model.getLatestSellDate(portfolioName, stockName))) {
      if (getRid.equalsIgnoreCase("Yes")) {
        model.removeSales(portfolioName, stockName, model.convertDate(sellDate));
        this.sellStock("");
      }
      else {
        view.displayMessage("Sales must be in chronological order. Get rid of future sales");
      }
    } else if (!model.convertDate(sellDate).isBefore(model.getLatestSellDate(portfolioName, stockName))) {
      try {
        shares = Integer.parseInt(numberOfShares);
        double currentShares = model.getBoughtShares(portfolioName, stockName, model.convertDate(sellDate))
                - model.getSoldShares(portfolioName, stockName, model.convertDate(sellDate));
        if (currentShares < shares) {
          view.displayMessage("Number of shares must be greater than the amount you currently own. "
                  + "You own " + currentShares + " shares of stock " + stockName + ".\n");
          view.disposeCreateFrame();
        } else {
          StockSale sale = new StockSale(shares, model.convertDate(sellDate));
          model.sellStock(portfolioName, stockName, sale);
          view.displayMessage("Stock successfully sold: " + portfolioName + " " + shares + " shares" +
                  " with stock " + stockName + " sold on " + sellDate);
        }
      } catch (NumberFormatException e) {
        view.displayMessage("Number of shares must be an integer.");
      }
    }
    view.disposeCreateFrame();
  }

  /**
   * Allows the user to view the total value of a portfolio
   * and its distribution in a gui.
   */
  @Override
  public void queryPortfolio() {
    String portfolioName = view.getPortfolioNameBox();
    String purchaseDate = view.getPurchaseDate();
    if (!checkDate(purchaseDate)) {
      view.displayMessage("Please input a valid date.");
    } else if (!model.existingPortfolio(portfolioName)) {
      view.displayMessage("Portfolio does not exist yet.");
    }
    else {
      view.displayMessage("\n" + "Total value is:  $" +
              model.calculatePortfolio(portfolioName, model.convertDate(purchaseDate)));
      for (String s : model.portfolioAsDistribution(portfolioName,
              model.convertDate(purchaseDate))) {
        if (s.contains("=")) {
          s = s.replace("=", " = $");
        }
        view.displayMessage(s);
      }
    }
    view.disposeCreateFrame();
  }

  /**
   * Manages the loading of a portfolio from a
   * selected file path.
   * @param filepath the path to the file.
   */
  @Override
  public void loadXml(String filepath) {
    try {
      model.loadPortfolioFromXML(filepath);
    }
    catch (Exception e) {
      view.displayMessage("Error getting portfolio from xml file.");
    }
    view.displayMessage("Portfolio loaded.");
  }

  /**
   * Manages the saving of a portfolio through the gui.
   * Handles miss inputs and errors that could possibly arise.
   * @param name the name of the portfolio.
   * @param filepath the file path for the storage of the portfolio.
   */
  @Override
  public void savePortfolio(String name, String filepath) {
    if (!model.existingPortfolio(name)) {
      view.displayMessage("Portfolio does not exist yet.");
    }
    else {
      try {
        model.portfolioToXML(filepath);
      }
      catch (Exception e) {
        view.displayMessage("Error saving portfolio to file.");
      }
    }
    view.disposeCreateFrame();
  }

  /**
   * A list of portfolios from the model so the gui allows
   * the user to select from existing portfolios.
   * @return a list of the portfolio names.
   */
  @Override
  public List<String> portfolioList() {
    List<String> portfolioNames = new ArrayList<>();
    for (BetterPortfolio b : model.getPortfolios()) {
      portfolioNames.add(b.name);
    }
    return portfolioNames;
  }
}