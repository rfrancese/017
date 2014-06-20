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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView; 
import android.widget.Toast;

public class ViaggiAdapter extends ArrayAdapter<String> {

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

			if (textView!=null) {
				textView.setText(travel.toString());
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
