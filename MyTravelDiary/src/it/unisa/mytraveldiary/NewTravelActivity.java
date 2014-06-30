package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class NewTravelActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_travel_message);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_travel_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_salva:
			salvaViaggio();
			return true;
			
		case R.id.action_inserisci:
			goInserisciDettagli();
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

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
		case R.id.Svago:
			if (checked) {
				setSvago();
				Log.d("TIPOLOGIA", viaggio.getTipologiaViaggio());
			}
			break;
		case R.id.Lavoro:
			if (checked) {
				setLavoro();
				Log.d("TIPOLOGIA", viaggio.getTipologiaViaggio());
			}
			break;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_new_travel_message, container, false);

			Bundle extra=getActivity().getIntent().getExtras();

			if (extra!=null) {
				RadioButton svago=(RadioButton) rootView.findViewById(R.id.Svago);
				RadioButton lavoro=(RadioButton) rootView.findViewById(R.id.Lavoro);
				AutoCompleteTextView localita=(AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
				TextView dataA=(TextView) rootView.findViewById(R.id.andataText);
				TextView dataR=(TextView) rootView.findViewById(R.id.ritornoText);
				MultiAutoCompleteTextView compViaggio=(MultiAutoCompleteTextView) rootView.findViewById(R.id.compagniViaggioAutocomplete);
				EditText descrizione=(EditText) rootView.findViewById(R.id.descrizioneViaggioInput);

				if ((extra.getString("tipologia")).equals("Svago"))
					svago.setChecked(true);
				else if ((extra.getString("tipologia")).equals("Lavoro"))
					lavoro.setChecked(true);

				localita.setText(extra.getString("localita"));
				dataA.setText(extra.getString("dataA"));
				dataR.setText(extra.getString("dataR"));
				compViaggio.setText(extra.getString("compagni"));
				descrizione.setText(extra.getString("descrizione"));
			}

			// Get a reference to the AutoCompleteTextView in the layout
			AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));

			// Compagni viaggio
			MultiAutoCompleteTextView compagniViaggio = (MultiAutoCompleteTextView) rootView.findViewById(R.id.compagniViaggioAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			compagniViaggio.setAdapter(new CompagniViaggioAdapter(getActivity(), R.layout.list_item));
			compagniViaggio.setThreshold(2);
			compagniViaggio.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			return rootView;
		}
	}

	private boolean viaggioSalvato=false;
	private Travel viaggio=new Travel();

	public void showDatePickerDialogAndata(View v) {
		TextView andata= (TextView) findViewById(R.id.andataText);
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setTipologia("Andata");
		((DatePickerFragment) newFragment).setTextView(andata);
	}

	public void showDatePickerDialogRitorno(View v) {
		TextView ritorno= (TextView) findViewById(R.id.ritornoText);
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setTipologia("Ritorno");
		((DatePickerFragment) newFragment).setTextView(ritorno);
	}

	public void avantiInserisciDettagli(View view) throws NumberFormatException, ParseException{
		if (viaggioSalvato) {
			goInserisciDettagli();
		}

		else
			showToast("Salva prima il viaggio!");
	}

	public void salvaViaggio() {

		// GET
		// Data andata e ritorno
		TextView dataAndata=(TextView) findViewById(R.id.andataText);
		TextView dataRitorno=(TextView) findViewById(R.id.ritornoText);

		// Descrizione
		EditText descrizione= (EditText) findViewById(R.id.descrizioneViaggioInput);

		// Località
		AutoCompleteTextView localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);

		// Compagni viaggio
		MultiAutoCompleteTextView compagniViaggio=(MultiAutoCompleteTextView) findViewById(R.id.compagniViaggioAutocomplete);

		// SET
		// Tipologia viaggio			

		//Log.d("Tipologia viaggio", viaggio.getTipologiaViaggio());

		// Località
		String localitaText=localita.getText().toString();
		viaggio.setLocalità(localitaText);

		// Data andata e ritorno
		String dataAndataString=dataAndata.getText().toString();
		String dataRitornoString=dataRitorno.getText().toString();

		/*if (!(dataAndataString.equals(""))) {
			Date dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataAndataString);
			viaggio.setDataAndata(dataA);
		}

		if (!(dataRitornoString.equals(""))) {
			Date dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataRitornoString);
			viaggio.setDataRitorno(dataR);
		}*/

		viaggio.setDataAndata(dataAndataString);
		viaggio.setDataRitorno(dataRitornoString);

		// Descrizione
		viaggio.setDescrizione(descrizione.getText().toString());

		// Compagni viaggio
		String compViaggio=compagniViaggio.getText().toString();
		viaggio.setCompagniViaggio(compViaggio);

		DatabaseHandler dbHandler=new DatabaseHandler(this);
		Bundle extra=getIntent().getExtras();
		boolean modifica=false;

		if (extra!=null)
			modifica=extra.getBoolean("modifica");

		if (viaggioSalvato) {


			Log.d("TRAVEL", "viaggio da modificare");
			Log.d("New travel modifica", viaggio.toString());

			// aggiornare viaggio nel database
			dbHandler.updateTravel(viaggio);
		}

		else if (modifica) {
			setTitle("Modifica Viaggio");
			Log.d("modifica", "yes");
			viaggio.setId(extra.getInt("id"));
			dbHandler.updateTravel(viaggio);
			viaggioSalvato=true;
		}

		else {
			viaggioSalvato=true;

			viaggio.setId(dbHandler.addTravel(viaggio));

			Log.d("New travel", viaggio.toString());
		}

		showToast("Viaggio salvato correttamente!");
	}

	private void setSvago() {
		viaggio.setTipologiaViaggio("Svago");
	}

	private void setLavoro() {
		viaggio.setTipologiaViaggio("Lavoro");
	}

	public void openMaps(View view){
		Intent intent = new Intent(this, MapsActivity.class);
		//intent.putExtra("Citta", value)
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("città", requestCode+" "+resultCode);
		if (requestCode==1 && resultCode==RESULT_OK) {
			Log.d("città", requestCode+" "+resultCode);
			if (data!=null) {
				Log.d("città", requestCode+" "+resultCode);
				String city=data.getExtras().getString("citta");
				AutoCompleteTextView localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
				localita.setText(city);
			}
		}
	}

	private void goInserisciDettagli() {
		Intent intent = new Intent(this, InserisciDettagliActivity.class);
		intent.putExtra("id", viaggio.getId());
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
	
	private LinearLayout ll;
	private int tag;

	public void addLocalita(View view) {
		ll=(LinearLayout) findViewById(R.id.localita);
		AutoCompleteTextView autocompleteTextView=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		String localita=(autocompleteTextView.getText()).toString();


		if (!(localita.equals(""))) {
			TextView textView=new TextView(this);
			textView.setTag(ll.getChildCount());
			Log.d("child ll", ll.getChildCount()+"");
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT));
			textView.setPadding(0, 0, 0, 20);
			textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_cancel, 0);
			textView.setCompoundDrawablePadding(20);
			textView.setTextAppearance(this, R.style.CodeFont);
			textView.setClickable(true);
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					tag=Integer.parseInt(view.getTag().toString());
					
					AlertDialog.Builder builder = new AlertDialog.Builder(NewTravelActivity.this);
					builder.setMessage(R.string.eliminaLocalitaInfo);
					builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int id) {
							
							ll.removeViewAt(tag);
							showToast("Località eliminata!");
						}
					});
					builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							showToast("Eliminazione annullata...");
						}
					});
					builder.setIcon(R.drawable.ic_action_warning);
					builder.setTitle(R.string.eliminaLocalita);

					builder.create();
					builder.show();
				}
			});
			Log.d("add localita", "+"+(autocompleteTextView.getText()).toString()+"+");
			textView.setText(localita);

			ll.addView(textView);
			
			autocompleteTextView.setText("");
		}
	}
}

