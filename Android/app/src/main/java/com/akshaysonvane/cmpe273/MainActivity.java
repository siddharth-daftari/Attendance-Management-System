package com.akshaysonvane.cmpe273;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshaysonvane.cmpe273.adapters.RestAdapterClass;
import com.akshaysonvane.cmpe273.api.ConnectionApi;
import com.akshaysonvane.cmpe273.model.ResponseModel;
import com.akshaysonvane.cmpe273.utils.CircleTransform;
import com.akshaysonvane.cmpe273.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.akshaysonvane.cmpe273.utils.Utils.ServerUUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener
{

    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket bluetoothSocket = null;
    private static InputStream mInputStream;
    private static OutputStream mOutputStream;

    private TextView snvName;
    private TextView snvEmail;

    private ImageView imgDisplayPic;

    private String PI_MAC;

    SharedPreferences cmpe273prefs;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Synergy");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                connectToPi(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        initUI();

        View header = navigationView.getHeaderView(0);
        snvName = (TextView) header.findViewById(R.id.snvName);
        snvEmail = (TextView) header.findViewById(R.id.snvEmail);
        imgDisplayPic = (ImageView) header.findViewById(R.id.imageView);

        setNavBarDetails();

        this.PI_MAC = Utils.PI_MAC;
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            getSharedPreferences("cmpe273", Context.MODE_PRIVATE).edit().clear().apply();

            startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        Fragment fragment = null;
        Class fragmentClass = FirstFragment.class;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_status)
        {
            fragmentClass = FirstFragment.class;
        }
        else if (id == R.id.nav_info)
        {
            fragmentClass = SecondFragment.class;
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void connectToPi(View view)
    {
        this.view = view;
        if (isBluetoothEnabled())
        {
            getLeader();
            //new ConnectPi().execute();
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(), R.string.enable_bluetooth, Toast.LENGTH_SHORT).show();
                mBluetoothAdapter.enable();
                getLeader();
                //new ConnectPi().execute();
            }
        }


    }

    public void write(byte[] bytes)
    {
        try
        {
            mOutputStream.write(bytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isBluetoothEnabled()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    public void setNavBarDetails()
    {
        cmpe273prefs = getSharedPreferences("cmpe273", Context.MODE_PRIVATE);
        String name = cmpe273prefs.getString("firstName", "John") + "  " + cmpe273prefs.getString("lastName", "Doe");
        snvName.setText(name);
        snvEmail.setText(cmpe273prefs.getString("emailId", "john.doe@gmail.com"));
        Picasso.with(this).load(cmpe273prefs.getString("displayPicUrl", "http://i1.wp.com/www.techrepublic.com/bundles/techrepubliccore/images/icons/standard/icon-user-default.png")).transform(new CircleTransform()).into(imgDisplayPic);
    }

    public void initUI()
    {
        Fragment fragment = null;
        Class fragmentClass = FirstFragment.class;

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }


    public void getLeader()
    {
        ConnectionApi connectionApi = new RestAdapterClass().getApiClassObject();

        connectionApi.getLeader(new Callback<ResponseModel>()
        {
            @Override
            public void success(ResponseModel responseModel, Response response)
            {
                if (responseModel != null)
                {
                    if (responseModel.getResult().equalsIgnoreCase("true"))
                    {
                        PI_MAC = responseModel.getData();

                        new ConnectPi().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplication(), "Server error, falling back to defaults.", Toast.LENGTH_LONG).show();
                        PI_MAC = Utils.PI_MAC;
                        new ConnectPi().execute();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getApplication(), "Cannot connect to server, falling back to defaults.", Toast.LENGTH_LONG).show();
                PI_MAC = Utils.PI_MAC;
                new ConnectPi().execute();
            }
        });
    }

    private class ConnectPi extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... aVoid)
        {
            try
            {
                Snackbar.make(view, "Connecting to PI", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(PI_MAC);
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(ServerUUID));
                Method method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                bluetoothSocket = (BluetoothSocket) method.invoke(device, 1);
                bluetoothSocket.connect();
                mOutputStream = bluetoothSocket.getOutputStream();
                mInputStream = bluetoothSocket.getInputStream();

                mBluetoothAdapter.cancelDiscovery();

                if (bluetoothSocket != null)
                {
                    Snackbar.make(view, "Connected to PI", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //Toast.makeText(MainActivity.this, "Connected to PI", Toast.LENGTH_SHORT).show();
                }

                cmpe273prefs = getSharedPreferences("cmpe273", Context.MODE_PRIVATE);
                String name = cmpe273prefs.getString("firstName", "John") + " " + cmpe273prefs.getString("lastName", "Doe");
                byte[] bytes = name.getBytes();

                write(bytes);  //sending data to pi

                //Toast.makeText(getApplicationContext(), "Attendance Marked Successfully", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "Attendance Marked Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            catch (Exception e)
            {
                Snackbar.make(view, "Connection failed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                e.printStackTrace();
                return false;
            }

            //closeProcessDialog();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            try
            {
                bluetoothSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
