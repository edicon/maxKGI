package com.maxk.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maxk.notebook.member.Member;
import com.maxk.notebook.misc.MaxkInfo;

/*
 * Full Text Search(FTS): http://blog.xojo.com/2014/03/14/full_text_search_with_sqlite/
 * prefix: default: query*
 * suffix search: *query or *query*: https://blog.kapeli.com/sqlite-fts-contains-and-suffix-matches
 */
public class MemberDB extends SQLiteOpenHelper {

	private String TAG = "MEMBER_DB";

	private static final int DATABASE_VERSION 	= 1;

	private static final String DATABASE_NAME 	= "MemberDatabase";
	public  static final String MEMBER_TABLE	= "members";

	// Contacts Table Columns names
	public static final String KEY_ID 			= "_id";
	public static final String NAME 			= "name";
	public static final String CLASS_NO 		= "classno";
	public static final String TERM 			= "term";			// Added
	public static final String MAJOR			= "major";
	public static final String MAJOR_TYPE		= "majortype";		// Added
	public static final String SEX 				= "sex";
	public static final String AGE 				= "age";
	public static final String ZIP_CODE 		= "zipcode";
	public static final String EMAIL 			= "email";
	public static final String HOME_ADDR 		= "homeaddr";
	public static final String OFFICE_PHONE 	= "officephone";
	public static final String HOME_PHONE 		= "homephone";
	public static final String MOBILE_PHONE 	= "mobilephone";
	public static final String COMPANY			= "company";
	public static final String POSITION 		= "position";
	public static final String OFFICER 			= "officer";
	public static final String OFFICER_TYPE 	= "officertype";	// Added	
	public static final String PROPESSOR 		= "propessor";
	public static final String PROPESSOR_TYPE 	= "propessortype";	// Added
	public static final String CIRCLE 			= "circle";
	public static final String CIRCLE_TYPE 		= "circletype";		// Added
	public static final String OFFICE_ADDR 		= "officeaddr";
	public static final String FAX 				= "fax";
	public static final String HOME_PAGE 		= "homePage";
	public static final String BIZ_TYPE 		= "biztype";
	public static final String ADMIN 			= "admin";
	
	public static final String SESSION 			= "session";
	
	public static final String SEM_SPARE1 		= "etc1";
	public static final String SEM_SPARE2 		= "etc2";
	public static final String SEM_SPARE3 		= "etc3";
	
	private String CREATE_SEM_TABLE = "CREATE VIRTUAL TABLE " + MEMBER_TABLE + " USING fts3"
		+ "("
			+ KEY_ID 			+ " INTEGER PRIMARY KEY,"
			
			+ NAME 				+ " TEXT,"		
			+ CLASS_NO 			+ " TEXT," 
			+ TERM 				+ " TEXT," 	// Added
			+ MAJOR 			+ " TEXT,"
			+ MAJOR_TYPE 		+ " TEXT,"	// Added
			+ SEX 				+ " TEXT,"
			+ AGE 				+ " TEXT,"
			+ ZIP_CODE 			+ " TEXT," 
			+ EMAIL 			+ " TEXT,"
			+ HOME_ADDR 		+ " TEXT," 
			+ OFFICE_PHONE 		+ " TEXT,"
			+ HOME_PHONE 		+ " TEXT," 
			
			+ MOBILE_PHONE 		+ " TEXT,"
			+ COMPANY 			+ " TEXT," 		
			+ POSITION 			+ " TEXT,"
			+ OFFICER 			+ " TEXT,"
			+ OFFICER_TYPE 		+ " TEXT," 	// Added
			+ PROPESSOR 		+ " TEXT,"
			+ PROPESSOR_TYPE 	+ " TEXT,"	// Added
			
			+ CIRCLE 			+ " TEXT,"
			+ CIRCLE_TYPE 		+ " TEXT,"	// Added
			+ OFFICE_ADDR 		+ " TEXT,"
			+ FAX 				+ " TEXT," 
			+ HOME_PAGE 		+ " TEXT,"
			+ BIZ_TYPE 			+ " TEXT,"
			+ ADMIN 			+ " TEXT,"
			
			+ SEM_SPARE1 		+ " TEXT," 
			+ SEM_SPARE2 		+ " TEXT,"
			+ SEM_SPARE3 		+ " TEXT" 
			
		+ ")";
	
	private String[] MEMBER_COLUMNS = { 	
			KEY_ID,
			
			NAME, 
			CLASS_NO,
			TERM,		// Added
			MAJOR, 
			MAJOR_TYPE, // Added
			SEX,
			AGE, 
			ZIP_CODE,
			EMAIL, 
			HOME_ADDR,
			OFFICE_PHONE, 
			HOME_PHONE,
			
			MOBILE_PHONE, 
			COMPANY,
			POSITION, 
			OFFICER,
			OFFICER_TYPE,	// Added
			PROPESSOR, 
			PROPESSOR_TYPE, // Added
			
			CIRCLE,
			CIRCLE_TYPE,	// Added
			OFFICE_ADDR, 
			FAX,
			HOME_PAGE, 
			BIZ_TYPE,
			ADMIN, 
			
			SEM_SPARE1,
			SEM_SPARE2, 
			SEM_SPARE3,
	};
	
	private Context mContext;
	public MemberDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SEM_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MEMBER_TABLE);
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	public void addMember( Member mem) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		setMember( mem, values );

		db.insert(MEMBER_TABLE, null, values);
		db.close(); // Closing database connection
	}
	
	public void addMember( ContentValues values ) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		db.insert(MEMBER_TABLE, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting Member
	 * @param  clsNo raw DB ID
	 * @return SEM
	 */
	public Cursor getMember( String clsNo) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(
				MEMBER_TABLE, 
				MEMBER_COLUMNS, 
				CLASS_NO + "=?",
				new String[] { clsNo }, null, null, null, null);
		
		if (cursor == null)
			return null;
		if( cursor.getCount() == 0 )
			return null;
		
		cursor.moveToFirst();
		
		return cursor;
	}
	
	public Cursor getMemberCursor(String sel, String[] selArg) {

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(
				MEMBER_TABLE, 
				MEMBER_COLUMNS, 
				sel, 
				selArg, 
				null, null, null, null);

		if (cursor == null || cursor.getCount() == 0)
			return null;
		
		return cursor;
	}
	
	public Cursor searchWord( String q) {
		// FTS Search: 아래 MATCH query 지원
		// prefix search(query*): Default Search: WHERE Table Name MATCH 'q*';
		// suffix search(*query): 지원하지 않음. 구현해야 함. Initial Comment 참고
		// LIKE로 구현: select * from members WHERE company like '%q%' or name like '%q%' or mobilephone like '%q%';
		// WHERE TabbleColumeName LIKE '%q%';

		// Select FTS
		String selectQuery = "SELECT  * FROM " + MEMBER_TABLE
				+ " WHERE "  + NAME 			+ " LIKE " + "'%" + q + "%'"
				+ " OR "     + COMPANY 			+ " LIKE " + "'%" + q + "%'"
				+ " OR "     + OFFICE_PHONE 	+ " LIKE " + "'%" + q + "%'"
				+ " OR "     + MOBILE_PHONE 	+ " LIKE " + "'%" + q + "%'";
				//		   + " WHERE "  + MEMBER_TABLE + " MATCH " + "'" + q + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}
	
	public Cursor searchMember( String q) {
		
		// Select FTS
		String selectQuery = "SELECT  * FROM " + MEMBER_TABLE
				 		   + " WHERE "  + MEMBER_TABLE + " MATCH " + "'" + q + "*'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}
	
	public Cursor searchAdmin( String q) {
		
		// Select FTS
		String selectQuery = "SELECT  * FROM " + MEMBER_TABLE 
						   + " WHERE "  + ADMIN + " MATCH " + "'" + q + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}
	
	public Cursor loginMember( String q) {
		
		// Select FTS
		String selectQuery = "SELECT  * FROM " + MEMBER_TABLE 
				   		   + " WHERE "  + MOBILE_PHONE + " MATCH " + "'" + q + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor;
	}
	
	/**
	 * Getting All Member List
	 * @return List<SEM>
	 */
	public Cursor getAllMember() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + MEMBER_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null); 

		return cursor;
	}

	/**
	 * Updating Member
	 * 
	 * @param mem
	 * @return int
	 */
	public int updateMember(Member mem) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		setMember( mem, values );

		return db.update(
				MEMBER_TABLE, values, 
				KEY_ID + " = ?",
				new String[] { String.valueOf(mem.getId()) });
	}
	
	public int updateMember( String clsNo, ContentValues values) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		return db.update(
				MEMBER_TABLE, values, 
				CLASS_NO + " = ?",
				new String[] { clsNo });
	}
	

	/**
	 * Deleting single SEM item
	 * @param mem IAB SKU ID
	 * @return void
	 */
	public void deleteMember(Member mem) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(
				MEMBER_TABLE, 
				CLASS_NO + " = ?",
				new String[] { mem.getClassNo() });
	}

	
	public void setMember( Member mem, ContentValues values ) {
		
		values.put(KEY_ID, 			mem.getId()); 		
		values.put(NAME, 			mem.getName());
		
		values.put(CLASS_NO, 		mem.getClassNo());
		values.put(TERM, 			mem.getTerm()); 
		values.put(MAJOR, 			mem.getMajor());
		values.put(MAJOR_TYPE, 		mem.getMajorType());
		values.put(SEX, 			mem.getSex()); 
		values.put(AGE, 			mem.getAge());
		values.put(ZIP_CODE, 		mem.getZipCode());
		
		values.put(EMAIL, 			mem.getMailAddr());
		values.put(HOME_ADDR, 		mem.getHomeAddr()); 
		values.put(OFFICE_PHONE, 	mem.getOfficePhone());
		values.put(HOME_PHONE, 		mem.getHomePhone()); 
		values.put(MOBILE_PHONE, 	mem.getMobilePhone());
		values.put(COMPANY, 		mem.getCompany()); 
		values.put(POSITION, 		mem.getPosition());
		values.put(OFFICER, 		mem.getOfficer());
		values.put(OFFICER_TYPE, 	mem.getOfficerType());
		
		values.put(PROPESSOR, 		mem.getProfessor());
		values.put(PROPESSOR_TYPE, 	mem.getProfessorType());
		values.put(CIRCLE, 			mem.getCircle()); 
		values.put(CIRCLE_TYPE, 	mem.getCircleType()); 
		values.put(OFFICE_ADDR, 	mem.getOfficeAddr());
		
		values.put(FAX, 			mem.getFax()); 
		values.put(HOME_PAGE, 		mem.getHomePage());
		values.put(BIZ_TYPE, 		mem.getBizType());
		values.put(ADMIN, 			mem.getAdmin());
		
		values.put(SEM_SPARE1, 		mem.getEtc1()); 
		values.put(SEM_SPARE2, 		mem.getEtc2());

	}
	
	public void getMember( Member mem, Cursor cursor ) {
		
		mem.setId(			cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

		mem.setName(		cursor.getString(cursor.getColumnIndexOrThrow(NAME))); 
		mem.setClassNo(		cursor.getString(cursor.getColumnIndexOrThrow(CLASS_NO))); 
		mem.setTerm(		cursor.getString(cursor.getColumnIndexOrThrow(TERM))); 
		mem.setMajor(		cursor.getString(cursor.getColumnIndexOrThrow(MAJOR))); 
		mem.setMajorType(	cursor.getString(cursor.getColumnIndexOrThrow(MAJOR_TYPE))); 
		mem.setSex(			cursor.getString(cursor.getColumnIndexOrThrow(SEX))); 
		mem.setAge(			cursor.getString(cursor.getColumnIndexOrThrow(AGE))); 
		mem.setZipCode(		cursor.getString(cursor.getColumnIndexOrThrow(ZIP_CODE))); 
		mem.setMailAddr(	cursor.getString(cursor.getColumnIndexOrThrow(EMAIL))); 
		mem.setHomeAddr(	cursor.getString(cursor.getColumnIndexOrThrow(HOME_ADDR)));
		
		mem.setOfficePhone(	cursor.getString(cursor.getColumnIndexOrThrow(OFFICE_PHONE))); 
		mem.setHomePhone(	cursor.getString(cursor.getColumnIndexOrThrow(HOME_PHONE))); 
		mem.setMobilePhone(	cursor.getString(cursor.getColumnIndexOrThrow(MOBILE_PHONE))); 	
		
		mem.setCompany(		cursor.getString(cursor.getColumnIndexOrThrow(COMPANY))); 
		mem.setPosition(	cursor.getString(cursor.getColumnIndexOrThrow(POSITION))); 
		mem.setOfficer(		cursor.getString(cursor.getColumnIndexOrThrow(OFFICER)));
		mem.setOfficerType(	cursor.getString(cursor.getColumnIndexOrThrow(OFFICER_TYPE))); 
		mem.setProfessor(	cursor.getString(cursor.getColumnIndexOrThrow(PROPESSOR)));
		mem.setProfessorType(cursor.getString(cursor.getColumnIndexOrThrow(PROPESSOR_TYPE))); 
		mem.setCircle(		cursor.getString(cursor.getColumnIndexOrThrow(CIRCLE))); 
		mem.setCircleType(	cursor.getString(cursor.getColumnIndexOrThrow(CIRCLE_TYPE))); 
		mem.setOfficeAddr(	cursor.getString(cursor.getColumnIndexOrThrow(OFFICE_ADDR))); 
		mem.setFax(			cursor.getString(cursor.getColumnIndexOrThrow(FAX))); 
		mem.setHomePage(	cursor.getString(cursor.getColumnIndexOrThrow(HOME_PAGE))); 
		mem.setAdmin(		cursor.getString(cursor.getColumnIndexOrThrow(ADMIN))); 
		mem.setBizType(		cursor.getString(cursor.getColumnIndexOrThrow(BIZ_TYPE))); 
			
		mem.setEtc1(		cursor.getString(cursor.getColumnIndexOrThrow(SEM_SPARE1))); 
		mem.setEtc2(		cursor.getString(cursor.getColumnIndexOrThrow(SEM_SPARE1))); 
		mem.setSession(		cursor.getString(cursor.getColumnIndexOrThrow(SESSION)));  
	}	
}
