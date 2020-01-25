package Controllers;


import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path("image/")

public class Image {


    @POST

    @Path("upload")

    @Consumes(MediaType.MULTIPART_FORM_DATA)

    @Produces(MediaType.APPLICATION_JSON)

    public String uploadImage(@FormDataParam("file") InputStream fileInputStream,

                              @FormDataParam("file") FormDataContentDisposition formData) {


        System.out.println("/image/upload - Request to upload image " + formData.getFileName());


        try {


            int read;

            byte[] bytes = new byte[1024];

            OutputStream outputStream = new FileOutputStream(new File("resources/client/img/" + formData.getFileName()));

            while ((read = fileInputStream.read(bytes)) != -1) {

                outputStream.write(bytes, 0, read);

            }

            outputStream.flush();

            outputStream.close();


            return "{\"status\":\"OK\"}";


        } catch (IOException ioe) {


            System.out.println("Image upload error: " + ioe.getMessage());

            return "{\"error\":\"" + ioe.getMessage() + "\"}";


        }


    }


}