package com.maxk.notebook.post;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.maxk.notebook.blog.PostActivity;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PostListActivity extends Activity {

	private Context 			context;
	private ListView 			listView;
	private PostAdapter 		postAdapter;
	private ArrayList<Post> 	postList;
	private ProgressBar 		postProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_list);
		
        context = this;
        
        postProgressBar = (ProgressBar)findViewById(R.id.post_pb);
        listView = (ListView)findViewById(R.id.postList);
		postList = new ArrayList<Post>();
		
		postProgressBar.setVisibility(View.VISIBLE);
		
		initPostMenu();
		initPostList();
		updatePostList();
	}

	private void initPostList() {
		
		postAdapter = new PostAdapter(context, postList);

		listView.setAdapter(postAdapter);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScroll(AbsListView listView, int firstVis, int visCount, int totals) {
			}

			public void onScrollStateChanged(AbsListView listView, int paramInt) {
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long paramLong) {
				if (BuildConfig.DEBUG)
					Log.i("TAG", "setOnItemClickListener");
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post_list, menu);
		return true;
	}

	/*
	 * Creating posts and refreshing the list will be controlled from the ActionBar.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        /*
		case R.id.action_refresh: {
			updatePostList();
			break;
		}
		*/
		// case R.id.action_new:
        case R.id.action_add:
        {
			newPost();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void updatePostList() {
		
		if( !MaxkInfo.logOn ) {
			MaxkUtils.loginError( context );
			postProgressBar.setVisibility(View.GONE);
			return;
		}
			
		// Create query for objects of type "Post"
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

		// Restrict to cases where the author is the current user.
		// Note that you should pass in a ParseUser and not the String reperesentation of that user
		if( false )
			query.whereEqualTo("author", ParseUser.getCurrentUser());
		
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> postLists, ParseException e) {
				if (e == null) {
					// If there are results, update the list of posts and notify the adapter
					postList.clear();
					for (ParseObject post : postLists) {
						Post p = new Post( post );
						postList.add(p);
					}
					postAdapter.notifyDataSetChanged();
					postProgressBar.setVisibility(View.GONE);
				} else {
					Log.d("Post retrieval", "Error: " + e.getMessage());
				}
			}
		});
	}

	private void newPost() {
		if ( true ) {
			Intent i = new Intent(this, PostActivity.class);
			startActivityForResult(i, 0);
		} else {
			Intent i = new Intent(this, PostWriteActivity.class);
			startActivityForResult(i, 0);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			// If a new post has been added, update the list of posts
			updatePostList();
		}
	}
	
	private void initPostMenu() {
		Button writePost 	= (Button)findViewById(R.id.writePost); 
		Button refreshPost 	= (Button)findViewById(R.id.refreshPost); 
        
		writePost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( MaxkInfo.isAdmin )
					newPost();
				else
					Toast.makeText(context, context.getString(R.string.post_error_admin), Toast.LENGTH_LONG).show();	
			}
        });
        
        refreshPost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updatePostList();
			}
        });
	}
}
