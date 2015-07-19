package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;

import com.example.mitchwebster.shopwithfriends.Models.Product;

import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.ProductAdapter;
import com.example.mitchwebster.shopwithfriends.R;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Nav extends Activity implements LocationListener {
    private static final String[] actionTitles = new String[]{
            "Home",
            "My Friends",
            "My Interests",
            "My Products",
            "Register Interest/Product",
            "Add Friends",
            "Map",
            "Log Out" };
    private CharSequence mTitle;
    private final CharSequence mDrawerTitle = "Shop With Friends";

    private static User myUser;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private LocationManager locationManager;
    private static double curLat;
    private static double curLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intent = getIntent();
        String userJson = intent.getStringExtra("myUser");
        Gson g = new Gson();
        myUser = g.fromJson(userJson,User.class);
        List<String> myInterestIDs = myUser.getInterests();
        List<Product> myInterests = new ArrayList<>();
        for (Product p : ConnectionManager.getProducts(getApplicationContext())) {
            if (myInterestIDs.contains(p.getDoc_id())) {
                myInterests.add(p);
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ProductAdapter(this,myInterests));
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, actionTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        startGPS();
        matchedInterest();

    }

    /**
     * Connects to GPS initially and checks if GPS and Network are enabled
     */
    private void startGPS(){
        Location location;
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Please Enable Location Services And Login Again");
                dialog.setPositiveButton("Open Location Services", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        //get gps
                    }
                });
                dialog.setNegativeButton("Back To Login", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                       finish();

                    }
                });
                dialog.show();
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            10, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            curLat = location.getLatitude();
                            curLong = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    //location=null;
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000,
                            10, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            curLat = location.getLatitude();
                            curLong = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this,"Please Check Your Location Services",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Checks if any new products match user's interests
     * Alerts user if there is an interest
     */
    private void matchedInterest() {
        String s = "The following interests have new related products: ";
        boolean matched = false;
        for (Product p : ConnectionManager.getProducts(this)) {
            if (myUser.getInterests().contains(p.getDoc_id())) {
                if (p.updated()) {
                    s += "\n" + p.getTitle();
                    p.setUpdated();
                    ConnectionManager.updateProduct(p,this);
                    matched = true;
                }
            }
        }
        //is something of interest
        if (matched){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(s);
            dialog.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void onLocationChanged(Location loc) {
        curLat = loc.getLatitude();
        curLong = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String pro) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Please Enable Location Services And Login Again");
        dialog.setPositiveButton("Open Location Services", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton("Back To Login", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                finish();

            }
        });
        dialog.show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        return super.onPrepareOptionsMenu(menu);
//    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Implements on item click listener for the position
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    /**
     * Lets user select item by checking their position
     * @param position position of item clicked
     */
    private void selectItem(int position) {
        // update the main content by replacing fragments
        if (position == 7) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Don't leave us! Please!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            updateUser();
            Fragment fragment = new OperationFragment();
            Bundle args = new Bundle();
            args.putInt(OperationFragment.ARG_NUMBER, position);
            args.putString("MyUser", new Gson().toJson(myUser));
            args.putDouble("lat",curLat);
            args.putDouble("long",curLong);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(actionTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getActionBar() != null)
            getActionBar().setTitle(mTitle);
    }


    /**
     * Updates user in database
     */
    private void updateUser() {
        for (User u : ConnectionManager.getUsers(this)) {
            if (u.getDoc_id().equals(myUser.getDoc_id())) {
                myUser = u;
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            myUser = new Gson().fromJson(data.getStringExtra("newUser"), User.class);
        }
    }


    /**
     * Stops the GPS services
     */
    private void stopGPS(){
        locationManager.removeUpdates(Nav.this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopGPS();

    }

    @Override
    protected void onResume(){
        super.onResume();
        startGPS();
    }
}
