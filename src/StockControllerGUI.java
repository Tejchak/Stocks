import java.time.LocalDate;

public class StockControllerGUI implements GUIFeatures {
  private StockModelTrader model;
  private IStockViewGUI view;

  /**
   * Constructor that takes in a model and view so it can send to both
   * and a readable.
   *
   * @param model    the model for the stock program.
   * @param view     the view for the stock progrqm.
   */
  public StockControllerGUI(StockModelTrader model, IStockViewGUI view) {
    this.model = model;
    this.view = view;
  }

  @Override public void startProgram() {
    view.setFeatures(this);
    view.createMenu();
  }

  private boolean checkDate(String date) {
    try {
      model.convertDate(date);
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public void createPortfolio() {
    String portfolioName = view.getPortfolioName();
    String stockName = view.getStockName().toUpperCase();
    String purchaseDate = view.getPurchaseDate();
    String numberOfShares = view.getNumShares();
    int shares = 0;
    if (portfolioName.isEmpty()) {
      view.displayMessage("Portfolio name cannot be empty.");
    }
    else if (!model.checkStockExists(stockName)) {
      view.displayMessage("We do not have your stock in our database. Sorry for the inconvenience");
    }
    else if (numberOfShares == null || numberOfShares.isEmpty() ) {
      view.displayMessage("Number of shares cannot be empty.");
    }
    else if (purchaseDate.isEmpty() || !checkDate(purchaseDate)) {
      view.displayMessage("Check the inputted date (ensure your inputted month has the correct number of days" +
              "e.g. February only has 28 days");
    }
    else {
      try {
         shares = Integer.parseInt(numberOfShares);
         if (shares < 0) {
           view.displayMessage("Number of shares must be greater than 0.");
           view.disposeCreateFrame();
         }
         else {
           StockPurchase purchase = new StockPurchase(shares, model.convertDate(purchaseDate));
           model.createPortfolio(portfolioName, stockName, purchase);
           view.displayMessage("New portfolio created: " + portfolioName + " " + shares + " shares" +
                   " with stock " + stockName + " bought on " + purchaseDate);
         }
      }
      catch (NumberFormatException e) {
        view.displayMessage("Number of shares must be an integer.");
      }
    }
    view.disposeCreateFrame();
  }

  @Override
  public void buyStock() {
    String portfolioName = view.getPortfolioName();
    String stockName = view.getStockName().toUpperCase();
    String purchaseDate = view.getPurchaseDate();
    String numberOfShares = view.getNumShares();
    int shares = 0;
    if (!model.existingPortfolio(portfolioName)) {
      view.displayMessage("Portfolio does not exist yet.");
    }
    else if (!model.checkStockExists(stockName)) {
        view.displayMessage("We do not have your stock in our database. Sorry for the inconvenience");
    }
    else if (numberOfShares == null || numberOfShares.isEmpty()) {
      view.displayMessage("Number of shares cannot be empty.");
    }
    else if (purchaseDate.isEmpty() || !checkDate(purchaseDate)) {
      view.displayMessage("Check the inputted date (ensure your inputted month has the correct number of days" +
              "e.g. February only has 28 days");
    }
    else {
      try {
        shares = Integer.parseInt(numberOfShares);
        if (shares < 0) {
          view.displayMessage("Number of shares must be greater than 0.");
          view.disposeCreateFrame();
        }
        else {
          StockPurchase purchase = new StockPurchase(shares, model.convertDate(purchaseDate));
          model.buyStock(portfolioName, stockName, purchase);
          view.displayMessage("Stock successfully bought: " + portfolioName + " " + shares + " shares" +
                  " with stock " + stockName + " bought on " + purchaseDate);
        }
      }
      catch (NumberFormatException e) {
        view.displayMessage("Number of shares must be an integer.");
      }
    }
    view.disposeCreateFrame();
  }


  @Override
  public void sellStock() {

  }

  @Override
  public void queryPortfolio() {
    String portfolioName = view.getPortfolioName();
    String purchaseDate = view.getPurchaseDate();
    if (!model.existingPortfolio(portfolioName)) {
      view.displayMessage("Portfolio does not exist yet.");
    }
    else {
      view.displayMessage("\n" +
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
}