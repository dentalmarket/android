package market.dental.application;

import android.app.Application;

import market.dental.receiver.ConnectionReceiver;

/**
 * Created by kemalsamikaraca on 16.03.2018.
 */

public class DentalMarketApplication extends Application {

    private static DentalMarketApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized DentalMarketApplication getInstance() {
        return mInstance;
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}
