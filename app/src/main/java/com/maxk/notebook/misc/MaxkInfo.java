package com.maxk.notebook.misc;

import com.edicon.lib.volly.Volly;


public class MaxkInfo {

	public static boolean  PARSE_STOPPED 		= true;
	public static int groupType 				= 1;
	public static int searchType 				= 1;
	public static int infoType 	 				= 1;
	public static int popupType 				= 0;
	public static int memberPos 				= 0;
	
	public static int maxButton 				= 15;
	
	public static String SEARCH_TYPE 			= "searchType";
	public static String SEARCH_TEXT 			= "searchText";
	public static String TITLE_TEXT 			= "titleText";
	public final static int SEARCH_ALL 			= 0;
	public final static int SEARCH_GROUP 		= 1;
	public final static int SEARCH_MEMBER 		= 2;
	public final static int SEARCH_ADMIN 		= 3;
	public final static int SEARCH_WORD 		= 4;

	public final static String SEARCH_SCHEDULE 	= "학사일정";
	public final static String SEARCH_LECTURE 	= "강의시간";
	public final static String SEARCH_MAIN_SCHED= "주요행사";
	
	public static boolean pushOn				= true;
	public static boolean logOn					= false;
	public static boolean isAdmin				= false;
	public final  static  String 	MAXK_ADMIN	="yes";
	public static boolean MENU_ADMIN			= true;

	public static boolean dataLoading			= false;
	public static long	  appDataVersion		= 0L;

	public static boolean MAP_LINK				= true;
	
	public static boolean USE_AD				= false;
	public static boolean LOGIN_TEST			= true;
	public static boolean CROP_MEMBER			= false;
	
	public static String phoneNo;
	
	public final static int INFO_TYPE1 	 		= 1;
	public final static int INFO_TYPE2 	 		= 2;
	public final static int INFO_TYPE3 	 		= 3;
	public final static int INFO_TYPE4 	 		= 4;
	public final static int INFO_TYPE5 	 		= 5;
	public final static int INFO_TYPE6 	 		= 6;
	public final static int INFO_TYPE7 	 		= 7;
	public final static int INFO_TYPE_HELP 		= 20;
	public final static int INFO_TYPE_USE 		= 21;

	public static Volly 	volly;
	public static boolean 	ASSETS_IMAGE	= false; 
	public static String 	ASSET_MAKX_UNIV1_IMAGE 			= "file:///android_asset/image/";
	public static String 	ASSET_MAKX_UNIV1_HTML 			= "file:///android_asset/html/";
	
	public static String 	EDICON_WEB_MAXK_UNIV1_HTML 		= "http://www.edicon.net/maxk/univ1/html/";
	public static String 	EDICON_WEB_MAXK_UNIV1_IMAGE 	= "http://www.edicon.net/maxk/univ1/image/";
	
	// Google Site for Maxk:Univ1
	public static String 	GOOGLE_SITE_MAXK_UNIV1_HTML 	= "https://sites.google.com/site/edicondev/projects/maxk/univ1/html/";
	// public static String 	GOOGLE_SITE_MAXK_UNIV1_IMAGE 	= "https://sites.google.com/site/edicondev/projects/maxk/univ1/image/";
	public static String 	GOOGLE_SITE_MAXK_UNIV1_IMAGE 	= "https://sites.google.com/site/edicondev/projects/maxk/kgi/image/member/";

	// Google Drive for Maxk:Univ1
	public static String 	GOOGLE_DRIVE_MAXK_UNIV1_HTML 	= "https://googledrive.com/host/0B97z1Yfua-TrMEotb0hvWGhIM2s/";
	public static String 	GOOGLE_DRIVE_MAXK_UNIV1_IMAGE 	= "https://googledrive.com/host/0B97z1Yfua-TrM3pvYU1Yc1lWMFk/";
	
	// Failed about Drop Box for Maxk:Univ1
	public static String 	DROPBOX_WEB_MAXK_UNIV1_HTML 	= "https://www.dropbox.com/sh/5mfxva0m0irup3u/AADm8lvYcgHdOEdANoWHnKcba?dl=0/html/";
	public static String 	DROPBOX_WEB_MAXK_UNIV1_IMAGE 	= "https://www.dropbox.com/sh/5mfxva0m0irup3u/AADm8lvYcgHdOEdANoWHnKcba?dl=0/image/";
	
	public static String 	PUB_ON_WEB_MAKX_UNIV1_HTML 		= ASSET_MAKX_UNIV1_HTML;		// ASSET_MAKX_UNIV1_HTML ==  GOOGLE_SITE_MAXK_UNIV1_HTML; 	// EDICON_WEB_MAXK_UNIV1_HTML;
	public static String 	PUB_ON_WEB_MAKX_UNIV1_IMAGE 	= GOOGLE_SITE_MAXK_UNIV1_IMAGE;	// EDICON_WEB_MAXK_UNIV1_IMAGE; 	// GOOGLE_SITE_MAXK_UNIV1_IMAGE;

	// ToDo: Account 수정, URL 수정
	public static String 	PUB_TO_WEB_MAKX_PROJECT_SHEET 	= "http://spreadsheets.google.com/feeds/list/1SGisT3OqmUs-EYKdF_VaA6XS38b5vdyc_pN3Yl02Nc8/od6/public/values?alt=json";
	public static String 	SERVICE_ACCOUNT_EMAIL = "maxkgi@maxdiary-1125.iam.gserviceaccount.com";  // dev.edicon@gmail.com
	public static String 	SERVICE_ACCOUNT_PKCS12_FILE_NAME = "MaxDiary_KGI.p12";
	public static String    GOOGLE_DRIVE_SHEET_FILE = "https://docs.google.com/spreadsheets/d/10UHye5ZjMvSVbeLz8b9jUfukNaTvNwc3r_hMG-Tjiaw/od6/public/values?alt=json";
	// ToDo: Parse App 등록: maxKGI
	public static String    YOUR_APPLICATION_ID = "pF7eyivyPV1gka4tZPgckC8OAaBn6BXU8U2M9sWs";
	public static String    YOUR_CLIENT_KEY = "r3ZVAQuWMHKRodeM5D2xCJnDxrRJ6ThEg7amDPiK";
	// ToDo: Resource File 수정
	// ToDo: ReFactoring: maxkgi --> NewName
	// ToDo: GOOGLE_SITE_MAXK_UNIV1_IMAGE, PUB_ON_WEB_MAKX_UNIV1_IMAGE
	// ToDo: ASSET_MAKX_UNIV1_HTML --> index20.html
}
