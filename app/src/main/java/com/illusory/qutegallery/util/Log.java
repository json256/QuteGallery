package com.illusory.qutegallery.util;

public class Log {
    private static final String TAG = "QUTE_GALLERY";

    public static int e(String msg) {
        return android.util.Log.e(TAG, msg);
    }

    public static int i(String msg) {
        return android.util.Log.i(TAG, msg);
    }

    public static int d(String msg) {
        return android.util.Log.d(TAG, msg);
    }
}
