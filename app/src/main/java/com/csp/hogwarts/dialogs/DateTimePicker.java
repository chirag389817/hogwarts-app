package com.csp.hogwarts.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.csp.hogwarts.R;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class DateTimePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final OnDateTimeSetListener onDateTimeSetListener;
    private LocalDateTime localDateTime;
    private Calendar dateTime;
    public DateTimePicker(OnDateTimeSetListener onDateTimeSetListener){
        this.onDateTimeSetListener = onDateTimeSetListener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dateTime = Calendar.getInstance();
        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH);
        int day = dateTime.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.Theme_Hogwarts_Dialog, this, year, month, day);
        datePickerDialog.setCanceledOnTouchOutside(false);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateTime.set(year, month, dayOfMonth);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.ofInstant(dateTime.toInstant(), dateTime.getTimeZone().toZoneId());
        }
        onDateTimeSetListener.onDateTimeSet(localDateTime);
    }

    public LocalDateTime getDateTime() {
        return localDateTime;
    }

    public void reset(){
        localDateTime = null;
    }

    public interface OnDateTimeSetListener{
        void onDateTimeSet(LocalDateTime dateTime);
    }
}
