package com.maxk.notebook.misc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxk.notebook.blog.BlogMainActivity;
import com.maxk.notebook.blog.Config;
import com.maxk.notebook.db.MemberDB;
import com.maxk.notebook.db.ProjectSheet;
import com.maxk.notebook.info.InfoGroupActivity;
import com.maxk.notebook.info.InfoViewActivity;
import com.maxk.notebook.member.MemberGroupListActivity;
import com.maxk.notebook.member.MemberInfoActivity;
import com.maxk.notebook.member.MemberListActivity;
import com.maxk.notebook.member.division.MemberGroupS31Activity;
import com.maxk.notebook.member.division.MemberGroupS32Activity;
import com.maxk.notebook.member.division.MemberGroupS33Activity;
import com.maxk.notebook.member.division.MemberGroupS34Activity;
import com.maxk.notebook.member.division.MemberGroupS35Activity;
import com.maxk.notebook.post.PostListActivity;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.MaxkMainActivity;
import com.maxk.notebook.maxkgi.R;
import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

public class MaxkUtils {
	private static final String TAG = MaxkUtils.class.getName();
	
	public static void getMemberFromSheet( Activity a ) {
		
		MaxkInfo.dataLoading = false;

		// "http://spreadsheets.google.com/feeds/list/1SGisT3OqmUs-EYKdF_VaA6XS38b5vdyc_pN3Yl02Nc8/od6/public/values?alt=json";
		String sUrl = MaxkInfo.PUB_TO_WEB_MAKX_PROJECT_SHEET;
		ProjectSheet ps = new ProjectSheet( a, sUrl );
		ps.queryProjectInfo();
	}
	
	public static void goGroupListActivity( Context cx ) {
    	Intent intent = new Intent(cx, MemberGroupListActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goMemberGroupActivity( Context cx ) {
		Intent intent = null;
		switch( MaxkInfo.groupType) {
		default:
		case 1:
			intent = new Intent(cx, MemberGroupS31Activity.class);
			break;
		case 2:
			intent = new Intent(cx, MemberGroupS32Activity.class);
			break;
		case 3:
			intent = new Intent(cx, MemberGroupS33Activity.class);
			break;
		case 4:
			intent = new Intent(cx, MemberGroupS34Activity.class);
			break;
		case 5:
			intent = new Intent(cx, MemberGroupS35Activity.class);
			break;
		}
		cx.startActivity(intent);
	}
	
	public static void goMemberListActivity( Context cx ) {
    	Intent intent = new Intent(cx, MemberListActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goMemberInfoActivity( Context cx ) {
    	Intent intent = new Intent(cx, MemberInfoActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goPostListActivity( Context cx ) {
		if( true ) {
			Intent intent = new Intent(cx, BlogMainActivity.class);
			cx.startActivity(intent);
		} else {
			Intent intent = new Intent(cx, PostListActivity.class);
			cx.startActivity(intent);
		}
	}
	
	public static void goInfoGroupActivity( Context cx ) {
    	Intent intent = new Intent(cx, InfoGroupActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goInfoActivity( Context cx ) {
    	Intent intent = new Intent(cx, InfoViewActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goMaxkMainActivity( Context cx ) {
    	Intent intent = new Intent(cx, MaxkMainActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goSearchActivity( FragmentActivity cx ) {
		maxkDialog.searchDialog( cx );
	}
	
	public static void goSettingActivity( Context cx ) {
    	Intent intent = new Intent(cx, SettingActivity.class);
		cx.startActivity(intent);
	}
	
	public static void goLoginActivity( Context cx ) {
		if( false && BuildConfig.DEBUG ) {
			Intent intent = new Intent(cx, LoginActivity.class);
			cx.startActivity(intent);
		} else {
			maxkDialog.loginDialog( (FragmentActivity) cx );
		}
	}
	
	public static MaxkDialog maxkDialog;
	public static void initMenu( final Activity cx ) {
		
		maxkDialog = new MaxkDialog( (FragmentActivity)cx );
		
		Button menuMain 	= (Button)((Activity) cx).findViewById(R.id.menuMain); 
		Button menuSearch 	= (Button)((Activity) cx).findViewById(R.id.menuSearch);
		Button menuSetting 	= (Button)((Activity) cx).findViewById(R.id.menuSetting);
		Button menuLogin 	= (Button)((Activity) cx).findViewById(R.id.menuLogin);
		
		if( MaxkInfo.MENU_ADMIN ) {
			menuLogin.setText(R.string.menuAdmin);
		} else {
			menuLogin.setText(R.string.menuLogin);
		}
		
		menuMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MaxkUtils.goMaxkMainActivity( cx );
				cx.finish();
			}
        });
		
		menuSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( !MaxkInfo.logOn ) {
					MaxkUtils.loginError( cx );
					return;
				}
				MaxkUtils.goSearchActivity( (FragmentActivity) cx );
				// cx.finish();
			}
        });
		
		menuSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MaxkUtils.goSettingActivity( cx );
				cx.finish();
			}
        });
		
		menuLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( MaxkInfo.MENU_ADMIN ) {
					searchAdmin( cx, MaxkInfo.MAXK_ADMIN );
				} else
					MaxkUtils.goLoginActivity( cx );
				// cx.finish();
			}
        });
	}
	
	public static void initPopupMenu(final Activity cx) {

		final ImageButton topPopupMenu = (ImageButton) cx.findViewById(R.id.topTitleRightMenu_ref);
		if( topPopupMenu == null ) return;
		
		topPopupMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupMenu(cx, topPopupMenu);
			}
		});
	}
	
	public static void showPopupMenu( Context cx, ImageButton v	 ) {
		if( false && BuildConfig.DEBUG ) {
			Log.i( TAG, "popupMainMenu");
			Toast.makeText(cx, "popupMenu", Toast.LENGTH_SHORT).show();
		} else {
			PopupMenu popupMenu = new PopupMenu( cx );
			popupMenu.showPopupWindow( v  );
		}
	}
	
	public static void goPopupActivity( Context cx ) {

		switch( MaxkInfo.popupType) {
		default:
		case 1:
			goGroupListActivity( cx );
			break;
		case 2:
			goPostListActivity( cx );
			break;
		case 3:
			goInfoGroupActivity( cx );
			break;
		case 4:
			goMaxkMainActivity( cx );
			break;
		case 5:
			goSettingActivity( cx );
			break;
		case 6:
			((Activity) cx).finish();
			break;
		}
	}
	
	public static void initMemberGroup( Activity a, Button[] buttons, String[] memberDiv, View.OnClickListener l) {
        
		if( memberDiv.length > MaxkInfo.maxButton )
			Log.w("ggSS", "Max Menu Count: " + MaxkInfo.maxButton);
		
		try {
			TextView topTitle = (TextView)a.findViewById(R.id.topTitle); 
			
	        buttons[0] = (Button)a.findViewById(R.id.mainButton1); 
	        buttons[1] = (Button)a.findViewById(R.id.mainButton2); 
	        buttons[2] = (Button)a.findViewById(R.id.mainButton3); 
	        buttons[3] = (Button)a.findViewById(R.id.mainButton4); 
	        buttons[4] = (Button)a.findViewById(R.id.mainButton5); 
	        buttons[5] = (Button)a.findViewById(R.id.mainButton6);
	        buttons[6] = (Button)a.findViewById(R.id.mainButton7); 
	        buttons[7] = (Button)a.findViewById(R.id.mainButton8); 
	        buttons[8] = (Button)a.findViewById(R.id.mainButton9); 
	        buttons[9] = (Button)a.findViewById(R.id.mainButton10); 
	        buttons[10] = (Button)a.findViewById(R.id.mainButton11);
	        buttons[11] = (Button)a.findViewById(R.id.mainButton12); 
	        buttons[12] = (Button)a.findViewById(R.id.mainButton13); 
	        buttons[13] = (Button)a.findViewById(R.id.mainButton14); 
	        buttons[14] = (Button)a.findViewById(R.id.mainButton15);
	        
	        LinearLayout[] layoutViews = new LinearLayout[ MaxkInfo.maxButton];
	        layoutViews[0] = (LinearLayout)a.findViewById(R.id.menuMain1); 
	        layoutViews[1] = (LinearLayout)a.findViewById(R.id.menuMain2); 
	        layoutViews[2] = (LinearLayout)a.findViewById(R.id.menuMain3); 
	        layoutViews[3] = (LinearLayout)a.findViewById(R.id.menuMain4); 
	        layoutViews[4] = (LinearLayout)a.findViewById(R.id.menuMain5); 
	        layoutViews[5] = (LinearLayout)a.findViewById(R.id.menuMain6);
	        layoutViews[6] = (LinearLayout)a.findViewById(R.id.menuMain7); 
	        layoutViews[7] = (LinearLayout)a.findViewById(R.id.menuMain8); 
	        layoutViews[8] = (LinearLayout)a.findViewById(R.id.menuMain9); 
	        layoutViews[9] = (LinearLayout)a.findViewById(R.id.menuMain10); 
	        layoutViews[10] = (LinearLayout)a.findViewById(R.id.menuMain11);
	        layoutViews[11] = (LinearLayout)a.findViewById(R.id.menuMain12); 
	        layoutViews[12] = (LinearLayout)a.findViewById(R.id.menuMain13); 
	        layoutViews[13] = (LinearLayout)a.findViewById(R.id.menuMain14); 
	        layoutViews[14] = (LinearLayout)a.findViewById(R.id.menuMain15); 
	        
	        for( int i = 0; i < MaxkInfo.maxButton; i++ )
	        	layoutViews[i].setVisibility(View.GONE);
	        
	        topTitle.setText(memberDiv[0]);
	        for( int i = 0; i < memberDiv.length-1; i++ ) {
	        	layoutViews[i].setVisibility(View.VISIBLE);
	        	buttons[i].setOnClickListener(l);
	        	buttons[i].setText(memberDiv[i+1]);
	        }
	        
		} catch( Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSearchText( View v, Button[] buttons, String[] memberType) {
		
		String query = null;

		// ToDo: Index 0부터 시작하게 변경
		if (v == buttons[0]) {
			query = memberType[0]; 		// 1 --> 0
		} else if (v == buttons[1]) {
			query = memberType[1];
		} else if (v == buttons[2]) {
			query = memberType[2];
		} else if (v == buttons[3]) {
			query = memberType[3];
		} else if (v == buttons[4]) {
			query = memberType[4];
		} else if (v == buttons[5]) {
			query = memberType[5];
		} else if (v == buttons[6]) {
			query = memberType[6];
		} else if (v == buttons[7]) {
			query = memberType[7];
		} else if (v == buttons[8]) {
			query = memberType[8];
		} else if (v == buttons[9]) {
			query = memberType[9];
		} else if (v == buttons[10]){	// 명예 위원: 11
			query = memberType[10];
		}
		
		if( BuildConfig.DEBUG ) {
			Log.d("ggSS", "getSearchText: " + query);
		}
		return query;
	}

	public static void searchGroup( Context context, String searchText, String title ) {
        
		if( BuildConfig.DEBUG ) {
			Log.d("ggSS", "searchGroup: searchText: " + searchText);
			// searchText = "2014";
		}
		
        if (searchText != null && searchText.length() != 0) {
        	// Intent intent = new Intent(activity, SearchActivity.class);
        	Intent intent = new Intent(context, MemberListActivity.class);
        	
    		intent.putExtra(MaxkInfo.SEARCH_TYPE, 		MaxkInfo.SEARCH_GROUP );
    		intent.putExtra(MaxkInfo.SEARCH_TEXT, 		searchText );
    		intent.putExtra(MaxkInfo.TITLE_TEXT, 		title );
    		
    		context.startActivity(intent);
        }
	}
	
	
	/**
	 * searchMember( context, String searchText)
	 */
	public static void searchMember( Context context, String searchText) {
		
		// InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
        
        if (searchText != null && searchText.length() != 0) {
        	Intent intent = new Intent(context, MemberListActivity.class);
        	
    		intent.putExtra(MaxkInfo.SEARCH_TYPE, 		MaxkInfo.SEARCH_MEMBER );
    		intent.putExtra(MaxkInfo.SEARCH_TEXT, 		searchText );
    		
    		context.startActivity(intent);
        }
	}

	public static void searchWord( Context context, String searchText, String title ) {

		if( BuildConfig.DEBUG ) {
			Log.d("ggSS", "searchGroup: searchText: " + searchText);
			// searchText = "2014";
		}

		if (searchText != null && searchText.length() != 0) {
			// Intent intent = new Intent(activity, SearchActivity.class);
			Intent intent = new Intent(context, MemberListActivity.class);

			intent.putExtra(MaxkInfo.SEARCH_TYPE, 		MaxkInfo.SEARCH_WORD );
			intent.putExtra(MaxkInfo.SEARCH_TEXT, 		searchText );
			intent.putExtra(MaxkInfo.TITLE_TEXT, 		title );

			context.startActivity(intent);
		}
	}

	/**
	/**
	 * searchAdmin( context, null)
	 */
	public static void searchAdmin( Context context, String searchText) {
		
		// InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
        
        if (searchText != null && searchText.length() != 0) {
        	Intent intent = new Intent(context, MemberListActivity.class);
        	
    		intent.putExtra(MaxkInfo.SEARCH_TYPE, 		MaxkInfo.SEARCH_ADMIN );
    		intent.putExtra(MaxkInfo.SEARCH_TEXT, 		searchText );
    		intent.putExtra(MaxkInfo.TITLE_TEXT, 		context.getString(R.string.menuAdmin) );
    		
    		context.startActivity(intent);
        }
	}
	
	public static String getPhoneNo( Activity a ) {
		
		TelephonyManager tMgr = (TelephonyManager)a.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();
		
		if( phoneNumber != null && phoneNumber.startsWith("+82"))
			phoneNumber =  phoneNumber.replace("+82", "0");
		
		return phoneNumber;
	}
	
    public static boolean queryLogin( Context cx, String id ) {
    	
    	MemberDB db = new MemberDB( cx );
		Cursor c = db.loginMember( id );
		if( c == null || c.getCount() == 0 ) {
			Log.w("LOG", "Login Failed: " + id);
			return false;
		}
		
		MaxkInfo.isAdmin  = queryAdmin( cx, id );
		c.close();

		Log.w("LOG", "Login Passed: " + id);

		return true;
    }
    
    public static boolean queryAdmin( Context cx, String id ) {
    	
    	MemberDB db = new MemberDB( cx );
		Cursor c = db.searchAdmin( MaxkInfo.MAXK_ADMIN  );
		if( c == null || c.getCount() == 0 || id == null ) {
			Log.w("LOG", "Admin Failed: " + id);
			return false;
		}
		
		c.moveToFirst();
		for( int i = 0; i < c.getCount(); i++ ) {
			int index = c.getColumnIndex(MemberDB.MOBILE_PHONE);
			String phone = c.getString(index);
			if( phone != null && id.equals( phone )) {
				Log.w("LOG", "Admin Member: " + id);
				c.close();
				return true;
			}
			c.moveToNext();
		}
		c.close();
		return false;
    }
    
	public static void loadImageFromAsset( Context cx, ImageView v, String imgPath ) {
		if( BuildConfig.DEBUG )
			Log.i("PIC", "loadImage: " + imgPath);
		try {
			InputStream ims = cx.getAssets().open(imgPath);
			if( ims.available() > 0 ) {
				Drawable d = Drawable.createFromStream(ims, null);
				v.setImageDrawable(d);
			} else
				v.setImageResource(R.drawable.member_photo);
		} catch( FileNotFoundException e) {
			v.setImageResource(R.drawable.member_photo);
			if( BuildConfig.DEBUG )
				e.printStackTrace();
		} catch( IOException e) {
			v.setImageResource(R.drawable.member_photo);
			e.printStackTrace();
		} 
	}
	
	public static String findTitle( String[] titles, String[] types, String key ) {
		for( int i = 0; i < types.length ; i++ ) {
			if( types[i].equals(key) )
				// ToDo: Index를 0에서 시작하여 변경
				return titles[i+1]; // i --> i+1
		}
		return titles[0];
	}
	
	public static void updateMember(final Activity context) {
		if( !MaxkInfo.PARSE_STOPPED ) {
			ParseConfig.getInBackground(new ConfigCallback() {
				@Override
				public void done(ParseConfig config, ParseException e) {
					if (e == null) {
						if (BuildConfig.DEBUG)
							Log.d("TAG", "Config: Success from the server.");
					} else {
						if (BuildConfig.DEBUG)
							Log.e("TAG", "Config: Failed. Using Cached Config.");
						config = ParseConfig.getCurrentConfig();
					}

					long dataVersion = config.getLong("appDataVersion", 0L);
					if (BuildConfig.DEBUG)
						Log.d("TAG", "Config: appDataVersion: " + dataVersion);

					if (MaxkInfo.appDataVersion < dataVersion) {
						if (BuildConfig.DEBUG)
							Log.w("TAG", "updateMember: " + dataVersion);
						MaxkUtils.getMemberFromSheet(context);
					}
				}
			});
		}
	}
    
    public static void getAppDataVersion( Context context){

        DatabaseReference databaseRef;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor edit = sp.edit();

        // ToDo: Parse Service is stopped!!
		if( !MaxkInfo.PARSE_STOPPED ) {
			ParseConfig.getInBackground(new ConfigCallback() {
				@Override
				public void done(ParseConfig config, ParseException e) {
					if (e == null) {
						if (BuildConfig.DEBUG)
							Log.d("TAG", "Config: Success from the server.");
					} else {
						if (BuildConfig.DEBUG)
							Log.e("TAG", "Config: Failed. Using Cached Config.");
						config = ParseConfig.getCurrentConfig();
					}

					MaxkInfo.appDataVersion = config.getLong("appDataVersion", 0L);
					if (BuildConfig.DEBUG)
						Log.w("TAG", "Config: appDataVersion: " + MaxkInfo.appDataVersion);

					edit.putBoolean("APP_DATA_LOADING", MaxkInfo.dataLoading);
					edit.putLong("APP_DATA_VER", MaxkInfo.appDataVersion);
					edit.apply(); // commit();
				}
			});
		} else {
			databaseRef = FirebaseDatabase.getInstance().getReference().child(Config.VERSION_PATH);
			databaseRef.addListenerForSingleValueEvent( versionListener );

			edit.putBoolean("APP_DATA_LOADING", MaxkInfo.dataLoading);
			edit.putLong(	"APP_DATA_VER",	 	MaxkInfo.appDataVersion);
			edit.apply(); // commit();
		}
	}
    
    public static void loginError( Context cx ) {
		String msg = cx.getString(R.string.login_error_info);
		Toast.makeText(cx, msg, Toast.LENGTH_SHORT).show();
		
		Log.e("LOG", "LogIn: " + msg);
    }

	private static ValueEventListener versionListener = new ValueEventListener() {
		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			// Get Post object and use the values to update the UI
            // ToDo:
			Object version = dataSnapshot.getValue();
			Log.i("LOG", "Version: " + dataSnapshot.toString() +  version);
		}

		@Override
		public void onCancelled(DatabaseError databaseError) {
			// Getting Post failed, log a message
			Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
			// ...
		}
	};
}
