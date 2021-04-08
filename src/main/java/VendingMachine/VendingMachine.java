package VendingMachine;

import VendingMachine.JDBC.PostgresDriver;
import VendingMachine.Payment.Order;
import VendingMachine.Payment.PaymentController;
import VendingMachine.Product.InventoryController;
import VendingMachine.User.AccountController;
import VendingMachine.User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VendingMachine {
    public final PostgresDriver psqlDriver;
    public final AccountController accountManager;
    public final InventoryController inventoryController;
    public final PaymentController paymentController;

    User currentUser;
    Order pendingOrder;

    public VendingMachine(PostgresDriver pd) {
        this.psqlDriver = pd;
        this.accountManager = new AccountController(pd);
        this.inventoryController = new InventoryController(pd);
        this.paymentController = new PaymentController(pd);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean register(String username, String password, int userGroup) {
        return accountManager.createUser(username, password, userGroup);
    }

    public boolean login(String username, String password) {
        User user = accountManager.authenticateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Order getPendingOrder() {
        return this.pendingOrder;
    }

    public void setPendingOrder(Order o) {
        this.pendingOrder = o;
    }

    public static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
