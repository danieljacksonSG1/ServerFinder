package au.com.wsit.mailserverfinder;

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

import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {

    // Debug Tag
    public static final String TAG = MainActivity.class.getSimpleName();

    Button mSearchButton;
    EditText mEmailAddress;
    ProgressBar mSearchProgress;
    public String EmailAddress;
    public String searchDomain;
    public String mDomain;



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
            public void onClick(View v)
            {
            // Show the search bar
            mSearchProgress.setVisibility(View.VISIBLE);
            // Get the email address
            EmailAddress = getEmail();
            // Show some info about what we are doing
            Toast.makeText(MainActivity.this, getString(R.string.SearchingSettings) + EmailAddress, Toast.LENGTH_LONG).show();

            // Check if domain or email
            if(DomainTrue())
            {
                // It's a domain
                mDomain = EmailAddress;
                // Proceed with checking network and server settings
                new HostCheckIMAP().execute(mDomain);

            }
            else if (DomainTrue() == false)
            {
                // Need to extract domain first
                mDomain = extractDomain();
                // Proceed with checking network and server settings
                new HostCheckIMAP().execute(mDomain);

            }
            else
            {
                // Error
                Log.i(TAG, getString(R.string.Generic_Error));
            }



            }
        });

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

        // Get the data from the EditText
        return mEmailAddress.getText().toString().trim();
    }


// * * *  AsyncTask * * *

    private class HostCheckIMAP extends AsyncTask<String, Void, String>
    {

        public String IPAddr;


        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                for (String hostname : MailServerDB.HOSTNAME_KEYS)
                {
                    Log.i(TAG, "Checking for: " + hostname + "." +params[0]);
                    // Add the hostname to the domain (hostname + domain)
                    InetAddress address = InetAddress.getByName(hostname + "." + params[0]);
                    IPAddr = address.getHostAddress();

                }

            }
            catch(UnknownHostException e)
            {
                Log.i(TAG, "Host not found");
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String hostname) {
            super.onPostExecute(hostname);
            if (hostname == null)
            {
                Log.i(TAG, "Host not found");
            }
            else
            {
                Log.i(TAG, "We found an server at" + hostname);
            }


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
}
