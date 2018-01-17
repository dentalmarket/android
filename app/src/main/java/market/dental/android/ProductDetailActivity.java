package market.dental.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductDetailActivity extends AppCompatActivity {


    private Context productDetailContext;
    private ImageView  pdImageView;
    private TextView pdProductName;
    private TextView storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productDetailContext = this.getApplicationContext();

        RequestQueue rq = Volley.newRequestQueue(this);
        pdImageView = (ImageView)findViewById(R.id.activity_product_detail_image);
        pdProductName = (TextView)findViewById(R.id.activity_product_detail_product_name);

        storeName = (TextView)findViewById(R.id.activity_product_detail_store_name);

        // *****************************************************************************************
        //                              GET PRODUCT DETAIL
        // *****************************************************************************************
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Resource.ajax_get_product_detail_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),response.toString());

                try {

                    // result objesinin kontrolü YAPILACAK
                    // result objesinin content değeri alınır
                    JSONObject content = response.getJSONObject("content");

                    pdProductName.setText((String) content.getString("name"));
                    Picasso.with(productDetailContext)
                            .load((String) content.getJSONArray("images").get(0))
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(pdImageView);

                    JSONObject store = content.getJSONObject("store");
                    storeName.setText(store.getString("name"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ERROR ON GET DATA");
            }
        }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("id", "XXX");
                return params;
            }
        };
        rq.add(jsonObjectRequest);

    }
}
