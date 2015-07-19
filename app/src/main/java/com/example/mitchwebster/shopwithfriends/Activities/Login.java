package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.R;
import com.github.sendgrid.SendGrid;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Hashtable;


public class Login extends Activity {
    private Iterable<User> users = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        users = ConnectionManager.getUsers(getApplicationContext());
        Button b = (Button) findViewById(R.id.fp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * Cancels login
     */
    public void cancelLogin(View v){
        Log.i("", "" + v.isActivated());
        finish();
    }

    /**
     * Checks if user is in database, launches home screen if true
     */
    public void userInDataBase(View v){
        Context context = getApplicationContext();
        Log.i("",""+v.isActivated());

        String email   = ((EditText)findViewById(R.id.emailLogin)).getText().toString();
        String password   = ((EditText)findViewById(R.id.passwordLogin)).getText().toString();

        if (!validEmail(email)) {
            Toast.makeText(context, "Email is a Required Field", Toast.LENGTH_SHORT).show();

        } else {
            boolean isValid = false;
            User myUser = new User();
            for (User u : users) {
                if (u.getEmail().equals(email) && u.isValidPassword(password)) {
                    isValid = true;
                    myUser = u;
                    break;
                }
            }
            //continue with login
            if (!isValid) {
                Toast.makeText(context, "Invalid Email/Password Combination", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Intent intent  = new Intent(this,Nav.class);
                Gson g = new Gson();
                String json = g.toJson(myUser);
                intent.putExtra("myUser", json);
                startActivity(intent);
            }
        }
    }

    /**
     * Checks if entry is a valid email
     * @param email email to test
     * @return  boolean about whether email is valid or not
     */
    private boolean validEmail(String email){
        return email.contains("@");
    }

    private void forgotPassword() {
        String email   = ((EditText)findViewById(R.id.emailLogin)).getText().toString();
        if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your email above so we can recover your password", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean notFound = true;
        User myUser = null;
        for (User u : ConnectionManager.getUsers(this)) {
            if (u.getEmail().toLowerCase().equals(email)) {
                notFound = false;
                myUser = u;
                break;
            }
        }
        if (notFound) {
            Toast.makeText(getApplicationContext(), "This email does not exist in our database", Toast.LENGTH_SHORT).show();
            return;
        }
        //send activation email
        String activationCode = new BigInteger(130,new SecureRandom()).toString(32);
        Hashtable<String,String> params = new Hashtable<>();
        params.put("to",email);
        params.put("from","shopwfriends@gmail.com");
        params.put("subject","SWF Password Reset");
        params.put("text","Forgot your password?\n\nHere is your activation code: "+activationCode +"\n\nThank you for using SWF!");
        SendEmailWithSendGrid newEmail = new SendEmailWithSendGrid();
        try {
            //noinspection unchecked
            newEmail.execute(params).get();
            finish();
            Intent intent  = new Intent(this,Forgot_Password.class);
            intent.putExtra("ac", activationCode);
            Gson g = new Gson();
            String json = g.toJson(myUser);
            intent.putExtra("myUser", json);
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, "Could not send email to this address", Toast.LENGTH_SHORT).show();
        }
    }

    private class SendEmailWithSendGrid extends AsyncTask<Hashtable<String,String>, Void, String> {

        @SafeVarargs
        @Override
        protected final String doInBackground(Hashtable<String, String>... params) {
            Hashtable<String,String> h = params[0];
            SendGrid sendgrid = new SendGrid("mwebster","pass17word");
            sendgrid.addTo(h.get("to"));
            sendgrid.setFrom(h.get("from"));
            sendgrid.setSubject(h.get("subject"));
            sendgrid.setText(h.get("text"));
            return sendgrid.send();
        }
    }
}
