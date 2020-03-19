package com.maxk.notebook.member;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxk.notebook.db.MemberDB;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;


public class MemberListActivity extends FragmentActivity {
	
	private final static String TAG = MemberListActivity.class.getSimpleName();
	
	private Context context;
	private ListView listView;
	public static ArrayList<Member> memberList;
	private MemberAdapter 			memberAdapter;
	private boolean CONTACT_INSERT_OR_EDIT = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.member_list);
        
        context = this;
        // MaxkInfo.volly = new Volly(context);
        
        int searchType = MaxkInfo.SEARCH_ALL;
        String searchText = null;
        Intent tedIntent  = getIntent();
        TextView titleText = (TextView)findViewById(R.id.topTitle);
		if (((Intent)tedIntent).getExtras() != null) {
			searchType 	= tedIntent.getIntExtra(MaxkInfo.SEARCH_TYPE, MaxkInfo.SEARCH_ALL);
			searchText 	= tedIntent.getStringExtra(MaxkInfo.SEARCH_TEXT);
			
			String title = tedIntent.getStringExtra(MaxkInfo.TITLE_TEXT);
			if( title != null )
				titleText.setText(title);
		}
        
        listView= (ListView)findViewById(R.id.memberList);
		// initMemberDb();
		memberList = new ArrayList<Member>();
		makeMemberList( searchType, searchText );
        memberAdapter = new MemberAdapter( context, memberList);

		listView.setAdapter(memberAdapter);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScroll(AbsListView listView, int firstVis, int visCount, int totals) {
			}
			public void onScrollStateChanged(AbsListView listView, int paramInt) {
			}
		});
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long paramLong) {
				if( BuildConfig.DEBUG )
					Log.i( TAG, "setOnItemClickListener");
				
				MaxkInfo.memberPos = position;
				MaxkUtils.goMemberInfoActivity(context);
			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long paramLong) {
				
				Intent intent;
				
				if( CONTACT_INSERT_OR_EDIT ) {
					intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
					intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
				} else {
					intent = new Intent(Intents.Insert.ACTION);
					intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
				}
				
				String name = memberList.get(position).getName();
				intent.putExtra(Intents.Insert.NAME, name);
				
				String company = memberList.get(position).getCompany();
				intent.putExtra(Intents.Insert.COMPANY, company);
				
				String mail = memberList.get(position).getMailAddr();
				intent.putExtra(Intents.Insert.EMAIL, mail);
				intent.putExtra(Intents.Insert.EMAIL_TYPE, CommonDataKinds.Email.TYPE_WORK);
				
				String phone = memberList.get(position).getMobilePhone();
				intent.putExtra(Intents.Insert.PHONE, phone);
				intent.putExtra(Intents.Insert.PHONE_TYPE, Phone.TYPE_MOBILE);
				
				String postal = memberList.get(position).getOfficeAddr();
				intent.putExtra(Intents.Insert.POSTAL, postal);
				intent.putExtra(Intents.Insert.POSTAL_TYPE, CommonDataKinds.StructuredPostal.TYPE_WORK);

				// Store Photo
				// http://stackoverflow.com/questions/15026292/insert-contact-contactscontract-via-intent-with-image-photo
				// Intent intent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI); 
				Bitmap bitmap;
				if( BuildConfig.DEBUG && false )
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_max);
				else { 
					ImageView image = (ImageView)view.findViewById(R.id.image);
					bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
				}

				ContentValues row = new ContentValues();
				ArrayList<ContentValues> data = new ArrayList<ContentValues>();
				
				row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE); 
				row.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bitmapToByteArray(bitmap));
				data.add(row); 
				
				intent.putParcelableArrayListExtra(Intents.Insert.DATA, data); 
				startActivity(intent);
				
				return true;
			}
		});
        
        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu( (Activity)context );
    }

	private void makeMemberList( int type, String text ){

		
		if( false && BuildConfig.DEBUG ) {
			for( int i = 0; i < 10; i++ ) {
				Member member = new Member();
				memberList.add( member );
				return;
			}
		}
		
		if( !MaxkInfo.logOn ) {
			MaxkUtils.loginError( context );
			return;
		}
		
		Cursor c;
		memberList.clear();
		MemberDB db = new MemberDB( this );

		switch( type ) {
		default:
		case MaxkInfo.SEARCH_ALL:
			c = db.getAllMember();
			break;
		case MaxkInfo.SEARCH_GROUP:
			c = db.searchMember( text );
			break;
		case MaxkInfo.SEARCH_MEMBER:
			c = db.searchMember( text );
			break;
        case MaxkInfo.SEARCH_WORD:
            c = db.searchWord( text );
            break;
		case MaxkInfo.SEARCH_ADMIN:
			c = db.searchAdmin( text );
			break;
		}

		int count = c.getCount();
		if( c == null || count == 0 )
			return;
		
		for( int i = 0; i < count; i++ ) {
			c.moveToPosition(i);
			Member m = new Member( c );
			memberList.add( m );
		}

		if( memberAdapter != null )
			memberAdapter.notifyDataSetChanged();
	}
	
	private void initMemberDb() {
		MemberDB memberDB= new MemberDB(getBaseContext());
	}
	
    private void addListHeader( ListView l ) {
		View v 	   		= LayoutInflater.from(this).inflate(R.layout.list_header, null);
		ProgressBar pb 	= ((ProgressBar)v.findViewById(R.id.progress));
		TextView headTxt= ((TextView)v.findViewById(R.id.footer_text));
		headTxt.setText("Member List ");
		l .addHeaderView(v, null, false);
    }
    
    private byte[] bitmapToByteArray( Bitmap b ) {

    	byte[] array;
    	
    	boolean bmError = true;
    	if( bmError ) {
    		// http://stackoverflow.com/questions/13758560/android-bitmap-to-byte-array-and-back-skimagedecoderfactory-returned-null
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.PNG , 100, stream);
			array = stream.toByteArray();
    	} else {
    		// http://stackoverflow.com/questions/10191871/converting-bitmap-to-bytearray-android/10192092#10192092
	    	int bytes = b.getByteCount(); 
	    	ByteBuffer buffer = ByteBuffer.allocate(bytes);
	    	b.copyPixelsToBuffer(buffer); 					// Move the byte data to the buffer
	    	array = buffer.array(); 						// Get the underlying array containing the data.
    	}
    	return array;
    }
}