package com.example.breezil.favnews.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.ui.BookMarksFragment;
import com.example.breezil.favnews.ui.HeadlinesFragment;
import com.example.breezil.favnews.ui.PreferredFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    SparseArray<Fragment> fragmentList = new SparseArray<>();

    private static final int preferred_Position = 0;
    private static final int headline_Position = 1;
    private static final int bookmark_Position = 2;


    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == preferred_Position){
            return new PreferredFragment();
        }else if(position == headline_Position){
            return new HeadlinesFragment();
        }else {
            return new BookMarksFragment();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Resources resources = context.getResources();
        switch (position){
            case preferred_Position:
                return resources.getString(R.string.preferred_fragment);

            case headline_Position:
                return resources.getString(R.string.head_line_fragment);
            case bookmark_Position:
                return resources.getString(R.string.book_mark_fragment);
            default:
                return "none";

        }
    }
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,position);
        fragmentList.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragmentList.remove(position);
        super.destroyItem(container,position,object);
    }
}
