package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViaggiAdapter extends ArrayAdapter<Travel> implements Filterable {

	private Activity activityMain;
	private ArrayList<Travel> viaggiList=new ArrayList<Travel>();
	private Travel travel;
	private DatabaseHandler dbHandler;

	public ViaggiAdapter(Context context, Activity activity, ArrayList<Travel> listaViaggi) {
		super(context, R.layout.list_item_travel);
		
		activityMain=activity;
		dbHandler=new DatabaseHandler(activityMain);
		viaggiList=listaViaggi;

		super.addAll(viaggiList);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;

		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_travel, null);
		}

		travel=viaggiList.get(position);

		if (travel!=null) {
			RatingBar valutazione=(RatingBar) v.findViewById(R.id.ratingBar1);
			TextView viaggio= (TextView) v.findViewById(R.id.viaggio);
			TextView data=(TextView) v.findViewById(R.id.data);

			v.setId(position);

			if (viaggio!=null) {
				viaggio.setText(travel.getLocalitaString());
				notifyDataSetChanged();
			}
			
			if (data!=null) {
				data.setText(travel.getDataAndata()+" - "+travel.getDataRitorno());
				notifyDataSetChanged();
			}
			
			if (valutazione!=null) {
				//valutazione.setRating(travel.getMedia());
				ArrayList<Integer> media=dbHandler.getMedia(travel.getId());
				int somma=0, len=0;
				float med=0;
				
				len=media.size();
				
				if (len!=0) {
					for (Integer i: media) {
						somma+=i;
					}
					med=(float) ((somma/len)*0.2);
					valutazione.setRating(med);
				}
				notifyDataSetChanged();
			}
		}

		return v;
	}
	
	@Override
	public int getCount() {
		return viaggiList.size();
	}

	@Override
	public Travel getItem(int position) {
		return (viaggiList.get(position));
	}

	public int getTravelId(int position) {
		return (viaggiList.get(position)).getId();
	}
	
	public void changeData(ArrayList<Travel> listaViaggi) {
		viaggiList=listaViaggi;
		
		super.addAll(viaggiList);
	}
	
	private ArrayList<String> getTravelsString(ArrayList<Travel> listaViaggi) {
		ArrayList<String> stringList=new ArrayList<String>();
		
		for (Travel t: listaViaggi) {
			stringList.add(t.toString());
		}
		
		return stringList;
	}
}
