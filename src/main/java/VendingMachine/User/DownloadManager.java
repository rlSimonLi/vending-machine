package VendingMachine.User;

import VendingMachine.JDBC.PostgresDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadManager {
    PostgresDriver pd;

    public DownloadManager(PostgresDriver pd) {
        this.pd = pd;
        // create Records folder if not exist
        new File("Records").mkdirs();
    }

    // for seller: download avaliable item list as a txt file
    public void getAvailableItems() {

        ResultSet rsr = this.pd.executeQuery("SELECT p.id, p.name, p.price, p.quantity, pc.name as category FROM product p JOIN product_category pc ON (p.category = pc.id) WHERE quantity > 0 ORDER BY p.id");

        try {
            // clear up the SellerHistory.txt 
            new FileWriter("Records/AvailableItems.txt", false).close();

            // rewrite SellerHistory.txt
            FileWriter writer = new FileWriter("Records/AvailableItems.txt", true);
            while (rsr.next()) {
                String id = rsr.getString(1);
                String name = rsr.getString(2);
                String price = rsr.getString(3);
                String quantity = rsr.getString(4);
                String category = rsr.getString(5);
                String record = String.format("%s.%s(%s) %s with %s left\n", id, name, category, price, quantity);
                writer.write(record);
            }
            System.out.println("AvailableItems.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // for seller: download selling history as a txt file 
    public void getSellerSummary() {
        // SELECT hp.product_id, p.name, SUM(hp.quantity)
        // FROM history h 
        //     JOIN history_product hp ON (h.id = hp.history_id)
        //     JOIN product p ON (hp.product_id = p.id)
        // WHERE h.type = 'consume'
        // GROUP BY hp.product_id, p.name
        ResultSet rsr = this.pd.executeQuery("SELECT hp.product_id, p.name, SUM(hp.quantity) FROM history h JOIN history_product hp ON (h.id = hp.history_id) JOIN product p ON (hp.product_id = p.id) WHERE h.type = 'consume' GROUP BY hp.product_id, p.name");

        try {
            // clear up the SellerSummary.txt 
            new FileWriter("Records/SellerSummary.txt", false).close();

            // rewrite SellerSummary.txt
            FileWriter writer = new FileWriter("Records/SellerSummary.txt", true);
            while (rsr.next()) {
                String id = rsr.getString(1);
                String name = rsr.getString(2);
                String quantity = rsr.getString(3);
                String record = String.format("%s %s(id:%s) has sold \n", quantity, name, id);
                writer.write(record);
            }
            System.out.println("SellerSummary.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // for Cashier: download available cash notes as a txt file 
    public void getAvailableChange() {

        ResultSet rsr = this.pd.executeQuery("SELECT * FROM cash");

        try {
            // clear up the AvailableChange.txt 
            new FileWriter("Records/AvailableChange.txt", false).close();

            // rewrite AvailableChange.txt
            FileWriter writer = new FileWriter("Records/AvailableChange.txt", true);
            while (rsr.next()) {
                String value = rsr.getString(1);
                String quantity = rsr.getString(2);
                String record = String.format("%s %s remains \n", quantity, value);
                writer.write(record);
            }
            System.out.println("AvailableChange.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // for Cashier: download selling history as a txt file 
    public void getCashierSummary() {
        // SELECT h.time, h.money_received, h.money_changed, h.payment, string_agg(p.name::text, ', ') as items, string_agg(hp.quantity::text, ', ') as quantity
        // FROM history h 
        //     JOIN history_product hp ON (h.id = hp.history_id)
        //     JOIN product p ON (hp.product_id = p.id)
        // WHERE h.type = 'consume'
        // GROUP BY h.id, h.time, h.money_received, h.money_changed, h.payment
        ResultSet rsr = this.pd.executeQuery("SELECT h.time, h.money_received, h.money_changed, h.payment, string_agg(p.name::text, ', ') as items, string_agg(hp.quantity::text, ', ') as quantity FROM history h JOIN history_product hp ON (h.id = hp.history_id) JOIN product p ON (hp.product_id = p.id) WHERE h.type = 'consume' GROUP BY h.id, h.time, h.money_received, h.money_changed, h.payment");

        try {
            // clear up the CashierSummary.txt 
            new FileWriter("Records/CashierSummary.txt", false).close();

            // rewrite CashierSummary.txt
            FileWriter writer = new FileWriter("Records/CashierSummary.txt", true);
            while (rsr.next()) {
                String time = rsr.getString(1);
                String money_received = rsr.getString(2);
                String money_changed = rsr.getString(3);
                String payment = rsr.getString(4);
                String items = rsr.getString(5);
                String quantity = rsr.getString(6);
                String record = String.format("AT %s, Received: %s Changed:%s Payment:%s SoldItems:%s SoldQuantity:%s \n", time, money_received, money_changed, payment, items, quantity);
                writer.write(record);
            }
            System.out.println("CashierSummary.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // for Owner: download available users as a txt file 
    public void getAvailableUsers() {

        ResultSet rsr = this.pd.executeQuery("SELECT u.username, ug.name FROM public.user u JOIN public.user_group ug ON (u.user_group = ug.id)");

        try {
            // clear up the AvailableUsers.txt 
            new FileWriter("Records/AvailableUsers.txt", false).close();

            // rewrite AvailableUsers.txt
            FileWriter writer = new FileWriter("Records/AvailableUsers.txt", true);
            while (rsr.next()) {
                String username = rsr.getString(1);
                String usergroup = rsr.getString(2);
                String record = String.format("%s: %s", usergroup, username);
                writer.write(record);
            }
            System.out.println("AvailableUsers.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // for Owner: download cancel history as a txt file
    public void getOwnerSummary() {
        // SELECT time, canceled_reason, username, id
        // FROM history h 
        // WHERE h.canceled = 'yes'
        // AND h.type = 'consume'
        ResultSet rsr = this.pd.executeQuery("SELECT time, canceled_reason, username, id FROM history h WHERE h.canceled = 'yes' AND h.type = 'consume'");

        try {
            // clear up the OwnerSummary.txt 
            new FileWriter("Records/OwnerSummary.txt", false).close();

            // rewrite OwnerSummary.txt
            FileWriter writer = new FileWriter("Records/OwnerSummary.txt", true);
            while (rsr.next()) {
                String time = rsr.getString(1);
                String canceled_reason = rsr.getString(2);
                String username = rsr.getString(3);
                String id = rsr.getString(4);
                String record = String.format("AT %s, %s canceled transcationId:%s because of %s \n", time, username, id, canceled_reason);
                writer.write(record);
            }
            System.out.println("OwnerSummary.txt is now downloaded to Records folder!");
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // // testing use
    // public static void main(String[] args){
    //     PostgresDriver pd = new PostgresDriver();
    //     DownloadManager dm = new DownloadManager(pd);
    //     dm.getAvailableItems();
    //     dm.getSellerSummary();
    //     dm.getAvailableChange();
    //     dm.getCashierSummary();
    //     dm.getAvailableUsers();
    //     dm.getOwnerSummary();
    // }
}
