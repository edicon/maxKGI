package com.maxk.notebook.post;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edicon.lib.parse.PushUtil;
import com.maxk.notebook.maxkgi.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostWriteActivity extends Activity {
	
	private Button 		saveButton;
	private Button 		cancelButton;
	private TextView 	postContent;
	private ProgressBar postProgressBar;
	
	private Context		thisContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_write);

		thisContext = this;
		
		postProgressBar = (ProgressBar)findViewById(R.id.post_pb);
		postProgressBar.setVisibility(View.INVISIBLE);
		postContent = ((EditText) findViewById(R.id.post_content));

		saveButton = ((Button) findViewById(R.id.writePost));
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// When the user clicks "Save," upload the post to Parse
				// Create the Post object
				ParseObject post = new ParseObject("Post");
				String postText = postContent.getText().toString();
				if( postText == null || postText.equals("")) {
					Toast.makeText(
							getApplicationContext(), 
							getApplicationContext().getString(R.string.post_no_text), 
							Toast.LENGTH_LONG).show();
					return;
				}
				postProgressBar.setVisibility(View.VISIBLE);
				post.put("textContent", postText);

				// Create an author relationship with the current user
				if( false )
					post.put("author", ParseUser.getCurrentUser());

				// Save the post and return
				post.saveInBackground(new SaveCallback () {
					@Override
					public void done(ParseException e) {
						postProgressBar.setVisibility(View.INVISIBLE);
						if (e == null) {
							String postText = postContent.getText().toString();
							if( postText != null && postText.length() > 20)
								postText = postText.substring(0, 20);
							
							sendPush( postText );
							
							setResult(RESULT_OK);
							finish();
						} else {
							Toast.makeText(
									getApplicationContext(), 
									getApplicationContext().getString(R.string.post_error_write) + "( " + e.getMessage() + " )", 
									Toast.LENGTH_LONG).show();
						}
					}					
				});
			}
		});

		cancelButton = ((Button) findViewById(R.id.cancelPost));
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postProgressBar.setVisibility(View.INVISIBLE);
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	private void sendPush( String msg ) {
		LinkedList<String> pushChannels = PushUtil.getPkgChannelList( thisContext );
		ParsePush push = new ParsePush();
		PushUtil.pushChannel( push, pushChannels, "공지: " + msg);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// When users indicate they are no longer Giants fans, we unsubscribe them.
		String channel = PushUtil.getChannelFromPackage(thisContext);
		// PushUtil.unsubScribe( thisContext, channel);
	}
}
