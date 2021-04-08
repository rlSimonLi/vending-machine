package VendingMachine.Product;

import VendingMachine.JDBC.PostgresDriver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private final PostgresDriver psqlDriver;
    private final int productId;

    public Product(PostgresDriver pd, int pid) {
        this.psqlDriver = pd;
        this.productId = pid;
    }

    public int getProductId() {
        return productId;
    }

    ;

    public Integer getCategoryId() {
        String query = String.format("select category from product where id = %s", productId);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return rs.getInt("category");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        String query = String.format("select name from product where id = %s", productId);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getPrice() {
        String query = String.format("select price from product where id = %s", productId);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPriceString() {
        double price = this.getPrice();
        return String.format("%.2f", price);
    }

    public Integer getStock() {
        String query = String.format("select quantity from product where id = %s", productId);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementStock(int increment) {
        int newQuantity = this.getStock() + increment;
        String query = String.format("update product set quantity = '%s' where id = '%s'", newQuantity, this.productId);
        psqlDriver.executeUpdate(query);
    }

    public static class productCategory {
        public static final int DRINK = 1;
        public static final int CHOCOLATE = 2;
        public static final int CHIPS = 3;
        public static final int CANDY = 4;
    }
}
