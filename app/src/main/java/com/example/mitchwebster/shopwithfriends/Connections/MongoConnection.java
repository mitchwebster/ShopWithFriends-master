package com.example.mitchwebster.shopwithfriends.Connections;

import com.example.mitchwebster.shopwithfriends.Models.Product;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.google.gson.Gson;

/**
 * Created by Mitch Webster on 2/8/2015.
 * File for all server connections
 * Specific details for mongo server connection
 */
class MongoConnection {
    @SuppressWarnings("SameReturnValue")
    String getDatabaseName() {
        return "shopwithfriends";
    }

    /**
     * Specify your MongoLab API here
     * @return api key
     */
    @SuppressWarnings("SameReturnValue")
    String getApiKey() {
        return "";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return overall database
     */
    String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return url format
     */
    String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Returns the users collection label
     * @return collection label
     */
    @SuppressWarnings("SameReturnValue")
    String usersRequest()
    {
        return "users";
    }

    /**
     * Returns the products collection label
     * @return collection label
     */
    @SuppressWarnings("SameReturnValue")
    String productsRequest()
    {
        return "products";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return overall url
     */
    public String buildUsersSaveURL()
    {
        return getBaseUrl()+usersRequest()+docApiKeyUrl();
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return overall url
     */
    public String buildProductsSaveURL()
    {
        return getBaseUrl()+productsRequest()+docApiKeyUrl();
    }

    /**
     * Formats the contact details for MongoHQ Posting
     * @param user: Details of the user
     * @return String of json formatted user
     */
    public String createUser(User user)
    {
        Gson g = new Gson();
        return g.toJson(user);
    }

    /**
     * Formats the contact details for MongoHQ Posting
     * @param p: Details of the user
     * @return String of json formatted product
     */
    public String createProduct(Product p)
    {
        Gson g = new Gson();
        return g.toJson(p);
    }

    /**
     * Attempts to prepare the user to be updated
     *
     * @param user , User
     * @return whether the push was successful or not
     */
    public String setUserData(User user) {
        Gson g = new Gson();
        return "{ \"$set\" : " + g.toJson(user) + "}";
    }

    /**
     * Attempts to prepare the product to be updated
     *
     * @param p product to set
     * @return whether the push was successful or not
     */
    public String setProductData(Product p) {
        Gson g = new Gson();
        return "{ \"$set\" : " + g.toJson(p) + "}";
    }

    /**
     * Get a Mongodb document that corresponds to the given object id
     * @param doc_id id of user
     * @return String that is the user's doc ID
     */
    public String buildUsersUpdateURL(String doc_id)
    {
        return getBaseUrl()+usersRequest()+docApiKeyUrl(doc_id);
    }

    /**
     * Get a Mongodb document that corresponds to the given object id
     * @param doc_id id of user
     * @return String that is the user's product URL
     */
    public String buildProductUpdateURL(String doc_id)
    {
        return getBaseUrl()+productsRequest()+docApiKeyUrl(doc_id);
    }

    /**
     * Helps build the url
     *
     * @param docid id of user
     * @return whether the push was successful or not
     */
    String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

}