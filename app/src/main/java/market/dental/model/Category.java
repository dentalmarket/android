package market.dental.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemalsamikaraca on 19.01.2018.
 */

public class Category {

    private int id;
    private String name;
    private int parentId;
    private String icon;

    public Category(JSONObject categoryJsonObject){
        try {
            this.id = categoryJsonObject.has("category_id") ?
                    categoryJsonObject.getInt("category_id") : -1;
            this.name = categoryJsonObject.has("category") ?
                    categoryJsonObject.getString("category") : null;
            this.parentId = categoryJsonObject.has("parent_category_id") && !categoryJsonObject.isNull("parent_category_id") ?
                    categoryJsonObject.getInt("parent_category_id") : -1;
            this.icon = categoryJsonObject.has("image") ?
                    categoryJsonObject.getString("image") : null;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> CategoryList(JSONArray categoryJsonObject){

        List<Category> categoryList = new ArrayList<Category>();
        for(int i=0; i<categoryJsonObject.length(); i++){
            try {
                categoryList.add(new Category((JSONObject) categoryJsonObject.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryList;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
