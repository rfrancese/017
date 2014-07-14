package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Localita;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

	private boolean nomeOk=false;
	private boolean cittaOk=false;

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
		switch (item.getItemId()) {
		case R.id.action_salva:
			salvaHotelRistorante();
			return true;

		case R.id.action_info:
			goInfo();
			return true;

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

	public void salvaHotelRistorante(){
		String errorNome="", errorCitta="";

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

		if ((nomeHR.getText().toString()).equals("")) {
			nomeOk=false;
			errorNome="- Inserisci il nome dell'hotel/ristorante";
		} else {
			nomeOk=true;
		}

		if ((cittaHR.getText().toString()).equals("")) {
			cittaOk=false;
			errorCitta="- Inserisci la città";
		} else {
			cittaOk=true;
		}

		if (nomeOk && cittaOk) {
			// Nome
			hotelRistorante.setNome(nomeHR.getText().toString());

			// Città
			hotelRistorante.setLocalita(new Localita(cittaHR.getText().toString()));

			// Valutazione
			hotelRistorante.setValutazione((int) valutazioneHR.getRating());

			Log.d("HOTELRISTORANTI", hotelRistorante.toString());

			Bundle extra=getIntent().getExtras();

			if (extra!=null) {
				hotelRistorante.setTId(extra.getInt("id"));
			}

			DatabaseHandler dbHandler=new DatabaseHandler(this);

			hotelRistorante.setId(dbHandler.addHotelRistorante(hotelRistorante));

			showToast("Hotel/Ristorante salvato correttamente!");
			goMain();
		} else {

			String error;

			if (errorNome.equals(""))
				error=errorCitta;
			else if (errorCitta.equals(""))
				error=errorNome;
			else {
				error=errorNome+"\n\n";
				error+=errorCitta;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Attenzione!");
			builder.setMessage(error);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});

			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}


	public void goMain() {
		Intent intent = new Intent(this, MainActivity.class);
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

	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
