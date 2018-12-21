package market.dental.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import market.dental.android.R;
import market.dental.model.Currency;

public class OfferDialog extends AppCompatDialogFragment {

    private OfferDialogListener listener;
    private EditText offerDesc;
    private EditText offerPrice;
    private int offerCurrency = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DentalMarket_AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.offer_dialog_layout,null);

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_offer_currency);
        offerDesc = view.findViewById(R.id.dialog_offer_desc);
        offerPrice = view.findViewById(R.id.dialog_offer_price);

        List<String> currencyList = new ArrayList<String>();
        currencyList.add("TL");
        currencyList.add("USD");
        currencyList.add("EUR");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, currencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: hızlı bir çözüm oldu değiştirilmesi gerekebilir
                // Ayrıca spinner içerisine konulan değerler static bir yerden alınmalıdır
                offerCurrency = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setView(view)
                .setTitle("Teklif Ver")
                .setPositiveButton("Teklif Ver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyOfferDialogValues(offerDesc.getText().toString() , Integer.parseInt(offerPrice.getText().toString()), offerCurrency);
                    }
                })
                .setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (OfferDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implemented OfferDialogListener");
        }
    }

    public interface OfferDialogListener{
        void applyOfferDialogValues(String desc, int price, int currency);
    }
}
