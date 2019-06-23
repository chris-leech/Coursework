import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("database.db");
        // I am so done with this
        try {

            PreparedStatement ps = db.prepareStatement("SELECT UserID, Email FROM Users WHERE Username = 'coolKat'");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String username = results.getString(1);
                System.out.println(username);
                String email = results.getString(2);
                System.out.println(email);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }



        closeDatabase();
    }

    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    private static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

}

