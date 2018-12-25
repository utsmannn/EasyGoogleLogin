# Easy Google Login
### Simplify Firebase Authenticate Google Sign In

 [ ![Download](https://api.bintray.com/packages/kucingapes/utsman/com.utsman/images/download.svg) ](https://bintray.com/kucingapes/utsman/com.utsman/_latestVersion)
 
 ## Setup google plugin and download library
 ### 1. Register your app in firebase auth
- Create new project in firebase console
- Add your app and register with SHA-1 in setting project
- Download copy ```google-services.json``` to root folder ```app```
- Enable Google Sign-in method in authentication

### 2. Add plugin ```google-services```
- Add this code your build.gradle files in ```depedencies``` (project level) to use the plugin
```gradle
classpath 'com.google.gms:google-services:4.2.0'
```
Like this
```gradle
buildscript {
  dependencies {
    ....
    // Add this line
    classpath 'com.google.gms:google-services:4.2.0'
  }
}
```

### 2. Apply plugin ```google-services``` in root build.gradle (app level)
```gradle
apply plugin: 'com.google.gms.google-services'
```
Like this
```gradle
apply plugin: 'com.android.application'
....
// add this line
apply plugin: 'com.google.gms.google-services'
```

### 3. Download this library, add in ```dependencies``` in build.gradle (app level)
```gradle
implementation 'com.utsman:easygooglelogin:1.0.5'
```

## Setup Google Sign-In
Define class ```EasyGoogleLogin``` and setup init in ```onCreate```
```java
private EasyGoogleLogin googleLogin;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // define class EasyGoogleLogin
    googleLogin = new EasyGoogleLogin(this);
    
    // setup
    String token = getString(R.string.default_web_client_id) // generate token google services
    googleLogin.initGoogleLogin(token, listener)
    
    // call login in button
    Button btnLogin = findViewById(R.id.btn_login)
    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        
            // start login
            googleLogin.signIn(context);
        }
    });
}
```

Override ```onStart``` and set ```initOnStart```
```java
@Override
protected void onStart() {
    super.onStart();
    // add this line
    googleLogin.initOnStart();
}
```

Last one, override ```onActivityResult```<br>
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // add this line
    googleLogin.onActivityResult(this, requestCode, data);
}
```

## Listener
Write ui changing in listener. Set listener with ```LoginResultListener()```. Setup like this
```java
googleLogin.initGoogleLogin(token, new LoginResultListener() {
    @Override
    public void onLoginSuccess(FirebaseUser user) {
    }

    @Override
    public void onLoginFailed(Exception exception) {
    }

    @Override
    public void onLogoutSuccess(Task<Void> task) {
    }

    @Override
    public void onLogoutError(Exception exception) {
    }
});
```

Or implement your activity with listener
```java
public class MainActivity extends AppCompatActivity implements LoginResultListener
```
Override ```onLoginSuccess```, ```onLoginFailed```, ```onLogoutSuccess``` and ```onLogoutError```<br>
And change your setup on ```onCreate``` like this
```java
googleLogin.initGoogleLogin(token, this) // `this` for LoginResultListener
```

## Logout and Revoke
Logout with
```java
googleLogin.signOut(context);
```

Revoke with
```java
googleLogin.revokeAccess(context);
```

## Google Sign In Button (Optional)
For add button sign in with style, download ```firebase-ui``` in depedencies
```gradle
implementation 'com.firebaseui:firebase-ui-auth:4.3.0'
```
After sync, add this xml
```xml
<com.google.android.gms.common.SignInButton
    android:id="@+id/sign_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
And Cast to ```SignInButton```
```java
SignInButton signInButton = findViewById(R.id.sign_button);
```

![](https://i.ibb.co/GQcYS9D/image.png)

## Simple example
[```MainActivity.java```](https://github.com/utsmannn/EasyGoogleLogin/blob/master/app/src/main/java/com/utsman/googlelogin/MainActivity.java#L25)

---

### License
[LICENSE](/LICENSE)
