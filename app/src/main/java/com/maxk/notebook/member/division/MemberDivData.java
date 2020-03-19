package com.maxk.notebook.member.division;

import com.maxk.notebook.maxkgi.R;

public class MemberDivData {

	// 임명부
	// ToDo: MemberGroupS2
	public static String[] MemberGroupS2 = {
		"임명부",		// Group Name
		"전체회원",
		"지역별회원",
		"임원구성",
		"위원회구성"
	};
	
	// 전체 그룹
	// ToDo: MemberGroupS31
	public static String[] MemberGroupS31 = {
		"전체회원",
		"정회원",
		"신입회원"
	};
	
	public static String[] MemberGroupS31Type = {
		"s3c0",
		"s3c1",
		"s3c2",
		"s3c3",
		"s3c4",
		"s3c5"
	};
	
	// 임원 그룹
	// ToDo: MemberGroupS32
	public static String[] MemberGroupS32 = {
		"지역별 회원", // "임원 그룹",
		"종로지역",
		"중구지역",
		"강서지역",
		"마포,경기지역",
		"대전,충청지역",
		"대구,경북지역",
		"부산,경남지역"
	};
	
	public static String[] MemberGroupS32Type = {
		"s3o00",
		"s3o01",
		"s3o02",
		"s3o03",
		"s3o04",
		"s3o05",
		"s3o06",
		"s3o07",
		"s3o08",
		"s3o09",
		"s3o10",
		"s3o11"
	};
	
	// 교수진 그룹
	// ToDo: MemberGroupS33
	public static String[] MemberGroupS33 = {
		"임원구성",
		"회장",
		"자문위원장",
		"부회장",
		"감사",
		"위원장",
		"지역이사"		// 06
	};
	
	public static String[] MemberGroupS33Type = {
		"s3t00",
		"s3t01",
		"s3t02",
		"s3t03",
		"s3t04",
		"s3t05",
		"s3t06",	// 교수
		// "s3t07",
		"s3t08",
		"s3t09",
		"s3t10"
	};
	
	// 동아리 그룹
	// ToDo: MemberGroupS34
	public static String[] MemberGroupS34 = {
		"위원회구성",
		"공정거래",
		"회원관리",
        "국제정보",
        "심사",
        "카타록",
        "기획.홍보",
        "인터넷",
        "재정"
	};
	
	public static String[] MemberGroupS34Type = {
		"s3g00",
		"s3g01",
		"s3g02",
        "s3g03",
        "s3g04",
        "s3g05",
        "s3g06",
        "s3g07"
	};
	
	// 전공별 그룹
	public static String[] MemberGroupS35 = {
		"전공별 그룹",		// Group Name
		"경찰ㆍ사법행정전공",
		"국제관계ㆍ안보전공",
		"공공정책전공",
		"북한ㆍ동아시아전공",
		"사회복지전공",
		"사회ㆍ문화전공",		// 06
		"일반행정전공",			// 08, skip 07, 
		"지방자치ㆍ도시행정전공",
		"정치행정리더십전공",
		"외교안보전공"
	};
	
	public static String[] MemberGroupS35Type = {
		"s3m00",		
		"s3m01",
		"s3m02",
		"s3m03",
		"s3m04",
		"s3m05",
		"s3m06",		// skip 07
		"s3m08",
		"s3m09",
		"s3m10",
		"s3m11"
	};
	
	public static int[] InfoGroupImage = {
		R.drawable.p001_300,		
		R.drawable.p005_300,	
		R.drawable.p008_150,	
		R.drawable.p009_150,	
		R.drawable.p001_300,	
		R.drawable.p001_300
	};
}