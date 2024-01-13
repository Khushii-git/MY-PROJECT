import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Authentication extends Exception {
    public Authentication (String message) {
        super(message);
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
    class User {
    private String userId;
    private String pin;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }
}

class Account {
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }
   
    public void deposit(double amount) {
        balance += amount;
        recordTransaction("Deposit", amount);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
        recordTransaction("Withdrawal", amount);
    }

    public void transfer(Account recipientAccount, double amount) throws InsufficientFundsException {
        withdraw(amount);
        recipientAccount.deposit(amount);
        recordTransaction("Transfer to " + recipientAccount, amount);
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    private void recordTransaction(String transactionType, double amount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        Transaction transaction = new Transaction(transactionType, amount, date);
        transactionHistory.add(transaction);
    }
}

class Transaction {
    private String transactionType;
    private double amount;
    private String date;

    public Transaction(String transactionType, double amount, String date) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}

class ATM {
    private User currentUser;
    private Account userAccount;

    public ATM(User user, Account account) {
        this.currentUser = user;
        this.userAccount = account;
    }

    public boolean authenticateUser(String userId, String pin) throws Authentication{
        if (userId.equals(currentUser.getUserId()) && pin.equals(currentUser.getPin())) {
            return true;
        } else {
            throw new Authentication("Invalid user ID or PIN");
        }
    }

    public void displayMenu() {
        System.out.print("\t***ATM Menu:***");
        System.out.print("\t================");
        System.out.println("1. View Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void performTransactions() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private void viewTransactionHistory() {
        List<Transaction> transactions = userAccount.getTransactionHistory();
        System.out.println("\nTransaction History:");

        for (Transaction transaction : transactions) {
            System.out.println("Type: " + transaction.getTransactionType() +
                    ", Amount: " + transaction.getAmount() +
                    ", Date: " + transaction.getDate());
        }
    }

    private void withdraw() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();
           userAccount.withdraw(amount);
            System.out.println("Withdrawal successful. Remaining balance: " + userAccount.getBalance());
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } finally 
        {
           scanner.nextLine(); // Consume the newline character
        }
    }

    private void deposit() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        userAccount.deposit(amount);
        System.out.println("Deposit successful. Updated balance: " + userAccount.getBalance());

        scanner.nextLine(); // Consume the newline character
    }

    private void transfer() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter recipient's account number: ");
            int recipientAccountNumber = scanner.nextInt();

           
            Account recipientAccount = new Account(0);

            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();

            userAccount.transfer(recipientAccount, amount);
            System.out.println("Transfer successful.");
            System.out.println("Your balance: " + userAccount.getBalance());
            System.out.println("Recipient's balance: " + recipientAccount.getBalance());
        } catch (InsufficientFundsException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } finally {
            scanner.nextLine(); // Consume the newline character
        }
    }
}

public class ATMApp {
    public static void main(String[] args) {
        User user = new User("123456", "7890");
        Account account = new Account(1000.0);
        ATM atm = new ATM(user, account);

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (atm.authenticateUser(userId, pin)) {
                atm.performTransactions();
            } else {
                System.out.println("Authentication failed. Exiting...");
            }
        } catch (Authentication e) {
            System.out.println("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
