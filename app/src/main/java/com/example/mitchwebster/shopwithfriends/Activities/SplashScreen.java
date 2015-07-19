package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mitchwebster.shopwithfriends.R;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
    /**
     * Sends user to login page, takes in view as a parameter
     *
     * @param v current view
     */
    public void openLogin(View v) {
        Log.i("", "" + v.isActivated());
        Intent intent  = new Intent(this,Login.class);
        startActivity(intent);
    }

    /**
     * Sends user to registration page, takes in view as a parameter
     *
     * @param v current view
     */
    public void openRegister(View v) {
        Log.i("",""+v.isActivated());
        Intent intent  = new Intent(this,Register.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
