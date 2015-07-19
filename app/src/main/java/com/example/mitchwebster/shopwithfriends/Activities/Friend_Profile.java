package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.R;
import com.google.gson.Gson;

public class Friend_Profile extends Activity {
    private User myUser;
    private User myFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__profile);
        Intent intent = getIntent();
        String userJson = intent.getStringExtra("myUser");
        String friendJson = intent.getStringExtra("myFriend");
        Gson g = new Gson();
        myUser = g.fromJson(userJson, User.class);
        myFriend = g.fromJson(friendJson, User.class);
        TextView name   = ((TextView)findViewById(R.id.friend_name));
        TextView email   = ((TextView)findViewById(R.id.friend_email));
        TextView recentProducts   = ((TextView)findViewById(R.id.friend_products));
        RatingBar rating   = ((RatingBar)findViewById(R.id.friend_rating));

        name.setText(myFriend.getFullName());
        email.setText(myFriend.getEmail());
        recentProducts.setText("Number of Sales Reported: " + myFriend.getProducts().size());
        rating.setRating((float)myFriend.getRating());
    }


    /**
     * Actually delete friend from your friend's list in the data base
     * But the former friend still exists
     */
    private void deleteFriendHelper() {
        if (myUser.deleteFriend(myFriend.getEmail()) && myFriend.deleteFriend(myUser.getEmail())) {
            ConnectionManager.updateUser(myUser,getApplicationContext());
            ConnectionManager.updateUser(myFriend,getApplicationContext());
            Intent outIntent = new Intent();
            outIntent.putExtra("newUser", new Gson().toJson(myUser));
            setResult(Activity.RESULT_OK, outIntent);
            finish();
        } else {
            //warn friend could not be deleted
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(),"Failed to delete friend", duration);
            toast.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend__profile, menu);
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
     * Checks if entry is a valid email
     */
    public void deleteFriend(View v) {
        //Make sure user wants to delete friend
        Log.i("", "" + v.isActivated());
        new AlertDialog.Builder(this)
                .setTitle("Delete Friend")
                .setMessage("Are you sure you want to delete this friend?")
                //If so, actually delete the friend
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFriendHelper();
                    }
                })
                //if not
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
