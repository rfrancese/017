package it.unisa.mytraveldiary;

import java.util.ArrayList;
import java.util.List;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Travel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class RistorantiAdapter extends ArrayAdapter<HotelRistorante> {

	private List<HotelRistorante> ristoranti;
	private HotelRistorante ristorante;

	public RistorantiAdapter(Context context, int resource,
			List<HotelRistorante> objects) {
		super(context, resource, objects);
		ristoranti=objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;

		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_dettagli, null);
		}

		if (ristoranti!=null) {
			TextView nome=(TextView) v.findViewById(R.id.textView1);
			TextView localita=(TextView) v.findViewById(R.id.textView2);
			RatingBar valutazione=(RatingBar) v.findViewById(R.id.ratingBar1);
			ristorante=ristoranti.get(position);

			if (nome!=null)
				nome.setText(ristorante.getNome());

			if (localita!=null)
				localita.setText(ristorante.getLocalita().toString());

			if (valutazione!=null)
				valutazione.setRating(ristorante.getValutazione());
		}
		return v;
	}
	
	@Override
	public int getCount() {
		return ristoranti.size();
	}

	@Override
	public HotelRistorante getItem(int position) {
		return (ristoranti.get(position));
	}
}
