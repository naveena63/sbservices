package com.app.sb.sbservices.Menu;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.app.sb.sbservices.GuestLoginActivity;
import com.app.sb.sbservices.Profile.ProfileActivity;
import com.app.sb.sbservices.R;
import com.app.sb.sbservices.ReferalId.EarnCreditsActivity;
import com.app.sb.sbservices.ReferalId.GetReferalCode;
import com.app.sb.sbservices.Utils.PrefManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    View rootView;
    GoogleApiClient mGoogleApiClient;
    PrefManager prefManager;
    TextView editProfile, earnCredits, referalID, help, teramNcondtns, rateUs, Faqs, privacyPolicy, logout, userEmail, userName;
 private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static Fragment newInstance(String s, String s1) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, s);
        args.putString(ARG_PARAM2, s1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        prefManager = new PrefManager(getContext());
        editProfile = rootView.findViewById(R.id.editProfile);
        earnCredits = rootView.findViewById(R.id.earnCredits);
        referalID = rootView.findViewById(R.id.referealId);
        help = rootView.findViewById(R.id.help);
        teramNcondtns = rootView.findViewById(R.id.terms);

        rateUs = rootView.findViewById(R.id.rateUs);
        Faqs = rootView.findViewById(R.id.faqs);
        privacyPolicy = rootView.findViewById(R.id.privacyPolicy);
        logout = rootView.findViewById(R.id.logout);
        userEmail = rootView.findViewById(R.id.userEmail);
        userName = rootView.findViewById(R.id.usernameTextView);

        userName.setText(prefManager.getUsername());
        userEmail.setText(prefManager.getEmailId() + "|" + prefManager.getPhoneNumber());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);

                startActivity(intent);


            }
        });
        earnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EarnCreditsActivity.class);

                startActivity(intent);
            }
        });
        teramNcondtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);

                startActivity(intent);
            }
        });
        referalID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GetReferalCode.class);
                startActivity(intent);

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "com.app.sb.sbservices"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });
        Faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FqsActivity.class);
                startActivity(intent);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrivacypolicyActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.logout();
                Intent loginscreen = new Intent(getActivity(), GuestLoginActivity.class);
                loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginscreen);
                LoginManager.getInstance().logOut();

               /* Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),GuestLoginActivity.class);
                                startActivity(i);
                            }
                        });
                getActivity().finish();
*/
            }
        });

        return rootView;
    }


}
