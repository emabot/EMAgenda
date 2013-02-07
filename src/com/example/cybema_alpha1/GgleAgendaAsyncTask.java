package com.example.cybema_alpha1;

import java.io.IOException;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import android.os.AsyncTask;
import android.view.View;

abstract class GgleAgendaAsyncTask extends AsyncTask<Void, Void, Boolean> {

	final GoogleAgenda activity;
	final com.google.api.services.calendar.Calendar client;
	private final View progressBar;

	public GgleAgendaAsyncTask(GoogleAgenda activity) {
		this.activity = activity;
		client = activity.client;
		progressBar = activity.findViewById(R.id.g_agenda_refresh_progress);
	}

	protected void onPreExecute() {
		super.onPreExecute();
		activity.numAsyncTasks++;
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			doInBackground();
			return true;
		} catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
			activity.showGooglePlayServicesAvailabilityErrorDialog(availabilityException
					.getConnectionStatusCode());
		} catch (UserRecoverableAuthIOException e) {
			activity.startActivityForResult(e.getIntent(), GoogleAgenda.REQUEST_AUTHORIZATION);
		} catch (IOException e) {
		}
		return false;
	}
	
	protected final void onPostExecute(Boolean success){
		super.onPostExecute(success);
		if(0== --activity.numAsyncTasks){
			progressBar.setVisibility(View.GONE);
		}
		if(success){
			activity.refreshView();
		}
	}

	abstract protected void doInBackground() throws IOException;
}
