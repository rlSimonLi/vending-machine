package VendingMachine.View;

import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static VendingMachine.View.ViewUtil.initialiseModel;

public class OwnerSettingView {
    VendingMachine vm;
    public Route serveOwnerRegisterPage = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        model.put("users", vm.accountManager.getAllUsers());

        if (vm.getCurrentUser() == null || vm.getCurrentUser().getUserGroup() != User.UserGroup.OWNER) {
            response.redirect("/");
        }

        return ViewUtil.render(request, model, "/gui/setting/owner.vm");
    };
    public Route handleOwnerRegisterPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        List<String> errors = new ArrayList<>();
        model.put("errors", errors);
        List<String> successes = new ArrayList<>();
        model.put("successes", successes);

        if (vm.getCurrentUser() == null || vm.getCurrentUser().getUserGroup() != User.UserGroup.OWNER) {
            response.redirect("/");
        }


        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String userGroup = request.queryParams("user_group");


        if (vm.accountManager.isUser(username)) {
            errors.add("Username already exists");
            return ViewUtil.render(request, model, "/gui/setting/owner.vm");
        } else {
            vm.register(username, password, Integer.parseInt(userGroup));
            successes.add("User registered!");
            model.put("users", vm.accountManager.getAllUsers());
            return ViewUtil.render(request, model, "/gui/setting/owner.vm");
        }
    };

    public Route deleteUser = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        List<String> errors = new ArrayList<>();
        model.put("errors", errors);
        List<String> successes = new ArrayList<>();
        model.put("successes", successes);

        if (vm.getCurrentUser() == null || vm.getCurrentUser().getUserGroup() != User.UserGroup.OWNER) {
            response.redirect("/");
            return null;
        }

        String username = request.params(":name");
        model.put("user", username);
        if (!vm.accountManager.isUser(username)) {
            response.redirect("/");
            return null;
        }
        User u = new User(vm.psqlDriver, username);
        if (u.getUserGroup() == User.UserGroup.OWNER) {
            if (!vm.accountManager.hasBackupOwner()) {
                errors.add("You cannot delete owner when there exists only one owner.");
                return ViewUtil.render(request, model, "/gui/setting/deleteuser.vm");
            }
            if (vm.getCurrentUser().getName().equals(username)) {
                vm.logout();
                vm.accountManager.deleteUser(username);
                response.redirect("/");
            }
        }
        vm.accountManager.deleteUser(username);
        successes.add("Account deleted");
        return ViewUtil.render(request, model, "/gui/setting/deleteuser.vm");
    };

    public OwnerSettingView(VendingMachine vm) {
        this.vm = vm;
    }
}
