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
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView; 
import android.widget.Toast;

public class ViaggiAdapter extends ArrayAdapter<String> implements Filterable {

	private Activity activityMain;
	private DatabaseHandler dbHandler;
	private ArrayList<String> viaggi=new ArrayList<String>();
	private ArrayList<Travel> viaggiList=new ArrayList<Travel>();
	private Travel travel;
	private int pos;

	public ViaggiAdapter(Context context, Activity activity) throws ParseException {
		super(context, R.layout.list_item_travel);

		activityMain=activity;
		dbHandler=new DatabaseHandler(activityMain);

		viaggiList=dbHandler.getAllTravels();

		for (Travel t: viaggiList) {
			viaggi.add(t.toString());
		}

		super.addAll(viaggi);
	}

	public ViaggiAdapter(Context context, ArrayList<Travel> listaViaggi) {
		super(context, R.layout.list_item_travel);

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
			ImageButton inserisciDettagli= (ImageButton) v.findViewById(R.id.inserisciDettagli);
			ImageButton elimina= (ImageButton) v.findViewById(R.id.elimina);
			ImageButton modifica= (ImageButton) v.findViewById(R.id.modifica);
			final Context context=parent.getContext();

			elimina.setTag(position);
			modifica.setTag(position);
			v.setId(position);

			if (textView!=null) {
				textView.setText(travel.getLocalità());
			}
			
			if (inserisciDettagli!=null) {
				inserisciDettagli.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//  Use position parameter of your getView() in this method it will current position of Clicked row button
						// code for current Row deleted...              
						Intent intent=new Intent(context, InserisciDettagliActivity.class);
						context.startActivity(intent);
					}
				});
				
				notifyDataSetChanged();
			}

			if (elimina!=null) {
				elimina.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//  Use position parameter of your getView() in this method it will current position of Clicked row button
						// code for current Row deleted...

						pos=Integer.parseInt(v.getTag().toString());
						Log.d("ADAPTER", ""+pos);

						//DeleteDialogFragment elimina=new DeleteDialogFragment();
						//elimina.show(activityMain.getFragmentManager(), "elimina");

						AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
						builder.setMessage(R.string.eliminaInfo);
						builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (dbHandler!=null) {
									dbHandler.deleteTravel(viaggiList.get(pos));

								}

								//remove(viaggi.get(pos));
								viaggi.remove(pos);
								viaggiList.remove(pos);

								notifyDataSetChanged();
								showToast("Viaggio eliminato!");
							}
						});
						builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								showToast("Eliminazione annullata...");
							}
						});
						builder.setIcon(R.drawable.ic_action_warning);
						builder.setTitle(R.string.eliminaViaggio);

						builder.create();
						builder.show();
					}
				});
			}

			if (modifica!=null) {
				modifica.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pos=Integer.parseInt(v.getTag().toString());
						Travel t=viaggiList.get(pos);

						//parcelable?
						Intent intent=new Intent(context, NewTravelActivity.class);
						intent.putExtra("modifica", true);
						intent.putExtra("tipologia", t.getTipologiaViaggio());
						intent.putExtra("localita", t.getLocalità());
						intent.putExtra("dataA", t.getDataAndata());
						intent.putExtra("dataR", t.getDataRitorno());
						intent.putExtra("compagni", t.getCompagniViaggio());
						intent.putExtra("descrizione", t.getDescrizione());
						intent.putExtra("id", t.getId());
						context.startActivity(intent);
						//showToast("Modifica");
					}
				});
			}
		}

		return v;
	}


	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					
					///viaggiList=dbHandler.getViaggiSvago();

					// Assign the data to the FilterResults
					filterResults.values = viaggiList;
					filterResults.count = viaggiList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
				else {
					notifyDataSetInvalidated();
				}
			}};
			return filter;
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
