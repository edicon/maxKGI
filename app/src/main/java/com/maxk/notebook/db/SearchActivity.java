package com.maxk.notebook.db;

import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


public class SearchActivity extends FragmentActivity {
    
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_info);

        context = this;
        
        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu((Activity)context);
    }
}