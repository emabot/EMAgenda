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

public class DownloadParametre extends AsyncTask<Object, Object, Object> {
		protected Object doInBackground(Object... urls) {
			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl((String) urls[0], (String) urls[1]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		// // onPostExecute displays the results of the AsyncTask.
		// protected void onPostExecute(String result) {
		// Toast.makeText(ParametreActivity.this, result, Toast.LENGTH_SHORT)
		// .show();
		// }

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl, String nom) throws IOException {
		InputStream is = null;
		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();

			is = conn.getInputStream();
			InputStreamReader ff = new InputStreamReader(is, "ISO-8859-1");
			BufferedReader bf = new BufferedReader(ff);
			String s;
			FileOutputStream fos = PresentationActivity.getContext().openFileOutput(nom + ".txt",
					Context.MODE_PRIVATE);
			while ((s = bf.readLine()) != null) {
				fos.write((s + "\n").getBytes());
			}
			fos.close();
			bf.close();
			ff.close();
			return "ok";

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}

