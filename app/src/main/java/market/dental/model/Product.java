package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 17.01.2018.
 */

public class Product {

    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private String price;
    private String salePrice;
    private String subtitle;
    private int currencyId;
    private int offerCount;
    private String offerDesc;
    private Brand brand;

    public Product (){}

    public Product(JSONObject projectJsonObject){
        try {
            this.id = projectJsonObject.has("product_id")?
                    projectJsonObject.getInt("product_id") : -1 ;
            this.name = projectJsonObject.has("title") && !projectJsonObject.isNull("title")?
                    projectJsonObject.getString("title"):"";
            this.subtitle = projectJsonObject.has("subtitle") && !projectJsonObject.isNull("subtitle")?
                    projectJsonObject.getString("subtitle"):"";
            this.description = projectJsonObject.has("description") && !projectJsonObject.isNull("description")?
                    projectJsonObject.getString("description"):"-";
            this.imageUrl = projectJsonObject.has("image")?
                    Resource.DOMAIN_NAME + "/" + projectJsonObject.getString("image"):"";
            this.price = projectJsonObject.has("price") && !projectJsonObject.isNull("price")?
                    projectJsonObject.getString("price"):"-";
            this.salePrice = projectJsonObject.has("sale_price") && !projectJsonObject.isNull("sale_price")?
                    String.format("%.2f", projectJsonObject.getDouble("sale_price")):"-";
            this.currencyId = projectJsonObject.has("currency") && !projectJsonObject.isNull("currency")?
                    projectJsonObject.getInt("currency"):-1;

            if(projectJsonObject.has("brand") && projectJsonObject.getJSONObject("brand")!=null){
                this.brand = new Brand(projectJsonObject.getJSONObject("brand"));
            }
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

    public static List<Product> ProductListWithExt(JSONArray projectJsonArray){

        List<Product> productList = new ArrayList<Product>();
        for(int i=0; i<projectJsonArray.length(); i++){
            try {
                productList.add(new Product( ((JSONObject) projectJsonArray.get(i)).getJSONObject("product") ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subTitle) {
        this.subtitle = subTitle;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }
}
