package com.example.cybema_alpha1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
 * Permet de créer l'interface graphique contenant les informations du cours
 */
public class CoursAdapter extends BaseAdapter {

	List<Cours> cours;
	LayoutInflater inflater;

	public CoursAdapter(Context context, List<Cours> cours) {
		inflater = LayoutInflater.from(context);
		this.cours = cours;
	}

	public int getCount() {
		return cours.size();
	}

	public Object getItem(int position) {
		return cours.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView tvNomDuCours;
		TextView tvNomDeLaSalle;
		TextView tvNomDuProf;
		TextView tvHeure;
		TextView tvLaNOTE;
		TextView tvType;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.cours_adapter, null);

			holder.tvNomDuCours = (TextView) convertView
					.findViewById(R.id.tvNomDuCours);
			holder.tvType = (TextView) convertView
					.findViewById(R.id.tvType);
			holder.tvNomDeLaSalle = (TextView) convertView
					.findViewById(R.id.tvNomDeLaSalle);
			holder.tvNomDuProf = (TextView) convertView
					.findViewById(R.id.tvNomDuProf);
			holder.tvHeure = (TextView) convertView.findViewById(R.id.tvHeure);
			holder.tvLaNOTE = (TextView) convertView
					.findViewById(R.id.tvLaNOTE);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvNomDuCours.setText(cours.get(position).getNomCours());
		holder.tvNomDeLaSalle.setText(cours.get(position).getSalle());
		holder.tvNomDuProf.setText(cours.get(position).getNomProf());
		holder.tvHeure.setText(cours.get(position).getTrancheHoraire());
		String laNote= cours.get(position).getlaNOTE();
		if(laNote!=null){
		holder.tvLaNOTE.setText(laNote);
		holder.tvLaNOTE.setVisibility(0);
		}
		String type= cours.get(position).getType();
		if(type!=null && !type.equals(" ")){
		holder.tvType.setText(type);
		holder.tvType.setVisibility(0);
		}
		return convertView;
	}
}
