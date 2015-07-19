package com.example.mitchwebster.shopwithfriends.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitch Webster on 1/26/2015.
 * Model for user
 */
public class User implements Comparable<User> {
    private final String firstName;
    private String lastName;
    private final String email;
    private String password;
    private final double rating;
    private final boolean admin;
    private final List<String> friends;
    private String doc_id;
    private final List<String> interests;
    private final List<String> products;


    /**
     * Sets this users document id
     *
     * @param doc_id , User
     */
    private void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    /**
     * Constructs a new user
     */
    public User() {
      this("","","user","pass", new ArrayList<String>());
    }
    /**
     * Constructs a new user
     */
    public User(String fN, String lN, String eM, String pW){
       this(fN,lN,eM,pW, new ArrayList<String>());
    }

    /**
     * Constructs a new user
     * @param email email
     * @param password password
     */
    public User(String email, String password) {
        this("John","Doe",email,password, new ArrayList<String>());
    }

    /**
     * Returns the user id
     * @return  String containing user id
     */
    public String getDoc_id(){
        return doc_id;
    }

    /**
     * Constructs a new user
     */
    public User(String fN, String lN, String eM, String pW, ArrayList<String> friend){
       firstName = fN;
       lastName = lN;
       lastName = lN;
       email = eM;
       password = pW;
       //String imgPath = "path to normal image";
       rating = 0.0;
       admin = false;
       friends = friend;
       doc_id = email;
       interests = new ArrayList<>();
       products = new ArrayList<>();
    }
    /**
     * Returns the user name
     * @return  String containing user first name
     */
    String getFirstName() {
        return firstName;
    }

    /**
     * Returns the password
     * @return  String containing user password
     */
    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    /**
     * Returns the last name
     * @return  String containing user last name
     */
    String getLastName() {
        return lastName;
    }

    /**
     * Returns the email
     * @return  String containing user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the admin status
     * @return  boolean whether user is admin or not
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Returns the user rating
     * @return  double containing user rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returns the possible password is valid
     * @param passAttempt password attempt
     * @return  boolean about whether password is valid or not
     */
    public boolean isValidPassword(String passAttempt) {
        return password.equals(passAttempt);
    }

    /**
     * Returns the possible user is a friend
     * @param u user id
     * @return  boolean about whether user is a friend
     */
    public boolean isFriend(String u) {
        return friends.contains(u);
    }

    /**
     * Adds a friend
     * @param u user id
     */
    public void addFriend(String u) {
        friends.add(u);
    }

    /**
     * Returns the friends of this user
     * @return  arraylist of friend emails
     */
    public List<String> getFriends() {
        return friends;
    }

    /**
     * Returns the usrs full name
     * @return  String containing user full name
     */
    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof User)) && (o == this || email.equals(((User) o).getEmail()));
    }
    @Override
    public int compareTo(@NonNull User another) {
        //sort alphabetically, could consider by rating etc
        return getFullName().compareTo(another.getFullName());
    }
    @Override
    public int hashCode() {
        //gets the hashCode of the last name
        return getLastName().hashCode();
    }

    @Override
    public String toString() {
        return email + " : " + password;
    }

    /**
     * Deletes friend if valid email
     * @param email of friend to delete
     * @return  whether friend was deleted or not
     */
    public boolean deleteFriend(String email){
        return friends.remove(email);
    }


    /**
     * Returns the interests of this user
     * @return  List of strings of user's interests
     */
    public List<String> getInterests(){ return interests;}

    /**
     * Adds the interest to user
     * @param i interest id
     */
    public void addInterest(String i){interests.add(i);}

    /**
     * Returns the products of this user
     * @return  List of strings of user's products
     */
    public List<String> getProducts(){ return products;}

    /**
     * Adds the product to user
     * @param p product id
     */
    public void addProduct(String p){products.add(p);}

    /**
     * Checks if product exists
     * @return Boolean value for if product exists
     */
    public boolean hasProduct(String p) {return products.contains(p);}

    /**
     * Checks if interest exists
     * @return Boolean value for if interest exists
     */
    public boolean hasInterest(String i) {return interests.contains(i);}

}
