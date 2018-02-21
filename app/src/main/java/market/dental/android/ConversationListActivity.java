package market.dental.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import market.dental.adapter.ConversationListAdapter;

public class ConversationListActivity extends AppCompatActivity {

    private ConversationListAdapter conversationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        List<String> conversationList = new ArrayList<>();
        conversationList.add("conversation 1");
        conversationList.add("conversation 2");
        conversationList.add("conversation 3");
        conversationList.add("conversation 4");
        conversationList.add("conversation 5");

        conversationListAdapter = new ConversationListAdapter(this);
        conversationListAdapter.setConversationList(conversationList);

        ListView listView = findViewById(R.id.activity_conversation_list_main);
        listView.setAdapter(conversationListAdapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //int productId = ((Product) parent.getItemAtPosition(position)).getId();
                        //Bundle bundle = new Bundle();
                        //bundle.putInt(Resource.KEY_PRODUCT_ID, productId);
                        //Intent intent = new Intent(view.getContext(),ProductDetailActivity.class);
                        //intent.putExtras(bundle);
                        //view.getContext().startActivity(intent);

                        Toast.makeText(view.getContext() , "DENEME ", Toast.LENGTH_LONG).show();
                    }
                }
        );
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
