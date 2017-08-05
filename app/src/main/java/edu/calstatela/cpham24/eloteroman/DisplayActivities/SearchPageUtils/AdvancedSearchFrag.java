package com.example.johnson.elosearch;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Johnson on 8/1/2017.
 */

public class AdvancedSearchFrag extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText cart;
    private EditText vend;
    private EditText food;
    private Spinner leftTime;
    private Spinner rightTime;
    private Spinner curTimeOne;
    private Spinner curTimeTwo;
    private Spinner theDay;
    private Button searchEm;

    public AdvancedSearchFrag() {

    }

    public interface OnDialogCloseListener {
        void closeDialog(String ca, String ve, String fo, String le, String ri, String on, String tw);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.adv, container, false);
        cart = (EditText) view.findViewById(R.id.whichCartName);
        vend = (EditText) view.findViewById(R.id.whichVendName);
        food = (EditText) view.findViewById(R.id.whichFood);

        searchEm = (Button) view.findViewById(R.id.advFindEm);


        // left
        leftTime = (Spinner) view.findViewById(R.id.leftT);
        ArrayAdapter<CharSequence> spindapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.numberTime, android.R.layout.simple_spinner_item);
        spindapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leftTime.setAdapter(spindapter);
        leftTime.setOnItemSelectedListener(this);

        // right
        rightTime = (Spinner) view.findViewById(R.id.rightT);
        ArrayAdapter<CharSequence> spindapterOne = ArrayAdapter.createFromResource(view.getContext(),
                R.array.numberTime, android.R.layout.simple_spinner_item);
        spindapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightTime.setAdapter(spindapterOne);
        rightTime.setOnItemSelectedListener(this);

        // left for time
        curTimeOne = (Spinner) view.findViewById(R.id.APMO);
        ArrayAdapter<CharSequence> spindapterTwo = ArrayAdapter.createFromResource(view.getContext(),
                R.array.timeOfDay, android.R.layout.simple_spinner_item);
        spindapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        curTimeOne.setAdapter(spindapterTwo);
        curTimeOne.setOnItemSelectedListener(this);

        // right for time
        curTimeTwo = (Spinner) view.findViewById(R.id.APMT);
        ArrayAdapter<CharSequence> spindapterThree = ArrayAdapter.createFromResource(view.getContext(),
                R.array.timeOfDay, android.R.layout.simple_spinner_item);
        spindapterThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        curTimeTwo.setAdapter(spindapterThree);
        curTimeTwo.setOnItemSelectedListener(this);


        searchEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();

                String ca = cart.getText().toString();
                String ve = vend.getText().toString();
                String fo = food.getText().toString();

                String le = leftTime.getSelectedItem().toString();
                String ri = rightTime.getSelectedItem().toString();
                String on = curTimeOne.getSelectedItem().toString();
                String tw = curTimeTwo.getSelectedItem().toString();

                //activity.closeDialog(ca, ve, fo, le, ri, on, tw);
                AdvancedSearchFrag.this.dismiss();
            }
        });



        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
