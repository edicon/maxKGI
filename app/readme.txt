-KaKao: https://devtalk.kakao.com/t/android-studio-gradle-sdk/8700
-Version 1.0.50과 1.1.x와 호환 되지 않음
-KGI-V1.5: 2018.0527
-주의: 엑셀 회원란에 공란이 있으면 에러

member 사진
-https://sites.google.com/site/edicondev/projects/maxk/kgi/image/member
-https://sites.google.com/site/edicondev/projects/maxk/kgi/image/kgi18
-https://sites.google.com/site/edicondev/projects/maxk/kgi/image/kgi18/rule

# Testing
LOGIN_TEST = true; // false: for release

2020 Update:
* Android Target SDK 26 --> 28 Version으로 수정(26), Build Tool은 26사용

# Member 사진 및 정관 Image
* Member 사진은 반드시 png, 나머지는 jpg
-https://sites.google.com/site/edicondev/projects/maxk/kgi/image/kgi20
-https://sites.google.com/site/edicondev/projects/maxk/kgi/image/kgi20/rule

# assets/html/index[1-8].html
kgi18 -> kgi20

# Excell
z컬럼 이후에 Null이어야 함.
즉, AA 컬럼이 Empty이어야 함. --> Sqlite에 Field 없어 Error 발생
