package com.example.mitchwebster.shopwithfriends.Connections;


import com.example.mitchwebster.shopwithfriends.Models.Product;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.util.HashSet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Mitch Webster on 2/9/2015.
 * File for all server connections
 */
public class ConnectionManager {

    /**
     * Attempts to add a update an existing user in the database
     *
     * @param u , User
     * @param c , Context
     * @return whether the update was successful or not
     */
    public static boolean  updateUser(User u, Context c){
        if (isOnline(c)) {
            UpdateUserAsyncTask tsk = new UpdateUserAsyncTask();
            tsk.execute(u);
            return true;
        } else {
            Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Attempts to add a update an existing product in the database
     *
     * @param p , Product
     * @param c , Context
     * @return whether the update was successful or not
     */
    public static boolean  updateProduct(Product p, Context c){
        if (isOnline(c)) {
            UpdateProductAsyncTask tsk = new UpdateProductAsyncTask();
            tsk.execute(p);
            return true;
        } else {
            Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Attempts to get all existing users from the server
     *
     * @param c , Context
     * @return a hashset containing the users
     */
    public static Iterable<User> getUsers(Context c) {
        if (isOnline(c)) {
            HashSet<User> users = new HashSet<>();
            GetUsersAsyncTask conn = new GetUsersAsyncTask();
            BasicDBList my_people = null;
            try {
                my_people = conn.execute(new User()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert my_people != null;
            for (Object user : my_people) {
                DBObject userObject = (DBObject) user;
                User newPerson = new Gson().fromJson(userObject.toString(),User.class);
                users.add(newPerson);
            }
            return users;
        } else {
            Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return new HashSet<>();
        }
    }

    /**
     * Attempts to get all products from the server
     *
     * @param c , Context
     * @return hashset of products
     */
    public static Iterable<Product> getProducts(Context c) {
        if (isOnline(c)) {
            HashSet<Product> products = new HashSet<>();
            GetProductsAsyncTask conn = new GetProductsAsyncTask();
            BasicDBList my_people = null;
            try {
                my_people = conn.execute(new Product()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert my_people != null;
            for (Object prod : my_people) {
                DBObject userObject = (DBObject) prod;
                Product newProd = new Gson().fromJson(userObject.toString(),Product.class);
                products.add(newProd);
            }
            return products;
        } else {
            Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return new HashSet<>();
        }
    }

    /**
     * Checks if this application has internet access
     *
     * @param c , Context
     * @return whether the app has internet or not
     */
    private static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
