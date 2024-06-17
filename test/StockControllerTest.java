import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the controller and view, by looking at outputs.
 */
public class StockControllerTest {
  private StockModelTrader mockModel;
  private StockViewImpl mockView;
  private ByteArrayOutputStream outContent;
  private StockController controller;

  /**
   * Static class representing a mock for the view.
   */
  public static class MockView implements StockView {
    private StringBuilder log;

    /**
     * Constructor with a stringbuilder log so the information can be appended.
     */
    public MockView() {

      log = new StringBuilder();
    }

    /**
     * Adds the menu to the mock view.
     */
    @Override
    public void createMenu() {
      log.append("1. Examine the gain or loss of a specific stock\n" +
              "2. Examine the x-day moving average of a stock\n" +
              "3. Find the x-day crossovers for a stock\n" +
              "4. Examine or create a portfolio\n" +
              "5. Quit the program\n");
    }

    /**
     * Adds the portfolio menu to the mock view.
     */
    @Override
    public void portfolioMenu() {
      log.append("1. Create a new portfolio?\n" +
              "2. Add stock to a portfolio?\n" +
              "3. Take away stock from a portfolio?\n" +
              "4. Calculate the value of a portfolio?\n");
    }

    /**
     * Adds to the log  of the mock.
     *
     * @param result a String to be displayed.
     */
    @Override
    public void displayResult(String result) {
      log.append(result + "\n");
    }
  }


  /**
   * Tests that the controller can properly handle the gain loss case.
   */
  @Test
  public void testViewCorrectlyCreatedAndReceivesCorrectInputWithHandleGainLoss() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "1\nAAPL\n2024\n05\n9\n2024\n5\n29\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the start date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "The gain/loss over that period of time is $5.72\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  @Test
  public void testInvalidSymbol() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "1\nblob\naapl\n2024\n05\n9\n2024\n5\n29\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "Your symbol doesn't exist in our database. Please try again.\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the start date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "The gain/loss over that period of time is $5.72\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  @Test
  public void testNotEnoughShares() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "4\n9\nResources/Jake.xml\n4\n3\nJake\nL\n2024\n5\n29\n80\n3\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "1. Create a new portfolio?\n"
            + "2. Add stock to a portfolio?\n"
            + "3. Take away stock from a portfolio?\n"
            + "4. Calculate the value of a portfolio?\n"
            + "Please enter a number between 1 and 9\n"
            + "Type the filepath (E.G. Resources/portfolios.xml): \n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "1. Create a new portfolio?\n"
            + "2. Add stock to a portfolio?\n"
            + "3. Take away stock from a portfolio?\n"
            + "4. Calculate the value of a portfolio?\n"
            + "Please enter a number between 1 and 9\n"
            + "Enter the name of the portfolio you would like to sell stock from: \n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the date you would like to sell (you must sell shares in Chronological " +
            "order!):  date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "How many shares would you like to sell:\n"
            + "Invalid number: you only have 70.0 shares available\n"
            + "How many shares would you like to sell:\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }


  /**
   * Tests that the controller can handle x-day crossover and
   * distpatch to the view and model.
   */
  @Test
  public void testXDayCrossOversModelToControllerToView() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "3\nGOOG\n2024\n05\n9\n2024\n5\n29\n30\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the start date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "The moving average will be calulated over the days we have, "
            + "and days prior to 2013-08-19, will not be included in "
            + "calculating the moving average\n"
            + "Type the number of days for moving average:\n"
            + "The following are x-day Crossovers: 2024-05-29, 2024-05-28, 2024-05-24, 2024-05-23, "
            + "2024-05-22, 2024-05-21, 2024-05-20, 2024-05-17, 2024-05-16, 2024-05-15, 2024-05-14,"
            + " 2024-05-13, 2024-05-10, 2024-05-09\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  /**
   * Tests that the controller will indicate when an invalid date
   * is inputed and will run the message again.
   */
  @Test
  public void testInvalidDay() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "1\nAAPL\n2024\n5\n9\n2024\n6\n1\n2024\n6\n3\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the start date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "Sorry the date you entered wasn't a trading day. Please make"
            + " sure you you have the correct format and try again.\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "The gain/loss over that period of time is $9.46\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  /**
   * Tests that the controller will indicate when an invalid date
   * is inputed on the purchase for a portfolio.
   */
  @Test
  public void testInvalidDay2() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "4\n1\nShesh\nGOOG\n4\n2024\n12\n9\n2024\n5\n9\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n" +
            "1. Examine the gain or loss of a specific stock\n" +
            "2. Examine the x-day moving average of a stock\n" +
            "3. Find the x-day crossovers for a stock\n" +
            "4. Examine or create a portfolio\n" +
            "5. Quit the program\n" +
            "Please enter a number between 1 and 5\n" +
            "1. Create a new portfolio?\n" +
            "2. Add stock to a portfolio?\n" +
            "3. Take away stock from a portfolio?\n" +
            "4. Calculate the value of a portfolio?\n" +
            "Please enter a number between 1 and 9\n" +
            "Type the name for your portfolio\n" +
            "You must have atleast one stock in your portfolio\n" +
            "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n" +
            "How many shares would you like to get(you can only purchase whole shares):\n" +
            "What is the purchase:  date\n" +
            "Type the year (e.g., 2017):\n" +
            "Type the month (e.g., 1):\n" +
            "Type the day (e.g., 1):\n" +
            "Sorry the date you entered wasn't a trading day. " +
            "Please make sure you you have the correct format and try again.\n" +
            "What is the purchase date\n" +
            "Type the year (e.g., 2017):\n" +
            "Type the month (e.g., 1):\n" +
            "Type the day (e.g., 1):\n" +
            "Successfully created portfolio\n" +
            "1. Examine the gain or loss of a specific stock\n" +
            "2. Examine the x-day moving average of a stock\n" +
            "3. Find the x-day crossovers for a stock\n" +
            "4. Examine or create a portfolio\n" +
            "5. Quit the program\n" +
            "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  /**
   * Tests that the controller will indicate when an invalid date
   * is inputed on the sale for a portfolio.
   */
  @Test
  public void testInvalidDay3() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input =
            "4\n9\nResources/Jake.xml\n4\n3\nTej\nGOOG\n2024\n12\n29\n2024\n5\n29\n80\n3\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "1. Create a new portfolio?\n"
            + "2. Add stock to a portfolio?\n"
            + "3. Take away stock from a portfolio?\n"
            + "4. Calculate the value of a portfolio?\n"
            + "Please enter a number between 1 and 9\n"
            + "Type the filepath (E.G. Resources/portfolios.xml): \n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "1. Create a new portfolio?\n"
            + "2. Add stock to a portfolio?\n"
            + "3. Take away stock from a portfolio?\n"
            + "4. Calculate the value of a portfolio?\n"
            + "Please enter a number between 1 and 9\n"
            + "Enter the name of the portfolio you would like to sell stock from: \n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the date you would like to sell (you must sell shares in Chronological " +
            "order!):  date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "Sorry the date you entered wasn't a trading day. " +
            "Please make sure you you have the correct format and try again.\n" +
            "What is the sell date\n" +
            "Type the year (e.g., 2017):\n" +
            "Type the month (e.g., 1):\n" +
            "Type the day (e.g., 1):\n" +
            "How many shares would you like to sell:\n" +
            "Invalid number: you only have 50.0 shares available\n" +
            "How many shares would you like to sell:\n" +
            "1. Examine the gain or loss of a specific stock\n" +
            "2. Examine the x-day moving average of a stock\n" +
            "3. Find the x-day crossovers for a stock\n" +
            "4. Examine or create a portfolio\n" +
            "5. Quit the program\n" +
            "Please enter a number between 1 and 5\n";


    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }


  /**
   * Tests that the controller can handle when
   * the start date is after the end date.
   */
  @Test
  public void testEndDateBeforeStartDate() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "1\nAAL\n2024\n5\n9\n2023\n6\n01\n2024\n06\n03\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "What is the start date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "End date must be after start date\n"
            + "What is the end date\n"
            + "Type the year (e.g., 2017):\n"
            + "Type the month (e.g., 1):\n"
            + "Type the day (e.g., 1):\n"
            + "The gain/loss over that period of time is $-2.95\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  /**
   * Tests what happens when the program tries to take from an invalid path.
   */
  @Test
  public void testInvalidFilePathForLoading() {
    StockModelTrader model = new StockModelNew();
    StockView view = new MockView();
    String input = "4\n9\ninvalidpath\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerNew(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "1. Create a new portfolio?\n"
            + "2. Add stock to a portfolio?\n"
            + "3. Take away stock from a portfolio?\n"
            + "4. Calculate the value of a portfolio?\n"
            + "Please enter a number between 1 and 9\n"
            + "Type the filepath (E.G. Resources/portfolios.xml): \n"
            + "Could not find file in xml format, please make sure the filepath is "
            + "correct and you have followed our format.\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }


}