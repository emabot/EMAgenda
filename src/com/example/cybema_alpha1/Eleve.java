package com.example.cybema_alpha1;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
/*
 * rien de compliqué dans cette classe
 */
public class Eleve{
	private String nom;
	private String prenom;
	private String numEleve;
	private ArrayList<Journee> journees = new ArrayList<Journee>();

	public String getNom() {
		return nom;
	}
	
	public ArrayList<Journee> getJournees(){
		return journees;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Override
	public String toString() {
		return "Eleve " + nom + ", prenom=" + prenom;
	}

	public void extractionNumEleve() {
		try {
			FileInputStream fis = PresentationActivity.getContext().openFileInput("eleves.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			String s;
			while ((s = bf.readLine()) != null) {
				String[] test = s.split(";");
				for (int loop = 0; loop < test.length; loop++) {
					if (test[loop].equals("NOM")
							&& test[loop + 1].startsWith(nom)) {
						for (int loop2 = 0; loop2 < loop; loop2++) {
							if (test[loop2].equals("EV")) {
								numEleve = test[loop2 + 1];
							}
						}
					}
				}
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		numEleve = null;
	}

	public String getNumEleve() {
		return numEleve;
	}

	public void setNumEleve(String numEleve) {
		this.numEleve = numEleve;
	}

	public void remplirJournees() {

		try {
			FileInputStream fis = PresentationActivity.getContext().openFileInput("planning.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);

			String s;
			String dateTemp = "";
			while ((s = bf.readLine()) != null) {
				String[] tableauString = s.split(";");
				for (int loop = 0; loop < tableauString.length; loop++) {
					if (tableauString[loop].equals("DATE")
							&& !dateTemp.equals(tableauString[loop + 1])) {
						dateTemp=tableauString[loop+1];
						journees.add(new Journee(tableauString[loop + 1]));
						break;
					}
				}
			}
		} catch (Exception e) {
		}

	}

	
	
	
	public Date donnerDateAujourdhui() {
		Date date = new Date();
		return date;
	}

	@SuppressLint("SimpleDateFormat")
	public String donnerStringDateAujourdhui() {
		String format = "yyyyMMdd";
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
				format);
		Date date = new Date();
		return formater.format(date);
	}
}
