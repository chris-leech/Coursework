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
           return(username + " " + password);
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Someone messed up.\"}";
        }
    }



    }

