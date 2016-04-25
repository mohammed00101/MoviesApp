package mno.mohamed_youssef.movie1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mohamed Yossif on 24/04/2016.
 */
public class JsonMoviesConnection {




   private final  String API_KEY = "85d53e9fd2fc912c68f3f3c278b3dc43";




    private Uri uri;
    private URL url;
    private HttpURLConnection connection = null;
    private  BufferedReader reader = null;
    private String temp = null;
    private StringBuffer buffer ;
    private String result = null;







    public JsonMoviesConnection(Uri baseuri){

     try{

         this.uri = baseuri.buildUpon().appendQueryParameter("api_key", API_KEY).build();
         this.url = new URL(uri.toString());
         this.connection = (HttpURLConnection) url.openConnection();
         this.buffer = new StringBuffer();
         this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

     }catch (Exception e) {

         e.printStackTrace();
     }

     }


    //get string from url


     public String getStirngJson(){



         try {




            while ((temp = reader.readLine()) != null) {

                buffer.append(temp);

            }

            reader.close();

            if (buffer.length() == 0) {

                return null;
            }
            result = buffer.toString();



        return result;


         }catch (Exception e){

             e.printStackTrace();
             return null;

         }


     }




}
