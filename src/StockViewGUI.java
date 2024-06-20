import javax.swing.*;
import javax.swing.text.IconView;
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
  private JComboBox<String> dayComboBox, monthComboBox, yearComboBox, portfolioComboBox;
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
    savePortfolioButton.addActionListener(evt -> displaySavePortfolio());
    loadPortfolioButton.addActionListener(evt -> displayLoadXml());
    sellStockButton.addActionListener(evt -> displaySellStock());
  }

  public void displaySavePortfolio() {
    String[] nameList = features.portfolioList().toArray(new String[]{});;
    portfolioComboBox = new JComboBox<>(nameList);
    int result = JOptionPane.showConfirmDialog(frame, portfolioComboBox, "Select Portfolio to Save",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String selectedPortfolioName = (String) portfolioComboBox.getSelectedItem();
      if (selectedPortfolioName != null && !selectedPortfolioName.isEmpty()) {
        JFileChooser fileChooser = new JFileChooser();
        int approved = fileChooser.showSaveDialog(frame);
        if (approved == JFileChooser.APPROVE_OPTION) {
          features.savePortfolio(selectedPortfolioName, fileChooser.getSelectedFile().getAbsolutePath());
          displayMessage("Saved Portfolio to " + fileChooser.getSelectedFile().getAbsolutePath());
        }
      } else {
        JOptionPane.showMessageDialog(frame, "No portfolio selected.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void displayBuyStock() {
    createFrame = new JFrame("Buy Stock");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());

    JLabel nameLabel = new JLabel("Portfolio you would like to use:");
    String[] nameList = features.portfolioList().toArray(new String[]{});;
    portfolioComboBox = new JComboBox<>(nameList);

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
    String[] years = new String[11];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2014 + i);
    }

    JLabel dateLabel = new JLabel("Purchase Date \n(will go to most recent trading day):");
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);

    JButton submitButton = new JButton("Buy");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioComboBox);
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
    frame.setVisible(false);
    submitButton.addActionListener(evt -> features.buyStock());
    cancelButton.addActionListener(e -> disposeCreateFrame());
  }

  public void displaySellStock() {
    createFrame = new JFrame("Sell Stock");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());

    JLabel nameLabel = new JLabel("Portfolio you would like to use:");
    String[] nameList = features.portfolioList().toArray(new String[]{});;
    portfolioComboBox = new JComboBox<>(nameList);

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
    String[] years = new String[11];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2014 + i);
    }

    JLabel dateLabel = new JLabel("Sell Date \n(will go to most recent trading day):");
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);
    JComboBox getRidComboBox = new JComboBox<>(new String[] {"No", "Yes"});

    JButton submitButton = new JButton("Sell");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioComboBox);
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
    datePanel.add(new JLabel("Would you like to get rid " +
            "of all future sales if they exist?"));
    datePanel.add(getRidComboBox);
    createFrame.add(datePanel);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);

    submitButton.addActionListener(evt -> features.sellStock(getRidComboBox.getSelectedItem() + ""));
    cancelButton.addActionListener(e -> disposeCreateFrame());
    frame.setVisible(false);
  }

  public void displayCreatePortfolio() {
    createFrame = new JFrame("Create New Portfolio");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());

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
    String[] years = new String[11];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2014 + i);
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
    frame.setVisible(false);

    submitButton.addActionListener(evt -> features.createPortfolio());
    cancelButton.addActionListener(e -> disposeCreateFrame());
  }

  public void displayQueryPortfolio() {
    createFrame = new JFrame("Query Portfolio");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(2, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());

    JLabel nameLabel = new JLabel("Portfolio Name:");
    String[] nameList = features.portfolioList().toArray(new String[]{});;
    portfolioComboBox = new JComboBox<>(nameList);

    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i  - 1 < 10) {
        days[i] = "0" + (i + 1);
      }
    }
    String[] years = new String[11];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2014 + i);
    }

    JLabel dateLabel = new JLabel("<html>Purchase Date (will go to most recent trading day)" +
            ":<html>");
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);

    JButton submitButton = new JButton("Query");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(portfolioComboBox);
    createFrame.add(submitButton);
    createFrame.add(dateLabel);
    JPanel datePanel = new JPanel();
    datePanel.add(new JLabel("Day:"));
    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("Year:"));
    datePanel.add(yearComboBox);
    createFrame.add(datePanel);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);
    frame.setVisible(false);
    submitButton.addActionListener(evt -> features.queryPortfolio());
    cancelButton.addActionListener(e ->  disposeCreateFrame());
  }

  public void displayLoadXml() {
    JFileChooser fileChooser = new JFileChooser();
    int approved = fileChooser.showOpenDialog(frame);
    if (approved == JFileChooser.APPROVE_OPTION) {
      features.loadXml(fileChooser.getSelectedFile().getAbsolutePath());
    }

  }

  public void displayMessage(String message) {
    outputArea.append(message + "\n");
  }

  public String getPortfolioName() {
      return portfolioNameField.getText();
  }

  public String getPortfolioNameBox() {
      return portfolioComboBox.getSelectedItem() + "";
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
    frame.setVisible(true);
    createFrame.dispose();
  }

  @Override
  public void setFeatures(GUIFeatures features) {
    this.features = features;
  }
}
