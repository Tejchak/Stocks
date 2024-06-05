import java.io.InputStream;
import java.util.Scanner;

public class StockView {
  private final Scanner scanner;

  public StockView(Scanner scanner) {
    this.scanner = scanner;
  }

  public int createMenu() {
    System.out.println("What would you like to do today?");
    System.out.println("1. Examine the gain or loss of a specific stock");
    System.out.println("2. Examine the x-day moving average of a stock");
    System.out.println("3. Find the x-day crossovers for a stock");
    System.out.println("4. Examine or create a portfolio");
    System.out.println("5. Quit the program");
    return getValidNum("Please enter a number between 1 and 5: ");
  }

  public int portfolioMenu() {
    System.out.println("1. Create a new portfolio?");
    System.out.println("2. Add stock to a portfolio?");
    System.out.println("3. Take away stock from a portfolio?");
    System.out.println("4. Calculate the value of a portfolio?");
    return getValidNum("Please enter a number between 1 and 4: ");
  }

  private int getValidNum(String prompt) {
    int input = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.print(prompt);
      if (scanner.hasNextInt()) {
        input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        validInput = true;
      } else {
        System.out.println("Invalid input. Please enter a number.");
        scanner.next(); // Discard invalid input
      }
    }
    return input;
  }

  public String getStringInput(String prompt) {
    System.out.println(prompt);
    return this.scanner.nextLine();
  }

  public String getStockSymbol() {
    return getStringInput("Type the stock symbol (e.g., GOOG):");
  }

  public String getDate(String prompt) {
    return getStringInput("Type the " + prompt + " date (YYYY-MM-DD):");
  }


  public int getXDays() {
    System.out.println("Type the number of days for moving average:");
    return this.scanner.nextInt();
  }

  public void displayResult(String result) {

    System.out.println(result);
  }
}