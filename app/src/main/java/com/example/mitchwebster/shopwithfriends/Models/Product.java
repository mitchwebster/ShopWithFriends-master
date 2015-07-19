package com.example.mitchwebster.shopwithfriends.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitch Webster on 2/9/2015.
 * Model for Product
 */
public class Product {
    private final String price;
    private final String title;
    private final String description;
    private final List<String> tags;
    private final String author;
    private final double latitude;
    private final double longitude;
    private final String doc_id;
    private final boolean isProduct;
    private final List<String> relatedProducts;
    private boolean updated;

    public Product() {this("","","",null,"0.00",0,0,true);}
    public Product(String author, String title, String description,List<String> tags, String price, double lat, double lon, boolean isProduct) {
        title = title.replaceAll(" ", "_").toLowerCase();
        this.author = author;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.price = price;
        latitude = lat;
        longitude = lon;
        if (isProduct) {
            doc_id = author + "_product_" + title;
        } else {
            doc_id = author + "_interest_" + title;
        }
        this.isProduct = isProduct;
        updated = false;
        relatedProducts = new ArrayList<>();
    }

    /**
     * Getter method for title
     * @return String that is the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for author
     * @return String that is the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter method for tags
     * @return List of string that contains the tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Getter method for price
     * @return Double that is the price
     */
    public double getPrice() {
        //need to format correctly and error check
        return Double.parseDouble(price);
    }

    /**
     * Getter method for description
     * @return String that is the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter method for Longitude and Latitude
     * @return Array of doubles that are the longitude and latitude
     */
    public double[] getLongLat() {
        return new double[]{latitude,longitude};
    }

    /**
     * Getter method for doc_id
     * @return String that is the doc_id
     */
    public String getDoc_id(){
        return doc_id;
    }

    /**
     * Returns if it is a product or not
     * @return Boolean value for if it's a product or not
     */
    public boolean isProduct() {
        return isProduct;
    }

    /**
     * Returns if it has been updated or not
     * @return Boolean value for if it's been updated or not
     */
    public boolean updated() {return updated;}

    /**
     * Getter method for related produces
     * @return List of strings that contain related products
     */
    public List<String> getRelatedProducts() {
        return relatedProducts;
    }

    /**
     * add related products to the related products list
     * @param t doc id of related product
     */
    public void addRelatedProduct(String t){
        if (!relatedProducts.contains(t)) {
            relatedProducts.add(t);
            updated = true;
        }
    }

    /**
     * Setter method for updated. Sets updated to be false.
     */
    public void setUpdated() {
        updated = false;
    }
}
