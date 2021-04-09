package murach.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import murach.business.FinancialCalculations;
import murach.business.Validation;

public class FutureValueFrame extends JFrame {

    // Declare JTextFields
    private JTextField investmentField;
    private JTextField interestRateField;
    private JTextField yearsField;
    private JTextField futureValueField;

    // Declare error labels
    private JLabel investmentErrorLabel;
    private JLabel interestRateErrorLabel;
    private JLabel yearsErrorLabel;
    
    // Declare Buttons
    private JButton calculateButton;
    private JButton exitButton;
    
    // Declare Panels
    private JPanel buttonPanel;
    private JPanel panel;

    // Declare dim 
    Dimension dim;
    
    
    // New validation object
    Validation v = new Validation();
    
    public FutureValueFrame() {
        initComponents();
    }

    
    private void initComponents() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }

        dim = new Dimension(150, 20);
        
        setTitle("Future Value Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        // Initialize JTextFields
        investmentField = new JTextField();
        interestRateField = new JTextField();
        yearsField = new JTextField();
        futureValueField = new JTextField();

        // Initialize error labels
        investmentErrorLabel = new JLabel("");
        interestRateErrorLabel = new JLabel("");
        yearsErrorLabel = new JLabel("");

        // initialize the Buttons
        calculateButton = new JButton("Calculate");
        exitButton = new JButton("Exit");

        
        // Make so the future value field cannot be tampered with
        futureValueField.setEditable(false);

        // Set the dimensions of the text fields to prevent collapse
        investmentField.setPreferredSize(dim);
        interestRateField.setPreferredSize(dim);
        yearsField.setPreferredSize(dim);
        futureValueField.setPreferredSize(dim);
        investmentField.setMinimumSize(dim);
        interestRateField.setMinimumSize(dim);
        yearsField.setMinimumSize(dim);
        futureValueField.setMinimumSize(dim);

        // Set button click listeners and point them at the appropriate methods
        calculateButton.addActionListener(e -> calculateButtonClicked());
        exitButton.addActionListener(e -> exitButtonClicked());

        //Set up button panel and add the buttons to it
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(calculateButton);
        buttonPanel.add(exitButton);

        // Set up the main panel and add the Labels and Fields to it
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("Monthly Investment:"), getConstraints(0, 0));
        panel.add(investmentField, getConstraints(1, 0));
        panel.add(investmentErrorLabel, getConstraints(2, 0));
        panel.add(new JLabel("Yearly Interest Rate:"), getConstraints(0, 1));
        panel.add(interestRateField, getConstraints(1, 1));
        panel.add(interestRateErrorLabel, getConstraints(2, 1));
        panel.add(new JLabel("Years:"), getConstraints(0, 2));
        panel.add(yearsField, getConstraints(1, 2));
        panel.add(yearsErrorLabel, getConstraints(2, 2));
        panel.add(new JLabel("Future Value:"), getConstraints(0, 3));
        panel.add(futureValueField, getConstraints(1, 3));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(new Dimension(320, 180));
        setVisible(true);
    }

    // helper method for getting a GridBagConstraints object
    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 0, 5);
        c.gridx = x;
        c.gridy = y;
        return c;
    }

    // Method used to make calculations and populate the future value field
    private void calculateButtonClicked() {
        // RESET ERROR LABELS
        investmentErrorLabel.setText("");
        interestRateErrorLabel.setText("");
        yearsErrorLabel.setText("");


        // If all data checks out, perform calculations
        if (v.isDouble(investmentField.getText(), "Monthly Investment").equals("")
                && v.isDouble(interestRateField.getText(), "Yearly Interest Rate").equals("")
                && v.isInteger(yearsField.getText(), "Years").equals("")) {

            // Reset frame size
            setSize(new Dimension(320, 180));

            // Collect input, perform calculation, and display result
            double investment = Double.parseDouble(investmentField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());
            int years = Integer.parseInt(yearsField.getText());

            double futureValue = FinancialCalculations.calculateFutureValue(
                    investment, interestRate, years);

            NumberFormat currency = NumberFormat.getCurrencyInstance();
            futureValueField.setText(currency.format(futureValue));

        } else { // THIS EXECUTES IF THERE ARE ERRORS

            // Change the size to account for error labels
            setSize(new Dimension(620, 180));

            // Reset the future value field
            futureValueField.setText("");

            /////// POPULATE ERROR FIELDS
            // Investment 
            if (!v.isDouble(investmentField.getText(), "Monthly Investment").equals("")) {
                if (!v.isPresent(investmentField.getText(), "Monthly Investment").equals("")) {
                    investmentErrorLabel.setText(v.isPresent(investmentField.getText(), "Monthly Investment"));
                } else {
                    investmentErrorLabel.setText(v.isDouble(investmentField.getText(), "Monthly Investment"));
                }
            }

            // Interest Rate
            if (!v.isDouble(interestRateField.getText(), "Yearly Interest Rate").equals("")) {
                if (!v.isPresent(interestRateField.getText(), "Yearly Interest Rate").equals("")) {
                    interestRateErrorLabel.setText(v.isPresent(interestRateField.getText(), "Yearly Interest Rate"));
                } else {
                    interestRateErrorLabel.setText(v.isDouble(interestRateField.getText(), "Yearly Interest Rate"));
                }
            }

            // Years
            if (!v.isDouble(yearsField.getText(), "Years").equals("")) {
                if (!v.isPresent(yearsField.getText(), "Years").equals("")) {
                    yearsErrorLabel.setText(v.isPresent(yearsField.getText(), "Years"));
                } else {
                    yearsErrorLabel.setText(v.isDouble(yearsField.getText(), "Years"));
                }
            }
        }

    }

    // Method used for closing the application
    private void exitButtonClicked() {
        System.exit(0);
    }

    // MAIN method just needs to create a thread that creates the 
    // FutureValueFrame object and adds it to the event queue that’s 
    // used by the event dispatcher thread (EDT)
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new FutureValueFrame();
        });
    }
}
