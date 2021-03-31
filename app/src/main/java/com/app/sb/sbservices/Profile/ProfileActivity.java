package com.app.sb.sbservices.Profile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.Utils.ApiCallingFlow;
import com.app.sb.sbservices.Utils.AppConstants;
import com.app.sb.sbservices.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    View view;
    PrefManager prefManager;
    public static final String KEY_USERID = "user_id";
    public static final String KEY_MOBILE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    Button updateProfile;
    CircleImageView editiImageView;
    EditText et_name, et_mobile, et_mail,et_address;
    private static final int PICK_IMAGE_REQUEST = 0;
    private final String TAG = "Main Activity";

    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        editiImageView = findViewById(R.id.editiImageView);
        prefManager = new PrefManager(this);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mImageUri = preferences.getString("image", null);
        et_mail = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        updateProfile = findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestServiceApi();
            }
        });
        et_name.setText(prefManager.getUsername());
        et_mail.setText(prefManager.getEmailId());
        et_mobile.setText(prefManager.getPhoneNumber());
        et_address.setText(prefManager.getLoctaion());

        editiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        if (mImageUri != null) {
            editiImageView.setImageURI(Uri.parse(mImageUri));
        } else {
            editiImageView.setImageResource(R.drawable.upload);
        }

    }
    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {

                    // This is the key line item, URI specifies the name of the data
                    mImageUri = data.getData();

                    // Saves image URI as string to Default Shared Preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("image", String.valueOf(mImageUri));
                    editor.commit();

                    // Sets the ImageView with the Image URI
                    editiImageView.setImageURI(mImageUri);
                    editiImageView.invalidate();
                }
            }
        }
    }






    private void requestServiceApi() {

        RelativeLayout parentLayout = (RelativeLayout)findViewById(R.id.relative_layout);

        ApiCallingFlow apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            editProfile();
        }
    }

    private void editProfile() {
        final String userName = et_name.getText().toString();
        final String email = et_mail.getText().toString();
        final String phonenumber = et_mobile.getText().toString();
        final String address = et_address.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SAVE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String myResponce = jsonObject.getString("status");

                            if (myResponce.equals("success")) {

                                Toast.makeText(ProfileActivity.this, "Profile Successfulley updated", Toast.LENGTH_SHORT).show();
                                prefManager.setPhoneNumber(phonenumber);
                                prefManager.setEmailId(email);
                                prefManager.setUsername(userName);
                                prefManager.setLoctaion(address);

                            } else if (myResponce.equals("error"))
                            {


                            }else  if (myResponce.equalsIgnoreCase("0")){
                                Toast.makeText(ProfileActivity.this, "Phone number required for payment", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("log", "_-------------- Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                        Log.i("uyt", "_----" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_MOBILE, phonenumber);
                map.put(KEY_EMAIL, email);
                map.put(KEY_NAME, userName);
                map.put("address", address);
                map.put(KEY_USERID, prefManager.getUserId());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
