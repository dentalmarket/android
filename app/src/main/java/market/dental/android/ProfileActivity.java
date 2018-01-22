package market.dental.android;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import market.dental.util.Result;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] cityList = {"Ankara" , "İstanbul" , "Kocaeli" , "İzmir" , "Bursa" , "Sakarya", "Kayseri" , "Konya" , "Antalya", "Eskişehir"};
        final String[] boroughList = {"Çankaya" , "Keçiören" , "Etimesgut" , "Sincan" };
        final String[] professionList = {"Diş Hekimliği" , "Doktor" };

        TextView cityText = findViewById(R.id.activity_profile_city_text);
        cityText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("Şehir seçiniz");
                mBuilder.setItems(cityList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "dedede");
                    }
                });
                mBuilder.show();
            }
        });


        TextView boroughText = findViewById(R.id.activity_profile_borough_text);
        boroughText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("İlçe seçiniz");
                mBuilder.setItems(boroughList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "meslek seçildi");
                    }
                });
                mBuilder.show();
            }
        });


        TextView professionText = findViewById(R.id.activity_profile_profession);
        professionText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
                mBuilder.setTitle("Mesleğinizi seçiniz");
                mBuilder.setItems(professionList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "meslek seçildi");
                    }
                });
                mBuilder.show();
            }
        });


        TextView birthdayText = findViewById(R.id.activity_profile_birthday);
        birthdayText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ProfileActivity.this , R.style.DialogTheme , new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.i(Result.LOG_TAG_INFO.getResultText() , "DENEME datepicker");
                    }
                } , year , month, day);

                mDatePicker.show();
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
