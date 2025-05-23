import java.io.InputStream;
import java.util.Scanner;

public class StockView {

  public StockView() {
  }

  public void createMenu() {
    System.out.println("What would you like to do today?");
    System.out.println("1. Examine the gain or loss of a specific stock");
    System.out.println("2. Examine the x-day moving average of a stock");
    System.out.println("3. Find the x-day crossovers for a stock");
    System.out.println("4. Examine or create a portfolio");
    System.out.println("5. Quit the program");
  }

  public void portfolioMenu() {
    System.out.println("1. Create a new portfolio?");
    System.out.println("2. Add stock to a portfolio?");
    System.out.println("3. Take away stock from a portfolio?");
    System.out.println("4. Calculate the value of a portfolio?");
  }


  public void displayResult(String result) {

    System.out.println(result);
  }
}