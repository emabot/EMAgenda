package com.example.cybema_alpha1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;

public final class GoogleAgenda extends Activity {

	GoogleAccountCredential credential;
	private static final String PREF_ACCOUNT_NAME = "accountName";
	com.google.api.services.calendar.Calendar client;
	final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	final JsonFactory jsonFactory = new GsonFactory();
	static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	static final int REQUEST_AUTHORIZATION = 1;

	int numAsyncTasks;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_agenda_layout);
		credential = GoogleAccountCredential.usingOAuth2(this,
				CalendarScopes.CALENDAR);
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME,
				null));
		client = new com.google.api.services.calendar.Calendar.Builder(
				transport, jsonFactory, credential).setApplicationName(
				"EMAgenda").build();
	}

	protected void onResume() {
		super.onResume();
		if (checkGooglePlayServicesAvailable()) {
			haveGooglePlayServices();
		}
	}

	private void haveGooglePlayServices() {
		if (credential.getSelectedAccount() == null) {
			chooseAccount();
		} else {
		}
	}

	private void chooseAccount() {
		startActivityForResult(credential.newChooseAccountIntent(),
				REQUEST_ACCOUNT_PICKER);
	}

	private boolean checkGooglePlayServicesAvailable() {
		final int connectionStatusCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
			showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
			return false;
		}
		return true;
	}

	void showGooglePlayServicesAvailabilityErrorDialog(
			final int connectionStatusCode) {
		runOnUiThread(new Runnable() {
			public void run() {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						connectionStatusCode, GoogleAgenda.this,
						REQUEST_GOOGLE_PLAY_SERVICES);
				dialog.show();

			}
		});
	}
	
	void refreshView(){
		
	}
}
