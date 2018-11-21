package com.example.breezil.favnews.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.breezil.favnews.R;
import com.example.breezil.favnews.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletSearchFragment extends Fragment {


    public TabletSearchFragment() {
        // Required empty public constructor
    }

    public static TabletSearchFragment getType(String type){
        TabletSearchFragment fragment = new TabletSearchFragment();
        Bundle args = new Bundle();
        args.putString(Constant.SEARCH_RESULT, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tablet_search, container, false);
    }

}
