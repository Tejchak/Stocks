import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    StockView view = new StockView(new Scanner(System.in));
    StockModel model = new StockModel();
    StockControllerImpl controller = new StockControllerImpl(model, view);
    controller.startProgram();
  }
}