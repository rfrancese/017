package it.unisa.mytraveldiary;

import java.util.ArrayList;
import java.util.List;

import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Travel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public final class MuseiAdapter extends ArrayAdapter<Museo> {

	private List<Museo> musei;
	private Museo museo;

	public MuseiAdapter(Context context, int resource, List<Museo> objects) {
		super(context, resource, objects);
		
		musei=objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;

		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_museo, null);
		}

		if (musei!=null) {
			TextView nome=(TextView) v.findViewById(R.id.textView1);
			TextView tipologia=(TextView) v.findViewById(R.id.textView2);
			TextView localita=(TextView) v.findViewById(R.id.textView3);
			RatingBar valutazione=(RatingBar) v.findViewById(R.id.ratingBar1);
			museo=musei.get(position);

			if (nome!=null)
				nome.setText(museo.getNome());

			if (tipologia!=null)
				tipologia.setText(museo.getTipologia());

			if (localita!=null)
				localita.setText(museo.getLocalita().toString());

			if (valutazione!=null)
				valutazione.setRating(museo.getValutazione());
		} 
		return v;
	}
	
	@Override
	public int getCount() {
		return musei.size();
	}

	@Override
	public Museo getItem(int position) {
		return (musei.get(position));
	}
}
