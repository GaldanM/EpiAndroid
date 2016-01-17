package theleatherguy.epiandroid.Fragments.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import theleatherguy.epiandroid.R;

public class Home extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setRetainInstance(true);
		FragmentTabHost tabHost = new FragmentTabHost(getActivity());

		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontent);

		tabHost.addTab(tabHost.newTabSpec("today").setIndicator("Today"), HomeToday.class, null);
		tabHost.addTab(tabHost.newTabSpec("tomorrow").setIndicator("Tomorrow"), HomeTomorrow.class, null);
		tabHost.addTab(tabHost.newTabSpec("deliveries").setIndicator("Deliveries"), HomeDeliveries.class, null);
		tabHost.addTab(tabHost.newTabSpec("marks").setIndicator("Marks"), HomeMarks.class, null);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(10);
        }
		return (tabHost);
	}
}
