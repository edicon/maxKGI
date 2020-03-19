package com.maxk.notebook.member;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.maxk.notebook.member.division.MemberDivData;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.R;


public class MemberGroupListActivity extends FragmentActivity {
    
	private Context context;
	private Button[] buttons = new Button[MaxkInfo.maxButton];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.member_group);
        
        context = this;
        
        String[] memberDiv = MemberDivData.MemberGroupS2;
        MaxkUtils.initMemberGroup( (Activity) context, buttons, memberDiv,  new mainMenuOnClickListener());

        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu((Activity)context);
    }
    
	private class mainMenuOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if( !MaxkInfo.logOn ) {
				MaxkUtils.loginError( context );
				return;
			}
			
			if (v == buttons[0]) {
				MaxkInfo.groupType = 1;
			} else if (v == buttons[1]) {
				MaxkInfo.groupType = 2;
			} else if (v == buttons[2]) {
				MaxkInfo.groupType = 3;
			} else if (v == buttons[3]) {
				MaxkInfo.groupType = 4;
			} else if (v == buttons[4]) {
				MaxkInfo.groupType = 5;
			} else if (v == buttons[5]) {
				MaxkInfo.groupType = 6;
			} else if (v == buttons[6]) {
				MaxkInfo.groupType = 7;
			} else if (v == buttons[7]) {
				MaxkInfo.groupType = 8;
			} else if (v == buttons[8]) {
				MaxkInfo.groupType = 9;
			} else if (v == buttons[9]) {
				MaxkInfo.groupType = 10;
			}
			
			MaxkUtils.goMemberGroupActivity(context);
			// finish();
		}
	}
}