

public class StockControllerImpl implements StockController{
  private final StockModel model;
  private final StockView view;

  public StockControllerImpl(StockModel model, StockView view) {
    this.model = model;
    this.view = view;
  }

  public void startProgram() {
    int option = view.createWelcomeMenu();
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
        view.displayResult("Examine or create portfolio not implemented yet.");
        break;
      default:
        view.displayResult("Invalid option.");
    }
  }

  private void handleGainLoss() {
    String stockSymbol = view.getStockSymbol();
    String startDate = view.getDate("start");
    String endDate = view.getDate("end");
    double gainLoss = model.stockGainLoss(model.getStockData(stockSymbol), startDate, endDate);
    view.displayResult("The gain/loss over that period of time is " + gainLoss);
  }

  private void handleMovingAverage() {
    String stockSymbol = view.getStockSymbol();
    String startDate = view.getDate("start");
    int xDays = view.getXDays();
    double movingAverage = model.movingAverage(model.getStockData(stockSymbol), startDate, xDays);
    view.displayResult("The " + xDays + "-day moving average is " + movingAverage);
  }

  private void handleCrossover() {
    String stockSymbol = view.getStockSymbol();
    String startDate = view.getDate("start");
    String endDate = view.getDate("end");
    int xDays = view.getXDays();
    StringBuilder crossovers = model.xDayCrossover(model.getStockData(stockSymbol), startDate, endDate, xDays);
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