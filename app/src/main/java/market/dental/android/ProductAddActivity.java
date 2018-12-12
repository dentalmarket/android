package market.dental.android;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import market.dental.adapter.CategoryListAdapter;
import market.dental.model.Category;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

/**
 * @author kemalsamikaraca
 * @version 1.0.1
 * @since 05/10/2018
 *
 */
public class ProductAddActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Context context;
    private View view;
    private AlertDialog progressDialog;
    private HashSet<Target> targetList = new HashSet<>();
    private CategoryListAdapter categoryListAdapter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialization
        context = this;
        requestQueue = Volley.newRequestQueue(this);
        categoryListAdapter = new CategoryListAdapter(this);

        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(this);
        progressDialogBuilder.setCancelable(false);
        progressDialogBuilder.setView(getLayoutInflater().inflate(R.layout.dialog_progressbar,null));
        progressDialog = progressDialogBuilder.create();

        // ALERT BUILDER
        builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(getString(R.string.dental_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        getCategoryList(-1);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void getCategoryList(final int categoryId){

        // *****************************************************************************************
        //                          AJAX - GET SUBCATEGORIES
        // *****************************************************************************************
        stringRequest = new StringRequest(Request.Method.POST,
                Resource.ajax_get_categories, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseString) {
                try {

                    JSONObject response = new JSONObject(responseString);
                    if(Result.SUCCESS.checkResult(new Result(response))){
                        JSONObject content = response.getJSONObject("content");
                        if(content.has("subCategories")) {
                            List<Category> categoryList = Category.CategoryList(content.getJSONArray("subCategories"));

                            categoryListAdapter.setCategoryList(categoryList);
                            ListView listView = findViewById(R.id.activity_product_add_category);
                            if(listView.getAdapter()==null )
                                listView.setAdapter(categoryListAdapter);
                            else{
                                categoryListAdapter.notifyDataSetChanged();
                            }

                            // -- EVENTS --
                            listView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            int categoryId = ((Category) parent.getItemAtPosition(position)).getId();
                                            builder.setMessage(categoryId + " >> " + ((Category) parent.getItemAtPosition(position)).getName());
                                            AlertDialog alert = builder.create();
                                            alert.show();

                                            /*
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(Resource.KEY_CATEGORY_ID, categoryId);
                                            Intent intent = new Intent(view.getContext(),ProductAddActivity.class);
                                            intent.putExtras(bundle);
                                            view.getContext().startActivity(intent);
                                            */
                                        }
                                    }
                            );

                        }else{
                            builder.setMessage("TEST ");
                            AlertDialog alert = builder.create();
                            alert.show();
                        }


                    } else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                        redirectLoginActivity();
                    } else {
                        Toast.makeText(context, getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                        Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_categories + " >> Exception >> ");
                    }

                } catch (Exception e) {
                    Toast.makeText(context, getString(R.string.unexpected_case_error) , Toast.LENGTH_LONG).show();
                    Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_categories + " >> Exception >> " + e.getStackTrace()[0].getLineNumber());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Crashlytics.log(Log.ERROR , Result.LOG_TAG_ERROR.getResultText() , this.getClass().getName() + " >> " + "Response.ErrorListener" + " >> " + Resource.ajax_get_categories + " >> Exception");
                Toast.makeText(context, getString(R.string.unexpected_network_error) , Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                params.put("parentId", String.valueOf(categoryId));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        stringRequest.setTag(this.getClass().getName());
        requestQueue.add(stringRequest);
    }

}
