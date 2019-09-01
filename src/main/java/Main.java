import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("database.db");
        // I am so done with this 4
        try {

            PreparedStatement ps = db.prepareStatement("SELECT userID, email FROM users WHERE firstName = 'Alex'");

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


       listUsers();
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



    private static void listUsers() {
        try  {
            PreparedStatement ps = db.prepareStatement("SELECT userID, username, email FROM users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int userID = results.getInt(1);
                String username = results.getString(2);
                String dateOfBirth = results.getString(3);
                System.out.println(userID + " " + username + " " + dateOfBirth);
            }
        }  catch(Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void updateUser (String firstName, String lastName, int userID){
        try {

            PreparedStatement ps = db.prepareStatement("UPDATE Users SET FirstName = ?, LastName = ? WHERE UserID = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, userID);
            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    public static void deleteCourse (int weightID){
        try {

            PreparedStatement ps = db.prepareStatement("DELETE FROM xxxx WHERE xxxID = ?");
            ps.setInt(1, weightID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());

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

