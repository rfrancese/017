package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;

public class HotelRistorantiActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inserisci_hotel_ristoranti);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hotel_ristoranti, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_info) {
			goInfo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_inserisci_hotel_ristoranti, container, false);
			
			// Get a reference to the AutoCompleteTextView in the layout
			 AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.cittaHotelRistoranteAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
			
			return rootView;
		}
	}

	public void salvaHotelRistorante(View view){
		
		// Tipologia
			RadioButton hotel= (RadioButton) findViewById(R.id.hotel);
		    RadioButton ristorante= (RadioButton) findViewById(R.id.ristorante);
				
		HotelRistorante dettaglio = new HotelRistorante();

		if (hotel.isChecked()) 
			dettaglio.setTipologia("Hotel");

		else if (ristorante.isChecked()) 
		    dettaglio.setTipologia("Ristorante");
		
		Log.d("RISTORANTE", dettaglio.getTipologia());
	}
	
	
	public void goInserisci(View view) {
		  Intent intent = new Intent(this, InserisciDettagliActivity.class);
		  startActivity(intent);
	  }
	
	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}
	
}
