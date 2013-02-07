package com.example.cybema_alpha1;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/*
 * Permet d'afficher une boite de dialogue pour choisir la date
 * Quand elle est choisit, onDateSet() est appelé
 */
public class TimePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Utilise la date d aujourd hui par defaut
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
		try {
//			PresentationActivity.telechargePlanning(date.getTime());
		} catch (Exception e) {
		}
	}
}