package Controllers;


import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.io.File;
import java.io.IOException;



import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;  //Read and write files
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;
import com.sun.pdfview.PDFFile;  //Main library
import com.sun.pdfview.PDFPage;
import org.json.simple.JSONArray;


@Path("image/")






public class Image2{





    public static void split() {
        try {
            String sourceDir = "resources/client/img/paper1.pdf";// PDF file
            String destinationDir = "resources/client/img/pdf/";//PDF pages saved in this folder

            File sourceFile = new File(sourceDir);
            File destinationFile = new File(destinationDir);

            String fileName = sourceFile.getName().replace(".pdf", "");
            if (sourceFile.exists()) {  //Check if the PDF file exists
                if (!destinationFile.exists()) {   //Check if the output directory exists
                    destinationFile.mkdir();   // If the directory does not exist, create it
                    System.out.println("Folder created in: "+ destinationFile.getCanonicalPath());
                }

                RandomAccessFile raf = new RandomAccessFile(sourceFile, "r");
                FileChannel channel = raf.getChannel();
                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                PDFFile pdf = new PDFFile(buf);
                int PageNum = pdf.getNumPages(); //Get the number of pages in the PDF file
                for (int i = 1; i <= PageNum; i++) { //Loops through the pages of the pdf
                    PDFPage page = pdf.getPage(i);

                    // image dimensions to save as
                    int width = 1785;
                    int height = 2526;

                    // create the image
                    Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                    // width & height, // clip rect, // null for the ImageObserver, // fill background with white, // block until drawing is done
                    Image image = page.getImage(width, height, rect, null, true, true);
                    Graphics2D bufImageGraphics = bufferedImage.createGraphics();
                    bufImageGraphics.drawImage(image, 0, 0, null);

                    File imageFile = new File(destinationDir + fileName + "_" + i + ".png");// file format to save as

                    ImageIO.write(bufferedImage, "png", imageFile);

                    System.out.println(imageFile.getName() + " File created in: " + destinationFile.getCanonicalPath());
                }
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();  //catch exceptions
        }

    }




















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

            split();
            return "{\"status\":\"OK\"}";


        } catch (IOException ioe) {


            System.out.println("Image upload error: " + ioe.getMessage());

            return "{\"error\":\"" + ioe.getMessage() + "\"}";


        }


    }



    @GET

    @Path("list")

    @Produces(MediaType.APPLICATION_JSON)

    public String listImages() {


        System.out.println("/image/list - Getting all image files from folder");


        File folder = new File("resources/client/img/pdf");

        File[] files = folder.listFiles();


        JSONArray images = new JSONArray();


        if (files != null) {

            for (File file : files) {

                images.add(file.getName());

            }

        }



        return images.toString();


    }



}