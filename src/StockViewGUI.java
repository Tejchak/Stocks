import javax.swing.*;
import javax.swing.text.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockViewGUI implements IStockViewGUI {
  private JFrame frame;
  private JTextArea outputArea;
  private JButton createPortfolioButton, buyStockButton,
          sellStockButton, queryPortfolioButton, savePortfolioButton, loadPortfolioButton;
  private JFrame createFrame;
  private JTextField portfolioNameField, stockNameField, numShares;
  private JComboBox<String> dayComboBox, monthComboBox, yearComboBox;
  private GUIFeatures features;

  public StockViewGUI() {

  }

  @Override
  public void createMenu() {
    frame = new JFrame("Stock Portfolio Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

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
    createPortfolioButton.addActionListener(evt -> displayCreatePortfolio());
    buyStockButton.addActionListener(evt -> displayBuyStock());
    queryPortfolioButton.addActionListener(evt -> displayQueryPortfolio());
  }

//  public void displaySavePortfolio() {
//    createFrame = new JFrame("Save Portfolio");
//    createFrame.setSize
//  }

  public void displayBuyStock() {
    createFrame = new JFrame("Buy Stock");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));

    JLabel nameLabel = new JLabel("Portfolio you would like to use:");
    portfolioNameField = new JTextField();

    JLabel stockLabel = new JLabel("Stock Name:");
    stockNameField = new JTextField();
    JLabel sharesLabel = new JLabel("Number of Shares (whole number):");
    numShares = new JTextField();
    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i  - 1 < 10) {
        days[i] = "0" + (i + 1);
      }
    }
    String[] years = new String[12];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2013 + i);
    }

    JLabel dateLabel = new JLabel("Purchase Date \n(will go to most recent trading day):");
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);

    JButton submitButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioNameField);
    createFrame.add(stockLabel);
    createFrame.add(stockNameField);
    createFrame.add(sharesLabel);
    createFrame.add(numShares);
    createFrame.add(dateLabel);
    JPanel datePanel = new JPanel();
    datePanel.add(new JLabel("Day:"));
    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("Year:"));
    datePanel.add(yearComboBox);
    createFrame.add(datePanel);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);

    submitButton.addActionListener(evt -> features.buyStock());
    cancelButton.addActionListener(e -> createFrame.dispose());
  }

  public void displayCreatePortfolio() {
    createFrame = new JFrame("Create New Portfolio");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));

    JLabel nameLabel = new JLabel("Portfolio Name:");
    portfolioNameField = new JTextField();

    JLabel stockLabel = new JLabel("Stock Name:");
    stockNameField = new JTextField();
    JLabel sharesLabel = new JLabel("Number of Shares (whole number):");
    numShares = new JTextField();
    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i  - 1 < 10) {
        days[i] = "0" + (i + 1);
      }
    }
    String[] years = new String[12];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2013 + i);
    }

    JLabel dateLabel = new JLabel("<html>Purchase Date (will go to most recent trading day)" +
            ":<html>");
     dayComboBox = new JComboBox<>(days);
     monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
     yearComboBox = new JComboBox<>(years);

    JButton submitButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioNameField);
    createFrame.add(stockLabel);
    createFrame.add(stockNameField);
    createFrame.add(sharesLabel);
    createFrame.add(numShares);
    createFrame.add(dateLabel);
    JPanel datePanel = new JPanel();
    datePanel.add(new JLabel("Day:"));
    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("Year:"));
    datePanel.add(yearComboBox);
    createFrame.add(datePanel);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);

    submitButton.addActionListener(evt -> features.createPortfolio());
    cancelButton.addActionListener(e -> createFrame.dispose());
  }

  public void displayQueryPortfolio() {
    createFrame = new JFrame("Create New Portfolio");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(2, 2, 10, 10));

    JLabel nameLabel = new JLabel("Portfolio Name:");
    portfolioNameField = new JTextField();

    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i  - 1 < 10) {
        days[i] = "0" + (i + 1);
      }
    }
    String[] years = new String[12];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2013 + i);
    }

    JLabel dateLabel = new JLabel("<html>Purchase Date (will go to most recent trading day)" +
            ":<html>");
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);

    JButton submitButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioNameField);
    createFrame.add(dateLabel);
    JPanel datePanel = new JPanel();
    datePanel.add(new JLabel("Day:"));
    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("Year:"));
    datePanel.add(yearComboBox);
    createFrame.add(datePanel);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);

    submitButton.addActionListener(evt -> features.queryPortfolio());
    cancelButton.addActionListener(e -> createFrame.dispose());
  }

  public void displayMessage(String message) {
    outputArea.append(message + "\n");
  }

  public String getPortfolioName() {
    return portfolioNameField.getText();
  }

  public String getStockName() {
    return stockNameField.getText();
  }

  public String getPurchaseDate() {
    return yearComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + dayComboBox.getSelectedItem();
  }

  public String getNumShares() {
    return numShares.getText();
  }

  @Override
  public void disposeCreateFrame() {
    createFrame.dispose();
  }

  @Override
  public void setFeatures(GUIFeatures features) {
    this.features = features;
  }
}
