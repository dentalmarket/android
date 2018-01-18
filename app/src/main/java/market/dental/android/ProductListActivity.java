package market.dental.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import market.dental.adapter.ProductListAdapter;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ProductListAdapter listAdapter = new ProductListAdapter(this);
        ListView listView = findViewById(R.id.activity_product_list_main);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String x = String.valueOf(parent.getItemAtPosition(position));
                    Toast.makeText(ProductListActivity.this,x,Toast.LENGTH_LONG).show();
                }
            }
        );
    }
}
