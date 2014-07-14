package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Localita;
import it.unisa.mytraveldiary.entity.Trasporto;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class TrasportiActivity extends ActionBarActivity {

	private boolean cittaPOk=false;
	private boolean cittaAOk=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trasporti);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trasporti, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_salva:
			salvaTrasporto();
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_trasporti, container, false);

			// Get a reference to the AutoCompleteTextView in the layout
			AutoCompleteTextView textViewAndata = (AutoCompleteTextView) rootView.findViewById(R.id.cittaPartenzaAutocomplete);
			AutoCompleteTextView textViewArrivo = (AutoCompleteTextView) rootView.findViewById(R.id.cittaArrivoAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textViewAndata.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
			textViewArrivo.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));

			return rootView;
		}
	}

	private Trasporto trasporto = new Trasporto();

	//Salva Viaggio
	public void salvaTrasporto(){
		String errorCittaP="", errorCittaA="";

		//TipologiaTrasporto
		Spinner tipologiaTrasporto = (Spinner) findViewById(R.id.tipoTrasporti);

		//CompagniaTrasporto
		EditText editCompagnia = (EditText) findViewById(R.id.compagniaSugg);

		//Città
		AutoCompleteTextView cittaP = (AutoCompleteTextView) findViewById(R.id.cittaPartenzaAutocomplete);
		AutoCompleteTextView cittaA = (AutoCompleteTextView) findViewById(R.id.cittaArrivoAutocomplete);

		//Valutazione
		RatingBar valutazioneT = (RatingBar) findViewById(R.id.ratingBar);

		if ((cittaP.getText().toString()).equals("")) {
			cittaPOk=false;
			errorCittaP="- Inserisci la città di partenza";
		} else {
			cittaPOk=true;
		}

		if ((cittaA.getText().toString()).equals("")) {
			cittaAOk=false;
			errorCittaA="- Inserisci la città di arrivo";
		} else {
			cittaAOk=true;
		}

		if (cittaPOk && cittaAOk) {
			//TipologiaTrasporto
			trasporto.setTipologia(tipologiaTrasporto.getSelectedItem().toString());

			//CompagniaTrasporto
			trasporto.setCompagnia(editCompagnia.getText().toString());

			//Città
			trasporto.setLocalitaPartenza(new Localita(cittaP.getText().toString()));
			trasporto.setLocalitaArrivo(new Localita(cittaA.getText().toString()));

			//Valutazione
			trasporto.setValutazione((int) valutazioneT.getRating());


			Log.d("TRASPORTI", trasporto.toString());

			Bundle extra=getIntent().getExtras();

			if (extra!=null) {
				trasporto.setTId(extra.getInt("id"));
			}

			DatabaseHandler dbHandler = new DatabaseHandler(this);

			trasporto.setId(dbHandler.addTrasporto(trasporto));

			showToast("Trasporto salvato correttamente!");
			goMain();
		} else {

			String error;

			if (errorCittaP.equals(""))
				error=errorCittaA;
			else if (errorCittaA.equals(""))
				error=errorCittaP;
			else {
				error=errorCittaP+"\n\n";
				error+=errorCittaA;
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
