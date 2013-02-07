package com.example.cybema_alpha1;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cybema_alpha1.SimpleGestureFilter.SimpleGestureListener;

@SuppressLint("SimpleDateFormat")
public class PresentationActivity extends FragmentActivity implements
		SimpleGestureListener {
	private ListView vue;
	private TextView tvDate;
	private TextView tvNomEleve;
	private static Activity context;
	private CoursAdapter adapter;
	private int numeroJournee = 5;
	private static SharedPreferences preferences;
	private SimpleGestureFilter detector;
	private Eleve e;
	private Handler mHandler = new Handler();
	boolean chargement = false;

	@Override
	/*
	 * onCreate() est appelé au lancement de l'application, j'y défini les
	 * différents élèments graphiques
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_presentation);
		tvNomEleve = (TextView) findViewById(R.id.tvNomEleve);
		tvDate = (TextView) findViewById(R.id.tvDate);
		vue = (ListView) findViewById(R.id.lvListe);
		detector = new SimpleGestureFilter(this, this);
	}

	/*
	 * onResume() est appelé juste après le onCreate(), ou si l'utilisateur
	 * passe à une autre application, en revenant à celle la il vérifie si c'est
	 * le premier lancement de l'appli, il ouvre les paramètres, sinon il
	 * récupère les données de la mémoire interne pour remplir l'agenda
	 */
	public void onResume() {
		super.onResume();
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (premierLancement()) {
			ouvrirParametre();
		} else {
			e = new Eleve();
			e.remplirJournees();
			rafraichirAffichageJournee();
			tvNomEleve.setText(preferences.getString("NOM_ELEVE", "Init..."));
		}

	}

	/*
	 * Permet d'ouvrir la boite de dialogue pour choisir une date précise
	 * (encore buggé: l'utilisateur doit manuellement relancer l'application
	 * pour raffraichir l'affichage)
	 */

	public void ouvrirDate(View v) {
		DialogFragment newFragment = (DialogFragment) new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	/*
	 * raffraichit l'affichage, si la journée n'est pas présente dans la memoire
	 * interne alors il la télécharge sur le serveur de l'école
	 */
	public void rafraichirAffichageJournee() {
		try {
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.anim2);

			ListView RelaL = (ListView) findViewById(R.id.lvListe);
			RelaL.clearAnimation();
			RelaL.setAnimation(animation);
			RelaL.startAnimation(animation);

			tvDate.setText(e.getJournees().get(numeroJournee).getDateHumain());
			adapter = new CoursAdapter(context, e.getJournees()
					.get(numeroJournee).getCours());
			vue.setAdapter(adapter);

		} catch (IndexOutOfBoundsException erreur) {
			int tailleJournee = e.getJournees().size();
			try {
				Date temp = new Date();
				if (numeroJournee >= tailleJournee) {
					temp = e.getJournees().get(tailleJournee - 1).getDateF();
				} else {
					if (numeroJournee < 0) {
						temp = e.getJournees().get(0).getDateF();
					}
				}
				telechargePlanning(temp);
				e = new Eleve();
				e.remplirJournees();
				numeroJournee = trouverNumeroJournee(temp);
				rafraichirAffichageJournee();
			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					Log.e("PresentationActivity", "", e);
				}
			}
		}
	}

	private int trouverNumeroJournee(Date temp) {
		int numeroJournee = 0;
		for (int i = 0; i < e.getJournees().size(); i++) {
			if (e.getJournees().get(i).getDateF().equals(temp)) {
				numeroJournee = i;
				break;
			}
		}
		return numeroJournee;
	}

	/*
	 * Deux méthodes qui permettent d'ouvrir les paramètres (lance l'activity
	 * ParametreActivity) la première nécessite un View est nécessaire pour le
	 * lancer à partir de l'interface graphique
	 */
	public void ouvrirParametre(View v) {
		Intent parametre = new Intent(context, ParametreActivity.class);
		startActivity(parametre);
	}

	public void ouvrirParametre() {
		Intent parametre = new Intent(context, ParametreActivity.class);
		startActivity(parametre);
	}

	/*
	 * Vérifie si c'est le premier lancement en regardant si le planning a déjà
	 * été téléchargé
	 */
	private boolean premierLancement() {
		try {
			openFileInput("planning.txt");
			return false;
		} catch (FileNotFoundException e) {
			return true;
		}
	}

	public static ContextWrapper getContext() {
		return context;
	}

	/*
	 * on peut décommenter ce qui suit pour avoir un menu qui s'ouvre quand on
	 * appuie sur la touche "menu"
	 */

	// Gestion du menu (obsolete)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_presentation, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_repas:
			donnerRepas();
			break;
		case R.id.google_agenda:
			Intent GoAgenda = new Intent(context, GoogleAgenda.class);
			startActivity(GoAgenda);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void donnerRepas() {
		Toast.makeText(getContext(), "Téléchargement en cours",
				Toast.LENGTH_SHORT).show();
		String url = "http://webdfd.mines-ales.fr/moodle/mod/resource/view.php?id=9507";
		WebView webview = new WebView(context);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
	}

	/*
	 * Télécharge le planning, ici j'ai mis sur une période d'un mois pour
	 * éviter de consommer de la batterie en téléchargant plusieurs petits
	 * fichiers de quelques octets (ce qui laisse la puce 3g/wifi allumé plus
	 * longtemps) (un mois <= 50 ko)
	 * 
	 */
	public void telechargePlanning(Date date) throws InterruptedException,
			ExecutionException {
		if (chargement) {
			return;
		} else {
			Toast.makeText(PresentationActivity.getContext(),
					"Chargement en cours...", Toast.LENGTH_SHORT).show();
			String format = "yyyyMMdd";
			SimpleDateFormat formater = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -7);
			String dateDebut = formater.format(cal.getTime());
			cal.add(Calendar.DATE, 23);
			String dateFin = formater.format(cal.getTime());

			preferences = PreferenceManager
					.getDefaultSharedPreferences(PresentationActivity
							.getContext());
			String numEleve = preferences.getString("NUM_ELEVE", "0");
			new DownloadText(this).execute(ParametreActivity.adresse
					+ "planning_txt&DATEDEBUT=" + dateDebut + "&DATEFIN="
					+ dateFin + "&TYPECLE=evcleunik&VALCLE=" + numEleve,
					"planning");
		}
	}

	/*
	 * La gestion des animations est encore un peu buggé donc n'est pas utiliser
	 * dans cette version
	 */
	public void animation(boolean directionGauche) {
		Animation animation;
		if (directionGauche) {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.anim_gauche);
		} else {
			animation = AnimationUtils.loadAnimation(context,
					R.anim.anim_droite);
		}
		ListView RelaL = (ListView) findViewById(R.id.lvListe);
		RelaL.clearAnimation();
		RelaL.setAnimation(animation);
		RelaL.startAnimation(animation);
		mHandler.postDelayed(new Runnable() {
			public void run() {
				rafraichirAffichageJournee();
			}
		}, 245);
	}

	// gestion des Gestures...
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			numeroJournee--;
			animation(false);
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			numeroJournee++;
			animation(true);
			break;
		}
	}

	public void onDoubleTap() {
	}
}
