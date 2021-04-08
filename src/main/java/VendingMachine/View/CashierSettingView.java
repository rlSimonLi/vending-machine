package VendingMachine.View;

import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

import static VendingMachine.View.ViewUtil.initialiseModel;

public class CashierSettingView {
    VendingMachine vm;
    public Route serveCashierSettingView = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        if (vm.getCurrentUser() == null || (vm.getCurrentUser().getUserGroup() != User.UserGroup.CASHIER && vm.getCurrentUser().getUserGroup() != User.UserGroup.OWNER)) {
            response.redirect("/");
        }
        Map<Double, Integer> notes = vm.paymentController.getNotes();
        model.put("notes", notes);
        return ViewUtil.render(request, model, "/gui/setting/cashier.vm");
    };

    public Route handleCahsierSettingPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        Map<Double, Integer> notes = vm.paymentController.getNotes();
        for (Double value : notes.keySet()) {
            String quantity = request.queryParams(String.valueOf(value));
            vm.paymentController.updateNoteStock(value, Integer.parseInt(quantity));
        }
        notes = vm.paymentController.getNotes();
        model.put("notes", notes);
        return ViewUtil.render(request, model, "/gui/setting/cashier.vm");
    };

    public CashierSettingView(VendingMachine vm) {
        this.vm = vm;
    }
}
