package VendingMachine.User;

import VendingMachine.JDBC.PostgresDriver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    PostgresDriver psqlDriver;
    String username;

    public User(PostgresDriver pd, String username) {
        this.psqlDriver = pd;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userGroup='" + getUserGroup() + '\'' +
                '}';
    }

    public String getName() {
        return this.username;
    }

    public Integer getUserGroup() {
        String query = String.format("select user_group from \"user\" where username = '%s'", username);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return rs.getInt("user_group");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserGroupName() {
        int userGroup = this.getUserGroup();
        String[] groupNames = {"owner", "cashier", "seller", "customer"};
        return groupNames[userGroup - 1];
    }

    public static class UserGroup {
        public static final int OWNER = 1;
        public static final int CASHIER = 2;
        public static final int SELLER = 3;
        public static final int CUSTOMER = 4;
    }

//    public boolean Order(String item_name, Integer requested_quantity){
//        //TODO: customer purchase items, if the name exists, check the quantity,
//
//        // if quantity <= avaliable items, ask user to pay, else, print error messages based on the condition
//        // and update the database. Check the print details with PO.
//        ResultSet rs;
//
//        try {
//
//          rs = psqlDriver.executeQuery("SELECT \"name\" FROM product");
//
//          boolean item_exists = false;
//          String name;
//
//          while(rs.next()) {
//            name = rs.getString(1);
//
//            if (name.equals(item_name)) {
//              item_exists = true;
//              break;
//            }
//          }
//
//          if (!item_exists) { // item being requested doesn't exist.
//            System.out.println("The item your enterred does not exist in the vending machine.");
//            return false;
//          }
//
//          String get_quantity = String.format("SELECT quantity FROM product WHERE \"name\"='%s'", item_name);
//
//          rs = psqlDriver.executeQuery(get_quantity);
//          rs.next();
//          Integer available_quantity = Integer.parseInt(rs.getString(1));
//
//          if (available_quantity < requested_quantity) { // requested_quantity too high.
//            System.out.println("Not enough items in the vending machine.");
//            return false;
//          }
//
//          // UPDATES AVAILABILITY OF ITEM.
//          String update_item = String.format("UPDATE product SET quantity=%d WHERE \"name\"='%s'",
//                                              available_quantity-requested_quantity, item_name);
//          psqlDriver.executeUpdate(update_item);
//
//
//          /* TODO: ASKS USER TO PAY */
//          // rs = psqlDriver.executeQuery("SELECT price FROM product WHERE \"name\"='%s'".format(item_name));
//          // rs.next();
//          // Double item_cost = Double.parseDouble(rs.getString(1));
//
//
//          //ADD TO USERS PURCHASE HISTORY.
//          // rs = psqlDriver.executeQuery(String.format("SELECT \"name\" FROM product WHERE \"name\"='%s'", item_name));
//          // rs.next();
//          // Integer item_code = Integer.parseInt(rs.getString(1));
//          // Date current_date = Date.valueOf(LocalDate.now());
//          //
//          // String add_to_history = String.format("INSERT INTO PurchaseHistory VALUES ('%s','%s','%s')",
//          //                                         username, item_code, current_date);
//          // psqlDriver.executeUpdate(add_to_history);
//
//          return true;
//
//        } catch (Exception e) {
//          //e.printStackTrace();
//          return false;
//
//        }
//    }
}
