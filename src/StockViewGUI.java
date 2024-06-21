import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class for the generative user interface, contains fields for each
 * of the ways that the user can interact with the program.
 * Uses java swing to create these buttons and menus.
 */
public class StockViewGUI implements IStockViewGUI {
  private JFrame frame;
  private JTextArea outputArea;
  private JButton createPortfolioButton, buyStockButton,
          sellStockButton, queryPortfolioButton, savePortfolioButton, loadPortfolioButton;
  private JFrame createFrame;
  private JTextField portfolioNameField, stockNameField, numShares;
  private JComboBox<String> dayComboBox, monthComboBox, yearComboBox, portfolioComboBox, getRidComboBox;
  private GUIFeatures features;

  /**
   * Empty constructor so the view can be initialized in the main.
   */
  public StockViewGUI() {
  }

  /**
   * Creates the opening menu for the stock view with the GUI.
   */
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

  private void setupFrame(String title, boolean isCreatePortfolio, boolean isSell, ActionListener submitAction) {

    createFrame = new JFrame(title);
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(5, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());
    JLabel nameLabel = new JLabel(isCreatePortfolio ? "Portfolio Name:" : "Portfolio you would like to use:");
    if (isCreatePortfolio) {
      portfolioNameField = new JTextField();
      createFrame.add(nameLabel);
      createFrame.add(portfolioNameField);
    } else {
       String[] portfolioList = features.portfolioList().toArray(new String[]{});
      portfolioComboBox = new JComboBox<>(portfolioList);
      createFrame.add(nameLabel);
      createFrame.add(portfolioComboBox);
    }

    stockNameField = new JTextField();
    numShares = new JTextField();

    JLabel stockLabel = new JLabel("Stock Name:");
    JLabel sharesLabel = new JLabel("Number of Shares (whole number):");
    JLabel dateLabel = new JLabel("Date (will go to most recent trading day):");

    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(stockLabel);
    createFrame.add(stockNameField);
    createFrame.add(sharesLabel);
    createFrame.add(numShares);
    createFrame.add(dateLabel);
    JPanel datePanel = initDatePanel();
    if (isSell) {
      getRidComboBox = new JComboBox<>(new String[]{"No", "Yes"});
      datePanel.add(new JLabel("Would you like to get rid " +
              "of all future sales if they exist?"));
      datePanel.add(getRidComboBox);
    }
    createFrame.add(datePanel);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);
    frame.setVisible(false);

    submitButton.addActionListener(submitAction);
    cancelButton.addActionListener(e -> disposeCreateFrame());
  }

  /**
   * Does a pop up that allows the user to select a file path and
   * the name of the portfolio that they will save.
   */
  private void displaySavePortfolio() {
    String[] nameList = features.portfolioList().toArray(new String[]{});
    ;
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

  //Displays the purchase of a stock in the gui.
  private void displayBuyStock() {
    setupFrame("Buy Stock", false, false, evt -> {
      features.buyStock();
    });
  }

  //Displays the selling of a stock on the gui.
  private void displaySellStock() {
    setupFrame("Sell Stock", false, true, evt -> {
      features.sellStock();
    });
  }

  //initializes the date.
  private JPanel initDatePanel() {
    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i - 1 < 10) {
        days[i] = "0" + (i + 1);
      }
    }
    String[] years = new String[11];
    for (int i = 0; i < years.length; i++) {
      years[i] = String.valueOf(2014 + i);
    }

    JPanel datePanel = new JPanel();
    dayComboBox = new JComboBox<>(days);
    monthComboBox = new JComboBox<>(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
    yearComboBox = new JComboBox<>(years);
    datePanel.add(new JLabel("Day:"));
    datePanel.add(dayComboBox);
    datePanel.add(new JLabel("Month:"));
    datePanel.add(monthComboBox);
    datePanel.add(new JLabel("Year:"));
    datePanel.add(yearComboBox);

    return datePanel;
  }

  /**
   * Works with the controller to display the GUI for the creation of a portfolio.
   */
  private void displayCreatePortfolio() {
    setupFrame("Create Portfolio", true, false, evt -> {
      features.createPortfolio();
    });
  }

  //displays the query portfolio.
  private void displayQueryPortfolio() {
    createFrame = new JFrame("Query Portfolio");
    createFrame.setSize(800, 600);
    createFrame.setLayout(new GridLayout(2, 2, 10, 10));
    createFrame.setLocation(frame.getLocation());

    JLabel nameLabel = new JLabel("Portfolio Name:");
    String[] nameList = features.portfolioList().toArray(new String[]{});
    ;
    portfolioComboBox = new JComboBox<>(nameList);

    String[] days = new String[31];
    for (int i = 0; i < days.length; i++) {
      days[i] = String.valueOf(i + 1);
      if (i - 1 < 10) {
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
    cancelButton.addActionListener(e -> disposeCreateFrame());
  }

  /**
   * Does a pop up that allows the user to select a file path
   * for the xml they will load from.
   */
  private void displayLoadXml() {
    JFileChooser fileChooser = new JFileChooser();
    int approved = fileChooser.showOpenDialog(frame);
    if (approved == JFileChooser.APPROVE_OPTION) {
      features.loadXml(fileChooser.getSelectedFile().getAbsolutePath());
    }

  }

  /**
   * Displays a given message in the GUI.
   *
   * @param message the message to be displayed.
   */
  @Override
  public void displayMessage(String message) {
    outputArea.append(message + "\n");
  }

  /**
   * Gets the name of a portfolio from a text field box.
   *
   * @return the name of the portfolio entered.
   */
  @Override
  public String getPortfolioName() {
    return portfolioNameField.getText();
  }

  /**
   * Gets the name of a portfolio from a selection box.
   *
   * @return the name of the portfolio entered.
   */
  @Override
  public String getPortfolioNameBox() {
    return portfolioComboBox.getSelectedItem() + "";
  }

  @Override
  public String getRidFutureSale() {
    return getRidComboBox.getSelectedItem() + "";
  }

  /**
   * Gets the name of the stock inputted by the user.
   *
   * @return the name of the stock inputted
   */
  @Override
  public String getStockName() {
    return stockNameField.getText();
  }

  /**
   * Gets the purchase date inputted to the combo boxes
   * by the user.
   *
   * @return the purchase date of the stock.
   */
  @Override
  public String getPurchaseDate() {
    return yearComboBox.getSelectedItem() + "-" + monthComboBox.getSelectedItem() + "-" + dayComboBox.getSelectedItem();
  }

  /**
   * Gets the number of shares inputted for sales or purchases by the user.
   *
   * @return the number of shares as a string.
   */
  @Override
  public String getNumShares() {
    return numShares.getText();
  }

  /**
   * Disposes of the current frame being used.
   */
  @Override
  public void disposeCreateFrame() {
    frame.setVisible(true);
    createFrame.dispose();
  }

  /**
   * Sets the features, i.e. the controller so the view can access
   * some information for displaying.
   *
   * @param features the gui controller.
   */
  @Override
  public void setFeatures(GUIFeatures features) {
    this.features = features;
  }
}
