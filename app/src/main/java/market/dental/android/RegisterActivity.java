package market.dental.android;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import market.dental.adapter.ProfessionListAdapter;
import market.dental.model.Profession;
import market.dental.util.Result;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] cityList = {"Ankara" , "İstanbul" , "Kocaeli" , "İzmir" , "Bursa" , "Sakarya", "Kayseri" , "Konya" , "Antalya", "Eskişehir"};
        final String[] boroughList = {"Çankaya" , "Keçiören" , "Etimesgut" , "Sincan" };
        final String[] professionList = {"Diş Hekimliği" , "Doktor" , "Mühendis"};

        Profession proOne = new Profession(null);
        proOne.setId(1);
        proOne.setName("Diş Hekimlipi");
        Profession proTwo = new Profession(null);
        proTwo.setId(2);
        proTwo.setName("Doktor");
        List<Profession> myProfessionList = new ArrayList<>();
        myProfessionList.add(proOne);
        myProfessionList.add(proTwo);

        final ProfessionListAdapter pListAdapter = new ProfessionListAdapter(this);
        pListAdapter.setProfessionList(myProfessionList);


        EditText professionTextView = findViewById(R.id.activity_register_job);
        professionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
                mBuilder.setTitle("Mesleğinizi seçiniz");

                mBuilder.setAdapter(pListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "Selected profession ID " +  ((Profession)pListAdapter.getItem(which)).getId());
                    }
                });
                mBuilder.show();
            }
        });
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
