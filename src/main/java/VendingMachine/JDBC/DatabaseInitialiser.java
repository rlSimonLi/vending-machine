package VendingMachine.JDBC;

import VendingMachine.App;
import VendingMachine.VendingMachine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseInitialiser {
    static public boolean resetDatabase(PostgresDriver psqlDriver) {
        try {
            InputStream is = App.class.getClassLoader().getResourceAsStream("init.sql");
            String query = VendingMachine.readFromInputStream(is);
            psqlDriver.executeUpdate(query);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    static public boolean loadCardInfo(PostgresDriver psqlDriver) {
        psqlDriver.executeUpdate("truncate table card");

        JSONArray arr;
        StringBuilder query = new StringBuilder();

        try {
            URL url = App.class.getClassLoader().getResource("credit_cards.json");
            FileReader reader = new FileReader(url.getPath());
            arr = (JSONArray) new JSONParser().parse(reader);
            for (Object el : arr) {
                JSONObject jsonObj = (JSONObject) el;
                String name = (String) jsonObj.get("name");
                String card_number = (String) jsonObj.get("number");
                query.append(String.format("insert into card values ('%s', '%s');", name, card_number));
            }
            psqlDriver.executeUpdate(query.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
