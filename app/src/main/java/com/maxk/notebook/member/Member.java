package com.maxk.notebook.member;

import org.json.JSONObject;

import com.maxk.notebook.db.MemberDB;
import com.maxk.notebook.maxkgi.BuildConfig;

import android.database.Cursor;
import android.util.Log;

public class Member {
	
	public Member(Cursor c) {
		setMember( c );
	}
	
	public Member(JSONObject jsonMember) {
		setMember( jsonMember );
	}
	
	public Member() {
		setMember();
	}
	
	private static int count = 0;
	public void setMember( Cursor c ) {
		int 	idIndex, 		nameIndex, 		class_noIndex, 	  majorIndex, 	  sexIndex, 		ageIndex, 		zipCodeIndex,
				termIndex, 		majorTypeIndex, officerTypeIndex, propessorTypeIndex, circleTypeIndex, 	// Added
				emailIndex, 	homeAddrIndex, 	officePhoneIndex, homePhoneIndex, mobilePhoneIndex, companyIndex,	positionIndex, 
				officerIndex, 	propessorIndex, circleIndex, 	  officeAddrIndex,faxIndex, 		homePageIndex,
				adminIndex,		bizTypeIndex;

		idIndex 		 	= c.getColumnIndex(MemberDB.KEY_ID);
		nameIndex 			= c.getColumnIndex(MemberDB.NAME);
		class_noIndex 		= c.getColumnIndex(MemberDB.CLASS_NO);
		termIndex 			= c.getColumnIndex(MemberDB.TERM);
		majorIndex 			= c.getColumnIndex(MemberDB.MAJOR);
		majorTypeIndex 		= c.getColumnIndex(MemberDB.MAJOR_TYPE);
		sexIndex 			= c.getColumnIndex(MemberDB.SEX);
		ageIndex 			= c.getColumnIndex(MemberDB.AGE);
		zipCodeIndex 		= c.getColumnIndex(MemberDB.ZIP_CODE);
		emailIndex 			= c.getColumnIndex(MemberDB.EMAIL);
		homeAddrIndex 		= c.getColumnIndex(MemberDB.HOME_ADDR);
		officePhoneIndex 	= c.getColumnIndex(MemberDB.OFFICE_PHONE);
		homePhoneIndex 		= c.getColumnIndex(MemberDB.HOME_PHONE);
		mobilePhoneIndex 	= c.getColumnIndex(MemberDB.MOBILE_PHONE);
		companyIndex		= c.getColumnIndex(MemberDB.COMPANY);
		positionIndex 		= c.getColumnIndex(MemberDB.POSITION);
		officerIndex 		= c.getColumnIndex(MemberDB.OFFICER);
		officerTypeIndex 	= c.getColumnIndex(MemberDB.OFFICER_TYPE);
		propessorIndex 		= c.getColumnIndex(MemberDB.PROPESSOR);
		propessorTypeIndex 	= c.getColumnIndex(MemberDB.PROPESSOR_TYPE);
		circleIndex 		= c.getColumnIndex(MemberDB.CIRCLE);
		circleTypeIndex 	= c.getColumnIndex(MemberDB.CIRCLE_TYPE);
		officeAddrIndex		= c.getColumnIndex(MemberDB.OFFICE_ADDR);
		faxIndex 			= c.getColumnIndex(MemberDB.FAX);
		homePageIndex		= c.getColumnIndex(MemberDB.HOME_PAGE);
		adminIndex			= c.getColumnIndex(MemberDB.ADMIN);
		bizTypeIndex		= c.getColumnIndex(MemberDB.BIZ_TYPE);
		
		id 		 			= c.getInt(idIndex);
		name 				= c.getString(nameIndex);
		class_no 			= c.getString(class_noIndex);
		term 				= c.getString(termIndex);
		major 				= c.getString(majorIndex);
		majorType 			= c.getString(majorTypeIndex);
		sex 				= c.getString(sexIndex);
		age 				= c.getString(ageIndex);
		zipCode 			= c.getString(zipCodeIndex);
		email 				= c.getString(emailIndex);
		homeAddr 			= c.getString(homeAddrIndex);
		officePhone 		= c.getString(officePhoneIndex);
		homePhone 			= c.getString(homePhoneIndex);
		mobilePhone 		= c.getString(mobilePhoneIndex);
		company				= c.getString(companyIndex);
		position 			= c.getString(positionIndex);
		officer 			= c.getString(officerIndex);
		officerType 		= c.getString(officerTypeIndex);
		propessor 			= c.getString(propessorIndex);
		propessorType 		= c.getString(propessorTypeIndex);
		circle 				= c.getString(circleIndex);
		circleType 			= c.getString(circleTypeIndex);
		officeAddr			= c.getString(officeAddrIndex);
		fax 				= c.getString(faxIndex);
		homePage			= c.getString(homePageIndex);
		admin				= c.getString(adminIndex);
		bizType				= c.getString(bizTypeIndex);
	}
	
	public void setMember() {
		id 		 		= count;
		name 			= "Name: " 			+ count;
		class_no 		= "ClassNo: " 		+ count;
		term 			= "Term: " 			+ count;
		major 			= "Major: " 		+ count;
		sex 			= "Sex: " 			+ count;
		age 			= "Age: " 			+ count;
		zipCode 		= "zipCode: " 		+ count;
		homeAddr 		= "homeAddr: " 		+ count;
		email 			= "email" 			+ count + "@gmail.com";
		officePhone 	= homePhone = mobilePhone = fax= "010-4716-000" + count;
		company 		= "Company: " 		+ count;
		position 		= "position" 		+ count;
		circle 			= "circle" 			+ count;
		officeAddr 		= "OfficeAddr: "	+ count;
		fax 			= "fax: " 			+ count;
		admin 			= "yes" 			+ "";
		bizType 		= "bizType: " 		+ "";
		homePage		= "www.who.com";

		session 		= "Session: " 		+ count;

		count++;
	}
	
	public void setMember( JSONObject jsonMember ) {
		try {
			id 		 	= jsonMember.getInt(MemberDB.KEY_ID);
			name 		= jsonMember.getString(MemberDB.NAME);
			class_no 	= jsonMember.getString(MemberDB.CLASS_NO);
			term 		= jsonMember.getString(MemberDB.TERM);
			major 		= jsonMember.getString(MemberDB.MAJOR);
			majorType 	= jsonMember.getString(MemberDB.MAJOR_TYPE);
			sex 		= jsonMember.getString(MemberDB.SEX);
			age 		= jsonMember.getString(MemberDB.AGE);
			zipCode 	= jsonMember.getString(MemberDB.ZIP_CODE);
			email 		= jsonMember.getString(MemberDB.EMAIL);
			homeAddr 	= jsonMember.getString(MemberDB.HOME_ADDR);
			officePhone = jsonMember.getString(MemberDB.OFFICE_PHONE);
			homePhone 	= jsonMember.getString(MemberDB.HOME_PHONE);
			mobilePhone = jsonMember.getString(MemberDB.MOBILE_PHONE);
			company		= jsonMember.getString(MemberDB.COMPANY);
			position 	= jsonMember.getString(MemberDB.POSITION);
			officer 	= jsonMember.getString(MemberDB.OFFICER);
			officerType = jsonMember.getString(MemberDB.OFFICER_TYPE);
			propessor 	= jsonMember.getString(MemberDB.PROPESSOR);
			propessorType = jsonMember.getString(MemberDB.PROPESSOR_TYPE);
			circle 		= jsonMember.getString(MemberDB.CIRCLE);
			circleType 	= jsonMember.getString(MemberDB.CIRCLE_TYPE);
			officeAddr	= jsonMember.getString(MemberDB.OFFICE_ADDR);
			fax 		= jsonMember.getString(MemberDB.FAX);
			homePage	= jsonMember.getString(MemberDB.HOME_PAGE);
			admin		= jsonMember.getString(MemberDB.ADMIN);
			bizType		= jsonMember.getString(MemberDB.BIZ_TYPE);
			
			if( BuildConfig.DEBUG ) {
				Log.d("jsonMember", "Member --> name: " + name + ", phoneNo: "  + mobilePhone );
			}
		} catch( Exception e ) {
			e.printStackTrace();
			Log.e("jsonMember", "Member --> name: " + name + ", phoneNo: "  + mobilePhone );
		}
	}
	
	int id;
	String name;
	String class_no;
	String term;			// Added
	String major;
	String majorType;		// Added
	String sex;
	String age;
	String zipCode;
	String homeAddr;
	String email;
	String mobilePhone;
	String company;
	String position;
	String homePhone;
	String officer;
	String officerType;		// Added
	String propessor;
	String propessorType;	// Added
	String circle;
	String circleType;		// Added
	String officePhone;
	String officeAddr;
	String fax;
	String homePage;
	String admin;
	String bizType;
	
	String session;
	String etc1, etc2, etc3;

	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}	
	public String getClassNo() {
		return class_no;
	}
	public String getTerm() {
		return term;
	}
	public String getMajor() {
		return major;
	}
	public String getMajorType() {
		return majorType;
	}
	public String getSex() {
		return sex;
	}
	public String getAge() {
		return age;
	}	
	public String getZipCode() {
		return zipCode;
	}	
	public String getMailAddr() {
		return email;
	}	
	public String getHomeAddr() {
		return homeAddr;
	}	
	public String getOfficePhone() {
		return officePhone;
	}	
	public String getHomePhone() {
		return homePhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public String getCompany() {
		return company;
	}	
	public String getPosition() {
		return position;
	}
	public String getOfficer() {
		return officer;
	}
	public String getOfficerType() {
		return officerType;
	}
	public String getProfessor() {
		return propessor;
	}	
	public String getProfessorType() {
		return propessorType;
	}	
	public String getCircle() {
		return circle;
	}	
	public String getCircleType() {
		return circleType;
	}
	public String getOfficeAddr() {
		return officeAddr;
	}
	public String getFax() {
		return fax;
	}	
	public String getHomePage() {
		return homePage;
	}
	public String getBizType() {
		return bizType;
	}
	public String getAdmin() {
		return admin;
	}
	public String getEtc1() {
		return etc1;
	}	
	public String getEtc2() {
		return etc2;
	}	
	public String getSession() {
		return session;
	}
	
	// Set
	public void setId( int param){
		 id = param;
	}
	public void setName(String param){
		 name = param;
	}	
	public void setClassNo(String param) {
		 class_no = param;
	}	
	public void setTerm(String param) {
		 term = param;
	}	
	public void setMajor(String param) {
		 major = param;
	}
	public void setMajorType(String param) {
		 majorType = param;
	}
	public void setSex(String param) {
		 sex = param;
	}
	public void setAge(String param) {
		 age = param;
	}	
	public void setZipCode(String param) {
		 zipCode = param;
	}	
	public void setMailAddr(String param) {
		 email = param;
	}	
	public void setHomeAddr(String param) {
		 homeAddr = param;
	}	
	public void setOfficePhone(String param) {
		 officePhone = param;
	}	
	public void setHomePhone(String param) {
		 homePhone = param;
	}
	public void setMobilePhone(String param) {
		 mobilePhone = param;
	}
	public void setCompany(String param) {
		 company = param;
	}	
	public void setPosition(String param) {
		 position = param;
	}
	public void setOfficer(String param) {
		 officer = param;
	}
	public void setOfficerType(String param) {
		 officerType = param;
	}
	public void setProfessor(String param) {
		 propessor = param;
	}	
	public void setProfessorType(String param) {
		 propessorType = param;
	}
	public void setCircle(String param) {
		 circle = param;
	}
	public void setCircleType(String param) {
		 circleType = param;
	}
	public void setOfficeAddr(String param) {
		 officeAddr = param;
	}
	public void setFax(String param) {
		 fax = param;
	}	
	public void setHomePage(String param) {
		 homePage = param;
	}
	public void setAdmin(String param) {
		 admin = param;
	}
	public void setBizType(String param) {
		 bizType = param;
	}
	public void setEtc1(String param) {
		 etc1 = param;
	}	
	public void setEtc2(String param) {
		 etc2 = param;
	}	
	public void setSession(String param) {
		session = param;
	}
}