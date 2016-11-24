package com.akshaysonvane.cmpe273;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginIntermediateActivity extends AppCompatActivity
{
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etStudentId;
    private EditText etEmailId;
    private Button btnRegister;

    String firstName;
    String lastName;
    String studentId;
    String emailid;
    String mac;

    SharedPreferences.Editor editor;
    SharedPreferences cmpe273prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_intermediate);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etStudentId = (EditText) findViewById(R.id.etStudentId);
        etEmailId = (EditText) findViewById(R.id.etEmailId);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        final Intent intent = getIntent();
        etFirstName.setText(intent.getStringExtra("firstName"));
        etLastName.setText(intent.getStringExtra("lastName"));
        etEmailId.setText(intent.getStringExtra("email"));

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (checkNetworkConnectivity())
                {
                    if (registerStudent())
                    {
                        storeLoginData(etFirstName.getText().toString().trim(), etLastName.getText().toString().trim(), etEmailId.getText().toString().trim(), etStudentId.getText().toString().trim(), "");

                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Connection Error. Try again Later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void storeLoginData(String firstName, String lastName, String emailId, String studentId, String mac)
    {
        cmpe273prefs = getSharedPreferences("cmpe273", Context.MODE_PRIVATE);

        editor = cmpe273prefs.edit();
        editor.putBoolean("userLogged", true);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("emailid", emailId);
        editor.putString("studentId", studentId);
        editor.putString("mac", mac);
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
}
