import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Class representing an improved portfolio, this tracks the purchases
 * and the stocks they are associated with.
 */
public class BetterPortfolio {
  String name;
  Map<String, ArrayList<StockPurchase>> purchases;
  Map<String, ArrayList<StockSale>> sales;

  /**
   * the constructor for the better portfolio that
   * initializes the map of purchases.
   * @param name the name of this portfolio.
   */
  BetterPortfolio(String name){
    this.name = name;
    this.purchases = new HashMap<>();
    this.sales = new HashMap<>();
  }
}