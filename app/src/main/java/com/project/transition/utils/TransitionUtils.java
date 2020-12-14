package com.project.transition.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

public class TransitionUtils {

    public static boolean isAtLeastLollipop() {
        return true;
    }

    public static float getViewRadius(View view) {
        return (float) Math.hypot(view.getHeight() / 2, view.getWidth() / 2);
    }

    public static int dpToPixels(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }
}
