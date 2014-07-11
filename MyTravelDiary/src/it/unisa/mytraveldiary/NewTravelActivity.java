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
import android.view.LayoutInflater;
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
	private int tag;
	private boolean localitaSalvate=false;
	private Travel viaggio=new Travel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_travel_message);

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
		
		Bundle extra=getIntent().getExtras();
		dbHandler=new DatabaseHandler(this);

		if (extra!=null) {
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
			String s="";
			
			for (Localita l: listaLocalita)
				s+=l.toString();
			
			holder.localita.setText(s);
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
		DialogFragment newFragment = new DatePickerFragment();
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
		
			salvaViaggio();
			SharedPreferences settings = getSharedPreferences("viaggio", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("id", viaggio.getId());

			// Commit the edits!
			editor.commit();

			DettagliDialogFragment dettagli=new DettagliDialogFragment();
			dettagli.show(getFragmentManager(), "dettagli");
		//}

		/*else
			showToast("Salva prima il viaggio!");*/
	}

	public void salvaViaggio() {
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
		
		if (dataAndataString.compareTo(dataRitornoString)>0)
			Log.d("data", "?");

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
			viaggio.setId(extra.getInt("id"));
			dbHandler.updateTravel(viaggio);
			//viaggioSalvato=true;
			
			/*dbHandler.deleteLocalitas(viaggio.getId());
			
			for (Localita loc: listaLocalita) {
				Log.d("localita", loc.toString());
				dbHandler.addLocalita(new Localita(loc.toString(), viaggio.getId()));
			}*/
			
			Log.d("modifica", "yes");
			
			setResult(RESULT_OK);
			showToast("Viaggio modificato correttamente!");
			goMain();
		}

		else {
			//viaggioSalvato=true;
			viaggio.setLocalità(listaLocalita);
			viaggio.setId(dbHandler.addTravel(viaggio));
			
			setResult(RESULT_OK);
			showToast("Viaggio salvato correttamente!");
			goMain();
		}
	}

	private void setSvago() {
		viaggio.setTipologiaViaggio("Svago");
	}

	private void setLavoro() {
		viaggio.setTipologiaViaggio("Lavoro");
	}

	public void openMaps(View view){
		/*ArrayList<String> locString=new ArrayList<String>();
		for (Localita l: listaLocalita)
			locString.add(l.toString());*/

		Intent intent = new Intent(this, MapsActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

	/*public void addLocalita(View view) {
		ViewHolder holder=new ViewHolder();
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		holder.localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		holder.ll=(LinearLayout) findViewById(R.id.localita);
		String localita=(holder.localita.getText()).toString();
		listaLocalita.add(new Localita(localita));
		

		if (!(localita.equals(""))) {
			TextView textView=(TextView) findViewById(R.id.localita_item);
			Log.d("add localita", "+"+(holder.localita.getText()).toString()+"+");
			textView.setText(localita);
			textView.setId(holder.ll.getChildCount());

			inflater.inflate(R.layout.info_window, holder.ll);

			holder.localita.setText("");
		}
	}*/
	
	public void deleteLocalita(View view) {
		tag=Integer.parseInt(view.getTag().toString());

		AlertDialog.Builder builder = new AlertDialog.Builder(NewTravelActivity.this);
		builder.setMessage(R.string.eliminaLocalitaInfo);
		builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				ViewHolder holder=new ViewHolder();
				holder.ll.removeViewAt(tag);
				listaLocalita.remove(tag);
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

