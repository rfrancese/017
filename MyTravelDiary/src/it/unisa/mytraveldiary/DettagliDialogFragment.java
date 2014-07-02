package it.unisa.mytraveldiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class DettagliDialogFragment extends DialogFragment {
	
	private int id;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		id=getTravelId();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Cosa vuoi inserire?")
		.setItems(R.array.dettagli, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position
				// of the selected item
				switch (which) {

				case 0:
					Intent intentF=new Intent(getActivity(), FotoVideoActivity.class);
					intentF.putExtra("id", id);
					startActivity(intentF);
					break;
					
				case 1:
					Intent intentHR=new Intent(getActivity(), HotelRistorantiActivity.class);
					intentHR.putExtra("id", id);
					startActivity(intentHR);
					break;
					
				case 2:
					Intent intentT=new Intent(getActivity(), TrasportiActivity.class);
					intentT.putExtra("id", id);
					startActivity(intentT);
					break;
					
				case 3:
					Intent intentM=new Intent(getActivity(), MuseiActivity.class);
					intentM.putExtra("id", id);
					startActivity(intentM);
					break;
				}
			}
		});
		return builder.create();
	}
	
	private int getTravelId() {
		SharedPreferences settings = getActivity().getSharedPreferences("viaggio", 0);
		int id = settings.getInt("id", -1);
		
		return id;
	}
}