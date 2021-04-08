package VendingMachine.Payment;

import VendingMachine.JDBC.PostgresDriver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentController {
    PostgresDriver psqlDriver;

    public PaymentController(PostgresDriver psqlDriver) {
        this.psqlDriver = psqlDriver;
    }

    public boolean isCard(String card_number) {
        String query = String.format("select count(*) as is_card from card where card_number = '%s'", card_number);
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            if (rs.next()) {
                return (rs.getInt("is_card") == 1);
            }
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean verifyCard(String cardholder, String card_number) {
        int sum = 0;
        boolean isEven = false;
        for (int i = card_number.length() - 1; i >= 0 ; i--) {
            int digit = card_number.charAt(i) - '0';
            if (isEven) digit = digit*2;
            if (digit > 9) digit -= 9;
            sum += digit;
            isEven = !isEven;
        }
        return(sum%10 == 0);
    }

    public Map<Double, Integer> getNotes() {
        Map<Double, Integer> notes = new LinkedHashMap<>();
        String query = "select * from cash";
        ResultSet rs = psqlDriver.executeQuery(query);
        try {
            while (rs.next()) {
                Double value = rs.getDouble("value");
                Integer quantity = rs.getInt("quantity");
                notes.put(value, quantity);
            }
        } catch (SQLException ignored){}

        return notes;
    }

    public void incrementNoteQuantity(double value, int increment){
        updateNoteStock(value, getNotes().get(value)+increment);
    }

    public Map<Double, Integer> getChange(double amount) {
        Map<Double, Integer> change = new LinkedHashMap<>();
        Double[] notes = {50.0, 20.0, 10.0, 5.0, 2.0, 1.0, 0.5, 0.2, 0.1, 0.01};
        for (Double noteValue : notes) {
            int currentStock = getNotes().get(noteValue);
            if (noteValue > amount) {
                continue;
            }
            int quantity = (int) (Math.floor(amount / noteValue));
            if (quantity > currentStock) {quantity = currentStock;}
            amount = amount - quantity * noteValue;
            change.put(noteValue, quantity);
            if (amount == 0) {
                for (Double changeNoteValue : change.keySet()) {
                    incrementNoteQuantity(changeNoteValue, -change.get(changeNoteValue));
                }
                return change;
            }
        }
        return null;
    };

    public void updateNoteStock(double value, int quantity) {
        String query = String.format("update cash set quantity = %s where value = '%s'::money",quantity, value);
        psqlDriver.executeUpdate(query);
    }
}
