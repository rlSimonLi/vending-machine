package VendingMachine.View;

import VendingMachine.Payment.Order;
import VendingMachine.Product.Product;
import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static VendingMachine.View.ViewUtil.initialiseModel;
import static VendingMachine.View.ViewUtil.render;

public class IndexView {
    VendingMachine vm;
    public Route serveIndexPage = (Request request, Response response) -> {
        if (vm.getCurrentUser() != null) {
            if (vm.getCurrentUser().getUserGroup() == User.UserGroup.OWNER) {
                response.redirect("/setting/owner");
                return null;
            }
            if (vm.getCurrentUser().getUserGroup() == User.UserGroup.SELLER) {
                response.redirect("/setting/seller");
                return null;
            }
            if (vm.getCurrentUser().getUserGroup() == User.UserGroup.CASHIER) {
                response.redirect("/setting/cashier");
                return null;
            }
        }
        Map<String, Object> model = initialiseModel(vm);
        Map<String, List<Product>> products = new LinkedHashMap<>();

        products.put("drink", vm.inventoryController.getProductsByCategory(Product.productCategory.DRINK));
        products.put("chocolate", vm.inventoryController.getProductsByCategory(Product.productCategory.CHOCOLATE));
        products.put("chips", vm.inventoryController.getProductsByCategory(Product.productCategory.CHIPS));
        products.put("candy", vm.inventoryController.getProductsByCategory(Product.productCategory.CANDY));
        model.put("products", products);

        return render(request, model, "/gui/index/index.vm");
    };


    public Route handleIndexPost = (Request request, Response response) -> {
        List<Integer> productIds = vm.inventoryController.getAllProductId();
        Order order = new Order();

        for (Integer productId : productIds) {
            int quantity = Integer.parseInt(request.queryParams(Integer.toString(productId)));
            if (quantity > 0) {
                Product product = new Product(vm.psqlDriver, productId);
                order.addItem(product, quantity);
            }
        }

        if (order.getItemSize() > 0) {
            vm.setPendingOrder(order);
            response.redirect("/checkout");
            return null;
        }
        response.redirect("/");
        return null;
    };

    public Route timeout = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        vm.logout();
        return render(request, model, "/gui/index/timeout.vm");
    };

    public IndexView(VendingMachine vm) {
        this.vm = vm;
    }
}
