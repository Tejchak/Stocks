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

  // Method to save the portfolio to an XML file
  public void saveToFile(String filename) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // root element
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("betterPortfolio");
      doc.appendChild(rootElement);

      // name element
      Element nameElement = doc.createElement("name");
      nameElement.appendChild(doc.createTextNode(this.name));
      rootElement.appendChild(nameElement);

      // purchases element
      Element purchasesElement = doc.createElement("purchases");
      rootElement.appendChild(purchasesElement);

      for (Map.Entry<String, ArrayList<StockPurchase>> entry : purchases.entrySet()) {
        Element stockElement = doc.createElement("stock");
        stockElement.setAttribute("ticker", entry.getKey());
        purchasesElement.appendChild(stockElement);

        for (StockPurchase sp : entry.getValue()) {
          Element purchaseElement = doc.createElement("purchase");
          purchaseElement.appendChild(createElement(doc, "shares", String.valueOf(sp.shares)));
          purchaseElement.appendChild(createElement(doc, "purchaseDate", sp.purchaseDate.toString()));
          stockElement.appendChild(purchaseElement);
        }
      }

      // sales element
      Element salesElement = doc.createElement("sales");
      rootElement.appendChild(salesElement);

      for (Map.Entry<String, ArrayList<StockSale>> entry : sales.entrySet()) {
        Element stockElement = doc.createElement("stock");
        stockElement.setAttribute("ticker", entry.getKey());
        salesElement.appendChild(stockElement);

        for (StockSale ss : entry.getValue()) {
          Element saleElement = doc.createElement("sale");
          saleElement.appendChild(createElement(doc, "shares", String.valueOf(ss.shares)));
          saleElement.appendChild(createElement(doc, "saleDate", ss.saledate.toString()));
          stockElement.appendChild(saleElement);
        }
      }

      // write the content into XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(filename));

      transformer.transform(source, result);

    } catch (ParserConfigurationException | TransformerException pce) {
      pce.printStackTrace();
    }
  }

  private Element createElement(Document doc, String name, String value) {
    Element element = doc.createElement(name);
    element.appendChild(doc.createTextNode(value));
    return element;
  }

  // Method to load the portfolio from an XML file
  public static BetterPortfolio loadFromFile(String filename) {
    BetterPortfolio portfolio = null;

    try {
      File file = new File(filename);
      if (!file.exists()) {
        return null;
      }

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);

      doc.getDocumentElement().normalize();

      String name = doc.getElementsByTagName("name").item(0).getTextContent();
      portfolio = new BetterPortfolio(name);

      // Load purchases
      NodeList purchaseNodes = doc.getElementsByTagName("purchase");
      for (int i = 0; i < purchaseNodes.getLength(); i++) {
        Element purchaseElement = (Element) purchaseNodes.item(i);
        String ticker = ((Element) purchaseElement.getParentNode()).getAttribute("ticker");
        double shares = Double.parseDouble(purchaseElement.getElementsByTagName("shares").item(0).getTextContent());
        LocalDate purchaseDate = LocalDate.parse(purchaseElement.getElementsByTagName("purchaseDate").item(0).getTextContent());

        StockPurchase sp = new StockPurchase(shares, purchaseDate);

        portfolio.purchases.computeIfAbsent(ticker, k -> new ArrayList<>()).add(sp);
      }

      // Load sales
      NodeList saleNodes = doc.getElementsByTagName("sale");
      for (int i = 0; i < saleNodes.getLength(); i++) {
        Element saleElement = (Element) saleNodes.item(i);
        String ticker = ((Element) saleElement.getParentNode()).getAttribute("ticker");
        double shares = Double.parseDouble(saleElement.getElementsByTagName("shares").item(0).getTextContent());
        LocalDate saleDate = LocalDate.parse(saleElement.getElementsByTagName("saleDate").item(0).getTextContent());

        StockSale ss = new StockSale(shares, saleDate);

        portfolio.sales.computeIfAbsent(ticker, k -> new ArrayList<>()).add(ss);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return portfolio;
  }
}