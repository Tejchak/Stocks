import java.io.InputStreamReader;

/**
 * Main class that calls the program with the correct
 * starting inputs.
 */
public class Main {

  /**
   * Static method that runs the program.
   *
   * @param args the arguments of the program.
   *             Takes in a view, model and readable for scanning inputs
   */
  public static void main(String[] args) {
    StockView view = new StockViewImpl(System.out);
    Readable readable = new InputStreamReader(System.in);
    StockModelTrader model = new StockModelNew();
    StockController controller = new StockControllerNew(model, view, readable);
    controller.startProgram();
  }
}
