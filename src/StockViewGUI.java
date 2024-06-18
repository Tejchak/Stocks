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
  private GUIFeatures features;

  public StockViewGUI() {
  }

  @Override
  public void createMenu() {
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
    createPortfolioButton.addActionListener(evt -> features.createPortfolio());
  }

  public void displayCreatePortfolio() {
    JFrame createFrame = new JFrame("Create New Portfolio");
    createFrame.setSize(300, 200);
    createFrame.setLayout(new GridLayout(3, 2, 10, 10));

    JLabel nameLabel = new JLabel("Portfolio Name:");
    JTextField nameField = new JTextField();

    JButton submitButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");

    createFrame.add(nameLabel);
    createFrame.add(nameField);
    createFrame.add(submitButton);
    createFrame.add(cancelButton);

    createFrame.setVisible(true);

    submitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String portfolioName = nameField.getText();
        if (!portfolioName.isEmpty()) {
          // Logic to create the new portfolio
          displayMessage("New portfolio created: " + portfolioName);
          createFrame.dispose(); // Close the frame after submission
        } else {
          JOptionPane.showMessageDialog(createFrame, "Portfolio name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    cancelButton.addActionListener(e -> createFrame.dispose());
  }

  public void displayMessage(String message) {
    outputArea.append(message + "\n");
  }


  @Override
  public void portfolioMenu() {

  }

  @Override
  public void displayResult(String result) {

  }

  @Override
  public void setFeatures(GUIFeatures features) {
    this.features = features;
 //   createPortfolioButton.addActionListener(evt -> features.createPortfolio());
  }
}
