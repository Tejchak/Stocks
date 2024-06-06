import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class StockControllerImplTest {
  private StockModel mockModel;
  private StockViewImpl mockView;
  private ByteArrayOutputStream outContent;
  private StockControllerImpl controller;

  public static class MockView implements StockView {
    private StringBuilder log;
    public MockView() {
      log = new StringBuilder();
    }
    @Override
    public void createMenu() {
      log.append("1. Examine the gain or loss of a specific stock\n" +
              "2. Examine the x-day moving average of a stock\n" +
              "3. Find the x-day crossovers for a stock\n" +
              "4. Examine or create a portfolio\n" +
              "5. Quit the program\n");
    }

    @Override
    public void portfolioMenu() {
      log.append("1. Create a new portfolio?\n" +
              "2. Add stock to a portfolio?\n" +
              "3. Take away stock from a portfolio?\n" +
              "4. Calculate the value of a portfolio?\n");
    }

    @Override
    public void displayResult(String result) {
      log.append(result + "\n");
    }
  }




  @Test
  public void testStartProgram_HandleGainLoss() {
    StockModel model = new StockModelImpl();
    StockView view = new MockView();
    String input = "1\nAAPL\n2024-05-09\n2024-05-29\n5\n";
    Readable rd = new StringReader(input);
    StockController controller = new StockControllerImpl(model, view, rd);

    controller.startProgram();

    String expectedOutput = "Welcome to the Stocks Program!\n" +
            "1. Examine the gain or loss of a specific stock\n" +
            "2. Examine the x-day moving average of a stock\n" +
            "3. Find the x-day crossovers for a stock\n" +
            "4. Examine or create a portfolio\n" +
            "5. Quit the program\n" +
            "Please enter a number between 1 and 5\n" +
            "Type the stock symbol (e.g., GOOG):\n" +
            "Type the start date (YYYY-MM-DD):\n" +
            "Type the end date (YYYY-MM-DD):\n" +
            "The gain/loss over that period of time is 5.719986000000006\n" +
            "1. Examine the gain or loss of a specific stock\n" +
            "2. Examine the x-day moving average of a stock\n" +
            "3. Find the x-day crossovers for a stock\n" +
            "4. Examine or create a portfolio\n" +
            "5. Quit the program\n" +
            "Please enter a number between 1 and 5\n";

    assertEquals(expectedOutput, ((MockView) view).log.toString());
  }
}