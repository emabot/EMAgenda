package com.example.cybema_alpha1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Journee {
	private ArrayList<Cours> cours = new ArrayList<Cours>();
	private int nbrDeCours;
	private String date;
	
	public int getNbrDeCours(){
		return nbrDeCours;
	}
	
	public String getDate(){
		return date;
	}
	
	public Date getDateF(){
		DateFormat df = new SimpleDateFormat("yyyyMMdd"); 
		Date date=null;
		try
		{
		  date= df.parse(this.date);
		} catch (ParseException e){
		} 
		return date;	
	}
	

	public String getDateHumain(){
	    SimpleDateFormat formater = new SimpleDateFormat("EEEE dd MMMMM yyyy");
	    return formater.format(getDateF());
	}
	
	public ArrayList<Cours> getCours(){
		return cours;
	}

	public Journee(String date) {
		this.date = date;
		nbrDeCours = trouverNbrDeCours();
		remplirJourneeDeCours();
	}
	/*
	 * A modifier: trouver une méthode moins lourde que le split de String, qui bouffe beaucoup de CPU
	 */
	public int trouverNbrDeCours() {
		try {
			FileInputStream fis = PresentationActivity.getContext()
					.openFileInput("planning.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			int tailleCours = 0;

			String s;

			while ((s = bf.readLine()) != null) {
				String[] tableauString = s.split(";");
				for (int loop = 0; loop < tableauString.length; loop++) {
					if (tableauString[loop].equals("DATE")
							&& date.equals(tableauString[loop + 1])) {
						tailleCours++;
						break;
					}
				}
			}
			bf.close();
			return tailleCours;
		} catch (Exception e) {
		}
		return 0;
	}
	
	/*
	 * méthode a remanier car elle prend 80% du temps de calcul lors du chargement du planning
	 */
	public void remplirJourneeDeCours() {
		for (int loop1 = 0; loop1 < nbrDeCours; loop1++) {
			cours.add(new Cours());
		}

		try {
			FileInputStream fis = PresentationActivity.getContext()
					.openFileInput("planning.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			String s;
			int increment = 0;

			while ((s = bf.readLine()) != null) {
				String[] tableauString = s.split(";");
				boolean estPresent = false;

				for (int i = 0; i < tableauString.length; i++) {
					if (date.equals(tableauString[i])) {
						estPresent = true;
						break;
					}
				}

				if (estPresent) {

					for (int loop = 0; loop < tableauString.length; loop++) {
						if (tableauString[loop].startsWith("COURS")) {
							cours.get(increment).setNomCours(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("SALLE")) {
							cours.get(increment).setSalle(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("HD")) {
							cours.get(increment).setHeureDebut(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("TYPE")) {
							cours.get(increment).setType(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("HF")) {
							cours.get(increment).setHeureFin(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("PROF")) {
							cours.get(increment).setNomProf(
									tableauString[loop + 1]);
						}else if (tableauString[loop].startsWith("LANOTE")) {
							cours.get(increment).setLaNote(
									tableauString[loop + 1]);
						}
					}
					increment++;
				}

			}
			bf.close();
		} catch (Exception e) {
		}
		triCours();
	}

	@Override
	public String toString() {
		return date + ", " + nbrDeCours + " cours: \n" + cours;
	}

	@SuppressWarnings("unchecked")
	public void triCours() {
		Collections.sort(cours);
	}

}
