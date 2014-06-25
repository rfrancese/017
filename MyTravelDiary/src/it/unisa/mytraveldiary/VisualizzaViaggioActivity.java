package it.unisa.mytraveldiary;

import java.text.ParseException;
import java.util.ArrayList;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private Travel t;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_viaggio, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());
			ArrayList<HotelRistorante> hotelRistoranteList=new ArrayList<HotelRistorante>();
			ArrayList<Museo> museiList=new ArrayList<Museo>();
			ArrayList<Trasporto> trasportiList=new ArrayList<Trasporto>();

			if (extra!=null) {
				int value=extra.getInt("id");

				try {
					t=dbHandler.getTravel(value);
					hotelRistoranteList=dbHandler.getHotelRistoranti(value);
					museiList=dbHandler.getMusei(value);
					trasportiList=dbHandler.getTrasporti(value);
					Log.d("VISUALIZZA", t.toString());

					/*for (HotelRistorante hr: hotelRistoranteList)
						Log.d("visualizza", hr.toString());*/
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			RatingBar valutazione= (RatingBar) rootView.findViewById(R.id.ratingBar);
			TextView tipologiaViaggio= (TextView) rootView.findViewById(R.id.tipologiaViaggio);
			TextView localita= (TextView) rootView.findViewById(R.id.localita);
			TextView dataAndata= (TextView) rootView.findViewById(R.id.dataAndata);
			TextView dataRitorno= (TextView) rootView.findViewById(R.id.dataRitorno);
			TextView compagniViaggio= (TextView) rootView.findViewById(R.id.compagniViaggio);
			TextView descrizione= (TextView) rootView.findViewById(R.id.descrizione);

			ListView lvHotelRistoranti=(ListView) rootView.findViewById(R.id.hotelRistoranti);
			ListView lvTrasporti=(ListView) rootView.findViewById(R.id.trasporti);
			ListView lvMusei=(ListView) rootView.findViewById(R.id.musei);

			valutazione.setRating(0);
			tipologiaViaggio.setText("Tipologia viaggio: "+t.getTipologiaViaggio());
			localita.setText("Localita: "+t.getLocalità());
			dataAndata.setText(t.getDataAndata().toString());
			dataRitorno.setText(t.getDataRitorno().toString());
			compagniViaggio.setText(t.getCompagniViaggio());
			descrizione.setText("Descrizione: "+t.getDescrizione());

			ArrayAdapter<HotelRistorante> hrAdapter=new ArrayAdapter<HotelRistorante>(getActivity(), 
					R.layout.list_item_dettagli, hotelRistoranteList);
			lvHotelRistoranti.setAdapter(hrAdapter);

			ArrayAdapter<Trasporto> tAdapter=new ArrayAdapter<Trasporto>(getActivity(), 
					R.layout.list_item_dettagli, trasportiList);
			lvTrasporti.setAdapter(tAdapter);

			ArrayAdapter<Museo> mAdapter=new ArrayAdapter<Museo>(getActivity(), 
					R.layout.list_item_dettagli, museiList);
			lvMusei.setAdapter(mAdapter);

			return rootView;
		}
	}

}
