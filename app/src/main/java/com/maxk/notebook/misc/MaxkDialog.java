package com.maxk.notebook.misc;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;

public class MaxkDialog {

	private final String TAG = MaxkDialog.class.getSimpleName();
	
	private static FragmentActivity 	activity;
	private Context 			context;
	private SharedPreferences 	mSharedPref;
	private SharedPreferences.Editor mEditor;
	
	public MaxkDialog( FragmentActivity a ) {
		activity 	= a;
		context 	= a.getBaseContext();
		
		mSharedPref   = PreferenceManager.getDefaultSharedPreferences(context);
		mEditor 	  = mSharedPref.edit();
		
		initDiaglogLogo();
	}
	
	private static int logoRid;
	private void initDiaglogLogo(){
	}

	
	private static String 		searchText;
	private static EditText 	searchEdit;
	public void searchDialog( FragmentActivity a ) {
		activity = a;
		context  = a.getBaseContext();
	    DialogFragment newFragment = new searchDialogFragment();
	    newFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "searchDialogFragment");
	}
	public static class searchDialogFragment extends DialogFragment {
		public searchDialogFragment() {
			//empty constructor
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

	    	AlertDialog.Builder  searchBuilder = new AlertDialog.Builder(getActivity());
			
		    LayoutInflater inflater = activity.getLayoutInflater();
		    View searchView  = inflater.inflate(R.layout.search_dialog, null, false);
		    searchEdit 	= (EditText)searchView.findViewById(R.id.search_edit);
		    searchEdit.setHint(R.string.search_hint);
		    setSearchView();
		    initImeMode();
		    
		    searchBuilder
		    .setView(searchView)
		    .setIcon(logoRid)
		    .setTitle(R.string.search_dialog_title)
		    /* .setSingleChoiceItems(R.array.portal_site, site,		// web_site: portal/sns/dict/input
					new  DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							site = which;
						}
					}) */
			.setPositiveButton(getString(android.R.string.search_go),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						MaxkInfo.infoType = 0;
				        searchText = searchEdit.getText().toString();
				        if( searchText != null && searchText.contains(MaxkInfo.SEARCH_MAIN_SCHED))		// 주요행사
				        	MaxkInfo.infoType = 4;
				        else if( searchText != null && searchText.contains(MaxkInfo.SEARCH_SCHEDULE))	// 11: 학사일정
				        	MaxkInfo.infoType = 11;
				        else if( searchText != null && searchText.contains(MaxkInfo.SEARCH_LECTURE))	// 12: 강의시간
				        	MaxkInfo.infoType = 12;

				        if( MaxkInfo.infoType == 0)
							MaxkUtils.searchWord( activity, searchText, "검색 결과" );
				        	// MaxkUtils.searchMember( activity, searchText );
				        else
				        	MaxkUtils.goInfoActivity( activity );
					}
				})
			.setNeutralButton(getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {						
					}
			});
			return searchBuilder.create();
		}
	}
	
	private static void setSearchView(){
		searchEdit.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( BuildConfig.DEBUG )
					Log.i("CaptionTube", "onKey" + keyCode);
				
				if (event.getAction() == KeyEvent.ACTION_UP) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
				        searchText = searchEdit.getText().toString();
						MaxkUtils.searchWord( activity, searchText, "검색 결과" );
				        // MaxkUtils.searchMember( activity, searchText );
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
		
		searchEdit.addTextChangedListener(new TextWatcher() {
			public void  afterTextChanged(Editable s){ 
				if( BuildConfig.DEBUG )
					Log.d("setSearchView", "afterTextChanged"); 
			} 
			public void  beforeTextChanged(CharSequence s, int start, int count, int after) { 
				if( BuildConfig.DEBUG )
					Log.w("onTextChanged", "beforeTextChanged: "+ s + ", start: " + start + ", count: " + count + ", after: " + after); 
			} 
			public void  onTextChanged(CharSequence s, int start, int before, int count) 
			{ 
				if( BuildConfig.DEBUG )
					Log.w("onTextChanged", "onTextChanged: "+ s + ", start: " + start + ", before: " + start + ", count: " + count); 
			}
		});
	}
	
	private static void initImeMode() {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(searchEdit, InputMethodManager.SHOW_IMPLICIT);
		searchEdit.requestFocus();
		
		if( Locale.getDefault().equals(Locale.ENGLISH) )
			searchEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		else
			searchEdit.setInputType(InputType.TYPE_CLASS_TEXT);
	}
	
	private static String 		loginText;
	private static EditText 	loginEdit;
	public void loginDialog( FragmentActivity a ) {
		activity = a;
		context  = a.getBaseContext();
	    DialogFragment newFragment = new loginDialogFragment();
	    newFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "loginDialogFragment");
	}
	public static class loginDialogFragment extends DialogFragment {
		public loginDialogFragment() {
			//empty constructor
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

	    	AlertDialog.Builder  loginBuilder = new AlertDialog.Builder(getActivity());
			
		    LayoutInflater inflater = activity.getLayoutInflater();
		    View loginView  = inflater.inflate(R.layout.search_dialog, null, false);
		    loginEdit 	= (EditText)loginView.findViewById(R.id.search_edit);
		    loginEdit.setHint(R.string.login_hint);
		    setLoginView();
		    initLoginImeMode();
		    
		    loginBuilder
		    .setView(loginView)
		    .setIcon(logoRid)
		    .setTitle(R.string.login_dialog_title)
		    /* .setSingleChoiceItems(R.array.portal_site, site,		// web_site: portal/sns/dict/input
					new  DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							site = which;
						}
					}) */
			.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						beginLogin( null );
					}
				})
			.setNeutralButton(getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {						
					}
			});
			return loginBuilder.create();
		}
	}
	
	private static void setLoginView(){
		loginEdit.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( BuildConfig.DEBUG )
					Log.i("CaptionTube", "onKey" + keyCode);
				
				if (event.getAction() == KeyEvent.ACTION_UP) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						beginLogin( null );
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
		
		loginEdit.addTextChangedListener(new TextWatcher() {
			public void  afterTextChanged(Editable s){ 
				if( BuildConfig.DEBUG )
					Log.d("loginEdit", "afterTextChanged"); 
			} 
			public void  beforeTextChanged(CharSequence s, int start, int count, int after) { 
				if( BuildConfig.DEBUG )
					Log.w("onTextChanged", "beforeTextChanged: "+ s + ", start: " + start + ", count: " + count + ", after: " + after); 
			} 
			public void  onTextChanged(CharSequence s, int start, int before, int count) 
			{ 
				if( BuildConfig.DEBUG )
					Log.w("onTextChanged", "onTextChanged: "+ s + ", start: " + start + ", before: " + start + ", count: " + count); 
			}
		});
	}
	
	private static void initLoginImeMode() {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(loginEdit, InputMethodManager.SHOW_IMPLICIT);
		loginEdit.requestFocus();
		
		if( Locale.getDefault().equals(Locale.ENGLISH) )
			loginEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
		else
			loginEdit.setInputType(InputType.TYPE_CLASS_TEXT);
	}
	
	private static void beginLogin( String cat) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginEdit.getWindowToken(), 0);

        loginText = loginEdit.getText().toString();
        if (loginText != null && loginText.length() != 0) {
        	Intent intent = new Intent( activity, LoginActivity.class);
    		intent.putExtra("login", 		loginText );
    		activity.startActivity(intent);
        }
	}
}