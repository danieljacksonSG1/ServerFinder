package au.com.wsit.mailserverfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by guyb on 18/02/15.
 */
public class ExchangeFragment extends Fragment{

    public static final String TAG = ExchangeFragment.class.getSimpleName();


    TextView mExchangeServerName;
    int responseCode;
    int respCodeCount;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exchange_results, container, false);
        mExchangeServerName = (TextView) rootView.findViewById(R.id.exchangeServerNameID);

        Intent MainActivityIntent = getActivity().getIntent();
        String domain = MainActivityIntent.getStringExtra("DOMAIN");
        Log.i(TAG, "Our domain is: " + domain);

        // Cast the serializable Intent extra to a hashmap
        ArrayList<String> hostPortsMapping = (ArrayList<String>)MainActivityIntent.getSerializableExtra("FQDN_PORT_MAPPING");

        for (int i = 0; i < hostPortsMapping.size(); i++)
        {
            Log.i(TAG, "RESULTS: " + hostPortsMapping.get(i));
            // Do some checking with the result
            if(responseCode == 200)
            {
                // Do nothing because we already found the server

            }
            else
            {
                CheckExchange(hostPortsMapping.get(i));
            }



        }


        Log.i(TAG, "RESP CODE COUNT: " + respCodeCount);
        Log.i(TAG, "RESP_CODE: " + responseCode);



        return rootView;

    }

    public void CheckExchange(String result)
    {

        // Found exchange server
        Log.i(TAG, "PORT IS: " + removePort(result));


        if (findPort(result).equals("443"))
        {
            // Check URL
            CheckExchange checkExchangeURL = new CheckExchange();
            checkExchangeURL.execute(removePort(result));

        }
    }

    private class CheckExchange extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            // Make URL
            String OWA_URL = "https://" + urls[0] + "/owa";
            Log.i(TAG, "OWA URL is: " + OWA_URL);
            try
            {
                URL owaURL = new URL(OWA_URL);
                HttpURLConnection connection = (HttpURLConnection) owaURL.openConnection();
                connection.connect();
                responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    Log.i(TAG, "response Code is: " + String.valueOf(responseCode));


                    return urls[0];
                }
                else
                {
                    Log.i(TAG, "response Code is: " + String.valueOf(responseCode));
                    return null;
                }

            }
            catch(MalformedURLException e)
            {
                Log.e(TAG, "Couldn't find exchange OWA url");
            }
            catch(IOException e)
            {
                Log.e(TAG, "IO Exception");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String URL) {
            super.onPostExecute(URL);
            Log.i(TAG, "DONE OWA Check");

            if(URL != null)
            {
                mExchangeServerName.setText(URL);
            }
            else if (responseCode == 200)
            {
                // Do nothing
            }
            else
            {
                mExchangeServerName.setText("Server not found");
            }

        }
    }

    // Removes the ":" from the result - e.g mail.bigpond.com:110 - would return mail.bigpond.com
    public String removePort(String result)
    {
        int portIndex = result.indexOf(":");
        result = result.substring(0, portIndex);
        Log.i(TAG, "Hostname is: " + result);
        return result;



    }

    // returns the TCP port number of the result string
    public String findPort(String result)
    {
        int portIndex = result.indexOf(":");
        result = result.substring(portIndex + 1, result.length());
        Log.i(TAG, "TCP port is: " + result);
        return result;
    }

}
