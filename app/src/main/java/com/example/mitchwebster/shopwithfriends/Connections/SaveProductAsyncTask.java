package com.example.mitchwebster.shopwithfriends.Connections;

import android.os.AsyncTask;

import com.example.mitchwebster.shopwithfriends.Models.Product;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Mitch Webster on 2/8/2015.
 * Class that saves user to the server
 * Subclass of AsyncTask, runs in background
 */
class SaveProductAsyncTask extends AsyncTask<Product, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Product... arg0) {
        try
        {
            Product p = arg0[0];
            //create connection
            MongoConnection qb = new MongoConnection();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildProductsSaveURL());

            StringEntity params =new StringEntity(qb.createProduct(p));
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