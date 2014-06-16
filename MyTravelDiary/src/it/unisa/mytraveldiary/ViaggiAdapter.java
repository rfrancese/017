package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViaggiAdapter extends ArrayAdapter<String> {

	private ArrayList<Travel> viaggiList=new ArrayList<Travel>();
	private ArrayList<String> viaggi=new ArrayList<String>();
	private DatabaseHandler dbHandler;

	public ViaggiAdapter(Context context) throws ParseException {
		super(context, R.layout.list_item_travel);
		
		dbHandler=new DatabaseHandler(context);
		viaggiList=dbHandler.getAllTravels();
		
		for (Travel i: viaggiList) {
			viaggi.add(i.toString());
		}
		
		super.addAll(viaggi);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		
		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_travel, null);
		}
		
		Travel i=viaggiList.get(position);
		
		if (i!=null) {
			TextView textView= (TextView) v.findViewById(R.id.viaggio);
			
			if (textView!=null) {
				//textView.setText(i.getId()+"");
				textView.setText(position+"");
			}
		}
		
		return v;
	}
}
