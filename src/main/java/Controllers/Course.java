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
@Path("course/")
public class Course {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)


    public static String listCourses() {

        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: course/list");
        JSONArray list = new JSONArray();
        try  {
            PreparedStatement ps = Main.db.prepareStatement("SELECT courseID, courseName, questionAmount FROM Courses");
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                JSONObject info = new JSONObject();
                int courseID = results.getInt(1);
                String courseName = results.getString(2);
                String questionAmount = results.getString(3);




                info.put("courseid", courseID);
                info.put("coursename", courseName);
                info.put("questionamount", questionAmount);

                list.add(info);


            }
            return(list.toString());
        }  catch(Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
        return null;
    }



    @GET
    @Path("get/{id}")   // allows user to pass information
    @Produces(MediaType.APPLICATION_JSON)

    public String listCourse(@PathParam("id") Integer id) {


        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: course/get/" + id);
        JSONArray list = new JSONArray();
        try  {
            PreparedStatement ps = Main.db.prepareStatement("SELECT courseID, courseName, questionAmount FROM Courses");
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                JSONObject info = new JSONObject();
                int courseID = results.getInt(1);
                String courseName = results.getString(2);
                String questionAmount = results.getString(3);




                info.put("courseid", courseID);
                info.put("coursename", courseName);
                info.put("questionamount", questionAmount);

                list.add(info);



            }
            return(list.toString());
        }  catch(Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
        return null;
    }





    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public static String createCourse(@FormDataParam("courseName") String courseName, @FormDataParam("questionAmount") int questionAmount) {
        try {
            System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/create");

            if(courseName == null ||  String.valueOf(questionAmount) == null) {
                throw new Exception("you need to fill everything in");
            }
            PreparedStatement ps = Main.db.prepareStatement(
                    "INSERT INTO Courses (courseName, questionAmount) VALUES (?, ?)"
            );

            ps.setString(1, courseName);
            ps.setInt(2, questionAmount);

            ps.executeUpdate();
            return "OK";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error: Something as gone wrong.  Please contact the administrator with the error code CC-CU.";
        }
    }


    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public static String updateCourse(@FormDataParam("courseID") int courseID, @FormDataParam("courseName") String courseName, @FormDataParam("questionAmount") int questionAmount) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Courses SET courseName = ?, questionAmount = ? WHERE courseID = ?");
            ps.setString(1, courseName);
            ps.setInt(2, questionAmount);
            ps.setInt(3, courseID);

            ps.execute();
            return("OK");
        } catch (Exception e) {

            return(e.getMessage());

        }

    }

}
