몰입캠프 1주차 프로젝트
============================
- 프로젝트 이름: 베이비로이드(Babyroid)
- 팀원: 김동하, 송지효
- APK : Babyroid.apk 설치

  
프로젝트 설명
-------------------
Jetpack Compose를 이용해 모든 레이아웃을 구성했습니다.  
기존에 설치되는 연락처, 갤러리 앱을 참고하여 비슷하게 구성해보려고 시도했습니다.  
Compose를 이용해 다크/라이트 모드를 추가로 구현했습니다.  

https://github.com/madcamp12/madcamp-week1/assets/64480531/f2f0bd3c-7012-4914-8cd4-3ea7a22dea58


- Tab 1
  
  휴대폰의 연락처를 가져와 화면에 표시해줍니다.  
  기존 연락처에 프로필 사진이 존재한다면, 이를 비트맵으로 표현해줍니다.  
  연락처를 터치하면 '전화/문자/정보'를 볼 수 있는 레이아웃이 추가로 나타납니다. 전화, 문자 등의 기존 앱으로 이동할 수 있습니다.  
  연락처가 몇 개나 표시되는지 보여주고 검색어를 입력할 때마다 동적으로 연락처 리스트가 달라집니다.
  ![캡처](https://github.com/madcamp12/madcamp-week1/assets/64480531/15c98add-533f-403e-a87a-4c089e3e394d)
  ![2](https://github.com/madcamp12/madcamp-week1/assets/64480531/336f7d26-9086-49e1-bc62-a1bc564d754d)


- Tab 2

  자신의 갤러리에서 좋아하는 사진을 등록할 수 있습니다.  
  휴대폰의 갤러리에 접근해 사진을 추가/삭제 할 수 있고, 확대해서 볼 수 있습니다.  
  앱의 내부 저장소를 이용해 파일을 저장하고, 이를 그려주는 방식으로 구현했습니다.
  ![KakaoTalk_20240103_175524944_01](https://github.com/madcamp12/madcamp-week1/assets/64480531/a265c713-20e3-42b3-83ff-7a36e932be2b)


- Tab 3

  간단한 제비뽑기를 할 수 있는 탭입니다.  
  최소 1명부터, 최대 10명까지 자유롭게 인원을 확대/축소하며 제비뽑기를 할 수 있습니다.  
  기본 값은 "후보 {숫자}"의 형식이며, 이 값은 아무 텍스트도 입력하지 않은 경우에 활용됩니다.
  
  https://github.com/madcamp12/madcamp-week1/assets/64480531/bf7f7d0b-14a5-4876-8445-a8914196ca78


    
