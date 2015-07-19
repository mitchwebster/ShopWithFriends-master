package com.example.mitchwebster.shopwithfriends.Connections;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;

import com.example.mitchwebster.shopwithfriends.Models.User;

/**
 * Created by Mitch Webster on 2/8/2015.
 * Class that saves user to the server
 * Subclass of AsyncTask, runs in background
 */
class SaveUserAsyncTask extends AsyncTask<User, Void, Boolean> {

    @Override
    protected Boolean doInBackground(User... arg0) {
        try
        {
            User contact = arg0[0];
            //create connection
            MongoConnection qb = new MongoConnection();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildUsersSaveURL());

            StringEntity params =new StringEntity(qb.createUser(contact));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            //try to save user

            return response.getStatusLine().getStatusCode() < 205;
        } catch (Exception e) {
            //e.getCause();
            return false;
        }
    }

}