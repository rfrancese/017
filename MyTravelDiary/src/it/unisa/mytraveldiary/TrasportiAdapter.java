package it.unisa.mytraveldiary;

import java.util.ArrayList;
import java.util.List;

import it.unisa.mytraveldiary.entity.Trasporto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class TrasportiAdapter extends ArrayAdapter<Trasporto> {
	
	private List<Trasporto> trasporti;
	private Trasporto trasporto;

	public TrasportiAdapter(Context context, int resource,
			List<Trasporto> objects) {
		super(context, resource, objects);
		trasporti=objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		
		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_trasporto, null);
		}
	
		if (trasporti!=null) {
			TextView tipologia=(TextView) v.findViewById(R.id.textView1);
			TextView compagnia=(TextView) v.findViewById(R.id.textView1);
			TextView localitaP=(TextView) v.findViewById(R.id.textView3);
			TextView localitaA=(TextView) v.findViewById(R.id.textView4);
			RatingBar valutazione=(RatingBar) v.findViewById(R.id.ratingBar1);
			trasporto=trasporti.get(position);
			
			if (tipologia!=null) 
				tipologia.setText(trasporto.getTipologia());
			
			if (compagnia!=null)
				compagnia.setText(trasporto.getCompagnia());
			
			if (localitaP!=null)
				localitaP.setText(trasporto.getLocalitaPartenza().toString());
			
			if (localitaA!=null)
				localitaA.setText(trasporto.getLocalitaArrivo().toString());
			
			if (valutazione!=null)
				valutazione.setRating(trasporto.getValutazione());
		}
		
		return v;
	}

}
