package com.maxk.notebook.post;

import java.util.Date;

import com.maxk.notebook.maxkgi.BuildConfig;
import com.parse.ParseObject;

import android.util.Log;

public class Post {
	
	public Post(ParseObject post) {
		setPost( post );
	}
	
	public void setPost( ParseObject post ) {
		try{		
			objId 		= post.getObjectId();
			createAt 	= post.getCreatedAt();
			postText 	= post.getString("textContent");
			postWriter 	= "Admin";
			
			if( BuildConfig.DEBUG ) {
				Log.d("Post", "setPost --> objId: " + objId + ", postText: "  + postText );
			}
		} catch( Exception e ) {
			e.printStackTrace();
			Log.e("Post", "setPost --> objId: " + objId + ", postText: "  + postText );
		}
	}
	
	String objId;
	String postText;
	String postWriter;
	Date   createAt;
	
	public String getObjId(){
		return objId;
	}
	
	public Date getCreateAt() {
		return createAt;
	}
	public String getPostText() {
		return postText;
	}
	
	public String getPostWriter() {
		return postWriter;
	}
}