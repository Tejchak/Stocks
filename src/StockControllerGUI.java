
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

  @Override
  public void createPortfolio() {
    view.displayCreatePortfolio();
  }

  @Override
  public void buyStock() {

  }

  @Override
  public void sellStock() {

  }
}