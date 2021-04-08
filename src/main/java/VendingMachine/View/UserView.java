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

public class UserView {
    VendingMachine vm;
    public Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);

        if (vm.getCurrentUser() != null) {
            response.redirect("/");
        }

        return ViewUtil.render(request, model, "/gui/account/login.vm");
    };
    public Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        List<String> errors = new ArrayList<>();
        model.put("errors", errors);

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if (vm.login(username, password)) {
            response.redirect("/");
        } else {
            errors.add("Username or password incorrect.");
            return ViewUtil.render(request, model, "/gui/account/login.vm");
        }
        return null;
    };
    public Route serveRegisterPage = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);

        if (vm.getCurrentUser() != null) {
            response.redirect("/");
        }

        return ViewUtil.render(request, model, "/gui/account/register.vm");
    };
    public Route handleRegisterPost = (Request request, Response response) -> {
        Map<String, Object> model = initialiseModel(vm);
        List<String> errors = new ArrayList<>();
        model.put("errors", errors);

        String username = request.queryParams("username");
        String password = request.queryParams("password");


        if (vm.accountManager.isUser(username)) {
            errors.add("Username already exists");
            return ViewUtil.render(request, model, "/gui/account/register.vm");
        } else {
            vm.register(username, password, User.UserGroup.CUSTOMER);
            vm.login(username, password);
            response.redirect("/");
        }
        return null;
    };
    public Route serveLogoutPage = (Request request, Response response) -> {
        vm.logout();
        response.redirect("/");
        return null;
    };

    public UserView(VendingMachine vm) {
        this.vm = vm;
    }
}