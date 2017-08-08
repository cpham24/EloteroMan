package edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.calstatela.cpham24.eloteroman.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Johnson on 8/1/2017.
 */

public class AdvancedSearchFrag extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText cart;
    private EditText vend;
    private EditText street;
    private EditText food;
    private Spinner leftTime;
    private Spinner rightTime;
    private Spinner curTimeOne;
    private Spinner curTimeTwo;
    private Spinner theDay;
    private TextView min;
    private TextView max;
    private Spinner minRate;
    private Spinner maxRate;
    private Button searchEm;

    public AdvancedSearchFrag() {

    }

    public interface OnDialogCloseListener {
        void closeDialog(String ca, String ve, String st, String fo, String le, String ri, String on, String tw, String dw);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.adv, container, false);
        cart = (EditText) view.findViewById(R.id.whichCartName);
        vend = (EditText) view.findViewById(R.id.whichVendName);
        street = (EditText) view.findViewById(R.id.AddressSpot);
        food = (EditText) view.findViewById(R.id.whichFood);

        min = (TextView) view.findViewById(R.id.minText);
        max = (TextView) view.findViewById(R.id.maxText);

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

        //choose days
        theDay = (Spinner) view.findViewById(R.id.dayWork);
        ArrayAdapter<CharSequence> spindapterFour = ArrayAdapter.createFromResource(view.getContext(),
                R.array.theDayMan, android.R.layout.simple_spinner_item);
        spindapterFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theDay.setAdapter(spindapterFour);
        theDay.setOnItemSelectedListener(this);

        //min
        minRate = (Spinner) view.findViewById(R.id.minRate);
        ArrayAdapter<CharSequence> spindapterFive = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rateNum, android.R.layout.simple_spinner_item);
        spindapterFive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minRate.setAdapter(spindapterFive);
        minRate.setOnItemSelectedListener(this);

        //max
        maxRate = (Spinner) view.findViewById(R.id.maxRate);
        ArrayAdapter<CharSequence> spindapterSix = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rateNum, android.R.layout.simple_spinner_item);
        spindapterSix.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxRate.setAdapter(spindapterSix);
        maxRate.setOnItemSelectedListener(this);

        searchEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();

                String ca = cart.getText().toString();
                String ve = vend.getText().toString();
                String st = street.getText().toString();
                String fo = food.getText().toString();

                String le = leftTime.getSelectedItem().toString();
                String ri = rightTime.getSelectedItem().toString();
                String on = curTimeOne.getSelectedItem().toString();
                String tw = curTimeTwo.getSelectedItem().toString();
                String dw = curTimeTwo.getSelectedItem().toString();

                Log.d(TAG, " where " + ca);

                activity.closeDialog(ca, ve, st, fo, le, ri, on, tw, dw);
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
