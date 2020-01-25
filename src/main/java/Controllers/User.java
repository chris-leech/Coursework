package Controllers;

import Controllers.Convertor;
import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import javax.ws.rs.core.Cookie;
import java.sql.ResultSet;
import java.util.UUID;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

@Path("user/")
public class User {

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)


    public String userLogin(@FormDataParam("username") String username, @FormDataParam("password") String password) {


        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/login | Username: " + username );
        System.out.println("Invoked UserController.userLogin() with username of " + username);

        int userID = getUserID(username, password);
        if (userID == -1) {
            return ("Error:  username or password is incorrect.  Are you sure you've registered?");
        }
        String uuid = UUID.randomUUID().toString();                 //create a unique ID for session
        String result = updateUUIDinDB(userID, uuid);               //store UUID for the user in the database
        if (result.equals("OK")) {
            return uuid;
        } else {
            return "Error: Something has gone wrong.  Please contact the administrator with the error code UC-UL.";                  //these messages are returned to client so no details of table names etc
        }
    }







    public static int getUserID(String username, String passwordHash) {

        System.out.println("Invoked UserController.getUserID()");

        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT userID FROM Users WHERE username = ? AND passwordHash = ?");
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.getInt("userID");

            //            Statement ps = DatabaseConnection.connection.createStatement();        //to test this make connection public in DBConnection class
//            String query = "SELECT UserID FROM Users WHERE Password = '"+ password+"'" ;
            //now user can enter      b' or '1'='1    evaluates to true so all records turned and they get logged in as the last result in resultsSet - ha ha ha
            //this won't work with prepared ps as all of       b' or '1'='1    is taken as the password
//            ResultSet resultSet = ps.executeQuery(query);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }


    public static String updateUUIDinDB(int userID, String UUID) {

        System.out.println("Invoked UserController.updateUUIDinDB()");
        System.out.println(userID);
        System.out.println(UUID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UUID = ? WHERE userID = ?");
            ps.setString(1, UUID);
            ps.setInt(2, userID);
            ps.executeUpdate();
            System.out.println("Update done");
            return "OK";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error";
        }
    }


    public static int validateSessionCookie(Cookie sessionCookie) {     //returns the userID that of the record with the cookie value

        System.out.println("Invoked UserController.validateSessionCookie()");

        try {
            String uuid = sessionCookie.getValue();
            PreparedStatement ps = Main.db.prepareStatement("SELECT userID FROM Users WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.getInt("userID");  //Retrieve by column name  (should really test we only get one result back!)
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;  //rogue value indicating error
        }
    }








    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public static String listUsers() {

        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/list");
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


    @GET
    @Path("get/{id}")   // allows user to pass information
    @Produces(MediaType.APPLICATION_JSON)

    public String listUser(@PathParam("id") Integer id) {


        System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/get/" + id);
        JSONArray list = new JSONArray();
        try  {
            if (id == null) {
                throw new Exception("no id given");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT firstName, lastName, username, email, enrolledCourses FROM Users WHERE userID = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                JSONObject info = new JSONObject();
                String firstName = results.getString(1);
                String lastName = results.getString(2);
                String username = results.getString(3);
                String email = results.getString(4);
                String enrolledCourses = results.getString(5);


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



    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public String userName(@CookieParam("sessionToken") Cookie sessionCookie) {

        System.out.println("Invoked UserController.userName()");

        if (sessionCookie == null) {
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-UN";
        }

        try {
            String uuid = sessionCookie.getValue();
            PreparedStatement ps = Main.db.prepareStatement(
                    "SELECT firstName FROM Users WHERE UUID = ?"
            );
            ps.setString(1, uuid);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.getString("FirstName");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-UN. ";

        }

    }


    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)

    public static String createUser(@FormDataParam("firstName") String firstName, @FormDataParam("lastName") String lastName, @FormDataParam("username") String username, @FormDataParam("email") String email, @FormDataParam("passwordHash") String passwordHash, @FormDataParam("enrolledCourses") String enrolledCourses, @FormDataParam("userPrivs") int userPrivs) {
        try {
            System.out.println(System.currentTimeMillis()/1000 + " | CLIENT ACCESS: user/create");

            if(firstName == null || lastName == null || username == null || email == null || passwordHash == null || enrolledCourses == null || String.valueOf(userPrivs) == null ) {
                throw new Exception("you need to fill everything in");
            }
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
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-CU.";
        }
    }


    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String userUpdate(@CookieParam("sessionToken") Cookie sessionCookie,
                             @FormDataParam("firstName") String firstName,
                             @FormDataParam("lastName") String lastName,
                             @FormDataParam("username") String username,
                             @FormDataParam("email") String email,
                             @FormDataParam("passwordHash") String passwordHash,
                             @FormDataParam("userPrivs") String userPrivs) {

        System.out.println("Invoked UserController.userUpdate()");

        if (sessionCookie == null) {
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-UU";
        }
        try {
            int userID = User.validateSessionCookie(sessionCookie);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET userID = ?, firstName = ?, lastName = ?, username = ?, email = ?, passwordHash = ?, userPrivs = ?  WHERE UserID = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setString(5, passwordHash);
            ps.setString(6, userPrivs);

            ps.executeUpdate();
            return "OK";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Error: Something as gone wrong.  Please contact the administrator with the error code UC-UU. ";
        }
    }






}

