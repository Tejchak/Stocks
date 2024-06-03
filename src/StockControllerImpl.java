public class StockControllerImpl implements StockController {
  final StockModel model;
  final StockView view;

  public StockControllerImpl(StockModel model, StockView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void start() {

  }
}
