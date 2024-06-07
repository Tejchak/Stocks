import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the controller and view, by looking at outputs.
 */
public class StockControllerImplTest {
  private StockModel mockModel;
  private StockViewImpl mockView;
  private ByteArrayOutputStream outContent;
  private StockControllerImpl controller;

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
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "1\nAAPL\n2024-05-09\n2024-05-29\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "Type the start date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "The gain/loss over that period of time is 5.719986000000006\n"
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
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "3\nGOOG\n2024-05-09\n2024-05-29\n30\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "Type the start date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
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
   * Tests that the controller will indicate when an invalid stockSymbol
   * is inputed and will run the message again.
   */
  @Test
  public void testInvalidStockSymbol() {
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "3\nblad\nGOOG\n2024-05-09\n2024-05-29\n30\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

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
            + "Type the start date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
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
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "1\nAAPL\n2024-05-09\n2024-06-01\n2024-06-03\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "Type the start date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Sorry the date you entered wasn't a trading day. Please make"
            + " sure you you have the correct format and try again.\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "The gain/loss over that period of time is 9.459992\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }

  /**
   * Tests that the controller can handle when
   * the start date is after the end date.
   */
  @Test
  public void testEndDateBeforeStartDate() {
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "1\nAAL\n2024-05-09\n2023-06-01\n2024-06-03\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n"
            + "Type the stock symbol (case insensitive, e.g., GOOG or goog):\n"
            + "Type the start date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "End date must be after start date\n"
            + "Type the end date (YYYY-MM-DD, e.g., 2024-05-09):\n"
            + "The gain/loss over that period of time is -2.950000000000001\n"
            + "1. Examine the gain or loss of a specific stock\n"
            + "2. Examine the x-day moving average of a stock\n"
            + "3. Find the x-day crossovers for a stock\n"
            + "4. Examine or create a portfolio\n"
            + "5. Quit the program\n"
            + "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }
}