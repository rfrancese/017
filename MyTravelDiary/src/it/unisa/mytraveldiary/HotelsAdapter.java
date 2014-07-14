package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelsAdapter extends ArrayAdapter<HotelRistorante> {
	
	private List<HotelRistorante> hotels;
	private HotelRistorante hotel;
	
	public HotelsAdapter(Context context, int resource, List<HotelRistorante> objects) {
		super(context, resource, objects);
		
		hotels=objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		
		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_dettagli, null);
		}
		
		if (hotels!=null) {
			TextView nome=(TextView) v.findViewById(R.id.textView1);
			TextView localita=(TextView) v.findViewById(R.id.textView2);
			RatingBar valutazione=(RatingBar) v.findViewById(R.id.ratingBar1);
			hotel=hotels.get(position);

			if (nome!=null) 
				nome.setText(hotel.getNome());
			
			if (localita!=null)
				localita.setText(hotel.getLocalita().toString());
			
			if (valutazione!=null)
				valutazione.setRating(hotel.getValutazione());
		} 		
		return v;
	}
	
	@Override
	public int getCount() {
		return hotels.size();
	}

	@Override
	public HotelRistorante getItem(int position) {
		return (hotels.get(position));
	}
}
