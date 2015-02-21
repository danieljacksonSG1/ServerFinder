package au.com.wsit.mailserverfinder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guyb on 18/02/15.
 */
public class PopFragment extends Fragment {

    public static final String TAG = PopFragment.class.getSimpleName();

    TextView mIncomingServerName;
    TextView mTCPports;

    int POP3_PLAIN_COUNT = 0;
    int POP3_SSL_COUNT = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pop_results, container, false);
        mIncomingServerName = (TextView) rootView.findViewById(R.id.incomingServerNameID);
        mTCPports = (TextView) rootView.findViewById(R.id.availableTCPPortsID);

        Intent MainActivityIntent = getActivity().getIntent();
        String domain = MainActivityIntent.getStringExtra("DOMAIN");
        Log.i(TAG, "Our domain is: " + domain);

        // Cast the serializable Intent extra to a hashmap
        ArrayList<String> hostPortsMapping = (ArrayList<String>)MainActivityIntent.getSerializableExtra("FQDN_PORT_MAPPING");

        for (int i = 0; i < hostPortsMapping.size(); i++)
        {
            Log.i(TAG, "RESULTS: " + hostPortsMapping.get(i));
            CheckPOP(hostPortsMapping.get(i));

        }

        return rootView;



    }

    @Override
    public void onResume() {
        super.onResume();



    }

    // Pass into for loop - checks if the string has the port we want in it
    public void CheckPOP(String result)
    {
        if (findPort(result).equals("110") && POP3_PLAIN_COUNT == 0)
        {
            Log.i(TAG, "Found a plain POP server");
            mIncomingServerName.setText(removePort(result));
            POP3_PLAIN_COUNT++;

            mTCPports.setText("110");
        }
        else if(findPort(result).equals("995") && POP3_PLAIN_COUNT == 0)
        {
            Log.i(TAG, "Found a SSL POP server");
            mIncomingServerName.setText(removePort(result));
            POP3_SSL_COUNT++;

            mTCPports.setText("995");
        }
        else if(findPort(result).equals("110") && POP3_SSL_COUNT > 0)
        {
            Log.i(TAG, "Found a plain server and a SSL server");
            mIncomingServerName.setText(removePort(result));
            POP3_PLAIN_COUNT++;

            mTCPports.setText("995 SSL or 110 PLAIN");
        }
        else if (findPort(result).equals("995") && POP3_PLAIN_COUNT > 0)
        {
            mIncomingServerName.setText(removePort(result));
            POP3_SSL_COUNT++;

            mTCPports.setText("995 SSL or 110 PLAIN");
        }
        else if (POP3_PLAIN_COUNT == 0 && POP3_SSL_COUNT == 0)
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

}
