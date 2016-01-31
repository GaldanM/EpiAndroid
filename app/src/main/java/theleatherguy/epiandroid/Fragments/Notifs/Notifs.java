package theleatherguy.epiandroid.Fragments.Notifs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import theleatherguy.epiandroid.R;

public class Notifs extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		View inflatedView = inflater.inflate(R.layout.content_notifs, container, false);

		TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabs);
		final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

		tabLayout.addTab(tabLayout.newTab().setText("Messages"));
		tabLayout.addTab(tabLayout.newTab().setText("Alerts"));

		viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				viewPager.setCurrentItem(tab.getPosition());
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{}
			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{}
		});

		return inflatedView;
	}

	public class PagerAdapter extends FragmentStatePagerAdapter
	{
		int numOfTabs;

		public PagerAdapter(FragmentManager fm, int NumOfTabs)
		{
			super(fm);
			this.numOfTabs = NumOfTabs;
		}

		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{
				case 0:
					return new NotifsMessages();
				case 1:
					return new NotifsAlerts();
				default:
					return null;
			}
		}

		@Override
		public int getCount()
		{
			return numOfTabs;
		}
	}
}
