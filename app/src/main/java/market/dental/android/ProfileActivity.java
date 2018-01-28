package market.dental.android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import market.dental.model.User;
import market.dental.util.Result;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView activity_profile_header_name = (TextView)findViewById(R.id.activity_profile_header_name);
        TextView activity_profile_header_mail = (TextView)findViewById(R.id.activity_profile_header_mail);
        TextView activity_profile_header_rate = (TextView)findViewById(R.id.activity_profile_header_rate);
        TextView activity_profile_content_name = (TextView)findViewById(R.id.activity_profile_content_name);
        TextView activity_profile_content_lastname = (TextView)findViewById(R.id.activity_profile_content_lastname);
        TextView activity_profile_profession = (TextView)findViewById(R.id.activity_profile_profession);


        // INITIALIZATION
        sharedPref = getSharedPreferences(getString(R.string.sp_dental_market), Context.MODE_PRIVATE);
        try {
            JSONObject userJsonObject = new JSONObject(sharedPref.getString(getString(R.string.sp_user_json_str) , ""));
            User user = new User(userJsonObject);
            activity_profile_header_name.setText(user.getFirst_name() + " " + user.getLast_name());
            activity_profile_header_mail.setText(user.getEmail());
            activity_profile_header_rate.setText(" ?? / 10");
            activity_profile_content_name.setText(user.getFirst_name());
            activity_profile_content_lastname.setText(user.getLast_name());
            activity_profile_profession.setText(user.getJob());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String[] cityList = {"Ankara" , "İstanbul" , "Kocaeli" , "İzmir" , "Bursa" , "Sakarya", "Kayseri" , "Konya" , "Antalya", "Eskişehir"};
        final String[] boroughList = {"Çankaya" , "Keçiören" , "Etimesgut" , "Sincan" };
        final String[] professionList = {"Diş Hekimliği" , "Doktor" , "Mühendis"};

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
