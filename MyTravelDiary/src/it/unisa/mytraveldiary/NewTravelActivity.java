package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Localita;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class NewTravelActivity extends ActionBarActivity {

	private DatabaseHandler dbHandler;
	private ArrayList<Localita> listaLocalita;
	private int username;
	private boolean dateOK=true;
	private boolean localitaOK=true;
	private boolean addDettagli=false;
	private boolean canAdd=false;
	private Travel viaggio=new Travel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_travel_message);
		
		SharedPreferences settings = getSharedPreferences("login", 0);
		username=settings.getInt("username", -1);

		ViewHolder holder=new ViewHolder();
		holder.svago=(RadioButton) findViewById(R.id.Svago);
		holder.lavoro=(RadioButton) findViewById(R.id.Lavoro);
		holder.dataA=(TextView) findViewById(R.id.andataText);
		holder.dataR=(TextView) findViewById(R.id.ritornoText);
		holder.descrizione=(EditText) findViewById(R.id.descrizioneViaggioInput);
		holder.ll=(LinearLayout) findViewById(R.id.localita);

		// Get a reference to the AutoCompleteTextView in the layout
		holder.localita = (AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		// Create the adapter and set it to the AutoCompleteTextView 
		holder.localita.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));

		// Compagni viaggio
		holder.compViaggio = (MultiAutoCompleteTextView) findViewById(R.id.compagniViaggioAutocomplete);
		// Create the adapter and set it to the AutoCompleteTextView 
		holder.compViaggio.setAdapter(new CompagniViaggioAdapter(this, R.layout.list_item));
		holder.compViaggio.setThreshold(2);
		holder.compViaggio.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		listaLocalita=new ArrayList<Localita>();
		Bundle extra=getIntent().getExtras();
		dbHandler=new DatabaseHandler(this);

		if (extra!=null) {
			getActionBar().setTitle("Modifica Viaggio");
			Travel t=dbHandler.getTravel(extra.getInt("id"));

			if ((t.getTipologiaViaggio()).equals("Svago")) 
				holder.svago.setChecked(true);
			else
				holder.lavoro.setChecked(true);

			holder.dataA.setText(t.getDataAndata());
			holder.dataR.setText(t.getDataRitorno());
			holder.compViaggio.setText(t.getCompagniViaggio());
			holder.descrizione.setText(t.getDescrizione());

			listaLocalita=dbHandler.getLocalitas(t.getId());
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
			avantiInserisciDettagli();
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


	public void showDatePickerDialogAndata(View v) {
		ViewHolder holder=new ViewHolder();
		holder.dataA=(TextView) findViewById(R.id.andataText);
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setTipologia("Andata");
		((DatePickerFragment) newFragment).setTextView(holder.dataA);
	}

	public void showDatePickerDialogRitorno(View v) {
		ViewHolder holder=new ViewHolder();
		holder.dataR=(TextView) findViewById(R.id.ritornoText);
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setTipologia("Ritorno");
		((DatePickerFragment) newFragment).setTextView(holder.dataR);
	}

	public void avantiInserisciDettagli() {
		//if (viaggioSalvato) {
		addDettagli=true;
		salvaViaggio();
		
		if (canAdd) {
			SharedPreferences settings = getSharedPreferences("viaggio", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("id", viaggio.getId());

			// Commit the edits!
			editor.commit();

			DettagliDialogFragment dettagli=new DettagliDialogFragment();
			dettagli.show(getFragmentManager(), "dettagli");
		}

		/*else
			showToast("Salva prima il viaggio!");*/
	}

	public void salvaViaggio() {
		String errorLoc="", errorDate="";
		ViewHolder holder=new ViewHolder();
		dbHandler=new DatabaseHandler(this);
		// Località
		viaggio.setLocalità(listaLocalita);

		holder.dataA=(TextView) findViewById(R.id.andataText);
		holder.dataR=(TextView) findViewById(R.id.ritornoText);
		holder.descrizione=(EditText) findViewById(R.id.descrizioneViaggioInput);
		holder.compViaggio=(MultiAutoCompleteTextView) findViewById(R.id.compagniViaggioAutocomplete);
		holder.ll=(LinearLayout) findViewById(R.id.localita);
		// Data andata e ritorno
		String dataAndataString=holder.dataA.getText().toString();
		String dataRitornoString=holder.dataR.getText().toString();

		if (listaLocalita.size()==0) {
			localitaOK=false;
			errorLoc="- Inserisci almeno una località";
		} else
			localitaOK=true;

		if (dataAndataString.compareTo(dataRitornoString)>0) {
			errorDate="- Data di andata prima della data di ritorno";
			dateOK=false;
		} else
			dateOK=true;


		if (dateOK && localitaOK) {
			
			canAdd=true;

			viaggio.setDataAndata(dataAndataString);
			viaggio.setDataRitorno(dataRitornoString);

			// Descrizione
			viaggio.setDescrizione(holder.descrizione.getText().toString());

			// Compagni viaggio
			String compViaggio=holder.compViaggio.getText().toString();
			viaggio.setCompagniViaggio(compViaggio);

			Bundle extra=getIntent().getExtras();
			boolean modifica=false;

			if (extra!=null) {
				modifica=extra.getBoolean("modifica");
				viaggio.setId(extra.getInt("id"));
			}

			if (modifica) {
				viaggio.setUId(username);
				viaggio.setId(extra.getInt("id"));
				dbHandler.deleteLocalitas(viaggio.getId());
				viaggio.setLocalità(listaLocalita);
				dbHandler.updateTravel(viaggio);

				Log.d("modifica", "yes");

				for (Localita l: listaLocalita)
					Log.d("localita", l.toString());

				setResult(RESULT_OK);
				showToast("Viaggio modificato correttamente!");

				if (!addDettagli)
					goMain();
			}

			else {
				viaggio.setUId(username);
				viaggio.setLocalità(listaLocalita);
				viaggio.setId(dbHandler.addTravel(viaggio));

				setResult(RESULT_OK);
				showToast("Viaggio salvato correttamente!");

				if (!addDettagli)
					goMain();
			}

		} else {
			canAdd=false;
			String error;
			
			if (errorLoc.equals(""))
				error=errorDate;
			else if (errorDate.equals(""))
				error=errorLoc;
			else {
				error=errorLoc+"\n\n";
				error+=errorDate;
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

	private void setSvago() {
		viaggio.setTipologiaViaggio("Svago");
	}

	private void setLavoro() {
		viaggio.setTipologiaViaggio("Lavoro");
	}

	public void openMaps(View view){
		ViewHolder holder=new ViewHolder();
		holder.localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		String autocompleteText=holder.localita.getText().toString();

		ArrayList<String> localitaString=new ArrayList<String>();

		if (listaLocalita!=null) {
			for (Localita l: listaLocalita)
				localitaString.add(l.toString());
		}

		Intent intent = new Intent(this, MapsActivity.class);
		intent.putStringArrayListExtra("localita", localitaString);
		intent.putExtra("autocomplete", autocompleteText);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ViewHolder holder=new ViewHolder();
		holder.localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		holder.localita.setText("");
		listaLocalita=new ArrayList<Localita>();
		if (requestCode==1 && resultCode==RESULT_OK) {
			if (data!=null) {
				ArrayList<String> city=data.getExtras().getStringArrayList("citta");

				for (String s: city) {
					listaLocalita.add(new Localita(s));
					Log.d("localita", s);
				}
			}
		}
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

	public void goMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private static class ViewHolder {
		RadioButton svago;
		RadioButton lavoro;
		AutoCompleteTextView localita;
		TextView dataA;
		TextView dataR;
		MultiAutoCompleteTextView compViaggio;
		EditText descrizione;
		LinearLayout ll;
		int id;
	}
}

