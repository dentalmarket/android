package market.dental.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import market.dental.util.Resource;

public class ProductDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get productId
        Intent intent = getIntent();
        String productDesc = intent.getStringExtra(Resource.KEY_PRODUCT_DESC);
        Toast.makeText(this,"Ürün Açıklaması" , Toast.LENGTH_LONG).show();

        WebView webView = findViewById(R.id.activity_product_description_web_view);
        webView.loadDataWithBaseURL("null", productDesc,"text/html", "UTF-8",null);
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
}
