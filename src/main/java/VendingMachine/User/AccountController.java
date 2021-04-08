package VendingMachine.User;

import VendingMachine.JDBC.PostgresDriver;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountController {
    PostgresDriver pd;

    public AccountController(PostgresDriver pd) {
        this.pd = pd;
    }

    public boolean createUser(String username, String password, int userGroup) {
        if (isUser(username)) {
            return false;
        }
        try {
            String hash = hashPassword(username, password);
            this.pd.executeUpdate(String.format("insert into \"user\" values ('%s', '%s', %s);", username, hash, userGroup));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        if (!isUser(username)) {
            return null;
        }
        try {
            String password_hash = hashPassword(username, password);
            String actual_hash = getStoredHash(username);
            assert password_hash != null;
            if (!password_hash.equals(actual_hash)) {
                throw new Exception("Incorrect password!");
            }
            return new User(pd, username);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean register(String username, String password, int role) {
        try {
            String hash = hashPassword(username, password);
            this.pd.executeUpdate(String.format("insert into public.user values ('%s', '%s', %s);", username, hash, role));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isUser(String user) {
        String query = String.format("select count(*) as is_user from \"user\" where username = '%s'", user);
        ResultSet rs = pd.executeQuery(query);
        try {
            if (rs.next()) {
                return (rs.getInt("is_user") == 1);
            }
        } catch (SQLException ignored) {
        }
        return false;
    }

    private String getStoredHash(String username) throws Exception {
        String query = String.format("select password_hash from \"user\" where username='%s'", username);
        ResultSet rs = pd.executeQuery(query);
        if (!rs.next()) {
            throw new Exception("Account not found!");
        } else {
            return rs.getString("password_hash").trim();
        }
    }

    ;

    private String hashPassword(String username, String password) {
        MessageDigest md;
        String salted = password + username;

        try {
            md = MessageDigest.getInstance("SHA3-512");
        } catch (Exception e) {
            return null;
        }
        byte[] message = md.digest(salted.getBytes());
        BigInteger no = new BigInteger(1, message);
        StringBuilder hash = new StringBuilder(no.toString(16));
        while (hash.length() < 32) {
            hash.insert(0, "0");
        }
        return hash.toString();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String query = "select * from \"user\"";
        ResultSet rs = pd.executeQuery(query);

        try {
            while (rs.next()) {
                users.add(new User(pd, rs.getString("username")));
            }
        } catch (SQLException ignored) {
        }
        return users;
    }

    public void deleteUser(String username) {
        String query = String.format("delete from \"user\" where username = '%s'", username);
        pd.executeUpdate(query);
    }

    public boolean hasBackupOwner() {
        String query = "select count(*) as \"count\" from \"user\" where user_group = 1";
        ResultSet rs = pd.executeQuery(query);

        try {
            if (rs.next()) {
                return rs.getInt("count") > 1;
            }
        } catch (SQLException ignored) {
        }
        return false;
    }
}
