package market.dental.model;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;

/**
 * Created by kemalsamikaraca on 20.01.2018.
 */

public class Currency {


    public static String getCurrencyString(Resources resources , int id){

        switch (id){
            case 1:
                return resources.getString(R.string.currency_turkish_lira);
            case 2:
                return resources.getString(R.string.currency_usd);
            case 3:
                return resources.getString(R.string.currency_euro);
            default:
                return resources.getString(R.string.currency_usd);
        }
    }

}
