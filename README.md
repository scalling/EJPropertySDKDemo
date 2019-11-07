# EJPropertySDKDemo】
### V1.1.0 版本更新说名
   - 新增: 投诉表扬模块( ThirdPartyManager.getInstance().navigation(Navigation.COMPLAINT_PRAISE_MAIN);
   
   
## EJPropertySDKDemo 库使用示例：（SDK接入(minSdkVersion    : 21)）
#### 一、 添加Jcenter仓库 Gradle依赖
```
dependencies {
   implementation 'com.eju:housekeeper-sdk:1.1.0'
}
```
#### 二、在项目中添加如下代码

##### 1、需继承extends Application implements App[【示例BaseApplication.java】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/BaseApplication.java)在里面进行初始化工作（直接复制就可）
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
##### 3、初始化工具（建议在Application进行初始化）
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
        ThirdPartyManager.getInstance().setMemberId();//设置第三方memberId 
        ThirdPartyManager.getInstance().setCommunityId();//设置第三方小区id
        ThirdPartyManager.getInstance().setAccessToken();//设置第三方token 
        
        如需要测试则调用:
        ThirdPartyManager.getInstance().test("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxN3NoaWh1aS5jb20iLCJzdWIiOiJBVVRIRU5USUNBVElPTl9KV1QiLCJpc3MiOiJBVVRIX1NFUlZFUiIsImlhdCI6MTU3MTY0Mzg4NiwiZXhwIjoxNTc0MzIyMjg2LCJqdGkiOiJiZWYzYjZjYS1iNGFiLTRlOGMtYWJjNC05OWZkOTAwYjFhYjAiLCJ1aWQiOjQ1MDV9.mPFonW5GQy54THbViOVSF1oMwlSlLuDO-hAg9w2P8Sw");
```
##### 5、跳转
```
    //跳转工单管理
    ThirdPartyManager.getInstance().navigation();
    或者ThirdPartyManager.getInstance().navigation(Navigation.WORK_ORDER_MAN);
    
    //跳转投诉表扬
    ThirdPartyManager.getInstance().navigation(Navigation.COMPLAINT_PRAISE_MAIN);
```
示例[【MainActivity.java】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/java/com/eju/ejpropertysdkdemo/MainActivity.java)


#### 三、[【AndroidManifest.xml相关配置】](https://github.com/scalling/EJPropertySDKDemo/blob/master/app/src/main/AndroidManifest.xml)

##### 1、相关权限
```
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.INTERNET" /> 
    <uses-permission android:name="android.permission.RECORD_AUDIO" /> 
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
    <uses-permission android:name="android.permission.READ_PHONE_STATE" /> 
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
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
  -keep public class com.eju.housekeeper.**{*;}
```


### 具体相关用法请查看demo
