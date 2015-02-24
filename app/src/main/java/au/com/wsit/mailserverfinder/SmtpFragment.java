package au.com.wsit.mailserverfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guyb on 18/02/15.
 */
public class SmtpFragment extends Fragment{

    public static final String TAG = SmtpFragment.class.getSimpleName();
    TextView mIncomingServerName;
    TextView mTCPports;

    ArrayList<String> hostPortsMapping;

    int SMTP_PLAIN_COUNT = 0;
    int SMTP_SSL_COUNT = 0;
    int SMTP_TLS_COUNT = 0;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smtp_results, container, false);


            mIncomingServerName = (TextView) rootView.findViewById(R.id.incomingServerNameID);
            mTCPports = (TextView) rootView.findViewById(R.id.availableTCPPortsID);


            Intent MainActivityIntent = getActivity().getIntent();
            String domain = MainActivityIntent.getStringExtra("DOMAIN");
            Log.i(TAG, "Our domain is: " + domain);

            // Cast the serializable Intent extra to a hashmap
            hostPortsMapping = (ArrayList<String>)MainActivityIntent.getSerializableExtra("FQDN_PORT_MAPPING");

              for (int i = 0; i < hostPortsMapping.size(); i++)
            {
                Log.i(TAG, "RESULTS: " + hostPortsMapping.get(i));
                CheckSMTP(hostPortsMapping.get(i));

            }


        return rootView;

    }

    // Pass into for loop - checks if the string has the port we want in it
    public void CheckSMTP(String result)
    {
        if (findPort(result).equals("25") && SMTP_PLAIN_COUNT == 0)
        {
            Log.i(TAG, "Found a plain SMTP server");
            mIncomingServerName.setText(removePort(result));
            SMTP_PLAIN_COUNT++;

            mTCPports.setText("25");
        }
        else if(findPort(result).equals("465") && SMTP_PLAIN_COUNT == 0)
        {
            Log.i(TAG, "Found a SSL SMTP server");
            mIncomingServerName.setText(removePort(result));
            SMTP_SSL_COUNT++;

            mTCPports.setText("465");
        }
        else if(findPort(result).equals("587") && SMTP_PLAIN_COUNT == 0 && SMTP_SSL_COUNT == 0)
        {
            Log.i(TAG, "Found a TLS SMTP server");
            mIncomingServerName.setText(removePort(result));
            SMTP_TLS_COUNT++;

            mTCPports.setText("587");
        }
        else if(findPort(result).equals("25") && SMTP_SSL_COUNT > 0)
        {
            Log.i(TAG, "Found a plain server and a SSL server");
            mIncomingServerName.setText(removePort(result));
            SMTP_PLAIN_COUNT++;

            mTCPports.setText("465 SSL or 25 PLAIN");
        }
        else if (findPort(result).equals("465") && SMTP_PLAIN_COUNT > 0)
        {
            mIncomingServerName.setText(removePort(result));
            SMTP_SSL_COUNT++;

            mTCPports.setText("465 SSL or 25 PLAIN");
        }
        else if (findPort(result).equals("587") && SMTP_PLAIN_COUNT > 0 && SMTP_SSL_COUNT > 0 )
        {
            mIncomingServerName.setText(removePort(result));
            SMTP_TLS_COUNT++;

            mTCPports.setText("465 SSL or 587 TLS or 25 PLAIN");
        }
        else if (findPort(result).equals("587") && SMTP_PLAIN_COUNT > 0)
        {
            mIncomingServerName.setText(removePort(result));
            SMTP_TLS_COUNT++;

            mTCPports.setText("587 TLS or 25 PLAIN");
        }

        else if(SMTP_TLS_COUNT == 0 && SMTP_SSL_COUNT == 0 &&  SMTP_PLAIN_COUNT == 0)
        {
            mIncomingServerName.setText("No results found");
            mTCPports.setText("No results found");
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


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }
}
