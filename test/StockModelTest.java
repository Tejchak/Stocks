import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class StockModelTest {

  @Test
  public void testStockModel() throws FileNotFoundException {
    StockModel model = new StockModel();
    System.out.print(model.getStockDataCSV("AMC")[0]);
    System.out.print(model.getStockDataCSV("AMC")[1]);
    System.out.print(model.getStockDataCSV("AMC")[2]);
  }
}