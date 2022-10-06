# AdConnection Android SDK Guide (V1.1.3)

**효율적인 이미지 관리를 위해 SDK에 Glide 라이브러리가 포함되어 있습니다**

### -  프로젝트 설정  (minSDK 21)


1. 제공된 AdConnection SDK AAR 파일을 app/libs 에 넣어줍니다.

![스크린샷 2022-04-14 오후 5 04 25](https://user-images.githubusercontent.com/103635743/163358380-c78ad268-9905-415c-9ba4-9ebbed6040b9.png)

<br/>

2. *java 프로젝트인 경우* kotlin 사용을 위해 
project 단위 build.gradle과, app 단위 build.gradle 내에 아래와 같이 코드를 추가합니다.

<br/>

**project > build.gradle**

![스크린샷 2022-04-14 오후 5 11 29](https://user-images.githubusercontent.com/103635743/163358839-7cd7825f-05a4-407b-870b-cb5a261278b3.png)

```c
classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31"
```

<br/>

**app > build.gradle**

![스크린샷 2022-04-14 오후 5 11 42](https://user-images.githubusercontent.com/103635743/163358891-39560af3-e5e7-4618-b22a-48e1b55be636.png)

```c
id 'kotlin-android'
```

<br/>

3.  app 단위 build.gradle 내에 아래와 같이 두줄을 추가합니다.

![스크린샷 2022-04-14 오후 5 07 51](https://user-images.githubusercontent.com/103635743/163358540-e625c490-5da1-4c7a-9a9c-fbd43a7729eb.png)

```c
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
implementation files('libs/adconnection-sdk-1.1.2-release.aar')
```

<br/>

4. 광고 사용을 위한 Manifest  퍼미션 추가

```c
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="com.google.android.gms.permission.AD_ID" />
```


<br/><br/>



### - 광고 사용을 위한 커넥터 설정

1. 각 라이프사이클 주기마다 아래와 같이 AdConnector 를 호출해줍니다.

```c
lateinit var adConnector: AdConnector

override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.layout_banner)

   adConnector = AdConnector(this, "발급받은 AdConnection 미디어키")
}

override fun onResume() {
   super.onResume()
   if(adConnector!= null) {
       adConnector.resume(this)
   }
}

override fun onPause() {
   if(adConnector!= null) {
       adConnector.pause(this)
   }
   super.onPause()
}

override fun onDestroy() {
   if(adConnector!= null) {
       adConnector.destroy(this)
   }
   super.onDestroy()
}
```

<br/>

### - 미디에이션 배너 요청

1. 쿠팡 광고 요청을 위한 클래스를 바인딩 합니다.

```c
adConnector.bindPlatform("COUPANG", "one.adconnection.sdk.sample.ads.SubAdViewCoupang")
```

- 첫번째 parameter 플랫폼명 “COUPANG” 은 변경하시면 안됩니다.
- 두번째 parameter : 실제 SubAdViewCoupang 클래스가 위치한 프로젝트 경로로 변경해주세요
- 쿠팡 광고 사용을 위한 프로젝트 설정은 쿠팡 가이드에 따라 추가해주시기 바랍니다.
- 쿠팡 광고 widget id는 SubAdViewCoupang 파일에서 설정해주세요

<br/>


2. 광고뷰를 보여줄 AdBanner 뷰를 바인딩 합니다.

```c
adConnector.bindAdBannerView(findViewById(R.id.container))
```

<br/>

3. 광고 요청

```c
val listener: AdConnectorListener = object : AdConnectorListener {

   override fun onReceiveAd(platform: String?) {
	// 광고 수신 성공시 호출됩니다. plaform : 수신 성공한 광고 플랫폼명
       Log.d("ADConnection", "onReceiveAd ")
   }

   override fun onFailedToReceiveAd(error: String?) {
	// 광고 수신 실패시 호출됩니다. AdConnection 광고가 아닌 타 플랫폼 광고는 
	// SubAdView에서 자세한 실패 로그를 확인할 수 있습니다.
       Log.d("ADConnection", "onFailedToReceiveAd : $error")
   }
}

if (adConnector != null) adConnector.requestBanner(AdSize.BANNER_320X100, listener)
```

|AdSize enum|배너 크기|
|---|---|
|AdSize.BANNER_320X50|320 x 50 배너|
|AdSize.BANNER_320X100|320 x 100 배너|
|AdSize.BANNER_320X250|320 x 250 배너|

<br/>
<br/>

### - 네이티브 광고 요청

1. 광고 요소 확인
<img width="407" alt="스크린샷 2022-06-28 오후 5 05 19" src="https://user-images.githubusercontent.com/103635743/176127792-3b928f4f-88c2-4ef1-84c1-7369d10d25ea.png">

<br/>

2. xml 정의 - 광고를 보여줄 layout을 아래 예시와 같이 정의합니다.

```c
<RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/native_ad_icon" .../>

        <TextView
            android:id="@+id/native_ad_title" .../>

    </RelativeLayout>

    <TextView
        android:id="@+id/native_ad_description" .../>

    <ImageView
        android:id="@+id/native_ad_main"  .../>

    <Button
        android:id="@+id/native_ad_button" .../>

</RelativeLayout>
```

<br/>

3. NativeAdViewBinder 객체를 생성하여 xml에 정의한 Layout id와 광고 요소를 바인딩 합니다.
   (Layout Id, title, icon은 필수 요소입니다.)

```c
val viewBinder: NativeAdViewBinder = NativeAdViewBinder.Builder(
            R.layout.native_ad_template,
            R.id.native_ad_title,
            R.id.native_ad_icon
        )
            .setDescriptionId(R.id.native_ad_description)
            .setMainImageId(R.id.native_ad_main)
            .setButtonId(R.id.native_ad_button)
            .build()
```

<br/>

4. 광고 요청

```c
val listener: NativeResultListener = object : NativeResultListener {

            override fun onReceiveAd(nativeView: NativeAdView) {
                // 광고 수신 성공시 호출됩니다. 광고요소가 그려진 FrameLayout이 return
            }

            override fun onError(errorCode: Int) {
	    	// 광고 수신 실패시 호출됩니다.
                Log.d("AdConnection", "[Native] onFailedToReceiveAd : $errorCode")
            }
        }

        if (adConnector != null) adConnector.requestNativeAd(viewBinder, listener)
```

<br/>
