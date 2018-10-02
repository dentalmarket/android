package market.dental.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import market.dental.application.DentalMarketApplication;
import market.dental.model.Message;
import market.dental.receiver.ConnectionReceiver;
import market.dental.util.Resource;
import market.dental.util.Result;

/**
 * Created by kemalsamikaraca on 6.03.2018.
 */

public class BaseActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    private FirebaseAnalytics firebaseAnalytics;

    /***********************************************************************************************
     ***********************************************************************************************
     **                             ENCAPSULATION
     ***********************************************************************************************
     **********************************************************************************************/
    public BroadcastReceiver getMessageReceiver(){
        return messageReceiver;
    }


    /***********************************************************************************************
     ***********************************************************************************************
     **                             ACTIVITY METHODS
     ***********************************************************************************************
     **********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((messageReceiver),
                new IntentFilter(Resource.KEY_LOCAL_BROADCAST)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //check the network connectivity when activity is created
        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        DentalMarketApplication.getInstance().setConnectionListener(this);

        //check the network connectivity when activity is resumed
        checkConnection();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected) {

            //show a No Internet Alert or Dialog
        }else{

            // dismiss the dialog or refresh the activity
        }
    }


    public BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            String snackBarBody =  intent.getExtras().containsKey("fromName") ? intent.getExtras().getString("fromName") : getString(R.string.new_campaigns_added);

            final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), snackBarBody , Snackbar.LENGTH_LONG);
            final String fromId = intent.getExtras().getString("fromId");
            final String catId = intent.getExtras().getString("catId");
            View snackView = snack.getView();
            TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(5);
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);

            snack.setAction("AÇ", new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if( intent.getExtras().containsKey("fromId")){
                        Bundle bundle = new Bundle();
                        bundle.putString(Resource.KEY_MESSAGE_RECEIVER_ID, fromId);
                        Intent intent = new Intent(view.getContext(),MessageListActivity.class);
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }else if(intent.getExtras().containsKey("catId")){
                        Bundle bundle = new Bundle();
                        bundle.putString(Resource.KEY_MESSAGE_CATEGORY_ID, catId);
                        Intent intent = new Intent(view.getContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }

                }
            });
            snack.setDuration(5000);
            snack.show();
        }
    };



    /***********************************************************************************************
     ***********************************************************************************************
     **                             UTIL METHODS
     ***********************************************************************************************
     **********************************************************************************************/
    public void redirectLoginActivity(){
        Resource.setDefaultAPITOKEN();
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected();
        if(!isConnected) {
            //show a No Internet Alert or Dialog
            Log.i(Result.LOG_TAG_INFO.getResultText(),"Internet connection ERROR");
        }
    }

    public void volleyOnErrorResponse(VolleyError error , StringBuilder errorMessage){
        // print error to stackTrace
        error.printStackTrace();

        try{
            NetworkResponse networkResponse = error.networkResponse;
            if(networkResponse!=null && networkResponse.data!=null){
                errorMessage.append("\n" + this.getClass().getName() + " >> " + "volleyOnErrorResponse" + " >>  networkResponse.statusCode:" +networkResponse.statusCode);
                JSONObject response = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                    redirectLoginActivity();
                }else{
                    errorMessage.append("\n" + this.getClass().getName() + " >> " + "volleyOnErrorResponse" + " >> UNEXPECTED ERROR");
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorMessage.append("\n" + this.getClass().getName() + " >> " + "volleyOnErrorResponse" + " >> UnsupportedEncodingException");

        } catch (Exception e){
            e.printStackTrace();
            errorMessage.append("\n" + this.getClass().getName() + " >> " + "volleyOnErrorResponse" + " >> Exception");
        } finally {
            Crashlytics.log(Log.ERROR ,Result.LOG_TAG_INFO.getResultText() ,errorMessage.toString());
        }

    }
}
