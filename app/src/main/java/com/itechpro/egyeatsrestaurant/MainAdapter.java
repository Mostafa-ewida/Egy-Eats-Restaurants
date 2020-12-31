package com.itechpro.egyeatsrestaurant;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"Profile", "Menu", "Cart"};

    public MainAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                ProfileTabFragment profileTabFragment = new ProfileTabFragment();
                return profileTabFragment;
            case 1:
                MenuTabFragment menuTabFragment = new MenuTabFragment();
                return menuTabFragment;
            case 2:
                CartTabFragment cartTabFragment = new CartTabFragment();
                return cartTabFragment;
            default:
                return null;
        }
    }
}
