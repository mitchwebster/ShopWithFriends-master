package com.example.mitchwebster.shopwithfriends.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mitchwebster.shopwithfriends.Connections.ConnectionManager;
import com.example.mitchwebster.shopwithfriends.HoldingArrayAdapter;
import com.example.mitchwebster.shopwithfriends.Models.Product;
import com.example.mitchwebster.shopwithfriends.Models.Tag;
import com.example.mitchwebster.shopwithfriends.Models.User;
import com.example.mitchwebster.shopwithfriends.ProductAdapter;
import com.example.mitchwebster.shopwithfriends.R;
import com.example.mitchwebster.shopwithfriends.TagAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitch Webster on 3/2/2015.
 * Fragment for content
 *
 */
public class OperationFragment extends Fragment {
    public static final String ARG_NUMBER = "view_number";
    private static TagAdapter myAdapter = null;
    private MapView mapView;
    private User myUser;
    private double curLat;
    private double curLong;
    private static final String[] actionTitles = new String[]{
            "Home",
            "My Friends",
            "My Interests",
            "My Products",
            "Register Interest/Product",
            "Add Friends",
            "Map",
            "Log Out" };

    public OperationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //default view
        final View rootView;
        int i = getArguments().getInt(ARG_NUMBER);
        myUser = new Gson().fromJson(getArguments().getString("MyUser"), User.class);
        curLat = getArguments().getDouble("lat");
        curLong = getArguments().getDouble("long");

        if (i == 1) {
            //My friends page
            rootView = inflater.inflate(R.layout.friends_list, container, false);
            final List<String> myFriendsAddresses = myUser.getFriends();
            final List<User> myFriends = new ArrayList<>();
            for (User u : ConnectionManager.getUsers(getActivity())) {
                if (myFriendsAddresses.contains(u.getEmail())) {
                    myFriends.add(u);
                }
            }
            final ListView listview = (ListView) rootView.findViewById(R.id.myFriends);
            HoldingArrayAdapter adapter = new HoldingArrayAdapter(getActivity(), myFriends);
            listview.setAdapter(adapter);
            final String json = getArguments().getString("MyUser");
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    User u = myFriends.get(position);
                    Intent intent  = new Intent(getActivity(),Friend_Profile.class);
                    intent.putExtra("myUser", json);
                    intent.putExtra("myFriend", new Gson().toJson(u));
                    startActivityForResult(intent,2);
                }

            });
        } else if (i == 2) {
            //My Interests Page
            rootView = inflater.inflate(R.layout.activity_my_interests, container, false);
            List<String> myInterestIDs = myUser.getInterests();
            List<Product> myInterests = new ArrayList<>();
            for (Product p : ConnectionManager.getProducts(getActivity())) {
                if (myInterestIDs.contains(p.getDoc_id())) {
                    myInterests.add(p);
                }
            }
            final ListView listview = (ListView) rootView.findViewById(R.id.interest_list);
            ArrayAdapter<Product> adapter = new ProductAdapter(getActivity(),myInterests);
            listview.setAdapter(adapter);
        } else if (i == 3) {
            //My Products Page
            rootView = inflater.inflate(R.layout.activity_my_products, container, false);
            List<String> myProductsIDs = myUser.getProducts();
            List<Product> myProducts = new ArrayList<>();
            for (Product p : ConnectionManager.getProducts(getActivity())) {
                if (myProductsIDs.contains(p.getDoc_id())) {
                    myProducts.add(p);
                }
            }
            final ListView listview = (ListView) rootView.findViewById(R.id.products_list);
            ArrayAdapter<Product> adapter = new ProductAdapter(getActivity(),myProducts);
            listview.setAdapter(adapter);
        } else if (i == 4) {
            //Create Interest/Product Page
            rootView = inflater.inflate(R.layout.activity_register__interest, container, false);
            final ListView listview = (ListView) rootView.findViewById(R.id.interest_search_tags);
            final ListView otherList = (ListView) rootView.findViewById(R.id.interest_tags);
            List<String> tagOptions = new ArrayList<>();
            for (Tag t : Tag.values()) {
                tagOptions.add(t.toString());
            }
            final List<String> tags = new ArrayList<>();

            myAdapter = new TagAdapter(getActivity(),tagOptions);
            final TagAdapter otherAdapter = new TagAdapter(getActivity(),tags);
            listview.setAdapter(myAdapter);
            otherList.setAdapter(otherAdapter);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String t = myAdapter.get(position);
                    myAdapter.remove(t);
                    otherAdapter.add(t);
                }


            });


            otherList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String t = otherAdapter.get(position);
                    myAdapter.add(t);
                    otherAdapter.remove(t);
                    return true;
                }
            });

            setupUI(rootView.findViewById(R.id.registerInterestParent));
            EditText mySearch = (EditText) rootView.findViewById(R.id.searchView);
            mySearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    myAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            final Button b = (Button) rootView.findViewById(R.id.submit_interest);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveInterest(rootView,tags);
                }
            });

        } else if (i == 5) {
            //Add Friends Page
            rootView = inflater.inflate(R.layout.activity_add__friend, container, false);
            //load users
            final Button b = (Button) rootView.findViewById(R.id.friend_search);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptsSearch(rootView, ConnectionManager.getUsers(getActivity()));
                }
            });
        } else if (i ==  6) {
            rootView = inflater.inflate(R.layout.map_view, container, false);
            mapView = (MapView) rootView.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            if (mapView != null) {
                GoogleMap googleMap;
                googleMap = mapView.getMap();
                if (googleMap != null) {
                    try {
                        MapsInitializer.initialize(getActivity());
                    } catch (Exception e) {
                        return rootView;
                    }
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    LatLng curLocation = new LatLng(curLat, curLong);
                    CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(curLocation, 15);
                    googleMap.addMarker(new MarkerOptions()
                            .title(myUser.getFullName())
                            .snippet(myUser.getEmail())
                            .position(curLocation));
                    googleMap.animateCamera(camera);

                    //add products to this shin dig
                    for (Product p : ConnectionManager.getProducts(getActivity())) {
                        double[] l = p.getLongLat();
                        String title = p.isProduct() ? "Product: " : "Interest: ";
                        googleMap.addMarker(new MarkerOptions()
                                .title(p.getTitle())
                                .snippet(title + p.getDescription() + " - $" + p.getPrice())
                                .position(new LatLng(l[0],l[1])));
                    }
                }
            }
        } else {
            //Something went wrong, load the homepage
            rootView = inflater.inflate(R.layout.activity_home, container, false);
        }
        getActivity().setTitle(actionTitles[i]);
        return rootView;
    }

    @Override
    public void onResume(){
        if (mapView != null) {
            mapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }


    /**
     * method for hiding keyboard
     * @param activity current activity
     */

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * method for registering non textbox touches
     * @param view current view
     */
    private void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    /**
     * method for saving tags
     * saves all important information
     * @param v current view
     * @param tags tags to save
     */
    private void saveInterest(View v,List<String> tags){
        String title = ((EditText) v.findViewById(R.id.interest_title)).getText().toString();
        String desc = ((EditText) v.findViewById(R.id.interest_description)).getText().toString();
        String price = ((EditText) v.findViewById(R.id.interest_price)).getText().toString();
        Context c = getActivity();
        if (title.isEmpty()) {
            Toast.makeText(c, "No title", Toast.LENGTH_SHORT).show();
        } else if (desc.isEmpty()) {
            Toast.makeText(c, "No description", Toast.LENGTH_SHORT).show();
        } else if (price.isEmpty()) {
            Toast.makeText(c, "No price", Toast.LENGTH_SHORT).show();
        } else if (myUser.hasProduct(myUser.getEmail() + "_product_" + title) ||
                myUser.hasInterest(myUser.getEmail() + "_interest_" + title)){
            Toast.makeText(c, "Product/Interest with this name already exists", Toast.LENGTH_SHORT).show();
        } else if (tags.size() > 10){
            Toast.makeText(c, "Please enter 10 or less tags", Toast.LENGTH_SHORT).show();
        } else {
            final Switch s = (Switch) v.findViewById(R.id.productSwitch);
            Product p = new Product(myUser.getEmail(), title, desc, tags, price, curLat, curLong, s.isChecked());
            if (s.isChecked()) {
                myUser.addProduct(p.getDoc_id());
            } else {
                myUser.addInterest(p.getDoc_id());
            }
            //update user
            boolean y = ConnectionManager.updateUser(myUser, c);
            boolean y2 = ConnectionManager.updateProduct(p, c);
            if (s.isChecked()) {
                productMatch(p);
            } else {
                interestMatch(p);
            }
            //toast it up
            if (y && y2)
                Toast.makeText(c, "Successfully registered " + (s.isChecked() ? "product" : "interest"), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(c,"Failed to register new User",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to search for friend
     * Error handling for several cases
     * @param v current view
     * @param users list of users
     */
    private void attemptsSearch(View v, Iterable<User> users) {
        Context context = getActivity();
        User newFriend = new User();

        String friendUsername = ((EditText) v.findViewById(R.id.friendUsername)).getText().toString();

        if (friendUsername.length() == 0) {
            Toast.makeText(context, "To add a friend, enter a username", Toast.LENGTH_SHORT).show();
        } else if (friendUsername.equals(myUser.getEmail())) {
            Toast.makeText(context, "You cannot add yourself...", Toast.LENGTH_SHORT).show();
        } else if (myUser.getFriends().contains(friendUsername)) {
            Toast.makeText(context, "This person is already your friend", Toast.LENGTH_SHORT).show();
        } else {
            boolean isValid = false;
            for (User u : users) {
                if (u.getEmail().equals(friendUsername)) {
                    isValid = true;
                    newFriend = u;
                    break;
                }
            }
            if (!isValid) {
                Toast.makeText(context, "Friend not found", Toast.LENGTH_SHORT).show();
            } else {
                //add friend
                Toast.makeText(context, "Friend Added", Toast.LENGTH_SHORT).show();
                myUser.addFriend(newFriend.getEmail());
                newFriend.addFriend(myUser.getEmail());
                //update myUser to database, must check if user is adding themselves
                boolean success = ConnectionManager.updateUser(myUser, getActivity());
                boolean success2 = ConnectionManager.updateUser(newFriend, getActivity());
                if (success && success2) {
                    Toast.makeText(context, "Friend Added", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * method for determining if a product matches as user's interests
     * @param i interest
     */
    private void interestMatch(Product i) {
        //called when new interest is created
        for (Product product : ConnectionManager.getProducts(getActivity())) {
            if (product.isProduct()) {
                //this is an interest time to determine if they match
                if ( product.getPrice() <= i.getPrice()) {
                    //within the price range, time to check if they are relevant to each other
                    boolean similar = false;
                    for (String t : i.getTags()) {
                        if (product.getTags().contains(t)) {
                            similar = true;
                            break;
                        }
                    }
                    if (similar) {
                        //we are done
                        relatedProduct(i, product);
                    } else if (i.getDescription().toLowerCase().contains(product.getTitle()) || i.getTitle().equals(product.getTitle())) {
                        //we are done
                        relatedProduct(i,product);
                    }
                    //not related
                }
            }
        }

    }

    /**
     * method for determining if a product applies to any of the user's interests
     * @param p product
     */
    private void productMatch(Product p) {
        //goes through each user and determines if this product applies to any of the interests
        for (Product interest : ConnectionManager.getProducts(getActivity())) {
            if (!interest.isProduct()) {
                //this is an interest time to determine if they match
                if ( p.getPrice() <= interest.getPrice()) {
                    //within the price range, time to check if they are relevant to each other
                    boolean similar = false;
                    for (String t : interest.getTags()) {
                        if (p.getTags().contains(t)) {
                            similar = true;
                            break;
                        }
                    }
                    if (similar) {
                        //we are done
                        relatedProduct(interest, p);

                    } else if (interest.getDescription().toLowerCase().contains(p.getTitle()) || interest.getTitle().equals(p.getTitle())) {
                        //we are done
                        relatedProduct(interest,p);
                    }
                }
            }
        }
    }

    /**
     * method for determining if a product is related to another
     * @param interest interest
     * @param other matching product
     */
    private void relatedProduct(Product interest, Product other) {
        interest.addRelatedProduct(other.getDoc_id());
        Log.i("", "should update now");
        if (!ConnectionManager.updateProduct(interest,getActivity())) {
            Toast.makeText(getActivity(),"Could Not Update Product",Toast.LENGTH_SHORT).show();
        }
    }


}
