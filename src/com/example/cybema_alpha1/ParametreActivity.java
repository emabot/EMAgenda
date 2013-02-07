package com.example.cybema_alpha1;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ParametreActivity extends Activity {
	static String adresse = "http://webdfd.ema.fr/cybema/cgi-bin/cgiempt.exe?TYPE=";

	private static Activity activity;
	private boolean initOK = true;
	private Spinner spinnerPromo;
	private Button buttonEffacer;
	private int onItemSelectedNumero;
	private AutoCompleteTextView ACTV;
	private SharedPreferences preferences;

	private EMA ema = new EMA();
	private ArrayList<String> nomsDePromo = new ArrayList<String>();
	private ArrayList<String> nomsEleves = new ArrayList<String>();

	// Cycle de l activité
	// Tout ce qui est dans le onCreate() sera lancé au lancement de
	// l'application uniquement
	// si l'utilisateur passe à une autre application: onPause() est appelé
	// si il revient à notre application, onResume() sera appelé SAUF si le
	// systeme a tué l'application (besoin de ressources) alors onCreate() est
	// appelé

	// Dans le onCreate(), il y a le Bundle savedInstanceState, qui permet de
	// reprendre l'application à l'état ou
	// elle au cas ou le systeme la tuerait
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_main);
		init();

		// Je donne un nom aux différents élèments graphiques que je vais
		// utiliser
		ACTV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		spinnerPromo = (Spinner) findViewById(R.id.spinnerPromo);
		buttonEffacer = (Button) findViewById(R.id.buttonEffacer);

		// Définis les différentes interactions avec l'UI
		spinnerPromo.setOnItemSelectedListener(Spinnerlistener);
		ACTV.setOnItemClickListener(ACTVlistener);
		buttonEffacer.setOnClickListener(BElistener);

		// Adapte les noms de promos pour pouvoir les afficher
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, nomsDePromo);
		// Affiche les noms de promos
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPromo.setAdapter(dataAdapter);

		// Adapte les noms d'eleves pour pouvoir les afficher
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, nomsEleves);
		// Affiche les noms d'élèves
		AutoCompleteTextView actvDev = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		actvDev.setAdapter(adapter);
	}

	// Les listeners permettent à l'interface de me dire comment l utilisateur
	// interagit avec elle
	// LISTENER======================================================================
	private OnClickListener BElistener = new OnClickListener() {
		public void onClick(View v) {
			ACTV.dismissDropDown();
			ACTV.setText("");
		}
	};

	private OnItemClickListener ACTVlistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
			String nom = "" + ACTV.getText();
			for (int i = 0; i < nomsEleves.size(); i++) {
				if (nomsEleves.get(i).equals(nom)) {
					pos = i;
				}
			}
			String numEleve = ema.getPromos()
					.get(spinnerPromo.getSelectedItemPosition()).getEleves()
					.get(pos).getNumEleve();

			try {
				telechargeFichier("planning", Integer.parseInt(numEleve));
			} catch (Exception e) {
			}

			/*
			 * Le PreferenceManager permet d'enregistrer simplement les
			 * préférences de l'application, je l'utilise ici pour garder le nom
			 * et le numéro de l'élève
			 */
			preferences = PreferenceManager
					.getDefaultSharedPreferences(activity);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("NOM_ELEVE", nom);
			editor.putString("NUM_ELEVE", numEleve);
			editor.commit();

			finish();
		}
	};

	private OnItemSelectedListener Spinnerlistener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (onItemSelectedNumero != arg2 || initOK) {
				trouveNomTextView();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						activity, android.R.layout.simple_dropdown_item_1line,
						nomsEleves);
				AutoCompleteTextView actvDev = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
				actvDev.setAdapter(adapter);
				onItemSelectedNumero = arg2;
				initOK = false;
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	// endLISTENER======================================================================

	public void init() {
		try {
			openFileInput("promos.txt");
			openFileInput("eleves.txt");
		} catch (FileNotFoundException e) {
			Toast.makeText(ParametreActivity.this, "Initialisation...",
					Toast.LENGTH_SHORT).show();
			try {
				telechargeFichier("promos", 0);
				telechargeFichier("eleves", 0);
			} catch (Exception e1) {
			}
		}

		ema.remplirEMA();
		int nmbrPromo = ema.getPromos().size();
		for (int i = 0; i < nmbrPromo; i++) {
			nomsDePromo.add(ema.getPromos().get(i).getNom());
		}
	}

	public void trouveNomTextView() {
		int nmbrPromo = spinnerPromo.getSelectedItemPosition();
		ema.getPromos().get(nmbrPromo).setNomEleve();
		nomsEleves = ema.getPromos().get(nmbrPromo).getNomsEleves();
	}

	// Methode qui permet de telecharger les differents fichiers et de les
	// sauvegarder sur la memoire interne
	public void telechargeFichier(String nom, int numEleve)
			throws InterruptedException, ExecutionException {
		Toast.makeText(PresentationActivity.getContext(),
				"Chargement en cours...", Toast.LENGTH_SHORT).show();
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			if (!nom.equals("planning")) {
				AsyncTask<Object, Object, Object> a = new DownloadParametre()
						.execute(adresse + nom + "_txt", nom);
				a.get();
			} else {
				AsyncTask<Object, Object, Object> a = new DownloadParametre()
						.execute(adresse + nom + "_txt&DATEDEBUT="
								+ donnerDate(-7) + "&DATEFIN=" + donnerDate(7)
								+ "&TYPECLE=evcleunik&VALCLE=" + numEleve, nom);
				a.get();
			}

		} else {
			Toast.makeText(ParametreActivity.this,
					"Verifier votre connexion svp", Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String donnerDate(int nmbrDeJours) {
		String format = "yyyyMMdd";
		SimpleDateFormat formater = new SimpleDateFormat(format);
		Calendar calendrier = new GregorianCalendar();
		calendrier.add(Calendar.DAY_OF_MONTH, nmbrDeJours);
		return formater.format(calendrier.getTime());
	}

}
