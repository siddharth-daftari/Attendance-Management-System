package com.akshaysonvane.cmpe273;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;

    SharedPreferences cmpe273prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkUserLog();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (checkNetworkConnectivity())
                {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });
    }

    public void checkUserLog()
    {
        cmpe273prefs = getSharedPreferences("cmpe273", Context.MODE_PRIVATE);

        if (cmpe273prefs.getBoolean("userLogged", false))
        {
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        //Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, acct.getDisplayName() + " " + acct.getEmail() + " " + acct.getPhotoUrl(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginIntermediateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("firstName", acct.getGivenName());
            intent.putExtra("lastName", acct.getFamilyName());
            intent.putExtra("email", acct.getEmail());
            startActivity(intent);
            //updateUI(true);
        }
        else
        {

            Toast.makeText(this, "unauthenticated" + result.getStatus(), Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean checkNetworkConnectivity()
    {
        if (isNetworkAvailable())
        {
            return true;
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setTitle("Network Error")
                    .setMessage("No internet connectivity.")
                    .setCancelable(false)
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            System.exit(0);
                        }
                    })
                    .show();
        }

        return false;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
