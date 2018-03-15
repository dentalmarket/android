package market.dental.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import market.dental.model.Message;
import market.dental.util.Resource;

/**
 * Created by kemalsamikaraca on 6.03.2018.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((messageReceiver),
                new IntentFilter(Resource.KEY_LOCAL_BROADCAST)
        );
    }

    public BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), intent.getExtras().getString("fromName" ) , Snackbar.LENGTH_LONG);
            final String fromId = intent.getExtras().getString("fromId");
            View snackView = snack.getView();
            TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(5);
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);

            snack.setAction("AÇ", new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Resource.KEY_MESSAGE_RECEIVER_ID, fromId);
                    Intent intent = new Intent(view.getContext(),MessageListActivity.class);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
            snack.setDuration(5000);
            snack.show();
        }
    };

    public BroadcastReceiver getMessageReceiver(){
        return messageReceiver;
    }

    public void redirectLoginActivity(){
        Resource.setDefaultAPITOKEN();
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
