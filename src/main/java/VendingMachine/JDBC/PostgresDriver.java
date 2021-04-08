package VendingMachine.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresDriver {
    private Connection conn;

    public PostgresDriver() {
        final String url = "jdbc:postgresql://db:5432/postgres";
        final String user = "postgres";
        final String password = "example";
        this.conn = getConnection(url, user, password);
    }

    public PostgresDriver(String url, String user, String password) {
        this.conn = getConnection(url, user, password);
    }

    private Connection getConnection(String url, String user, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection(url, user, password);
        } catch (Exception ignored) {
        }
        return conn;
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        Statement stmt = null;

        try {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(query);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return rs;
    }

    public void executeUpdate(String query) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
