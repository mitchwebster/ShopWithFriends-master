package com.example.mitchwebster.shopwithfriends.Connections;

import android.os.AsyncTask;

import com.example.mitchwebster.shopwithfriends.Models.User;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mitch Webster on 2/10/2015.
 * File for updating users on server
 */
final class UpdateUserAsyncTask extends AsyncTask<Object, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Object... params) {
        User user = (User) params[0];

        try {
            //make a new connection
            MongoConnection qb = new MongoConnection();
            URL url = new URL(qb.buildUsersUpdateURL(user.getDoc_id()));
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(
                    connection.getOutputStream());

            osw.write(qb.setUserData(user)); //attempt to update the user
            osw.flush();
            osw.close();
            return connection.getResponseCode() < 205;

        } catch (Exception e) {
            e.getMessage();
            return false;

        }

    }

}
