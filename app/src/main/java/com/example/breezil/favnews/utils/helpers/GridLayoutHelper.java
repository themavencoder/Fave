package com.example.breezil.favnews.utils.helpers;

import android.content.Context;
import android.util.DisplayMetrics;

public class GridLayoutHelper {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 400;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 3)
            noOfColumns = 1;
        return noOfColumns;
    }
}
