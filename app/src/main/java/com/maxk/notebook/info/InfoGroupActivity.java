package com.maxk.notebook.info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;


public class InfoGroupActivity extends FragmentActivity {
    
	private Context context;
	private Button[] buttons = new Button[MaxkInfo.maxButton];
	Button mainButton1, mainButton2, mainButton3, mainButton4, mainButton5, mainButton6;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_group);
        
        context = this;
        
        String[] infoData = InfoData.InfoGroupS1;
        MaxkUtils.initMemberGroup( (Activity) context, buttons, infoData,  new mainMenuOnClickListener());

        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu((Activity)context);
    }
    
	public class mainMenuOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if( !MaxkInfo.logOn ) {
				MaxkUtils.loginError( context );
				return;
			}
			
			getInfoType( v, buttons );
			MaxkUtils.goInfoActivity(context);
			// finish();
		} 
	}
	
	public void getInfoType( View v, Button[] buttons ) {
		
		if (v == buttons[0]) {
			MaxkInfo.infoType = 1;
		} else if (v == buttons[1]) {
			MaxkInfo.infoType = 2;
		} else if (v == buttons[2]) {
			MaxkInfo.infoType = 3;
		} else if (v == buttons[3]) {
			MaxkInfo.infoType = 4;
		} else if (v == buttons[4]) {
			MaxkInfo.infoType = 5;
		} else if (v == buttons[5]) {
			MaxkInfo.infoType = 6;
		} else if (v == buttons[6]) {
			MaxkInfo.infoType = 7;
		} else if (v == buttons[7]) {
			MaxkInfo.infoType = 8;
		} else if (v == buttons[8]) {
			MaxkInfo.infoType = 9;
		} else if (v == buttons[9]) {
			MaxkInfo.infoType = 10;
		} else if (v == buttons[10]) {
			MaxkInfo.infoType = 11;
		} else if (v == buttons[11]) {
			MaxkInfo.infoType = 12;
		} else if (v == buttons[12]) {
			MaxkInfo.infoType = 13;
		} else if (v == buttons[13]) {
			MaxkInfo.infoType = 14;
		} else if (v == buttons[14]) {
			MaxkInfo.infoType = 15;
		} 
		
		if( BuildConfig.DEBUG ) {
			Log.d("ggSS", "getInfoType: " + MaxkInfo.infoType);
		}
	}
}