import javax.swing.*;
import javax.swing.text.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockViewGUI implements StockView {
  private JFrame frame;
  private JTextArea outputArea;
  private JButton createPortfolioButton, buyStockButton,
          sellStockButton, queryPortfolioButton, savePortfolioButton, loadPortfolioButton;

  public StockViewGUI() {
    frame = new JFrame("Stock Portfolio Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 400);

    outputArea = new JTextArea();
    outputArea.setEditable(false);

    createPortfolioButton = new JButton("Create Portfolio");
    buyStockButton = new JButton("Buy Stock");
    sellStockButton = new JButton("Sell Stock");
    queryPortfolioButton = new JButton("Query Portfolio");
    savePortfolioButton = new JButton("Save Portfolio");
    loadPortfolioButton = new JButton("Load Portfolio");

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 3, 10, 10));
    panel.add(createPortfolioButton);
    panel.add(buyStockButton);
    panel.add(sellStockButton);
    panel.add(queryPortfolioButton);
    panel.add(savePortfolioButton);
    panel.add(loadPortfolioButton);

    frame.getContentPane().add(panel, BorderLayout.NORTH);
    frame.getContentPane().add(new JScrollPane(outputArea), BorderLayout.CENTER);

    frame.setVisible(true);

    // Add action listeners for buttons
    createPortfolioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        createNewPortfolio();
      }
    });

    buyStockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buyStock();
      }
    });

    sellStockButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sellStock();
      }
    });

    queryPortfolioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        queryPortfolio();
      }
    });

    savePortfolioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        savePortfolio();
      }
    });

    loadPortfolioButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadPortfolio();
      }
    });
  }

  public void showMenu() {
    // GUI does not need to show a menu like the text-based interface
  }

  public void createNewPortfolio() {
    displayMessage("Creating a new portfolio...");
    // Logic to create a new portfolio
  }

  public void buyStock() {
    displayMessage("Buying stock...");
    // Logic to buy stock
  }

  public void sellStock() {
    displayMessage("Selling stock...");
    // Logic to sell stock
  }

  public void queryPortfolio() {
    displayMessage("Querying portfolio...");
    // Logic to query portfolio
  }

  public void savePortfolio() {
    displayMessage("Saving portfolio...");
    // Logic to save portfolio
  }

  public void loadPortfolio() {
    displayMessage("Loading portfolio...");
    // Logic to load portfolio
  }

  public void displayMessage(String message) {
    outputArea.append(message + "\n");
  }

  @Override
  public void createMenu() {

  }

  @Override
  public void portfolioMenu() {

  }

  @Override
  public void displayResult(String result) {

  }
}
