package au.com.wsit.mailserverfinder;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    // Debug Tag
    public static final String TAG = MainActivity.class.getSimpleName();

    Button mSearchButton;
    EditText mEmailAddress;
    ProgressBar mSearchProgress;
    public String EmailAddress;
    public String searchDomain;
    public String mDomain;
    public Intent resultsIntent;

    public final String KEY_HOSTNAME = "hostname";
    public final String KEY_TCP_PORT = "tcpPort";

    // To store hostnames and ports
    HashMap<String, ArrayList<Integer>> hostPortsHashMap;

    // Stores a counter so we know if we found any hostnames
    public int domainFoundCount = 0;

    // Stores results
    ArrayList<String> HostResults = new ArrayList<String>();


    // String array that stores hostnames that were able to be resolved
    String[] resolvedHosts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mEmailAddress = (EditText) findViewById(R.id.emailEditText);
        mSearchProgress = (ProgressBar) findViewById(R.id.searchingProgress);


        // Hide progress bar
        mSearchProgress.setVisibility(View.INVISIBLE);


        // Listen for button clicks
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the email address
                EmailAddress = getEmail();

                if (EmailAddress.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Enter an email or domain", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Show the search bar
                    mSearchProgress.setVisibility(View.VISIBLE);
                    // Show some info about what we are doing
                    Toast.makeText(MainActivity.this, getString(R.string.SearchingSettings) + EmailAddress, Toast.LENGTH_LONG).show();

                    // Check if domain or email
                    if (DomainTrue()) {
                        // It's a domain
                        mDomain = EmailAddress;
                        // Proceed with checking network and server settings
                        new HostCheckIMAP().execute(mDomain);

                    } else if (DomainTrue() == false) {
                        // Need to extract domain first
                        mDomain = extractDomain();

                        // Proceed with checking network and server settings
                        new HostCheckIMAP().execute(mDomain);

                    } else {
                        // Error
                        Log.i(TAG, getString(R.string.Generic_Error));
                    }


                }
            }
        }); // End of onClick
    }


    // Extract the domain part of an email
    private String extractDomain()
    {
        // @ Index

        int IndexVal = EmailAddress.indexOf("@");
        Log.i(TAG, "The @ Symbol is at: " + IndexVal);
        searchDomain = EmailAddress.substring(IndexVal + 1, EmailAddress.length());
        Log.i(TAG, "The domain is: " + searchDomain);

        return searchDomain;


    }
    // Check if the user entered a domain or not
    private boolean DomainTrue()
    {
        if (EmailAddress.contains("@"))
        {
            Log.i(TAG, "User entered email address");
            return false;
        }
        else
        {
            Log.i(TAG, "User probably entered a domain");
            return true;
        }

    }

    // Gets the data from the edit text
    public String getEmail()
    {

        return mEmailAddress.getText().toString().trim();

    }


// * * *  AsyncTask * * *

    private class HostCheckIMAP extends AsyncTask<String, Void, String>
    {

        public String IPAddr;


        @Override
        protected String doInBackground(String... params)
        {

                // for each hostname in the array
            ArrayList<HashMap<String, String>> hostPorts = new ArrayList<HashMap<String, String>>();

                for (String hostname : MailServerDB.HOSTNAME_KEYS)
                {


                    String fqdn = hostname + "." +params[0];

                        // Check if the host exists
                        if(checkHostExists(fqdn))
                        {
                            // Checks the ports and adds data to the ArrayList
                            checkServerPorts(fqdn);
                            // Increment the domain found count
                            domainFoundCount = domainFoundCount + 1;

                            // For that host, Check what ports are open


                        }


                 }



            return null;
        }

        @Override
        protected void onPostExecute(String hostname) {
            super.onPostExecute(hostname);

            // Once we are done - hide the progress bar
            mSearchProgress.setVisibility(View.INVISIBLE);

            if (domainFoundCount == 0)
            {
                Toast.makeText(MainActivity.this, "Unable to find server settings", Toast.LENGTH_LONG).show();
            }
            else
            {
                // Get an instance of the intent so we can add "Extras" to it
                resultsIntent = new Intent(MainActivity.this, Results.class);

                // Add the domains we found and ports to the intent
                resultsIntent.putExtra("FQDN_PORT_MAPPING", HostResults);


                // Add our domain
                resultsIntent.putExtra("DOMAIN", mDomain);

                // Start the result activity
                startActivity(resultsIntent);
            }



        }
    }

    // Check if hostname exists - returns true if we were able to resolve
    public boolean checkHostExists(String fqdn)
    {
        String IPAddress;
        try
        {
            InetAddress address = InetAddress.getByName(fqdn);
            IPAddress = address.getHostAddress();
            Log.i(TAG, fqdn + " Resolves to: " + IPAddress);
            return true;
        }
        catch(UnknownHostException e)
        {
            Log.i(TAG, "Unable to resolve: " + fqdn);
            return false;
        }

    }

    // Returns an array of tcp ports that are open on the host
    public ArrayList checkServerPorts(String fqdn)
    {
        // Array that stores the ports that were open on the host
        ArrayList<Integer> openPorts = new ArrayList<Integer>();

        int count = 0;

        // Cycle through the array of mail server ports
        for (int tcp_port : MailServerDB.TCP_PORTS)
        {
            // If the port is open then it's true so add the port to the return array
            if (CheckTCPport(fqdn, tcp_port))
            {

                HostResults.add(fqdn + ":" + tcp_port);

            }
        }

        return openPorts;
    }


    // Checks if the TCP port is open or not
    public boolean CheckTCPport(String hostname, int port)
    {
        Socket portCheck = new Socket();
        int CONNECT_TIMEOUT = 1000;

        try
        {

            portCheck.connect(new InetSocketAddress(hostname, port), CONNECT_TIMEOUT);

            portCheck.close();
            Log.i(TAG, hostname + ":" + port + " is open");

            return true;
        }
        catch(IOException e)
        {
            Log.i(TAG, hostname + ":" + port + " is closed");
            return false;
        }




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_stop:
                // Cancel the search
                StopSearching();
                break;
            case R.id.action_settings:
                // Go to settings
                break;
            case R.id.action_feedback:
                // Go to feedback
                break;
            case R.id.action_about:
                // Go to about
                break;
            case R.id.action_exit:
                // Go to exit
                System.exit(0);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // Stop searching for mail server settings
    private void StopSearching()
    {
        mSearchProgress.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // On resume set the domain found count back to zero
        domainFoundCount = 0;
    }
}
