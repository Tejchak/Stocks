import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class StockModelTest {

  @Test
  public void testStockModel() throws FileNotFoundException {
    StockModel model = new StockModel();
    model.getStockDataCSV("AMC");

  }
}