package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import market.dental.util.Resource;

public class OfferProduct {

    private int id;
    private int productId;
    private String productTitle;
    private String imageUrl;
    private int unit;
    private String description;

    public OfferProduct(){
        this.id=0;
    }

    public OfferProduct(JSONObject projectJsonObject){
        try {
            this.id = projectJsonObject.has("id")?
                    projectJsonObject.getInt("id") : -1 ;
            this.productId = projectJsonObject.has("product_id")?
                    projectJsonObject.getInt("product_id") : -1 ;
            this.productTitle = projectJsonObject.has("product_title") && !projectJsonObject.isNull("product_title")?
                    projectJsonObject.getString("product_title"):"";
            this.imageUrl = projectJsonObject.has("image")?
                    Resource.DOMAIN_NAME + "/" + projectJsonObject.getString("image"):"";
            this.unit = projectJsonObject.has("unit")?
                    projectJsonObject.getInt("unit") : 1 ;
            this.description = projectJsonObject.has("description") && !projectJsonObject.isNull("description")?
                    projectJsonObject.getString("description"):"";

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static List<OfferProduct> offerProductList(JSONArray projectJsonArray){

        List<OfferProduct> productList = new ArrayList<OfferProduct>();
        for(int i=0; i<projectJsonArray.length(); i++){
            try {
                productList.add(new OfferProduct((JSONObject) projectJsonArray.get(i)));
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
