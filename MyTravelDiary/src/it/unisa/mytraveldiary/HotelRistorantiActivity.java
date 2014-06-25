package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

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

//	private boolean hotelRistoranteSalvato=false;
	private HotelRistorante hotelRistorante=new HotelRistorante();

	public void salvaHotelRistorante(View view){

		// GET
		// Tipologia
		RadioButton hotel= (RadioButton) findViewById(R.id.hotel);
		RadioButton ristorante= (RadioButton) findViewById(R.id.ristorante);

		// Nome
		EditText nomeHR= (EditText) findViewById(R.id.nomeHotelRistoranteInput);

		// Città
		AutoCompleteTextView cittaHR= (AutoCompleteTextView) findViewById(R.id.cittaHotelRistoranteAutocomplete);

		// Valutazione
		RatingBar valutazioneHR= (RatingBar) findViewById(R.id.ratingBar);

		//SET
		//Tipologia
		if (hotel.isChecked()) {
			hotelRistorante.setTipologia("Hotel");
		}

		else if (ristorante.isChecked()) {
			hotelRistorante.setTipologia("Ristorante");
		}

		// Nome
		hotelRistorante.setNome(nomeHR.getText().toString());

		// Città
		hotelRistorante.setCitta(cittaHR.getText().toString());

		// Valutazione
		hotelRistorante.setValutazione((int) valutazioneHR.getRating());

		Log.d("HOTELRISTORANTI", hotelRistorante.toString());
		
		Bundle extra=getIntent().getExtras();
		
		if (extra!=null) {
			hotelRistorante.setTId(extra.getInt("id"));
		}

		DatabaseHandler dbHandler=new DatabaseHandler(this);

//		dbHandler.updateHotelRistorante(hotelRistorante);

		hotelRistorante.setId(dbHandler.addHotelRistorante(hotelRistorante));

		showToast("Hotel/Ristorante salvato correttamente!");
		goInserisci();
	}


	public void goInserisci() {
		Intent intent = new Intent(this, InserisciDettagliActivity.class);
		startActivity(intent);
	}

	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}

	private void showToast(String msg) {
		Context context=getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}	
}
