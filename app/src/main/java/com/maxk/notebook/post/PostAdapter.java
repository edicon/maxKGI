package com.maxk.notebook.post;

// ListView Lazy Loading Sample
// http://stackoverflow.com/questions/541966/android-how-do-i-do-a-lazy-load-of-images-in-listview
// https://github.com/thest1/LazyList
// https://github.com/nostra13/Android-Universal-Image-Loader
// http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html
import java.util.ArrayList;
import java.util.Date;

import com.maxk.notebook.maxkgi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter implements ListAdapter
{
	private LayoutInflater mInflater;
	private ArrayList<Post> postList;
	
	public PostAdapter(Context cx, ArrayList<Post> posts ) {
		mInflater  = LayoutInflater.from(cx);
		postList 	= posts;
	}

	@Override
	public int getCount() {
		if( postList == null )
			return 0;
		return postList.size();
	}

	@Override
	public Object getItem(int position) {
		if( postList!= null )
			return(postList.get(position));
		return null;
	}
	
	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		View v 			= view;
		ViewHolder vh 	= null;
		
		if (v == null) {
			v = mInflater.inflate(R.layout.post_list_item, null);
			vh = new ViewHolder();
			LinearLayout titleLayout 			= ((LinearLayout)v.findViewById(R.id.postTitleLayout));
			final LinearLayout contentLayout 	= ((LinearLayout)v.findViewById(R.id.postContentLayout));
			titleLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( contentLayout.getVisibility() == View.GONE )
						contentLayout.setVisibility(View.VISIBLE);
					else
						contentLayout.setVisibility(View.GONE);
				}	
			});
			vh.image 	= ((ImageView)v.findViewById(R.id.image));
			vh.title 	= ((TextView) v.findViewById(R.id.title));
			vh.time 	= ((TextView) v.findViewById(R.id.time));
			vh.content	= ((TextView) v.findViewById(R.id.content));
			v.setTag(vh);
		} else
			vh = (ViewHolder)v.getTag();
		
		try {
			Date date = postList.get(pos).getCreateAt();
			String time = date.toLocaleString();
			time = time.replace("오전", "\n오전");
			time = time.replace("오후", "\n오후");
			vh.time.setVisibility(View.VISIBLE);
			vh.time.setText(time);
			
			String txt = postList.get(pos).getPostText();
			vh.title.setText("제목: " + txt);
			vh.content.setText("" + txt + "\n");
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return v;
	}

	public static class ViewHolder {
		public ImageView 	image;
		public TextView 	title;
		public TextView 	time;
		public TextView 	content;
	}
}