package com.example.cybema_alpha1;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

/*
 * Le gestionnaire de téléchargement est asynchrone pour ne pas bloquer le thread UI pendant le téléchargement
 */

public class DownloadText extends AsyncTask<Object, Object, Object> {
	final PresentationActivity activity;
	private final View progressBar;

	public DownloadText(PresentationActivity activity) {
		this.activity = activity;
		progressBar = activity.findViewById(R.id.presentation_progress_bar);
	}

	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
		activity.chargement = true;
	}

	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		progressBar.setVisibility(View.GONE);
		activity.chargement = false;
		activity.rafraichirAffichageJournee();
	}

	protected Object doInBackground(Object... urls) {
		// params comes from the execute() call: params[0] is the url.
		try {
			return downloadUrl((String) urls[0], (String) urls[1]);
		} catch (IOException e) {
			return "Impossible de récupérer la page. URL invalide.";
		}
	}

	// // onPostExecute displays the results of the AsyncTask.
	// protected void onPostExecute(String result) {
	// Toast.makeText(ParametreActivity.this, result, Toast.LENGTH_SHORT)
	// .show();
	// }

	private String downloadUrl(String myurl, String nom) throws IOException {

		InputStream is = null;
		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();

			is = conn.getInputStream();
			InputStreamReader ff = new InputStreamReader(is, "ISO-8859-1");
			BufferedReader bf = new BufferedReader(ff);
			String s;
			FileOutputStream fos = PresentationActivity.getContext()
					.openFileOutput(nom + ".txt", Context.MODE_PRIVATE);
			while ((s = bf.readLine()) != null) {
				fos.write((s + "\n").getBytes());
			}
			fos.close();
			bf.close();
			ff.close();
			return "ok";
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}
