package theleatherguy.epiandroid.Fragments.Modules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import theleatherguy.epiandroid.Fragments.Home.HomeTomorrow;
import theleatherguy.epiandroid.R;

public class Modules extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.content_home, container, false);

        final TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("My Modules"));
        tabLayout.addTab(tabLayout.newTab().setText("All Modules"));

        final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
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
                    return new MyModules();
                case 1:
                    return new AllModules();
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
