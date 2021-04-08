package VendingMachine.Product;

import VendingMachine.JDBC.PostgresDriver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    PostgresDriver psqlDriver;

    public InventoryController(PostgresDriver psqlDriver) {
        this.psqlDriver = psqlDriver;
    }

    public List<Product> getProductsByCategory(int category_id) {
        List<Product> products = new ArrayList<>();

        String query = String.format("select id from product where category = %s", category_id);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            while (rs.next()) {
                int product_id = rs.getInt("id");
                Product product = new Product(psqlDriver, product_id);
                products.add(product);
            }
        } catch (SQLException ignored) {
        }
        return products;
    }

    public List<Integer> getAllProductId() {
        List<Integer> pid = new ArrayList<>();
        String query = "select id from product where quantity > 0";
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            while (rs.next()) {
                pid.add(rs.getInt("id"));
            }
        } catch (SQLException ignored) {
        }
        return pid;
    }

    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        for (int id: getAllProductId()) {
            products.add(new Product(psqlDriver, id));
        }
        return products;
    }


    public void setProduct(String name, int category, int quantity, double price, int pid) {
        String query = String.format("update product set name = '%s', category = %s, quantity = %s, price = %s where id = %s", name, category, quantity, price, pid);
        psqlDriver.executeUpdate(query);
    }
}
