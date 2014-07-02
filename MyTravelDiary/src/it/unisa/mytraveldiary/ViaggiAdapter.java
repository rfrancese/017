package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView; 
import android.widget.Toast;

public class ViaggiAdapter extends ArrayAdapter<String> implements Filterable {

	private Activity activityMain;
	private ArrayList<String> viaggi=new ArrayList<String>();
	private ArrayList<Travel> viaggiList=new ArrayList<Travel>();
	private Travel travel;
	private int pos;

	public ViaggiAdapter(Context context, Activity activity, ArrayList<Travel> listaViaggi) {
		super(context, R.layout.list_item_travel);
		
		activityMain=activity;

		viaggiList=listaViaggi;

		for (Travel t: viaggiList) {
			Log.d("ADAPTER", t.toString());
			viaggi.add(t.toString());
		}

		super.addAll(viaggi);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;

		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_travel, null);
		}

		travel=viaggiList.get(position);

		if (travel!=null) {
			TextView textView= (TextView) v.findViewById(R.id.viaggio);
			

			
			v.setId(position);

			if (textView!=null) {
				textView.setText(travel.getLocalitaString());
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
	public String getItem(int position) {
		return (viaggiList.get(position)).toString();
	}

	public int getTravelId(int position) {
		return (viaggiList.get(position)).getId();
	}
	
	private void showToast(String msg) {
		Context context=getContext().getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}	
}
