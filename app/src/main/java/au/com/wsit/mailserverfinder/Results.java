package au.com.wsit.mailserverfinder;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;


public class Results extends ActionBarActivity {

    public static final String TAG = Results.class.getSimpleName();

    // Type of server
    TextView IMAP_TYPE;
    TextView POP3_TYPE;
    TextView EXCHANGE_TYPE;

    // hostnames
    TextView incomingHostname;
    TextView outgoingHostname;


    TextView mIncomingPorts;

    TextView mSMTP_TLS, mSMTP_SSL, mSMTP_PLAIN;

    Intent MainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Type
        IMAP_TYPE = (TextView) findViewById(R.id.imap_TV);
        POP3_TYPE = (TextView) findViewById(R.id.pop3_TV);
        EXCHANGE_TYPE = (TextView) findViewById(R.id.exchange_TV);

        // Hostnames
        incomingHostname = (TextView) findViewById(R.id.incomingServerNameInput);
        outgoingHostname = (TextView) findViewById(R.id.outgoingServerNameInput);

        // Incoming
        mIncomingPorts = (TextView) findViewById(R.id.incomingPorts);



        // Outgoing
        mSMTP_PLAIN = (TextView) findViewById(R.id.SMTP_PLAIN);
        mSMTP_TLS = (TextView) findViewById(R.id.SMTP_TLS_ID);
        mSMTP_SSL = (TextView) findViewById(R.id.SMTP_SSL_ID);


        MainActivityIntent = getIntent();

        Bundle Values = MainActivityIntent.getExtras();

        Object IMAP_SSL = Values.get("IMAP_SSL");
        Object IMAP_PLAIN = Values.get("IMAP_PLAIN");
        Object SMTP_PLAIN = Values.get("SMTP_PLAIN");
        Object POP3_PLAIN = Values.get("POP3_PLAIN");
        Object POP3_SSL = Values.get("POP3_SSL");
        Object SMTP_TLS = Values.get("SMTP_TLS");
        Object SMTP_SSL = Values.get("SMTP_SSL");
        Object EXCHANGE = Values.get("EXCHANGE");

        // Hostnames
        Object INCOMING_HOSTNAME_IMAP = Values.get("imap");
        Object INCOMING_HOSTNAME_POP3 = Values.get("pop3");
        Object INCOMING_OR_OUTGOING_HOSTNAME_MAIL = Values.get("mail");
        Object OUTGOING_HOSTNAME_SMTP = Values.get("smtp");
        Object INCOMING_HOSTNAME_REMOTE = Values.get("remote");
        Object DOMAIN = Values.get("DOMAIN");

        // We are using both if this true
        if (IMAP_SSL != null && IMAP_PLAIN != null)
        {
            IMAP_TYPE.setText("IMAP");
            mIncomingPorts.setText("tcp/993 OR tcp/143");

        }
        // Just plain
        else if (IMAP_PLAIN != null && IMAP_SSL == null)
        {
            IMAP_TYPE.setText("IMAP");
            mIncomingPorts.setText("tcp/143");
        }
        if(POP3_SSL != null)
        {
            POP3_TYPE.setText("POP3");
        }
        if (POP3_PLAIN != null)
        {
            POP3_TYPE.setText("POP3");
        }
        if (EXCHANGE != null)
        {
            EXCHANGE_TYPE.setText("Exchange");
        }
        if (SMTP_PLAIN != null)
        {
            mSMTP_PLAIN.setText("tcp/25");
        }
        if (SMTP_SSL != null)
        {
            mSMTP_SSL.setText("tcp/465");
        }
        if (SMTP_TLS != null)
        {
            mSMTP_TLS.setText("tcp/587");
        }


        // Get the hostnames
        if (INCOMING_HOSTNAME_IMAP != null)
        {
            incomingHostname.setText("imap." + DOMAIN.toString());
        }
        if (OUTGOING_HOSTNAME_SMTP != null)
        {
            outgoingHostname.setText("smtp." + DOMAIN.toString());
        }
        else if (INCOMING_OR_OUTGOING_HOSTNAME_MAIL != null)
        {
            incomingHostname.setText("mail." + DOMAIN.toString());
            outgoingHostname.setText("mail." + DOMAIN.toString());
        }
        else if (INCOMING_HOSTNAME_REMOTE != null)
        {
            incomingHostname.setText("remote." + DOMAIN.toString());
            outgoingHostname.setText("mail." + DOMAIN.toString());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
            case R.id.action_back:
                // Go back
                finish();
                break;
            case R.id.action_exit:
                System.exit(0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
