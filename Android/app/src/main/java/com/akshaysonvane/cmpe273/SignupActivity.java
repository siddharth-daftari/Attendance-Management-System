package com.akshaysonvane.cmpe273;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity
{
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_fname)
    EditText _fNameText;
    @Bind(R.id.input_lname)
    EditText _lNameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_studentid)
    EditText _studentId;
    @Bind(R.id.btn_signup)
    Button _signupButton;

    SharedPreferences.Editor editor;
    SharedPreferences cmpe273prefs;

    String displayPicUrl = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        _fNameText.setText(intent.getStringExtra("firstName"));
        _lNameText.setText(intent.getStringExtra("lastName"));
        _emailText.setText(intent.getStringExtra("email"));
        displayPicUrl = intent.getStringExtra("displayPicUrl");
        Toast.makeText(this, displayPicUrl, Toast.LENGTH_LONG).show();


        _signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signup();
            }
        });
    }

    public void signup()
    {
        Log.d(TAG, "Signup");

        if (!validate())
        {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        String firstName = _fNameText.getText().toString();
        String lastName = _lNameText.getText().toString();
        String emailId = _emailText.getText().toString();
        String studentId = _studentId.getText().toString();
        String mac = getMacAddr();

        Toast.makeText(this, mac, Toast.LENGTH_LONG);


        if (checkNetworkConnectivity())
        {
            if (registerStudent())
            {
                storeLoginData(firstName.trim(), lastName.trim(), emailId.trim(), studentId.trim(), mac);

                onSignupSuccess();
                progressDialog.dismiss();

                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Connection Error. Try again Later.", Toast.LENGTH_SHORT).show();
            }
        }

//        new android.os.Handler().postDelayed(
//                new Runnable()
//                {
//                    public void run()
//                    {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    public void onSignupSuccess()
    {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate()
    {
        boolean valid = true;

        String fName = _fNameText.getText().toString();
        String lName = _lNameText.getText().toString();
        String email = _emailText.getText().toString();
        String studentId = _studentId.getText().toString();

        if (fName.isEmpty() || fName.length() < 3)
        {
            _fNameText.setError("at least 3 characters");
            valid = false;
        }
        else
        {
            _fNameText.setError(null);
        }

        if (lName.isEmpty())
        {
            _lNameText.setError("Enter Valid Name");
            valid = false;
        }
        else
        {
            _lNameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailText.setError("Enter a valid email address");
            valid = false;
        }
        else
        {
            _emailText.setError(null);
        }

        if (studentId.isEmpty() || studentId.length() != 9)
        {
            _studentId.setError("Enter Valid Student ID");
            valid = false;
        }
        else
        {
            _studentId.setError(null);
        }

        return valid;
    }

    private void storeLoginData(String firstName, String lastName, String emailId, String studentId, String mac)
    {
        cmpe273prefs = getSharedPreferences("cmpe273", Context.MODE_PRIVATE);

        editor = cmpe273prefs.edit();
        editor.putBoolean("userLogged", true);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("emailId", emailId);
        editor.putString("studentId", studentId);
        editor.putString("mac", mac);
        editor.putString("displayPicUrl", displayPicUrl);
        editor.commit();
    }

    private boolean registerStudent()
    {
        return true;
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

    public String getMacAddr()
    {
        try
        {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all)
            {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null)
                {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes)
                {
                    String str = Integer.toHexString(b & 0xFF);
                    if (str.length() == 1)
                    {
                        str = "0" + str;
                    }
                    res1.append(str + ":");
                }

                if (res1.length() > 0)
                {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        }
        catch (Exception ex)
        {
        }
        return "c0:ee:fb:30:09:e8";
    }
}