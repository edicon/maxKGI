package com.maxk.notebook.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edicon.lib.volly.VolleySingleton;
import com.maxk.notebook.db.LoadGoogleSheet.ggSpreadSheedTask;
import com.maxk.notebook.maxkgi.BuildConfig;
import com.maxk.notebook.maxkgi.R;
import com.maxk.notebook.misc.MaxkInfo;

public class ProjectSheet {

	// Project File: ToTheWeb:
	// https://docs.google.com/spreadsheets/d/1SGisT3OqmUs-EYKdF_VaA6XS38b5vdyc_pN3Yl02Nc8/edit#gid=0
	// For Univ
	// private static String ggSheetUrl = "http://spreadsheets.google.com/feeds/list/1SGisT3OqmUs-EYKdF_VaA6XS38b5vdyc_pN3Yl02Nc8/od6/public/values?alt=json";
	// For KGI
	private static String ggSheetUrl = MaxkInfo.GOOGLE_DRIVE_SHEET_FILE;

	// Private Sheet Cell Name
	private static final String TAG_GSX_PACKAGE 	= "gsx$package";
	private static final String TAG_GSX_ID 			= "gsx$id";
	private static final String TAG_GSX_PASS 		= "gsx$pass";
	private static final String TAG_GSX_UPDATE 		= "gsx$update";
	private static final String TAG_GSX_FILE 		= "gsx$file";
	private static final String TAG_GSX_SHEET 		= "gsx$sheet";
	
	private static final String TAG_GSX_TEXT 			= "$t";
	
	private RequestQueue volleyQueue;
	private ProjectInfo projectInfo;
	private Context context;

	public ProjectSheet( Activity a, String sheetUrl ) {
		
		if( sheetUrl != null )
			ggSheetUrl = sheetUrl;
		
		context = a;
		volleyQueue = VolleySingleton.getInstance( context ).getRequestQueue();
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	
	public void queryProjectInfo() {
		projectInfo = null;
		VolleySingleton.startProgress( null );
		queryJsonVolly( ggSheetUrl, context.getPackageName());
	}
	
	public static final String TAG_FEED 				= "feed";
	public static final String TAG_ENTRY 				= "entry";
	public static final String TAG_ENTRY_CONTENT		= "content";
	private static JSONObject 	feed 	= null;
	private static JSONArray 	entrys 	= null;
	private void queryJsonVolly( String jsonUrl, final String pkgName ) {

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, jsonUrl, (JSONObject)null,
			new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject json) {
					try {
						feed		= json.getJSONObject(TAG_FEED);
						entrys 		= feed.getJSONArray(TAG_ENTRY);
						ArrayList<ProjectInfo> infoList	= getSheetEntryList( entrys, pkgName );
		            	
						if( projectInfo != null ) {
							String id 	 = projectInfo.getId();
							String pass  = projectInfo.getPass();
							String file  = projectInfo.getFile();
							String sheet = projectInfo.getSheet();
							loadMemberData( pass + "." + id + "@gmail.com", id + "." + pass, file, sheet);
						} else {
							Toast.makeText(context, context.getString(R.string.error_no_project_info), Toast.LENGTH_LONG).show();
						}
		            	VolleySingleton.stopProgress( null );
					} catch ( JSONException e ) {
						VolleySingleton.stopProgress( null );
						e.printStackTrace();
					}
				}
			}, 
			new Response.ErrorListener() {	
				@Override
				public void onErrorResponse(VolleyError error) {
					if( BuildConfig.DEBUG )
						Log.d("VOLLY", "onErrorResponse: " + error.getLocalizedMessage());
					VolleySingleton.stopProgress( null );
				}
		});
		volleyQueue.add(jsObjRequest);
	}
	
	public ArrayList<ProjectInfo> getSheetEntryList( JSONArray entrys, String name ) {	
		
		ArrayList<ProjectInfo> entryList = new ArrayList<ProjectInfo>();		
		try {
			for(int i = 0; i < entrys.length(); i++){
				JSONObject e = entrys.getJSONObject(i);

				JSONObject eObj = e.getJSONObject(TAG_GSX_PACKAGE);
				String pkgName 	= eObj.getString(TAG_GSX_TEXT);
				eObj			= e.getJSONObject(TAG_GSX_ID);
				String id 		= eObj.getString(TAG_GSX_TEXT);
				eObj 			= e.getJSONObject(TAG_GSX_PASS);
				String pass 	= eObj.getString(TAG_GSX_TEXT);
				eObj			= e.getJSONObject(TAG_GSX_UPDATE);
				long update 	= eObj.getLong(TAG_GSX_TEXT);
				eObj			= e.getJSONObject(TAG_GSX_FILE);
				String file 	= eObj.getString(TAG_GSX_TEXT);
				eObj			= e.getJSONObject(TAG_GSX_SHEET);
				String sheet 	= eObj.getString(TAG_GSX_TEXT);

				ProjectInfo pjtInfo = new ProjectInfo( pkgName, id, pass, update, file, sheet);
				entryList.add(pjtInfo);
				if( pkgName.equals(name)) {
					projectInfo = pjtInfo;
					return entryList;
				}
				if( BuildConfig.DEBUG )
					Log.d("ggSS", "getSheetEntryList: pkgName: " + pkgName);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entryList;
	}

	public class ProjectInfo {
		public ProjectInfo( String p, String i, String pa, long u, String f, String s) {
			pkgName = p;
			id 		= i;
			pass 	= pa;
			update 	= u;
			file 	= f;
			sheet 	= s;
		}
		private String pkgName;
		private String id;
		private String pass;
		private long   update;
		private String file;
		private String sheet;
		
		public String getPackage() {
			return pkgName;
		}		
		public String getId() {
			return id;
		}		
		public String getPass() {
			return pass;
		}		
		public long getUpdate() {
			return update;
		}		
		public String getFile() {
			return file;
		}
		public String getSheet() {
			return sheet;
		}	
	}
	
	public static ggSpreadSheedTask ssTask;
	private void loadMemberData( String id, String pass, String file, String sheet) {
		
		LoadGoogleSheet.initMemberDB( context );
		LoadGoogleSheet.initAuth( id, pass);
		LoadGoogleSheet.initQuery( file, sheet, null, -1);
		if( BuildConfig.DEBUG )
			Log.i("ggSS", "file: " + file + " sheet: " + sheet);
		
		if( ssTask != null )
			ssTask.cancel(true);
		ssTask = new ggSpreadSheedTask();
		ssTask.execute("");
	}
}