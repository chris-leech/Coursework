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
        JSONArray list = new JSONArray();
        try  {
            PreparedStatement ps = Main.db.prepareStatement("SELECT userID, firstName, lastName, username, email, enrolledCourses FROM Users");
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                JSONObject info = new JSONObject();
                int userID = results.getInt(1);
                String firstName = results.getString(2);
                String lastName = results.getString(3);
                String username = results.getString(4);
                String email = results.getString(5);
                String enrolledCourses = results.getString(6);



                info.put("userid", userID);
                info.put("firstname", firstName);
                info.put("lastname", lastName);
                info.put("username", username);
                info.put("email", email);
                info.put("enrolled", enrolledCourses);
                list.add(info);


            }
            return(list.toString());
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



    public static String updateUser (int userID, String firstName, String lastName, String username, String email, String passwordHash, String enrolledCourses, int userPrivs){
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET firstName = ?, lastName = ?, username = ?, email = ?, passwordHash = ?, enrolledCourses = ?, userPrivs = ? WHERE userID = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setString(5, passwordHash);
            ps.setString(6, enrolledCourses);
            ps.setInt(7, userPrivs);
            ps.setInt(8, userID);

            ps.execute();
            return("OK");
        } catch (Exception e) {

            return(e.getMessage());

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

