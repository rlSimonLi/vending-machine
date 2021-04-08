package VendingMachine;

import VendingMachine.JDBC.DatabaseInitialiser;
import VendingMachine.JDBC.PostgresDriver;
import VendingMachine.View.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;

public class App {
    private static final int PORT = 5233;
    private static final String STATIC_FILE_PATH = "/gui/static";

    public static void main(String[] args) {
        PostgresDriver pd = new PostgresDriver();
        VendingMachine vm = new VendingMachine(pd);

        // uncomment below line to reset VM to initial state
        DatabaseInitialiser.resetDatabase(pd);

//        DatabaseInitialiser.loadCardInfo(pd);

        staticFiles.location(STATIC_FILE_PATH);
        port(PORT);

        IndexView indexView = new IndexView(vm);
        UserView userView = new UserView(vm);
        PaymentView paymentView = new PaymentView(vm);
        OwnerSettingView ownerSettingView = new OwnerSettingView(vm);
        SellerSettingView sellerSettingView = new SellerSettingView(vm);
        CashierSettingView cashierSettingView = new CashierSettingView(vm);

//      handle GET requests
        get("/", indexView.serveIndexPage);
        get("/logout", userView.serveLogoutPage);
        get("/login", userView.serveLoginPage);
        get("/timeout", indexView.timeout);
        get("/register", userView.serveRegisterPage);
        get("/checkout", paymentView.serveCardPaymentPage);
        get("/pay/confirm", paymentView.serverConfirmPage);
        get("/setting/owner", ownerSettingView.serveOwnerRegisterPage);
        get("/deleteuser/:name", ownerSettingView.deleteUser);
        get("/setting/seller", sellerSettingView.serveSellerSettingView);
        get("/setting/cashier", cashierSettingView.serveCashierSettingView);

//      handle POST requests
        post("/login", userView.handleLoginPost);
        post("/register", userView.handleRegisterPost);
        post("/checkout", paymentView.handleCardPaymentPost);
        post("/", indexView.handleIndexPost);
        post("/setting/owner", ownerSettingView.handleOwnerRegisterPost);
        post("/setting/seller", sellerSettingView.handleSellerSettingPost);
        post("/setting/cashier", cashierSettingView.handleCahsierSettingPost);

        System.out.println("Welcome to the magical vending machine");
        System.out.printf("Please access the vending machine at http://localhost:%s%n", PORT);
        System.out.println(getVmAscii());
        openBrowser();
    }

    private static String getVmAscii() {
        InputStream is = App.class.getClassLoader().getResourceAsStream("ascii.txt");
        String ascii;
        try {
            ascii = VendingMachine.readFromInputStream(is);
        } catch (IOException e) {
            ascii = "";
        }
        return ascii;
    }

    private static void openBrowser() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(String.format("http://localhost:%s", PORT)));
            }
        } catch (Exception ignored) {
        }
    }
}
