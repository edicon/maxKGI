package com.maxk.notebook.member;

// ListView Lazy Loading Sample
// http://stackoverflow.com/questions/541966/android-how-do-i-do-a-lazy-load-of-images-in-listview
// https://github.com/thest1/LazyList
// https://github.com/nostra13/Android-Universal-Image-Loader
// http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.R;

public class MemberAdapter extends BaseAdapter implements ListAdapter {
	private Context context;
	private LayoutInflater 			mInflater;
	private ArrayList<Member> 		memberList;
	private Cursor 					memberCursor;
	private boolean listType		= true;

	public MemberAdapter(Context cx, ArrayList<Member> members ) {
		context = cx;
		mInflater  = LayoutInflater.from(cx);
		memberList = members;
		listType = true;
	}
	
	public MemberAdapter(Context cx, Cursor c ) {
		context = cx;
		mInflater  = LayoutInflater.from(cx);
		memberCursor = c;
		listType = false;
	}

	@Override
	public int getCount() {
		if( listType ) {
			if( memberList == null )
				return 0;
			return memberList.size();
		} else {
			return( memberCursor.getCount());
		}
	}

	@Override
	public Object getItem(int position) {
		if( listType ) {
			if( memberList!= null )
				return(memberList.get(position));
		} else {
			if( memberCursor!= null )
				return(memberCursor.moveToPosition(position));
		}
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
			v = mInflater.inflate(R.layout.member_list_item, null);
			vh = new ViewHolder();
			vh.image 	= ((ImageView)v.findViewById(R.id.image));
			// vh.name 	= ((TextView) v.findViewById(R.id.name));
			vh.nameMajor= ((TextView) v.findViewById(R.id.name_major));
			vh.phoneNo  = ((TextView) v.findViewById(R.id.phoneNo));
			vh.mailId  	= ((TextView) v.findViewById(R.id.mailId));
			v.setTag(vh);
		} else
			vh = (ViewHolder)v.getTag();
		
		try {
			String classNo = memberList.get(pos).getClassNo();
			if( MaxkInfo.ASSETS_IMAGE ) {
				String assetPath = "picture/" + classNo + ".png";
				MaxkUtils.loadImageFromAsset( context, vh.image, assetPath );
			} else {
				String imageUrl;
                imageUrl = MaxkInfo.PUB_ON_WEB_MAKX_UNIV1_IMAGE +       classNo + ".png";
				MaxkInfo.volly.loadImage(imageUrl, vh.image);
				// MaxkInfo.volly.loadNetworkImage(imageUrl, vh.image);
			}

			// String nameMajor = memberList.get(pos).getName()   + ", " + memberList.get(pos).getMajor();
			String nameMajor = memberList.get(pos).getName()   + "(" + memberList.get(pos).getOfficer() + ")";
			String memberPos = memberList.get(pos).getPosition();
			String compName  = memberList.get(pos).getCompany();
			if( memberPos != null )
				nameMajor = nameMajor + "(" + memberPos + ")";
			vh.nameMajor.setText(nameMajor);
			vh.phoneNo.setText(memberList.get(pos).getMobilePhone());
			vh.mailId.setText(compName);
			// vh.mailId.setText(memberList.get(pos).getMailAddr());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return v;
	}
	
	public static class ViewHolder {
		public ImageView 	image;
		public TextView 	name;
		public TextView 	nameMajor;
		public TextView 	phoneNo;
		public TextView 	mailId;
	}
}