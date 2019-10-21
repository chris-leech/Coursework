package Controllers;


import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
@Path("user/")
public class User {

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)


    public String userLogin(@FormDataParam("username") String username, @FormDataParam("password") String password) {


        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/login | Username: " + username );

        try {

            JSONObject info = new JSONObject();
            info.put("username", username);
            info.put("password", password);
           return(info.toString());
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Someone messed up.\"}";
        }






    }

    public static String listUsers() {
        try  {
            PreparedStatement ps = Main.db.prepareStatement("SELECT userID, username, email FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int userID = results.getInt(1);
                String username = results.getString(2);
                String dateOfBirth = results.getString(3);

                JSONObject info = new JSONObject();
                info.put("userID", userID);
                info.put("username", username);
                info.put("DOB", dateOfBirth);
                return(info.toString());
            }
        }  catch(Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
        return null;
    }


    public static String createUser(String firstName, String lastName, String username, String email, String passwordHash, String enrolledCourses, int userPrivs) {
        try {
            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Users (firstName, lastName, username, email, passwordHash, enrolledCourses, userPrivs) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setString(5, passwordHash);
            ps.setString(6, enrolledCourses);
            ps.setInt(7, userPrivs);
            ps.executeUpdate();
            return "OK";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-UA.";
        }
    }



    public static void updateUser (int userID, String firstName, String lastName, String username, String email, String passwordHash, String enrolledCourses, int userPrivs){
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET firstName = ?, lastName = ?, username = ?, email = ?, passwordHash = ?, enrolledCourses = ?, userPrivs = ? WHERE UserID = ?");
            ps.setInt(1, userID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, username);
            ps.setString(5, email);
            ps.setString(6, passwordHash);
            ps.setString(7, enrolledCourses);
            ps.setInt(8, userPrivs);

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }


    public static void deleteCourse (int courseID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Courses WHERE CourseID = ?");
            ps.setInt(1, courseID);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }

}

