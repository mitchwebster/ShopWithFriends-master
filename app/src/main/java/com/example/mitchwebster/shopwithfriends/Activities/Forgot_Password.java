package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.R;
import com.google.gson.Gson;


public class Forgot_Password extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        Intent intent = getIntent();

        String email = intent.getStringExtra("Email");
        //String AC = intent.getStringExtra("AC");
        ((TextView)findViewById(R.id.emailToReset)).setText(email);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot__password, menu);
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

    public void submitPasswordReset(View v) {
        String acode   = ((EditText)findViewById(R.id.activationReset)).getText().toString();
        String password1   = ((EditText)findViewById(R.id.passwordReset)).getText().toString();
        String password2   = ((EditText)findViewById(R.id.confirmReset)).getText().toString();

        Intent intent = getIntent();
        String userJson = intent.getStringExtra("myUser");
        Gson g = new Gson();
        User myUser = g.fromJson(userJson,User.class);
        String AC = intent.getStringExtra("ac");

        if (acode.length() == 0) {
            Toast.makeText(this,"Please enter an activation code",Toast.LENGTH_SHORT).show();
        } else if (!acode.equals(AC)) {
            Toast.makeText(this,"Invalid activation code",Toast.LENGTH_SHORT).show();
        } else if (password1.length() == 0 || password1.length() < 8) {
            Toast.makeText(this,"Please enter a password with at least 8 characters",Toast.LENGTH_SHORT).show();
        } else if (!password1.equals(password2)) {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        } else {
            myUser.setPassword(password1);
            ConnectionManager.updateUser(myUser, this);
            Toast.makeText(this,"Password has been reset",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
