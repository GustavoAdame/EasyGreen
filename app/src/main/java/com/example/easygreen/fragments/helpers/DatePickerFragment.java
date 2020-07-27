package com.example.easygreen.fragments.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static String currentDateString;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime()) != null){
            currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        }
    }
}
