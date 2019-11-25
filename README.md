# EJPropertySDKDemo
### V1.2.x 版本更新说明
   - 新增: 投诉表扬模块(ThirdPartyManager.getInstance().navigation(Navigation.COMPLAINT_PRAISE_MAIN));
   - 新增: 巡检管理模块(ThirdPartyManager.getInstance().navigation(Navigation.INSPECTION_MAN));
   - 新增: 高德地图接入（所需3D地图、定位SDK）,[SDK相关下载](https://lbs.amap.com/api/android-sdk/download)
   - 新增: uses-permission权限配置,具体请查看[【AndroidManifest.xml】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/AndroidManifest.xml)
   
#### **EJPropertySDKDemo 库使用示例**：（SDK接入(**minSdkVersion    : 21**)）

#### 一、 添加本地仓库 Gradle依赖
### 1、复制[SDKProperty](https://github.com/scalling/EJPropertySDKDemo/blob/master/SDKProperty)到项目工程跟目录
### 2、根目录build.gradle加入以下代码
```
    allprojects {
        repositories {
            google()
            jcenter()
            maven { url "https://jitpack.io" }
            maven { url uri("${rootProject.projectDir}/SDKProperty")}
        }
    }
```
### 3、引用项目
```
dependencies {
   implementation 'com.eju.housekeeper:sdk:1.2.0'
}
```
#### 二、在项目中添加如下代码

##### 1、需继承extends Application implements App[【示例BaseApplication.java】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/BaseApplication.java)在里面进行初始化工作(直接复制就可),[AndroidManifest.xml](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/MainActivity.java) application name需要继承自定义的[BaseApplication](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/BaseApplication.java)
```
public class BaseApplication extends Application implements App {
    private SdkAppDelegate mAppDelegate;

    /**
     * 这里会在 {@link com.eju.ejpropertysdkdemo.BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null)
            this.mAppDelegate = new SdkAppDelegate(this);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate();
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate();
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
```
```
 <application
        android:name=".BaseApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"/>
```
##### 2、打开日志
```
     //方便调试数据  正式环境下可以不打开如需使用必须在SdkAppDelegate onCreate之前调用
     ThirdPartyManager.openLog(); 
```
##### 3、初始化工具(建议在【Application】进行初始化)
```
    //初始化和设置颜色值越早越好
    ThirdPartyManager.init(this, "10000000")//第二位参数写死"10000000"就行
                   .setThemeColor("#009d8d")//设置主题颜色
                   .setTimeOutInterface(new TimeOutInterface() {
                       @Override
                       public void timeOut() {
                           //登录超时 登录过期 
                       }
                   })
```
##### 4、设置登录信息
```
        //设置第三方memberId 
        ThirdPartyManager.getInstance().setMemberId();
        //设置第三方小区id
        ThirdPartyManager.getInstance().setCommunityId();
        //设置第三方token 
        ThirdPartyManager.getInstance().setAccessToken();
```
```
    //如需要测试则调用:
      ThirdPartyManager.getInstance().test("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxN3NoaWh1aS5jb20iLCJzdWIiOiJBVVRIRU5USUNBVElPTl9KV1QiLCJpc3MiOiJBVVRIX1NFUlZFUiIsImlhdCI6MTU3MTY0Mzg4NiwiZXhwIjoxNTc0MzIyMjg2LCJqdGkiOiJiZWYzYjZjYS1iNGFiLTRlOGMtYWJjNC05OWZkOTAwYjFhYjAiLCJ1aWQiOjQ1MDV9.mPFonW5GQy54THbViOVSF1oMwlSlLuDO-hAg9w2P8Sw");
```
##### 5、跳转
```
    //跳转工单管理
    ThirdPartyManager.getInstance().navigation();
    或者ThirdPartyManager.getInstance().navigation(Navigation.WORK_ORDER_MAN);
    
    //跳转投诉表扬
    ThirdPartyManager.getInstance().navigation(Navigation.COMPLAINT_PRAISE_MAIN);
    
    //跳转巡检管理
    ThirdPartyManager.getInstance().navigation(Navigation.INSPECTION_MAN)
```
##### 6、[**高德地图**](https://lbs.amap.com/)接入【可引用本工程lib及so文件或去官方下载,[官方SDK相关下载(需要3D地图、定位SDK)](https://lbs.amap.com/api/android-sdk/download)】，添加**lib包**引入本地[**【AMap3DMap_7.1.0_AMapLocation_4.7.2_20191030.jar】**](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/libs),**build.gradle**引入本地[**【so文件】**](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/jniLibs)(**一定要引入so文件，否则地图黑屏**),在[AndroidManifest.xml](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/AndroidManifest.xml)配置【apikey】

```
 implementation files('libs/AMap3DMap_7.0.0_AMapLocation_4.7.0_20190924.jar')
```
```
 ndk {
            //选择要添加的对应cpu类型的.so库。
            abiFilters 'armeabi', 'armeabi-v7a'
        }
```
```
 <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="申请的apikey" /> 
```
示例[【MainActivity.java】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/MainActivity.java)


#### 三、[【AndroidManifest.xml相关配置】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/AndroidManifest.xml)

##### 1、相关权限
```
   <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> <!-- 地图包、搜索包需要的基础权限 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" /> <!-- 定位包、导航包需要的额外权限（注：基础权限也需要） -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```
##### 2、需提供文件权限[【file_paths.xml】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/res/xml/file_paths.xml)的配置
```
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="包名xxx.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
```

```
    <paths>
        <external-path
            name="my_images"
            path="Pictures"/>
    </paths>
```
#### 四、如果使用到了混淆请加入以下内容(如有问题麻烦联系我)
```
  -dontwarn com.eju.housekeeper.**
  -keep public class * implements com.jess.arms.integration.ConfigModule
  -keep public class com.eju.housekeeper.app.**{*;}
  -keep public class com.eju.housekeeper.commonsdk.**{*;}
  -keep public class com.eju.housekeeper.housekeeper.**{*;}
  -keep public class com.eju.housekeeper.net.**{*;}
  -keep public class com.eju.housekeeper.inspection{*;}
  -keep public class com.eju.housekeeper.workorder{*;}
  
  ################arouter#################
  -keep public class com.alibaba.android.arouter.routes.**{*;}
  -keep public class com.alibaba.android.arouter.facade.**{*;}
  -keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
  ################arouter#################
  ################高德地图#################
  #3D 地图 V5.0.0之前：
  -keep   class com.amap.api.maps.**{*;}
  -keep   class com.autonavi.amap.mapcore.*{*;}
  -keep   class com.amap.api.trace.**{*;}
  
  #3D 地图 V5.0.0之后：
  -keep   class com.amap.api.maps.**{*;}
  -keep   class com.autonavi.**{*;}
  -keep   class com.amap.api.trace.**{*;}
  
  #定位
  -keep class com.amap.api.location.**{*;}
  -keep class com.amap.api.fence.**{*;}
  -keep class com.autonavi.aps.amapapi.model.**{*;}
  
  #搜索
  -keep   class com.amap.api.services.**{*;}
  
  #2D地图
  -keep class com.amap.api.maps2d.**{*;}
  -keep class com.amap.api.mapcore2d.**{*;}
  
  #导航
  -keep class com.amap.api.navi.**{*;}
  -keep class com.autonavi.**{*;}
   ################高德地图#################
```


### 具体相关用法请查看demo[点击下载 EJPropertySDKDemo.apk](https://fir.im/qrle)
