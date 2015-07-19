package com.example.mitchwebster.shopwithfriends.Connections;

import android.os.AsyncTask;

import com.example.mitchwebster.shopwithfriends.Models.Product;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mitch Webster on 2/8/2015.
 * Class that gets the users from the server
 * Subclass of AsyncTask, runs in background
 */

class GetProductsAsyncTask extends AsyncTask<Product, Void, BasicDBList> {
    private static String server_output = null;

    @Override
    protected BasicDBList doInBackground(Product... arg0) {
        try
        {
            //create a new connection
            MongoConnection qb = new MongoConnection();
            URL url = new URL(qb.buildProductsSaveURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String temp_output;
            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }
            // create a basic db list
            String mongoarray = "{ currentProducts: "+server_output+"}"; //parse the json
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            DBObject dbObj = (DBObject) o;
            return (BasicDBList) dbObj.get("currentProducts"); // return the users

        }catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
