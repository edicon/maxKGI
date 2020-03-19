package com.maxk.notebook.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.edicon.lib.parse.PushUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;
import com.maxk.notebook.maxkgi.ToDo;

// V2012.0831: add kpopLyricsLang for each lyrics language
//
// DOC URL: https://developers.google.com/google-apps/spreadsheets/?hl=ko-KR#authorizing_requests
//        : See Sheets API URLs, visibilities, and projections for PubOnWeb and PubToWeb
// REMARK: Not Support OAuth 2.0 By Today

public class LoadGoogleSheet {

	private final static String TAGS 			= "ggSS";	// Google SpreadSheet
	
	private final static int LIST_QUERY 		= 1;
	private final static int SELL_QUERY 		= 2;
	private static 		 int qType 				= LIST_QUERY;
	// 1:TimeStamp, 2:vid, 3:singer, 4:title, 5:subtitle, 6:script, 7:locale(like as en, jp, kr)
	private final static int VID_CELL_INDEX 	= 2;
	private final static int SCRIPT_CELL_INDEX 	= 6;
	private final static int LOCALE_CELL_INDEX 	= 7;
	
	// For Maxk Note
	private final static int INDEX_NAME 		= 1;
	private final static int INDEX_CLASS_NO 	= 2;
	private final static int INDEX_MAJOR 		= 3;
	private final static int INDEX_SEX 			= 4;
	private final static int INDEX_AGE 			= 5;
	private final static int INDEX_ZIP_CODE 	= 6;
	private final static int INDEX_HOME_ADDR 	= 7;
	private final static int INDEX_EMAIL 		= 8;
	private final static int INDEX_MOBILE_PHONE = 9;
	private final static int INDEX_COMPANY 		= 10;	
	private final static int INDEX_POSITION 	= 11;
	private final static int INDEX_OFFICER 		= 12;
	private final static int INDEX_PROFESSOR 	= 13;
	private final static int INDEX_HOME_PHONE 	= 14;
	private final static int INDEX_CIRCLE 		= 15;
	private final static int INDEX_OFFICE_PHONE = 16;
	
	private final static int INDEX_OFFICEK_ADDR = 17;
	private final static int INDEX_FAX 			= 18;
	private final static int INDEX_HOME_PAGE 	= 19;

	private static SpreadsheetService service;

	private static String sheetName;
	private static String workSheetName;
	private static String query = null;
	private static int 	  count = -1;
	public static void initQuery(String sName, String wName, String q, int c) {
		sheetName 	  = sName;
		workSheetName = wName;
		query = q;
		count = c;
	}

	private static Context context;
	private static MemberDB memberDB;
	public static void initMemberDB( Context cx ) {
		context = cx;
		memberDB = new MemberDB(cx);
	}
	
	private static String USERNAME, PASSWORD;
	public static void initAuth( String id, String pass ) {
		USERNAME = id;
		PASSWORD = pass;
	}
	
	private static boolean initAuthAndGoogleService( ) {
		
		/**
		 * service.setProtocolVersion(SpreadsheetService.Versions.V3);
		 * Google just stopped support OAuth1.0. OAuth2 needs to be used
		 *	- http://stackoverflow.com/questions/30483601/create-spreadsheet-using-google-spreadsheet-api-in-google-drive-in-java
		 * Google Spreadsheet getFeed error and comprehension
		 *  - http://stackoverflow.com/questions/16198518/google-spreadsheet-getfeed-error-and-comprehensiono
		 * if (spreadsheets.size() == 0)
		 *  - http://stackoverflow.com/questions/30592152/using-google-spreadsheet-api-with-oauth2
		 *  - condole.developer.google.com/project/maxdiary...
		 *   API 및 인증 --> 사용자 인증 정보 --> 서비스  계정을 만들고,
		 *   생성된 email(account-1@maxdiary-1125.iam.gserviceaccount.com)로 google spreadsheet(MaxUniv)를 open하고
		 *   파일 --> 공유 --> 다른 사용자와 공유에 상기 email로 공유(특정 사용자와 공유)
		**/

		try {

			// String SERVICE_ACCOUNT_EMAIL = "account-1@maxdiary-1125.iam.gserviceaccount.com";  // dev.edicon@gmail.com
			// String SERVICE_ACCOUNT_PKCS12_FILE_NAME = "MaxDiary-655ac142ccc6.p12";
			File SERVICE_ACCOUNT_PKCS12_FILE = getTempPkc12File( MaxkInfo.SERVICE_ACCOUNT_PKCS12_FILE_NAME );

			if( ToDo.OAUTH2_CREDENTAL ) {
				HttpTransport httpTransport = new NetHttpTransport();
			    JacksonFactory jsonFactory = new JacksonFactory();
			    String [] SCOPESArray= {
						"https://spreadsheets.google.com/feeds",
						// "https://www.googleapis.com/auth/drive", 		// For Write
						"https://docs.google.com/feeds"
						// "https://www.googleapis.com/auth/userinfo.email",
						// "https://www.googleapis.com/auth/userinfo.profile",우
				};
			    final List SCOPES = Arrays.asList(SCOPESArray);
			    GoogleCredential credential = new GoogleCredential.Builder()
				     .setTransport(httpTransport)
				     .setJsonFactory(jsonFactory)
				     .setServiceAccountId(MaxkInfo.SERVICE_ACCOUNT_EMAIL)
				     .setServiceAccountScopes(SCOPES)
				     .setServiceAccountPrivateKeyFromP12File(SERVICE_ACCOUNT_PKCS12_FILE)
				     .build();
				credential.refreshToken();

				service = new SpreadsheetService("MySpreadsheetIntegration-v3");
				service.setProtocolVersion(SpreadsheetService.Versions.V3);
				service.setOAuth2Credentials(credential);
			} else {
				service = new SpreadsheetService("MySpreadsheetIntegration-v3");
				service.setUserCredentials(USERNAME, PASSWORD);
			}
			
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// http://stackoverflow.com/questions/15497888/how-to-use-google-drive-sdk-in-android-project-with-hardcoded-credentials
	private static File getTempPkc12File( String pks12FileName ) {
		
		File tempPkc12File = null;
		try {
			InputStream pkc12Stream = context.getAssets().open(pks12FileName);
		    tempPkc12File = File.createTempFile("temp_pkc12_file", "p12");
		    OutputStream tempFileStream = new FileOutputStream(tempPkc12File);
	
		    int read = 0;
		    byte[] bytes = new byte[1024];
		    while ((read = pkc12Stream.read(bytes)) != -1) {
		        tempFileStream.write(bytes, 0, read);
		    }
		} catch( Exception e ) {
			e.printStackTrace();
		}
	    return tempPkc12File;
	}

	private static boolean cancelLoading = false;
	public static class ggSpreadSheedTask extends AsyncTask<String, String, Boolean> {

		ProgressDialog progDialog;
		
		@Override
		protected void onPreExecute() {

			progDialog = new ProgressDialog(context);
			progDialog.setIcon(R.drawable.push_icon);
			progDialog.setTitle(context.getString(R.string.member_info_title));
			progDialog.setMessage(context.getString(R.string.member_info_down_start));
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setOnCancelListener(new OnCancelListener () {
				@Override
				public void onCancel(DialogInterface dialog) {
					if( ProjectSheet.ssTask != null )
						ProjectSheet.ssTask.cancel( true );
				}
			});
			progDialog.show();
			
			cancelLoading = false;
		}

		@Override
		protected Boolean doInBackground(String... params) {

			// ALL for query = null, ALL for count = -1;
			// query = "?min-row=2&min-col=3&max-col=3"; // Fetch column 3(C), and every row after row 1
			// query = "?min-row=2&min-col=4&max-col=4"; // Fetch column 4(D), and every row after row 1
			
			if( !initAuthAndGoogleService())
				return false;
			
			initSpreadSheet();

			qType = LIST_QUERY;
			switch (qType) {
			case LIST_QUERY:
				query = "?reverse=false&orderby=TimeStamp"; 	// ?orderby=lastname
				// query = "?reverse=true&orderby=id"; 			// ?orderby=lastname
				boolean r = queryListFeed(worksheet, query, count);
				if( r == false )
					return r;
				break;
			case SELL_QUERY:
				queryCellBasedFeed(worksheet, query, count);
				break;
			}
			/*
			 * String cellPos; String cellValue; cellPos = "A1"; cellValue =
			 * "200"; updateSheet( worksheet, cellPos, cellValue ); cellPos =
			 * "B1"; cellValue = "=SUM(A1, 200)"; updateSheet( worksheet,
			 * cellPos, cellValue ); queryCellBasedFeed( worksheet, query, count
			 * );
			 */
			return true;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			return;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			
			if( result == false ) {
				dismissProgressDialog();
				if( BuildConfig.DEBUG )
					Log.w(TAGS, "ggSpreadSheedTask: onPostExecute: result: FAILED" + result);
				Toast.makeText(context, context.getString(R.string.member_error_info), Toast.LENGTH_LONG).show();
				return;
			}
			
			if( BuildConfig.DEBUG )
				Log.w(TAGS, "ggSpreadSheedTask: onPostExecute: result: " + result);

	        MaxkInfo.dataLoading = true;
			MaxkUtils.getAppDataVersion(context);
	        MaxkInfo.logOn = MaxkUtils.queryLogin( context, MaxkInfo.phoneNo );
	        if (!MaxkInfo.logOn) {
				MaxkInfo.pushOn = false;
			}
			PushUtil.setPushSubscribe(context);
	        
	        dismissProgressDialog();
	        Toast.makeText(context, context.getString(R.string.member_info_down_end), Toast.LENGTH_LONG).show();
		}
		
		@Override
    	protected void onCancelled() {
    		super.onCancelled();
    		
    		if( BuildConfig.DEBUG )
				Log.w(TAGS, "ggSpreadSheedTask: onCancelled");
    		
    		cancelLoading = true;
    		dismissProgressDialog();
    		
    		ProjectSheet.ssTask = null;
		}
		
		private void dismissProgressDialog() {
			if (progDialog != null && progDialog.isShowing()) { 
				progDialog.dismiss(); 
	        } 
		}
	}

	private static SpreadsheetEntry spreadsheet;
	private static WorksheetEntry 	worksheet;
	private static void initSpreadSheet() {
		// Define the URL to request. This should never change.
		URL SPREADSHEET_FEED_URL = null;
		try {
			SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = null;
		try {
			feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e ) {
			e.printStackTrace();
		}

		if( feed == null )	// Internet connection broken
			return;
		
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		if (spreadsheets.size() == 0) {
			// TODO: There were no spreadsheets, act accordingly.
			return;
		}

		spreadsheet = getSpreadSheet(spreadsheets, sheetName);
		// worksheets in the spreadsheet.
		List<WorksheetEntry> worksheets = null;
		try {
			worksheets = spreadsheet.getWorksheets();
			worksheet = getWorkSheet(worksheets, workSheetName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	private static SpreadsheetEntry getSpreadSheet( List<SpreadsheetEntry> spreadsheets, String sheetName) {

		SpreadsheetEntry spreadsheet = null;
		for (SpreadsheetEntry sheet : spreadsheets) {
			String SheetName = sheet.getTitle().getPlainText();

			if (BuildConfig.DEBUG)
				Log.i("SpreadSheet", "Spread Sheet: " + sheet.getTitle().getPlainText());

			if (SheetName.contains(sheetName)) {
				spreadsheet = sheet;
				break;
			}
		}
		return (spreadsheet);
	}

	private static WorksheetEntry getWorkSheet(List<WorksheetEntry> worksheets, String sheetName) {
		
		for (WorksheetEntry worksheet : worksheets) {
			// Get the worksheet's title, row count, and column count.
			String title = worksheet.getTitle().getPlainText();
			int rowCount = worksheet.getRowCount();
			int colCount = worksheet.getColCount();

			// Print the fetched information to the screen for this worksheet.
			if (BuildConfig.DEBUG)
				Log.i(TAGS, "getWorkSheet: " + title + " rows:" + rowCount + " cols: " + colCount);

			// by date, by event, by speaker
			if (title.contains(sheetName)) {
				return worksheet;
			}
		}
		return null;
	}

	// public static Map<String, String> queryListFeed(WorksheetEntry worksheet, String query, int rCount) {
	public static boolean queryListFeed(WorksheetEntry worksheet, String query, int rCount) {

		try {
			if (BuildConfig.DEBUG)
				Log.i(TAGS, "queryListFeed: " + "feedUrl: " + worksheet.getListFeedUrl().toString() + " \n\t\tquery: " + query);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		URL listFeedUrl = null;
		// Fetch the list feed of the worksheet.
		if (query == null)
			listFeedUrl = worksheet.getListFeedUrl();
		else {
			try {
				listFeedUrl = new URI(worksheet.getListFeedUrl().toString() + query).toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}

		ListFeed listFeed = null;
		try {
			listFeed = service.getFeed(listFeedUrl, ListFeed.class);

			int i = 0;
			// Iterate through each row, printing its cell values.
			for (ListEntry row : listFeed.getEntries()) {
				
				if( cancelLoading )
					return false;
				
				// Print the first column's cell value
				if (BuildConfig.DEBUG )
					Log.i(TAGS, row.getTitle().getPlainText() + "\t");

				// WorkSheet: Field Name
				Set<String> filedSet = row.getCustomElements().getTags();
				Object[] filedArray = filedSet.toArray();
				if (i++ == 0)
					Log.i(TAGS, filedSet + "\n");

				// Iterate over the remaining columns, and print each cell value
				int index = 0;
				String clsNo = null;
				String term = null;
				ContentValues memberInfos = new ContentValues();
				for (String tag : row.getCustomElements().getTags()) {
					
					String field = (String) filedArray[index];
					String value = row.getCustomElements().getValue(tag);
					// 010-1234-5678 --> 01012345678 for Login Search
					if( field != null && field.equals( MemberDB.MOBILE_PHONE )) {
						if( value != null )
							value = value.replaceAll("-", "");
					}
					memberInfos.put(field, 	value);
					
					if( MemberDB.CLASS_NO.equals(field))
						clsNo =  value;
					if( MemberDB.TERM.equals(field))
						term =  value;
					if (BuildConfig.DEBUG) {
						Log.i(TAGS, "" + index + ". "+ field + ":" + value+ "\n");
					}
					index++;
				}
				
				// EDICON: Debugging: java.lang.IllegalArgumentException: the bind value at index 1 is null <-- clsNo 
				if( clsNo == null )	
					continue;
				
	   	    	// Insert or Update
				// ToDO: Member 중복 허용: classNo:하나만
				if( memberDB.getMember( clsNo ) == null )
					// memberDB.updateMember( clsNo, memberInfos );
					memberDB.addMember( memberInfos );
				else {
					if( term != null || term.equals("multi"))
						memberDB.addMember( memberInfos );
					else
                        memberDB.updateMember( clsNo, memberInfos );
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e ) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private static void queryCellBasedFeed(WorksheetEntry worksheet,
			String query, int rCount) {

		URL cellFeedUrl = null;
		// Fetch the list feed of the worksheet.
		if (query == null)
			cellFeedUrl = worksheet.getCellFeedUrl();
		else {
			try {
				cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString() + query).toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}

		CellFeed cellFeed = null;
		try {
			cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e ) {
			e.printStackTrace();
		}

		/*
		 * For TED Sheet: inputValue = Excel Formula such as "= A1 + B1" A1 ID
		 * inputValue numericValue literalVlaue A1 R1C1 null null URL B1 R1C2
		 * null null 'ID C1 R1C3 null null URL D1 R1C4 null null Speaker E1 R1C5
		 * null null Name F1 R1C6 null null Short Summary G1 R1C7 null null
		 * Event H1 R1C8 null null Duration I1 R1C9 null null Publish date
		 */
		int c = 0;
		// Iterate through each cell, printing its value.
		for (CellEntry cell : cellFeed.getEntries()) {
			// Print the cell's address in A1 notation
			if (BuildConfig.DEBUG) {
				
				Log.i(TAGS, cell.getTitle().getPlainText() + "\t");
				Log.i("SpreadSheet", cell.getId().substring(cell.getId().lastIndexOf('/') + 1) + "\t");

				// Print the cell's formula or text value
				Log.i(TAGS, cell.getCell().getInputValue() + "\t");
				// Print the cell's calculated value if the cell's value is numeric
				// Prints empty string if cell's value is not numeric
				
				Log.i(TAGS, cell.getCell().getNumericValue() + "\t");
				// Print the cell's displayed value (useful if the cell has a formula)

			Log.i(TAGS, cell.getCell().getValue() + "\t");
			}
			if (rCount < 0)
				;
			else if (c++ == rCount)
				break;
		}
	}

	private static void updateSheet(WorksheetEntry worksheet, String cellPos, String cellValue) {
		// Fetch the cell feed of the worksheet.
		URL cellFeedUrl = worksheet.getCellFeedUrl();
		CellFeed cellFeed = null;
		try {
			cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e ) {
			e.printStackTrace();
		}

		// Iterate through each cell, updating its value if necessary.
		// TODO: Update cell values more intelligently.
		for (CellEntry cell : cellFeed.getEntries()) {
			if (cell.getTitle().getPlainText().equals(cellPos)) {
				cell.changeInputValueLocal(cellValue);
				try {
					cell.update();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
