package com.example.cybema_alpha1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EMA {
	private ArrayList<Promo> EMA = new ArrayList<Promo>();
	
	
	public ArrayList<Promo> getPromos(){
		return EMA;
	}

	public int trouverNbrDePromos(){
		try {
			FileInputStream fis = PresentationActivity.getContext().openFileInput("promos.txt");
			InputStreamReader ff = new InputStreamReader(fis);
			BufferedReader bf = new BufferedReader(ff);
			int taillePromo = -1;
			while (bf.readLine() != null) {
				taillePromo++;
			}
			fis.close();
			ff.close();
			bf.close();
			return taillePromo;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void initEMA() {
		int nmbrPromo = trouverNbrDePromos();
		for (int i = 0; i < nmbrPromo; i++) {
			Promo test = new Promo();
			EMA.add(test);
		}
	}

	public void remplirEMA() {
		try {
			String s;
			initEMA();
			FileInputStream fis = PresentationActivity.getContext().openFileInput("promos.txt");
			InputStreamReader ff = new InputStreamReader(fis, "UTF-8");
			BufferedReader bf = new BufferedReader(ff);
			int increment = 0;
			while ((s = bf.readLine()) != null) {
				String[] test = s.split(";");
				for (int loop = 0; loop < test.length; loop++) {
					if (test[loop].equals("NOM")) {
						EMA.get(increment).setNom(test[loop + 1]);
					}
					if (test[loop].equals("P0")) {
						EMA.get(increment).setNumPromo(test[loop + 1]);
					}
				}
				increment++;
			}
			bf.close();
			ff.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
