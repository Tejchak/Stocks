import java.io.InputStream;
import java.util.Scanner;

public class StockView {
  private final Scanner scanner;

  public StockView(Scanner scanner) {
    this.scanner = scanner;
  }

  public int createWelcomeMenu() {
    System.out.println("Welcome to the stocks program\n Here you can practice trading stocks!");
    System.out.println("What would you like to do today?");
    System.out.println("Would you like to examine the gain or loss of a specific stock? Type 1");
    System.out.println("Would you like to examine the x-day moving average of a stock for a "
            + "specified date and a specified value of x? Type 2");
    System.out.println("Would you like to find the x-day crossovers for a stock"
            + " over a specified date range and a specified value of x? Type 3");
    System.out.println("Would you like to examine or create a portfolio? Type 4");
    return this.scanner.nextInt();
  }

  public String getStockSymbol() {
    System.out.println("Type the stock symbol (e.g., GOOG):");
    return this.scanner.next();
  }

  public String getDate(String prompt) {
    System.out.println("Type the " + prompt + " date (YYYY-MM-DD):");
    return this.scanner.next();
  }

  public int getXDays() {
    System.out.println("Type the number of days for moving average:");
    return this.scanner.nextInt();
  }

  public void displayResult(String result) {
    System.out.println(result);
  }
}