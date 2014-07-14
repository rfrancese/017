package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Localita;
import it.unisa.mytraveldiary.entity.Museo;
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


public class MuseiActivity extends ActionBarActivity {

	private boolean nomeOk=false;
	private boolean cittaOk=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musei);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.musei, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_salva:
			salvaMuseo();
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
			View rootView = inflater.inflate(R.layout.fragment_musei, container, false);

			AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.cittaMuseoAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));

			return rootView;
		}
	}


	private Museo museo = new Museo();

	//Salva Viaggio
	public void salvaMuseo(){
		String errorNome="", errorCitta="";

		//Tipologia Museo
		Spinner tipologiaMuseo = (Spinner) findViewById(R.id.tipoMusei);

		//Nome Museo
		EditText editNome = (EditText) findViewById(R.id.nomeMuseoSugg);

		//Città Museo
		AutoCompleteTextView cittaMuseo = (AutoCompleteTextView) findViewById(R.id.cittaMuseoAutocomplete);

		//Valutazione
		RatingBar valutazioneM = (RatingBar) findViewById(R.id.ratingBar);

		if ((editNome.getText().toString()).equals("")) {
			nomeOk=false;
			errorNome="- Inserisci il nome del museo";
		} else {
			nomeOk=true;
		}

		if ((cittaMuseo.getText().toString()).equals("")) {
			cittaOk=false;
			errorCitta="- Inserisci la città";
		} else {
			cittaOk=true;
		}

		if (nomeOk && cittaOk) {

			//Tipologia Museo
			museo.setTipologia(tipologiaMuseo.getSelectedItem().toString());

			//Nome Museo
			museo.setNome(editNome.getText().toString());

			//Città
			museo.setLocalita(new Localita(cittaMuseo.getText().toString()));

			//Valutazione
			museo.setValutazione((int) valutazioneM.getRating());


			Log.d("MUSEO", museo.toString());

			Bundle extra=getIntent().getExtras();

			if (extra!=null) {
				museo.setTId(extra.getInt("id"));
			}

			DatabaseHandler dbHandler = new DatabaseHandler(this);

			museo.setId(dbHandler.addMuseo(museo));

			showToast("Museo salvato correttamente!");
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
