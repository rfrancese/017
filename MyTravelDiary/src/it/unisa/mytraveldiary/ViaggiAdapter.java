package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

	private ArrayList<String> viaggiList=new ArrayList<String>();
	private Activity activityMain;

	public ViaggiAdapter(Context context, Activity activity, ArrayList<String> viaggi) throws ParseException {
		super(context, R.layout.list_item_travel, viaggi);

		activityMain=activity;
		viaggiList=viaggi;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;

		if (v==null) {
			LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.list_item_travel, null);
		}

		String i=viaggiList.get(position);

		if (i!=null) {
			TextView textView= (TextView) v.findViewById(R.id.viaggio);
			ImageButton inserisciDettagli= (ImageButton) v.findViewById(R.id.inserisciDettagli);
			ImageButton elimina= (ImageButton) v.findViewById(R.id.elimina);
			ImageButton modifica= (ImageButton) v.findViewById(R.id.modifica);
			final Context context=parent.getContext();

			if (textView!=null) {
				textView.setText(i.toString());
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
						Log.d("ADAPTER", "elimina");

						//DeleteDialogFragment elimina=new DeleteDialogFragment();
						//elimina.show(activityMain.getFragmentManager(), "elimina");
						
						AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
				        builder.setMessage(R.string.eliminaInfo);
				        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	   ((MainActivity) activityMain).doPositiveClick();
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
	
	private void showToast(String msg) {
		Context context=getContext().getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}	

	
	/*public class DeleteDialogFragment extends DialogFragment {

		 @Override
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        // Use the Builder class for convenient dialog construction
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setMessage(R.string.eliminaInfo)
		               .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   ((MainActivity)getActivity()).doPositiveClick();
		                	   showToast("Viaggio eliminato!");
		                   }
		               })
		               .setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // User cancelled the dialog
		                	   showToast("Eliminazione annullata...");
		                   }
		               })
		               .setIcon(R.drawable.ic_action_warning)
		               .setTitle(R.string.eliminaViaggio);
		        // Create the AlertDialog object and return it
		        return builder.create();
		    }
	}
*/
}
