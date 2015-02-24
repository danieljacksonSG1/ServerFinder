package au.com.wsit.mailserverfinder;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by guyb on 24/02/15.
 */
public class Exchange
{
    public final static String TAG = Exchange.class.getSimpleName();
    public int responseCode = 0;


    public int getURL(String URL)
    {

                String OWA_URL = "https://" + URL + "/owa";
                Log.i(TAG, "OWA URL is: " + OWA_URL);
                try {
                    URL owaURL = new URL(OWA_URL);
                    HttpURLConnection connection = (HttpURLConnection) owaURL.openConnection();
                    connection.connect();
                    responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        Log.i(TAG, "response Code is: " + String.valueOf(responseCode));
                        connection.disconnect();
                        return responseCode;

                    }
                    else
                    {
                        Log.i(TAG, "response Code is: " + String.valueOf(responseCode));
                        connection.disconnect();
                        return 0;
                    }

                } catch (MalformedURLException e) {
                    Log.e(TAG, "Couldn't find exchange OWA url");
                    return 0;
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception");
                    return 0;
                }

            }

}
