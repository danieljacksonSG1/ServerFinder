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
    String exchangeServerName;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exchange_results, container, false);
        mExchangeServerName = (TextView) rootView.findViewById(R.id.exchangeServerNameID);

        Intent MainActivityIntent = getActivity().getIntent();
        exchangeServerName = MainActivityIntent.getStringExtra("EXCHANGE_SERVER_URL");

        mExchangeServerName.setText(exchangeServerName);

        return rootView;
    }

}


