package VendingMachine.Payment;

import VendingMachine.Product.Product;

import java.util.HashMap;
import java.util.Map;

public class Order {
    Map<Product, Integer> items = new HashMap<>();

    public void addItem(Product p, int quantity) {
        items.put(p, quantity);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : items.keySet()) {
            total += product.getPrice() * items.get(product);
        }
        return total;
    }

    public String getTotalPriceString() {
        double totalPrice = this.getTotalPrice();
        return String.format("%.2f", totalPrice);
    }

    public double getProductTotalPrice(Product p) {
        int quantity = this.getQuantity(p);
        return quantity * p.getPrice();
    }

    public String getProductTotalPriceString(Product p) {
        double productTotalPrice = this.getProductTotalPrice(p);
        return String.format("%.2f", productTotalPrice);
    }

    public int getItemSize() {
        return items.size();
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public int getQuantity(Product p) {
        return items.get(p);
    }
}
