import javax.swing.*; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Abstract class for transactions
abstract class Transaction {
    private String transactionId;
    private double amount;
    private String date;

    public Transaction(String transactionId, double amount, String date) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
    }

    public abstract String viewDetails();

    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getTransactionId() { return transactionId; }
}

// Income class extending Transaction
class Income extends Transaction {
    private String source;

    public Income(String transactionId, double amount, String date, String source) {
        super(transactionId, amount, date);
        this.source = source;
    }

    @Override
    public String viewDetails() {
        return "Income: $" + getAmount() + " from " + source + " on " + getDate();
    }
}

// Expense class extending Transaction
class Expense extends Transaction {
    private String category;

    public Expense(String transactionId, double amount, String date, String category) {
        super(transactionId, amount, date);
        this.category = category;
    }

    @Override
    public String viewDetails() {
        return "Expense: $" + getAmount() + " on " + category + " on " + getDate();
    }
}

// Budget class
class Budget {
    private double budgetAmount;
    private double remainingAmount;

    public Budget(double budgetAmount) {
        this.budgetAmount = budgetAmount;
        this.remainingAmount = budgetAmount;
    }

    public void trackSpending(double amount) {
        remainingAmount -= amount;
    }

    public void addIncome(double amount) {
        remainingAmount += amount;
    }

    public double getRemainingAmount() { return remainingAmount; }
}

// TransactionManager class
class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

// Main Application class with GUI
public class FinanceTrackerApp extends JFrame implements ActionListener {
    private TransactionManager transactionManager = new TransactionManager();
    private Budget budget;

    private JTextArea transactionArea;
    private JButton addIncomeBtn, addExpenseBtn, viewBudgetBtn, refreshBtn, viewSavingsBtn;

    public FinanceTrackerApp() {
        // Prompt user to enter initial budget
        double initialBudget = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter your initial budget for the week:", "Initial Budget", JOptionPane.PLAIN_MESSAGE));
        budget = new Budget(initialBudget);

        // Frame setup
        setTitle("Personal Finance Tracker");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450); 
        setResizable(false);
        setLocationRelativeTo(null);

        // Background color
        getContentPane().setBackground(new Color(210, 180, 140));

        // Transaction area
        transactionArea = new JTextArea(10, 45);
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("SansSerif", Font.ITALIC, 14));
        transactionArea.setLineWrap(true);
        transactionArea.setWrapStyleWord(true);
        transactionArea.setBackground(new Color(255, 250, 240));
        transactionArea.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));
        JScrollPane scrollPane = new JScrollPane(transactionArea);
        add(scrollPane);

        // Buttons setup
        addIncomeBtn = createButton("Add Income");
        addExpenseBtn = createButton("Add Expense");
        viewBudgetBtn = createButton("View Budget");
        viewSavingsBtn = createButton("View Savings");
        refreshBtn = createButton("Refresh");

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));
        buttonPanel.add(addIncomeBtn);
        buttonPanel.add(addExpenseBtn);
        buttonPanel.add(viewBudgetBtn);
        buttonPanel.add(viewSavingsBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(165, 42, 42));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Italic", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addIncomeBtn) {
            handleAddIncome();
        } else if (e.getSource() == addExpenseBtn) {
            handleAddExpense();
        } else if (e.getSource() == viewBudgetBtn) {
            handleViewBudget();
        } else if (e.getSource() == refreshBtn) {
            refreshTransactionArea();
        } else if (e.getSource() == viewSavingsBtn) {
            handleViewSavings();
        }
    }

    private void handleAddIncome() {
        JTextField sourceField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField dateField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Source:"));
        panel.add(sourceField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Income", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String source = sourceField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();
            Income income = new Income("INC" + (transactionManager.getTransactions().size() + 1), amount, date, source);
            transactionManager.addTransaction(income);
            budget.addIncome(amount);
            refreshTransactionArea();
        }
    }

    private void handleAddExpense() {
        JTextField categoryField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JTextField dateField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();
            Expense expense = new Expense("EXP" + (transactionManager.getTransactions().size() + 1), amount, date, category);
            transactionManager.addTransaction(expense);
            budget.trackSpending(amount);
            refreshTransactionArea();
        }
    }

    private void handleViewBudget() {
        JOptionPane.showMessageDialog(this, "Remaining Budget: $" + budget.getRemainingAmount(), "Budget Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleViewSavings() {
        double totalIncome = budget.getRemainingAmount();
        JOptionPane.showMessageDialog(this, "Total Savings for the Week: $" + totalIncome, "Weekly Savings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTransactionArea() {
        transactionArea.setText("");
        for (Transaction transaction : transactionManager.getTransactions()) {
            transactionArea.append(transaction.viewDetails() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinanceTrackerApp app = new FinanceTrackerApp();
            app.setVisible(true);
        });
    }
}