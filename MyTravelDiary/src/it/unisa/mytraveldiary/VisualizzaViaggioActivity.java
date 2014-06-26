package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class VisualizzaViaggioActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizza_viaggio);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizza_viaggio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_logout:
			SharedPreferences settings = getSharedPreferences("login", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", null);
			// Commit the edits!
			editor.commit();
			goLogin();
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private Travel t;
		private ArrayList<Museo> museiList=new ArrayList<Museo>();
		private ArrayList<Trasporto> trasportiList=new ArrayList<Trasporto>();
		private ArrayList<HotelRistorante> hotelRistoranteList=new ArrayList<HotelRistorante>();
		private ArrayAdapter<HotelRistorante> hrAdapter;
		private ArrayAdapter<Trasporto> tAdapter;
		private ArrayAdapter<Museo> mAdapter;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_viaggio, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());

			RatingBar valutazione= (RatingBar) rootView.findViewById(R.id.ratingBar);
			TextView tipologiaViaggio= (TextView) rootView.findViewById(R.id.tipologiaViaggio);
			TextView localita= (TextView) rootView.findViewById(R.id.localita);
			TextView dataAndata= (TextView) rootView.findViewById(R.id.dataAndata);
			TextView dataRitorno= (TextView) rootView.findViewById(R.id.dataRitorno);
			TextView compagniViaggio= (TextView) rootView.findViewById(R.id.compagniViaggio);
			TextView descrizione= (TextView) rootView.findViewById(R.id.descrizione);
			Button ristoranti=(Button) rootView.findViewById(R.id.hotelRistoranti);
			Button trasporti=(Button) rootView.findViewById(R.id.trasporti);
			Button musei=(Button) rootView.findViewById(R.id.musei);

			if (extra!=null) {
				int value=extra.getInt("id");

				try {
					t=dbHandler.getTravel(value);
					hotelRistoranteList=dbHandler.getHotelRistoranti(value);
					museiList=dbHandler.getMusei(value);
					trasportiList=dbHandler.getTrasporti(value);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			hrAdapter=new ArrayAdapter<HotelRistorante>(getActivity(), 
					R.layout.list_item_dettagli, hotelRistoranteList);

			ristoranti.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Ristoranti")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					})
					.setAdapter(hrAdapter, null);
					// Create the AlertDialog object and return it
					builder.show();
				}
			});

			tAdapter=new ArrayAdapter<Trasporto>(getActivity(), 
					R.layout.list_item_dettagli, trasportiList);

			trasporti.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Trasporti")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					})
					.setAdapter(tAdapter, null);
					// Create the AlertDialog object and return it
					builder.show();
				}
			});

			mAdapter=new ArrayAdapter<Museo>(getActivity(), 
					R.layout.list_item_dettagli, museiList);

			musei.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("Musei")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					})
					.setAdapter(mAdapter, null);
					// Create the AlertDialog object and return it
					builder.show();
				}
			});


			valutazione.setRating(0);
			tipologiaViaggio.setText("Tipologia viaggio: "+t.getTipologiaViaggio());
			localita.setText("Localita: "+t.getLocalità());
			dataAndata.setText(t.getDataAndata().toString());
			dataRitorno.setText(t.getDataRitorno().toString());
			compagniViaggio.setText(t.getCompagniViaggio());
			descrizione.setText("Descrizione: "+t.getDescrizione());

			tAdapter=new ArrayAdapter<Trasporto>(getActivity(), 
					R.layout.list_item_dettagli, trasportiList);

			mAdapter=new ArrayAdapter<Museo>(getActivity(), 
					R.layout.list_item_dettagli, museiList);


			return rootView;
		}
	}
	
	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
