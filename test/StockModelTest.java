import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class StockModelTest {

  @Test
  public void testStockModel() throws FileNotFoundException {
    StockModel model = new StockModelImpl();
    System.out.print(model.getStockData("AMC")[0]);
    System.out.print(model.getStockData("AMC")[1]);
    System.out.print(model.getStockData("AMC")[2]);
  }
}