package VendingMachine.View;

import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.Map;

import static VendingMachine.View.ViewUtil.initialiseModel;

public class SellerSettingView {
    VendingMachine vm;
    public Route serveSellerSettingView = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        model.put("products", vm.inventoryController.getAllProduct());

        if (vm.getCurrentUser() == null || (vm.getCurrentUser().getUserGroup() != User.UserGroup.SELLER && vm.getCurrentUser().getUserGroup() != User.UserGroup.OWNER)) {
            response.redirect("/");
        }

        return ViewUtil.render(request, model, "/gui/setting/seller.vm");
    };
    public Route handleSellerSettingPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        model.put("products", vm.inventoryController.getAllProduct());
        List<Integer> ids = vm.inventoryController.getAllProductId();

        for (Integer id : ids) {
            String name = request.queryParams(id + "-name");
            int category = Integer.parseInt(request.queryParams(id + "-category"));
            int stock = Integer.parseInt(request.queryParams(id + "-stock"));
            double price = Double.parseDouble(request.queryParams(id + "-price"));
            vm.inventoryController.setProduct(name, category, stock, price, id);
        }
        return ViewUtil.render(request, model, "/gui/setting/seller.vm");
    };

    public SellerSettingView(VendingMachine vm) {
        this.vm = vm;
    }

}
