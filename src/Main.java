import java.io.InputStreamReader;

/**
 * Main class that calls the program with the correct
 * starting inputs.
 */
public class Main {

  /**
   * Static method that runs the program.
   * @param args the arguments of the program.
   */
  public static void main(String[] args) {
    StockViewImpl view = new StockViewImpl();
    Readable readable = new InputStreamReader(System.in);
    StockModelImpl model = new StockModelImpl();
    StockControllerImpl controller = new StockControllerImpl(model, view, readable);
    controller.startProgram();
  }
}
