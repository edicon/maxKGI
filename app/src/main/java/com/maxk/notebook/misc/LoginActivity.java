package com.maxk.notebook.misc;

import com.maxk.notebook.maxkgi.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;


public class LoginActivity extends FragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // setContentView(R.layout.nb_univ_list);
        
        String msg = getString(R.string.login_error_info);
        Intent i = getIntent();
        Bundle ie = i.getExtras();
        
        if( ie != null ) {
			String id = ie.getString("login" );
			id = id.replaceAll("-", "");
	        MaxkInfo.logOn = MaxkUtils.queryLogin( this, id );
	        
			if( MaxkInfo.logOn ) {
				msg = getString(R.string.login_passed);
			} else {
				msg = getString(R.string.login_error_info);
			}
        }
        
		Toast.makeText( this, msg, Toast.LENGTH_SHORT).show();
		Log.w("LOG", "LogIn: " + msg);
        finish();
    }
}