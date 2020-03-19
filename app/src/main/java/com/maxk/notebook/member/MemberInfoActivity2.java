package com.maxk.notebook.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.maxk.notebook.misc.MapViewer;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;


public class MemberInfoActivity2 extends FragmentActivity {
	
	private static String TAG = MemberInfoActivity.class.toString();
	
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.member_info);

        context = this;

        initTopMenu();
        initMemberInfo();

        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu( (Activity)context );
    }
    
    private void initTopMenu() {
    	
        ImageButton btnKatalk = (ImageButton)findViewById(R.id.btnKatalk);
        ImageButton btnSMS = (ImageButton)findViewById(R.id.btnSMS);

        btnKatalk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					goKatalk((Activity) context );
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					/*
	                new AlertDialog.Builder(MemberInfoActivity.this)
                    .setTitle(R.string.kakao_app)
                    .setMessage(R.string.kakao_install_msg)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	Intent intent = new Intent(Intent.ACTION_VIEW);
                        	intent.setData(Uri.parse("market://details?id=com.kakao.talk"));
                        	startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
                    */
				}
			}
        });
        
        btnSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMobilePhone();
				sendSMS( (Activity) context, phoneNo );
			}
        });
    }
    
    private View memberInfoLayout;
    private ImageView memberImage;
    private static TextView memberOfficeTelNo, memberHomeTelNo, memberMobilePhoneNo;
    private static TextView memberOfficeAddr, memberHomeAddr;
    private void initMemberInfo() {
    	
    	memberInfoLayout  = (View)findViewById(R.id.memberInfo);
    	
		String classNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getClassNo();
		memberImage = (ImageView)findViewById(R.id.memberImage);
		if (MaxkInfo.ASSETS_IMAGE) {
			String assetPath = "picture/" + classNo + ".png";
			MaxkUtils.loadImageFromAsset(context, memberImage, assetPath);
		} else {
			String imageUrl;
            imageUrl = MaxkInfo.PUB_ON_WEB_MAKX_UNIV1_IMAGE +       classNo + ".png";
			MaxkInfo.volly.loadImage(imageUrl, memberImage);
			// MaxkInfo.volly.loadNetworkImage(imageUrl, memberImage);
		}
		memberImage.setOnClickListener(new imageOnClickListener());
		
    	String name = MemberListActivity.memberList.get(MaxkInfo.memberPos).getName();
    	String pos  = MemberListActivity.memberList.get(MaxkInfo.memberPos).getPosition();
    	if( pos != null )
    		name = name + "(" + pos + ")";
    	TextView memberName = (TextView)findViewById(R.id.memberName);
    	memberName.setText(name);

		/*
    	String majorName = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMajor();
    	TextView memberMajorName = (TextView)findViewById(R.id.memberMajorName);
    	memberMajorName.setText(majorName);
    	
    	String termName = MemberListActivity.memberList.get(MaxkInfo.memberPos).getTerm();
    	TextView memberTermName = (TextView)findViewById(R.id.memberTermName);
    	termName = symbolToTerm( termName );
    	if( termName != null  )
    		memberTermName.setText(termName);
    	else {
    		LinearLayout memberTermLayout = (LinearLayout)findViewById(R.id.memberTermLayout);
    		memberTermLayout.setVisibility(View.INVISIBLE);
    	}
    	
    	String companyName = MemberListActivity.memberList.get(MaxkInfo.memberPos).getCompany();
    	TextView memberCompanyName = (TextView)findViewById(R.id.memberCompanyName);
    	memberCompanyName.setText(companyName);
    	*/
    	
    	String officeAddr 	= MemberListActivity.memberList.get(MaxkInfo.memberPos).getOfficeAddr();
    	memberOfficeAddr 	= (TextView)findViewById(R.id.memberOfficeAddr);
    	memberOfficeAddr.setText(officeAddr);
    	
    	String homeAddr 	= MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomeAddr();
    	memberHomeAddr 		= (TextView)findViewById(R.id.memberHomeAddr);
    	memberHomeAddr.setText(homeAddr);
    	memberOfficeAddr.setOnClickListener(new addressOnClickListener());
    	memberHomeAddr.setOnClickListener(new addressOnClickListener());
    	
    	
    	memberOfficeTelNo 	= (TextView)findViewById(R.id.memberOfficeTelNo);    	
    	memberHomeTelNo 	= (TextView)findViewById(R.id.memberHomeTelNo);    	
    	memberMobilePhoneNo = (TextView)findViewById(R.id.memberMobilePhoneNo);
    	
    	memberOfficeTelNo.setOnClickListener(new phoneOnClickListener());
    	memberHomeTelNo.setOnClickListener(new phoneOnClickListener());
    	memberMobilePhoneNo.setOnClickListener(new phoneOnClickListener());
    	
    	String officeNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getOfficePhone();
    	String homeNo 	= MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomePhone();
    	String mobileNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMobilePhone();
    	
    	if( officeNo == null ) 	officeNo = "";
    	if( homeNo == null ) 	homeNo = "";
    	if( mobileNo == null ) 	mobileNo = "";
    	memberOfficeTelNo.setText(officeNo);
    	memberHomeTelNo.setText(homeNo);
    	memberMobilePhoneNo.setText(mobileNo);

    	String faxNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getFax();
    	TextView memberFaxNo = (TextView)findViewById(R.id.memberFaxNo);
    	memberFaxNo.setText(faxNo);   	
    	
    	String homePageUrl = MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomePage();
    	TextView memberHomePageUrl = (TextView)findViewById(R.id.memberHomePageUrl);
    	memberHomePageUrl.setText(homePageUrl);
    	memberHomePageUrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomePage();
				linkUrl( context, url);
			}
        });
    	
    	String mailAddr = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMailAddr();
    	TextView memberEmailAddr = (TextView)findViewById(R.id.memberEmailAddr);
    	memberEmailAddr.setText(mailAddr);
    	memberEmailAddr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mail = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMailAddr();
				sendMail( context, mail);
			}
        });
    } 
    
    private class imageOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			String classNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getClassNo();
			if( BuildConfig.DEBUG )
				initImagePopup( memberInfoLayout );
		}
	}
    
    private PopupWindow 	popupWindow;
    private ImageView 		popupImage;
    private void initImagePopup( View parentView ) {
    	
    	LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.member_image, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);	// NOTE true: to enable setOnItemClickListener 

		Button 	btnSave 	= (Button) 	popupView.findViewById(R.id.save); 
		Button 	btnCancel 	= (Button) 	popupView.findViewById(R.id.cancel); 
		popupImage  		= (ImageView)popupView.findViewById(R.id.memberImage); 
		Button 	btnCamera 	= (Button) 	popupView.findViewById(R.id.camera); 
		Button 	btnGallery 	= (Button) 	popupView.findViewById(R.id.gallery);
		btnCamera.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( BuildConfig.DEBUG )
					Log.i("TAG", "initImagePopup: btnCamera: ");

				takePhotoIntent( false );
			}
		});
		
		btnGallery.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( BuildConfig.DEBUG )
					Log.i("TAG", "initImagePopup: btnGallery: ");
				
				takeAlbumIntent( true );
			}
		});
		
		btnSave.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( BuildConfig.DEBUG )
					Log.i("TAG", "initImagePopup: btnSave: ");
				popupWindow.dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( BuildConfig.DEBUG )
					Log.i("TAG", "initImagePopup: btnCancel: ");
				popupWindow.dismiss();
			}
		});
		
		Drawable d = memberImage.getDrawable();
		popupImage.setImageDrawable(d);
		
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ShapeDrawable());
		popupWindow.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss() {
				if( BuildConfig.DEBUG )
					Log.w(TAG, "setOnDismissListener");
				popupWindow = null;
			}					
		});
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 100); 
    }
    
    // For Crop Picker
    private static final int PROFILE_IMAGE_ASPECT_X = 3;
    private static final int PROFILE_IMAGE_ASPECT_Y = 2;
    private static final int PROFILE_IMAGE_OUTPUT_X = 600;
    private static final int PROFILE_IMAGE_OUTPUT_Y = 400;
    
    private static final int PICK_FROM_CAMERA 			= 501;
    private static final int PICK_FROM_ALBUM 			= 502;
    private static final int CROP_FROM_CAMERA 			= 503;

    private void takePhotoIntent( boolean thumbnail ) { 
    	if( BuildConfig.DEBUG )
    		Log.i(TAG, "takePhotoIntent()");
    	
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        	if( !thumbnail ) {
        		// publicFolder: false
        		imageCaptureUri = createImageFile( false ); 
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri );
        	}
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
        } 
    } 
    
    private void takeAlbumIntent( boolean crop ) { 
    	if( BuildConfig.DEBUG )
    		Log.i(TAG, "takeAlbumIntent()");
    	
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        
        if( false )
        	setCropIntent( intent );
        else {
        	imageCaptureUri = createImageFile( false );
        	
    		intent.putExtra( "crop", "true" );
    		intent.putExtra( "aspectX", PROFILE_IMAGE_ASPECT_X );
    		intent.putExtra( "aspectY", PROFILE_IMAGE_ASPECT_Y );
    		intent.putExtra( "outputX", PROFILE_IMAGE_OUTPUT_X );
    		intent.putExtra( "outputY", PROFILE_IMAGE_OUTPUT_Y);
    		intent.putExtra( "scale", true );
    		intent.putExtra( MediaStore.EXTRA_OUTPUT, imageCaptureUri );
    		intent.putExtra( "outputFormat", Bitmap.CompressFormat.JPEG.toString() );
        }
        startActivityForResult(intent, PICK_FROM_ALBUM); 
    } 
    
    private void setCropIntent( Intent intent ) {

    	imageCaptureUri = createImageFile( false );
    	
		intent.putExtra( "crop", "true" );
		intent.putExtra( "aspectX", PROFILE_IMAGE_ASPECT_X );
		intent.putExtra( "aspectY", PROFILE_IMAGE_ASPECT_Y );
		intent.putExtra( "outputX", PROFILE_IMAGE_OUTPUT_X );
		intent.putExtra( "outputY", PROFILE_IMAGE_OUTPUT_Y);
		intent.putExtra( "scale", true );
		intent.putExtra( MediaStore.EXTRA_OUTPUT, imageCaptureUri );
		intent.putExtra( "outputFormat", Bitmap.CompressFormat.JPEG.toString() );
    }
    
    // http://stackoverflow.com/questions/12758425/how-to-set-the-output-image-use-com-android-camera-action-crop
    private Uri 	imageCaptureUri;
    private Bitmap 	memberBitmapImage;
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if( resultCode != RESULT_OK)
    		return;

    	boolean scaledImage = true;
    	
    	 if (requestCode == PICK_FROM_CAMERA ) {
	        	// Thumbnail 
	        	if( data != null ) {
		            Bundle extras = data.getExtras();
		            memberBitmapImage = (Bitmap) extras.get("data");
		        	popupImage.setImageBitmap(memberBitmapImage);
	        	} else {
	        		if( scaledImage ) {
	        			setPic( popupImage, currentPhotoPath );
	        		} else {
		        		Uri uri = Uri.parse("file:" + currentPhotoPath);
		        		popupImage.setImageURI(uri);
	        		}
	        	}
        } else if( requestCode == PICK_FROM_ALBUM ) {
        	if( data != null ) {
	            Uri imageUri = (Uri) data.getData();

	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	            Cursor c = getContentResolver().query(imageUri, filePathColumn, null, null, null);
				if( c == null )
					return;
	            c.moveToFirst();
	            int columnIndex = c.getColumnIndex(filePathColumn[0]);
	            String picturePath = c.getString(columnIndex);
	            currentPhotoPath = picturePath.toString();
	            
	            if( scaledImage )
	            	setPic( popupImage, currentPhotoPath );
	            else 
	            	popupImage.setImageURI(imageUri);
				c.close();
        	}
        }
    	 
    	if( true ) return;
    	 
    	switch( requestCode ) {
    	case PICK_FROM_ALBUM:
    		Log.d(TAG, "PICK_FROM_ALBUM");
            
            imageCaptureUri = data.getData();
            File albumFile = getImageFile(imageCaptureUri);
             
            Log.w(TAG, "비트맵 Image path = "+ imageCaptureUri);
             
            if( scaledImage )
            	setPic( popupImage, albumFile.getAbsolutePath() );
            else {
	            Bitmap bmPhoto = BitmapFactory.decodeFile(albumFile.getAbsolutePath());
	            popupImage.setImageBitmap(bmPhoto);
            }

            break;
    	case PICK_FROM_CAMERA:
    		Log.d(TAG, "PICK_FROM_CAMERA"); 
    		 
            // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
            // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
            imageCaptureUri = data.getData();
            File cameraFile = getImageFile(imageCaptureUri);
             
            imageCaptureUri = createImageFile( false );
            File cropFile = new File(imageCaptureUri.getPath()); 
 
            // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
            copyFile(cameraFile , cropFile);
            
    		Intent intent;
    		if( false ){
            // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
            // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
 
            intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageCaptureUri, "image/*"); 
             
            // Crop한 이미지를 저장할 Path
            intent.putExtra("output", imageCaptureUri);
             
            // Return Data를 사용하면 번들 용량 제한으로 크기가 큰 이미지는
            // 넘겨 줄 수 없다.
//          intent.putExtra("return-data", true); 
    		} else {
    			intent = new Intent(Intent.ACTION_PICK);
    	        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
    			setCropIntent(intent);
    		}
            startActivityForResult(intent, CROP_FROM_CAMERA);
 
            break;
        case CROP_FROM_CAMERA:

            Log.w(TAG, "CROP_FROM_CAMERA");
             
            // Crop 된 이미지를 넘겨 받습니다.
            Log.w(TAG, "mImageCaptureUri = " + imageCaptureUri);
 
            String photoPath = imageCaptureUri.getPath();
             
            Log.w(TAG, "비트맵 Image path = "+photoPath);
             
            if( scaledImage )
            	setPic( popupImage, photoPath );
            else {
	            Bitmap photo = BitmapFactory.decodeFile(photoPath);
	            popupImage.setImageBitmap(photo);
            }
 
            break;
    	}
    }

    private void setPic( ImageView imageView, String filePath ) { 
        // Get the dimensions of the View 
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
     
        // Get the dimensions of the bitmap 
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
     
        // Determine how much to scale down the image 
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
     
        // Decode the image file into a Bitmap sized to fill the View 
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile( filePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    } 
    
	private class phoneOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			String phoneNo = "";
			if( v == memberOfficeTelNo )
				phoneNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getOfficePhone();
			else if( v == memberHomeTelNo )
				phoneNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomePhone();
			else if( v == memberMobilePhoneNo )
				phoneNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMobilePhone();
			
			phoneCall( (Activity) context, phoneNo);
		}
	}
	
	private class addressOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			String address = "";
			if( v == memberOfficeAddr )
				address = MemberListActivity.memberList.get(MaxkInfo.memberPos).getOfficeAddr();
			else if( v == memberHomeAddr )
				address = MemberListActivity.memberList.get(MaxkInfo.memberPos).getHomeAddr();

			linkMap( (Activity) context, address);
		}
	}
    
	public void goKatalk( Context cx ) throws NameNotFoundException {
		
		KakaoLink kakaoLink;
		KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
		
		String msg;
		if( BuildConfig.DEBUG )
			msg = "카카오 시험 메시지";
		else
			msg = "";
		// Recommended: Use application context for parameter.
		 try {
			kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
	        kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
	        kakaoTalkLinkMessageBuilder.addText(msg);
	 		kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
		} catch (KakaoParameterException e) {
			e.printStackTrace();
		}
	}
	
	private void alert(String message) {
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.app_name)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null)
			.create().show();
	}
    
    private void sendSMS( Activity a , String num) {
		String  phoneNo = MemberListActivity.memberList.get(MaxkInfo.memberPos).getMobilePhone();
		if( BuildConfig.DEBUG )
			Log.i( "TAG", "phoneNo: " + phoneNo);
		Uri uri = Uri.parse("smsto:" + phoneNo);   
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
		it.putExtra("sms_body", "");   
		startActivity(it);  
    }
    
    private void phoneCall( Activity a , String num) {
    	Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
    	startActivity(intent);
    }
    
    private void linkUrl( Context cx , String url) {
    	if( url != null && (!url.startsWith("http://") && !url.startsWith("https://")))
    		url = "http://" + url;
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(url));
    	startActivity(intent);
    }

	private void linkMap(Activity a, String address ) {
		if( address == null || address.equals(""))
			return;
		
		if( !MaxkInfo.MAP_LINK ) {
			Toast.makeText(a, "지도 서비스는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		MapViewer.queryAddr = address;
		
		Intent intent = new Intent();	// Intent.FLAG_ACTIVITY_CLEAR_TOP
		intent.setClass(this, MapViewer.class);
		startActivity(intent);
		
		return;
	}
    
    /*
    private void sendMail( Context context , String num) {
    	Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/html");
    	intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
    	intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
    	intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
    	startActivity(Intent.createChooser(intent, "Send Email"));
    }
    */
    
    private void sendMail( Context context , String mail) {
		
		Intent i=new Intent(Intent.ACTION_SEND); 
		
		i.addCategory(Intent.CATEGORY_DEFAULT); 
		i.setType("message/rfc822");
		
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailTitle)); 
		i.putExtra(Intent.EXTRA_TEXT,    getString(R.string.mailText));
		
		i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail}); 
		// i.putExtra(Intent.EXTRA_CC, new String[]{"webmaster1@website.com", "webmaster2@website.com"}); 
		// i.putExtra(Intent.EXTRA_BCC, new String[]{"webmaster@website.com"}); 
		
		startActivity(Intent.createChooser(i, getString(R.string.mailSendMsg)));		
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
    
    private String symbolToTerm( String symbol ) {
    	
    	String term = null;
    	if( symbol.equals("s3c1"))
    		term = "1학기";
    	else if( symbol.equals("s3c2"))
    		term = "2학기";
    	else if( symbol.equals("s3c3"))
    		term = "3학기";
    	else if( symbol.equals("s3c4"))
    		term = "4학기";
    	else if( symbol.equals("s3c5"))
    		term = "5학기";
    	else if( symbol.equals("s3c0"))
    		term = null;
    	
    	return term;
    }
    
    String currentPhotoPath;
    private Uri createImageFile( boolean publicFolder ) {
    	File storageDir;
        // Create an image file name 
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        
        if( publicFolder )
        	storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        else
        	storageDir = context.getExternalFilesDir( Environment.DIRECTORY_PICTURES );
        File image = null;
		try {
			image = File.createTempFile(
			    imageFileName,  /* prefix */
			    ".jpg",         /* suffix */ 
			    storageDir      /* directory */
			);
		     
	        // Save a file: path for use with ACTION_VIEW intents 
			currentPhotoPath = "" + image.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return Uri.fromFile(image);
    } 
    
    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     * @param uri
     * @return
     */
    private File getImageFile(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
 
        Cursor mCursor = getContentResolver().query(uri, projection, null, null, 
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();
 
        String path = mCursor.getString(column_index);
 
        if (mCursor !=null ) {
            mCursor.close();
            mCursor = null;
        }
 
        return new File(path);
    }
    
    /**
     * 파일 복사
     * @param srcFile : 복사할 File
     * @param destFile : 복사될 File
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }
    
    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}