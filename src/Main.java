import java.io.InputStreamReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    StockViewImpl view = new StockViewImpl();
    Readable readable = new InputStreamReader(System.in);
    StockModelImpl model = new StockModelImpl();
    StockControllerImpl controller = new StockControllerImpl(model, view, readable);
    controller.startProgram();
  }
}
