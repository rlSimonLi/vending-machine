package VendingMachine.View;

import VendingMachine.Product.Product;
import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static VendingMachine.View.ViewUtil.initialiseModel;
import static VendingMachine.View.ViewUtil.render;

public class PaymentView {
    VendingMachine vm;
    public Route serveCardPaymentPage = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);

        if (vm.getCurrentUser() != null && vm.getCurrentUser().getUserGroup() != User.UserGroup.CUSTOMER) {
            response.redirect("/");
            return null;
        }
        if (vm.getPendingOrder() == null) {
            response.redirect("/");
            return null;
        }
        model.put("order", vm.getPendingOrder());
        return render(request, model, "/gui/payment/card_payment.vm");
    };
    public Route handleCardPaymentPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        List<String> errors = new ArrayList<>();
        model.put("errors", errors);
        model.put("order", vm.getPendingOrder());

        if (request.queryParams("payment-method").equals("card")) {
            String cardholder = request.queryParams("cardholder");
            String cardnumber = request.queryParams("cardnumber");

            if (!vm.paymentController.verifyCard(cardholder, cardnumber)) {
                errors.add("Card declined.");
                return render(request, model, "/gui/payment/card_payment.vm");
            }
        }

        if (request.queryParams("payment-method").equals("cash")) {
            double amountPaid = 0 ;
            Map<Double, Integer> notes = vm.paymentController.getNotes();
            for (Double value : notes.keySet()) {
                int quantity = Integer.parseInt(request.queryParams(String.valueOf(value)));
                amountPaid += value * quantity;
                vm.paymentController.incrementNoteQuantity(value, quantity);
            }
            if (amountPaid == vm.getPendingOrder().getTotalPrice()) {
            } else if (amountPaid <= vm.getPendingOrder().getTotalPrice()) {
                errors.add("Payment insufficient.");
                return render(request, model, "/gui/payment/card_payment.vm");
            } else {
                Map<Double, Integer> change = vm.paymentController.getChange(amountPaid - vm.getPendingOrder().getTotalPrice());
                if (change == null) {
                    errors.add("Cannot dispense appropriate change. Please try different combination of notes/coins.");
                    return render(request, model, "/gui/payment/card_payment.vm");
                }
                model.put("change", change);
            }
        }

        for (Product product : vm.getPendingOrder().getItems().keySet()) {
            product.incrementStock(-vm.getPendingOrder().getItems().get(product));
        }
        vm.setPendingOrder(null);
        return render(request, model, "/gui/payment/order_complete.vm");
    };
    public Route serverConfirmPage = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        return render(request, model, "/gui/payment/order_complete.vm");
    };

    public PaymentView(VendingMachine vm) {
        this.vm = vm;
    }

}
