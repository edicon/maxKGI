package com.maxk.notebook.member.division;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.R;


public class MemberGroupS32Activity extends FragmentActivity {
    
	private Context context;
	private Button[] buttons = new Button[MaxkInfo.maxButton];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.member_group);
        
        context = this;
        
        String[] memberDiv = MemberDivData.MemberGroupS32;
        MaxkUtils.initMemberGroup( (Activity) context, buttons, memberDiv,  new mainMenuOnClickListener());
        
        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu((Activity)context);
    }
    
	private class mainMenuOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			String query = MaxkUtils.getSearchText( v,  buttons, MemberDivData.MemberGroupS32Type );
			String title = MaxkUtils.findTitle( MemberDivData.MemberGroupS32, MemberDivData.MemberGroupS32Type, query);
			MaxkUtils.searchGroup( context, query, title );
		}
	}
}