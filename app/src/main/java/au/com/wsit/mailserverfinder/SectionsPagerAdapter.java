package au.com.wsit.mailserverfinder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by guyb on 18/02/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public SectionsPagerAdapter(Context context, android.support.v4.app.FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position)
        {
            case 0:
                return new PopFragment();
            case 1:
                return new ImapFragment();
            case 2:
                return new ExchangeFragment();
            case 3:
                return new SmtpFragment();

        }

    return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.POPSettings).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.IMAPSettings).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.ExchangeSettings).toUpperCase(l);
            case 3:
                return mContext.getString(R.string.SMTPSettings).toUpperCase(l);
        }
        return null;
    }
}
