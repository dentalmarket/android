package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemalsamikaraca on 17.01.2018.
 */

public class Product {

    private int id;
    private String name;
    private String imageUrl;
    private int price;
    private int currencyId;

    public Product(JSONObject projectJsonObject){
        try {
            this.id = projectJsonObject.getInt("id");
            this.name = projectJsonObject.getString("name");
            this.imageUrl = projectJsonObject.getString("image");
            this.price = projectJsonObject.getInt("price");
            this.currencyId = projectJsonObject.getInt("currency");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static List<Product> ProductList(JSONArray projectJsonArray){

        List<Product> productList = new ArrayList<Product>();
        for(int i=0; i<projectJsonArray.length(); i++){
            try {
                productList.add(new Product((JSONObject) projectJsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
}
