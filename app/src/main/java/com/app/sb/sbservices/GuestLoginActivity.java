package com.app.sb.sbservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.HomeBanners.Banner;
import com.app.sb.sbservices.HomeBanners.GuestBanners;
import com.app.sb.sbservices.User.LoginActivity;
import com.app.sb.sbservices.User.RegisterActivity;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import com.app.sb.sbservices.Utils.SharedPreference;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;

public class GuestLoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    CallbackManager callbackManager;
    private TextView fbName,fbEmail;
    private ViewPager mPager;
    private int currentPage;

    ProgressDialog progressBar;
    private List<Banner> stringList;
    PrefManager prefManager;
    private ImageView imgProfilePic, profile_pic;
    String email, last_name, first_name, id, personName, googleEmail, personPhotoUrl, fbProfileImage;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 00;
    LoginButton FbloginButton;
    private CircleImageView circleImageView;
    Button loginButton, registerButton, mobilenumButn;
    CheckBox check_checkbox;
    RequestQueue requestQueue;
    Context context;
    private static final int GRANT_LOC_ACCESS = 124;

    public static final int RequestPermissionCode = 7;
    //google variables
    private TextView txtName, txtEmail;
    private static final int RC_SIGN_IN = 007;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        getSupportActionBar().hide();
        callbackManager = CallbackManager.Factory.create();
        prefManager = new PrefManager(this);
       progressBar=new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        //google+
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);
        if(CheckingPermissionIsEnabledOrNot())
        {
            Toast.makeText(GuestLoginActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            RequestMultiplePermission();
        }

        // fb login
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.sb.sbservices",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {
        }
        FbloginButton = findViewById(R.id.login_button);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        mobilenumButn = findViewById(R.id.mobileNumber);
        check_checkbox = findViewById(R.id.check_checkbox);
        fbName = findViewById(R.id.profile_name);
        fbEmail = findViewById(R.id.profile_email);
        circleImageView = findViewById(R.id.profile_pic);

        FbloginButton.setPermissions(Arrays.asList("email", "public_profile"));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {



                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(GuestLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.d("errorreposnse", "" + exception);
                            Toast.makeText(GuestLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        mobilenumButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_checkbox.isChecked()) {
                    Intent intent = new Intent(GuestLoginActivity.this, GuestOtpActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else {
                    Toast.makeText(GuestLoginActivity.this, "Please agree Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        check_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check_checkbox.isChecked()) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            GuestLoginActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("terms and conditions");

                    // Setting Dialog Message
                    alertDialog.setMessage(" In order to use the Service, you must first agree to the Terms. You may not use the Service if you do not accept the Terms." +
                            "You can accept the Terms by clicking to accept or agree to the Terms, where this option is made available to you by Google.\n" +
                            "You may not use the Service and may not accept the Terms if you are not of legal age to form a binding contract with Google.");


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            alertDialog.dismiss();

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else {
                }
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(), "MontserratAlternates-Medium.otf");
        loginButton.setTypeface(typeface);
        registerButton.setTypeface(typeface);
        mobilenumButn.setTypeface(typeface);
        check_checkbox.setTypeface(typeface);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_checkbox.isChecked()) {
                    Intent intent = new Intent(GuestLoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GuestLoginActivity.this, "Please accept the privacy policy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_checkbox.isChecked()) {


                    Intent intent = new Intent(GuestLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GuestLoginActivity.this, "Please accept the privacy policy", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSliderImages();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(GuestLoginActivity.this, new String[]
                {
                        CAMERA,
                        RECORD_AUDIO,
                        SEND_SMS,
                        GET_ACCOUNTS,
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean SendSMSPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean GetAccountsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocationPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && RecordAudioPermission && SendSMSPermission && GetAccountsPermission&&GetLocationPermission) {

                        Toast.makeText(GuestLoginActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                      //  Toast.makeText(GuestLoginActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int FifthPemissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
        FifthPemissionResult == PackageManager.PERMISSION_GRANTED ;
    }


    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("fbresponse", "fbresponse" + object);
                    first_name = object.getString("first_name");
                    last_name = object.getString("last_name");
                    email = object.optString("email");
                    id = object.getString("id");
                    fbProfileImage = "https://graph.facebook.com/" + id + "/picture?type=normal";
                    prefManager.storeValue(AppConstants.FACEBOOK_App_ID, id);
                    prefManager.setFacebookAppId(id);
                    fbEmail.setText(email);
                    fbName.setText(first_name);
                    Log.e("fbmail", "fbmail" + email);
                    Log.e("fbname", "fbname" + first_name);
                    Log.e("fbname", "fbname" + last_name);
                    Log.e("fbid", "fbid" + id);

                   /* RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(getApplicationContext()).load(fbProfileImage).into(circleImageView);*/
                     facebookLogin();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void facebookLogin() {
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.FACEBOOK_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("fb Response", "fb Response" + response);
                try {
                    progressBar.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        String userId = jsonObject1.getString("user_id");
                        String emial = jsonObject1.getString("email");
                        String uname = jsonObject1.getString("name");
                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, userId);
                        prefManager.setUserId(userId);
                        Log.i("fb Userid", "fbuserId" + userId);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, uname);
                        prefManager.setUsername(uname);
                        Log.i("fb username", "name" + uname);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, emial);
                        prefManager.setEmailId(emial);
                        Log.i("fb emailid", "emial" + emial);

                        Intent intent = new Intent(GuestLoginActivity.this, BottomNavActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(status.equalsIgnoreCase("0")){
                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("email", email);
                Log.i("email", "email" + email);

                map.put("username", first_name + last_name);
                Log.i("username", "username" + first_name + last_name);

                map.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                map.put("fcm_token", SharedPreference.getStringPreference(GuestLoginActivity.this,"TOKEN"));
Log.i("fbtoken","fbtoken"+ SharedPreference.getStringPreference(GuestLoginActivity.this,"TOKEN"));
                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    private void googleLogin() {
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GOOGLE_PLUS_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("google Response", "google Response" + response);
                try {
                    progressBar.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                //    String msg = jsonObject.getString("msg");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        String userId = jsonObject1.getString("user_id");
                        String email = jsonObject1.getString("email");
                     //   String name = jsonObject1.getString("name");

                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, userId);
                        prefManager.setUserId(userId);
                        Log.i("google Userid", "userId" + userId);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, email);
                        prefManager.setEmailId(email);
                        Log.i("google phone", "phone" + email);

                      /*  prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, name);
                        prefManager.setUsername(name);
                        Log.i("google name", "name" + name);*/

                        Intent intent = new Intent(GuestLoginActivity.this, BottomNavActivity.class);
                        startActivity(intent);

                    }
                    else if(status.equalsIgnoreCase("0"))
                    {
                        Intent intent = new Intent(GuestLoginActivity.this, BottomNavActivity.class);
                        startActivity(intent);
                        Toast.makeText(GuestLoginActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(context,"Something Went wrong.. try again",Toast.LENGTH_LONG ).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put("email", googleEmail);
                Log.i("googleplusemail", "email" + googleEmail);

                map.put("name",personName);
                Log.i("googleplususername", "username" + personName);
                map.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                map.put("fcm_token", SharedPreference.getStringPreference(GuestLoginActivity.this,"TOKEN"));
               // map.put("id", "123");
                return map;
            }

        };
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d("response",""+result);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);

                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("googleresponse", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            personName = acct.getDisplayName();
            //personPhotoUrl = acct.getPhotoUrl().toString();
            googleEmail = acct.getEmail();
            prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, personName);
            prefManager.setUsername(personName);
            Log.i("google name", "name" + personName);

            prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, googleEmail);
            prefManager.setEmailId(googleEmail);
            Log.i("google phone", "phone" + googleEmail);

            Log.d("email", "display email: " + acct.getEmail());
            Log.d("name", "display name: " + acct.getDisplayName());
            Log.d("name", "Name: " + personName + ", email: " + email);
            txtName.setText(personName);
            txtEmail.setText(googleEmail);
            Toast.makeText(GuestLoginActivity.this, "success", Toast.LENGTH_SHORT).show();
            googleLogin();

        } else {
            updateUI(false);
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in:
                if (check_checkbox.isChecked()) {
                    signIn();
                } else {
                    Toast.makeText(GuestLoginActivity.this, "Please agree Terms and Conditions", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_sign_out:
                signOut();
                break;
            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("sajdlk", "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                txtName.setText("");
                txtEmail.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(GuestLoginActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
            } else
                loadUserProfile(currentAccessToken);
        }
    };


    private void getSliderImages() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GUEST_BANNERS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("GUESTBANNERS","guest"+response);
                        stringList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("banners");
                                Log.e("banner", String.valueOf(jsonArray));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Log.i("image","guest"+object);
                                    String image = object.getString("banner_image");
                                    Log.i("image","guest"+image);
                                    String imageName = object.getString("banner_name");
                                    Banner banner = new Banner();
                                    banner.setBannerImage(image);
                                    if (imageName != null) {
                                        banner.setImageName(imageName);
                                    } else if (imageName.matches(".")) {
                                        banner.setImageName("Banners");
                                    }
                                    stringList.add(banner);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (stringList.size() > 0) {

                            mPager = findViewById(R.id.pager);
                            mPager.setAdapter(new GuestBanners(getApplicationContext(), stringList));
                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            indicator.setViewPager(mPager);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == stringList.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("token","c0304a62dd289bdc7364fb974c2091f6");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}

