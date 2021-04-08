package VendingMachine.View;

import VendingMachine.User.User;
import VendingMachine.VendingMachine;
import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ViewUtil {

    // Renders a template given a model and a request
    public static String render(Request request, Map<String, Object> model, String templatePath) {
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    public static Map<String, Object> initialiseModel(VendingMachine vm) {
        Map<String, Object> model = new HashMap<>();
        model.put("currentHour", LocalDateTime.now().getHour());
        model.put("USERGROUP_OWNER", User.UserGroup.OWNER);
        model.put("USERGROUP_CASHIER", User.UserGroup.CASHIER);
        model.put("USERGROUP_SELLER", User.UserGroup.SELLER);
        model.put("USERGROUP_CUSTOMER", User.UserGroup.CUSTOMER);
        model.put("currentUser", vm.getCurrentUser());
        return model;
    }

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}