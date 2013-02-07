package com.example.cybema_alpha1;

@SuppressWarnings("rawtypes")
public class Cours implements java.lang.Comparable {
	private String nomCours;
	private String heureDebut;
	private String heureFin;
	private String salle;
	private String nomProf;
	private String laNOTE;
	private String type;
	
	public String getlaNOTE(){
		return laNOTE;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Cours() {
		this.nomCours = "nomCours";
		this.heureDebut = "heureDebut";
		this.heureFin = "heureFin";
		this.salle = "salle";
		this.nomProf = "nomProf";
	}

	@Override
	public String toString() {
		return nomCours + ", de " + transformerHeure(heureDebut) + " à "
				+ transformerHeure(heureFin) + " en salle " + salle + " avec "
				+ nomProf + "\n";
	}
	
	public String getTrancheHoraire(){
		return transformerHeure(heureDebut)+"-"+transformerHeure(heureFin);
	}
	public void setNomCours(String nomCours) {
		this.nomCours = nomCours;
	}

	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}

	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}

	public void setSalle(String salle) {
		this.salle = salle;
	}

	public void setNomProf(String nomProf) {
		this.nomProf = nomProf;
	}
	
	public void setLaNote(String lanote){
		laNOTE=lanote;
	}
	public String transformerHeure(String heureTemp) {
		String heure = heureTemp.substring(0, 2) + "h" + heureTemp.substring(2);
		return heure;
	}

	public String getNomCours() {
		if(nomCours.equals("- ")) {
			return "";
		}
		return nomCours;
	}

	public String getHeureDebut() {
		return heureDebut;
	}

	public String getHeureFin() {
		return heureFin;
	}

	public String getSalle() {
		if(salle.contains("-")){
			return "";
		}
		return salle;
	}

	public String getNomProf() {
		if(nomProf.contains("-")) {
			return "";
		}
		return nomProf;
	}

	
//Permet de comparer les heures pour trier les cours 
	public int compareTo(Object o) {
		int nombre1 = ((Cours) o).getValeurHeureDebut();
		int nombre2 = this.getValeurHeureDebut();
		if (nombre1 > nombre2)
			return -1;
		else if (nombre1 == nombre2)
			return 0;
		else
			return 1;
	}
	
	public int getValeurHeureDebut() {
		int val = Integer.parseInt(heureDebut);
		return val;
	}
}
