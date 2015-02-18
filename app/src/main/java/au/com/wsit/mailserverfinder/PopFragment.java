package au.com.wsit.mailserverfinder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    // Create the intent from Main



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pop_results, container, false);


        return rootView;

    }



}
