package com.maxk.notebook.maxkgi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edicon.libs.ads.AdMob;
import com.maxk.notebook.misc.MaxkInfo;

public class MaxkIntroActivity extends FragmentActivity {

	private boolean started = false;
	private Context thisContext;
	
    private class adHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AdMob.AD_FAILED: 
			case AdMob.AD_OPENED: {
				if( !started )
					startMaxk();
				started = true;
				break;
			}
			default:
				break;
			}
		}
    }
    
    private SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maxk_intro);
        
        thisContext = this;
        
        started = false;
        Handler adHandler 	= new adHandler();
        
        /* Move to MaxkMainActivity for ProgressDialog of Member Info. Loading
        sp 						= PreferenceManager.getDefaultSharedPreferences(this);
        MaxkInfo.pushOn 		= sp.getBoolean("PUSH_ON", true);
        MaxkInfo.dataLoading 	= sp.getBoolean("APP_DATA_LOADING", false);
        MaxkInfo.appDataVersion = sp.getLong("APP_DATA_VER", 0L);
        MaxkInfo.phoneNo		= MaxkUtils.getPhoneNo(this);

		// Move to MaxkMainActivity PushUtil.setPushSubscribe(this);

        if( MaxkInfo.appDataVersion == 0L)
        	MaxkUtils.getMemberFromSheet( this );
        else
        	MaxkUtils.updateMember( this );
		
		*/

		// NullTest.getNull( null );
		startIntroAni();
		// com.edicon.lib.utils.Utils.addShortcut( this, getString(R.string.app_name_ted), R.drawable.ic_launcher);
		if( MaxkInfo.USE_AD ) {
	        AdMob.loadInterstitial( this, null, adHandler);
	        adHandler.sendEmptyMessageDelayed(AdMob.AD_FAILED, 7000);
        } else
        	adHandler.sendEmptyMessageDelayed(AdMob.AD_FAILED, 2000);
    }
    
    private void startIntroAni(){
        ImageView image = (ImageView)findViewById(R.id.intro_img); 
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.set);
        image.startAnimation(anim);
    }
    
	private void startMaxk() {
        
		Intent intent;
		intent = new Intent(MaxkIntroActivity.this, MaxkMainActivity.class);
    	// overridePendingTransition(R.anim.fade, R.anim.hold);
		startActivity(intent);

		finish();
	}
}