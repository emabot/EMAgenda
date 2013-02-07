package com.example.cybema_alpha1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Promo {
	@Override
	public String toString() {
		return "Promo: " + nomPromo + ", numero de promo:" + numPromo;
	}
	private String nomPromo;
	private String numPromo;
	private ArrayList<Eleve> Eleves = new ArrayList<Eleve>();
	private ArrayList<String> nomsEleves = new ArrayList<String>();
	
	public String getNom() {
		return nomPromo;
	}
	
	public ArrayList<Eleve> getEleves(){
		return Eleves;
	}
	
	public ArrayList<String> getNomsEleves(){
		return nomsEleves;
	}
	
	public void setNom(String nom) {
		this.nomPromo = nom;
	}
	public String getNumPromo() {
		return numPromo;
	}
	public void setNumPromo(String numPromo) {
		this.numPromo = numPromo;
	}

	public void initEleve(){
		int nmbrEleve=trouverNmbrEleve();
		for(int i=0;i<nmbrEleve;i++){
			Eleve Tempo = new Eleve();
			Eleves.add(Tempo);
		}
	}
	
	public int trouverNmbrEleve(){
		try {
			FileInputStream fis = PresentationActivity.getContext().openFileInput("eleves.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			String s;
			int increment=0;
			while ((s = bf.readLine()) != null) {
				String[] test = s.split(";");
				for (int loop = 0; loop < test.length; loop++) {
					if (test[loop].equals("P0")&& test[loop+1].startsWith(numPromo)) {
					increment++;
					}
				}
			}
			bf.close();
			return increment;
		} catch (Exception e) {
		}
		return 0;	
		
	}
	
	/*
	 * récupere le nom et prénom de l'élève, cette méthode nécessite un remaniement car
	 * boucle infinie si le planning est mal téléchargé, et est très mal codé
	 */
	public void setNomEleve(){
		if(nomsEleves.size()!=0) return;
		try {
			FileInputStream fis = PresentationActivity.getContext().openFileInput("eleves.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			initEleve();
			String s;
			int increment=0;
			while ((s = bf.readLine()) != null) {
				String[] test = s.split(";");
				for (int loop = 0; loop < test.length; loop++) {
					if (test[loop].equals("P0")&& test[loop+1].equals(numPromo+" ")) {
						int loop2=0;
						while (!test[loop2].equals("NOM")) {
							loop2++;
						}
						Eleves.get(increment).setNom(test[loop2+1]);
						loop2=0;
						while (!test[loop2].equals("PRENOM")) {
							loop2++;
						}
						Eleves.get(increment).setPrenom(test[loop2+1]);
						loop2=0;
						while (!test[loop2].equals("EV")) {
							loop2++;
						}
						Eleves.get(increment).setNumEleve(test[loop2+1]);
						increment++;
					}
				}
				}
			remplirTableauNomsEleves();
			bf.close();
		} catch (Exception e) {
		}	
		
	}
	
	public void remplirTableauNomsEleves(){
		for(int i=0;i<Eleves.size();i++){
			nomsEleves.add(Eleves.get(i).getNom() + Eleves.get(i).getPrenom());
		}
	}
	
	public void lireNomEleve(){
		for(int i=0;i<Eleves.size();i++){
			System.out.println(Eleves.get(i));
		}
	}
}
