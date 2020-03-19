package com.maxk.notebook.maxkgi;

import android.content.Context;
import android.support.annotation.NonNull;

public class NullTest {
    private static Context nullTest = null;

    public static Context getNull(@NonNull Context c) {
        nullTest = c;
       return nullTest;
    }
}
