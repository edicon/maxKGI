package com.maxk.notebook.misc;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maxk.notebook.maxkgi.R;

/**
 * @History	
 *  2014.01.14: sdcard == null && USE_PUBLIC_DIR_FOR_SHARE --> USE_PUBLIC_DIR_FOR_SHARE, NPE: mContext( to context )
 * 
 */
public class PopupMenu {

	private Context context;
	
	public PopupMenu( Context cx ){
		context = cx;
	}
	
	private PopupWindow popupWindow;
	public void showPopupWindow( View anchor ) {

		int w = (getScreenWidth( context )/3)*2;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView 				  = layoutInflater.inflate(R.layout.popup_menu, null, false);
		initPopupMenu( popupView );
		popupWindow = new PopupWindow(popupView, w, LayoutParams.WRAP_CONTENT); // USE!! LayoutParams.MATCH_PARENT for Gallery
		popupWindow.setAnimationStyle(-1); // �ִϸ��̼� ����(-1:����, 0:�������� or Animation Resource)
		// popupWindow.showAsDropDown(anchor);
		popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0); // Parent View
	}
	
	private TextView popupMenu1, popupMenu2, popupMenu3, popupMenu4, menu_setting, menu_close;
	private void initPopupMenu( View v ) {
		
		popupMenu1 = (TextView)v.findViewById(R.id.popup_menu1); 
		popupMenu2 = (TextView)v.findViewById(R.id.popup_menu2); 
		popupMenu3 = (TextView)v.findViewById(R.id.popup_menu3); 
		popupMenu4 = (TextView)v.findViewById(R.id.popup_menu4); 
		menu_setting = (TextView)v.findViewById(R.id.popup_menu_setting); 
		menu_close 	 = (TextView)v.findViewById(R.id.popup_menu_close); 
		
		popupMenu1.setOnClickListener(new popupMenuOnClickListener());
		popupMenu2.setOnClickListener(new popupMenuOnClickListener());
		popupMenu3.setOnClickListener(new popupMenuOnClickListener());
		popupMenu4.setOnClickListener(new popupMenuOnClickListener());
		menu_setting.setOnClickListener(new popupMenuOnClickListener());
		menu_close.setOnClickListener(new popupMenuOnClickListener());
		
	}
	
	private class popupMenuOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == popupMenu1) {
				MaxkInfo.popupType = 1;
			} else if (v == popupMenu2) {
				MaxkInfo.popupType = 2;
			} else if (v == popupMenu3) {
				MaxkInfo.popupType = 3;
			} else if (v == popupMenu4) {
				MaxkInfo.popupType = 4;
			} else if (v == menu_setting) {
				MaxkInfo.popupType = 5;
			} else if (v == menu_close) {
				// MaxkInfo.popupType = 6;
				popupWindow.dismiss();
				return;
			}
			
			MaxkUtils.goPopupActivity(context);
			popupWindow.dismiss();
			((Activity) context).finish();
		}
	}
	
	private int getScreenWidth( Context cx ) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) cx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		
		return width;
	}
}
