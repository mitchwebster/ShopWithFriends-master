package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.R;


public class Register extends Activity {
    private Iterable<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        users = ConnectionManager.getUsers(getApplicationContext());
    }

    /**
     * Cancels Registration with a warning, does not add user
     */
    public void cancelReg(View v) {
        //Context context = getApplicationContext();
        Log.i("", "" + v.isActivated());
        new AlertDialog.Builder(this)
                .setTitle("Cancel Registration")
                .setMessage("Are you sure you want to cancel registration?\nYour data will not be saved.")
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
    }
    /**
     * Adds a new user if they have entered all required data
     */
    public void finishRegister(View v) {
        Context context = getApplicationContext();
        Log.i("",""+v.isActivated());
        String firstName   = ((EditText)findViewById(R.id.firstName_reg)).getText().toString();
        String lastName   = ((EditText)findViewById(R.id.lastName_reg)).getText().toString();
        String email   = ((EditText)findViewById(R.id.email_registration)).getText().toString();
        String pass1   = ((EditText)findViewById(R.id.password_registration)).getText().toString();
        String pass2   = ((EditText)findViewById(R.id.confirm_password_reg)).getText().toString();
        if (firstName.length() == 0) {
            Toast.makeText(context, "First Name is a Required Field", Toast.LENGTH_SHORT).show();
        } else if (lastName.length() == 0) {
            Toast.makeText(context, "Last Name is a Required Field", Toast.LENGTH_SHORT).show();
        } else if (email.length() == 0 || (!email.contains("@"))) {
            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (pass1.length() < 8) {
            Toast.makeText(context, "Password must be 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!pass2.equals(pass1)) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (usersHasEmail(email)) {
            Toast.makeText(context, "Email Already Exists", Toast.LENGTH_SHORT).show();
        } else {
            ConnectionManager.updateUser(new User(firstName,lastName,email,pass1), getApplicationContext());
            Toast.makeText(context, "Successfully Created New User!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Checks if any user already has this email
     * @param email email
     * @return  boolean about email presence
     */
    private boolean usersHasEmail(String email) {
        boolean valid = false;
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                valid = true;
            }
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
