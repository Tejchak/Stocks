import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class StockModelTest {

  @Test
  public void testStockModel() throws FileNotFoundException {
    StockModel model = new StockModel();
    System.out.print(Arrays.toString(model.getStockDataCSV("AMC")));
  }
}