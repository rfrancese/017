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
	
	DatabaseHandler dbHandler;

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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<Localita> listaLocalita=new ArrayList<Localita>();
		private int tag;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_new_travel_message, container, false);

			ViewHolder holder=new ViewHolder();
			holder.svago=(RadioButton) rootView.findViewById(R.id.Svago);
			holder.lavoro=(RadioButton) rootView.findViewById(R.id.Lavoro);
			holder.dataA=(TextView) rootView.findViewById(R.id.andataText);
			holder.dataR=(TextView) rootView.findViewById(R.id.ritornoText);
			holder.descrizione=(EditText) rootView.findViewById(R.id.descrizioneViaggioInput);
			holder.ll=(LinearLayout) rootView.findViewById(R.id.localita);

			// Get a reference to the AutoCompleteTextView in the layout
			holder.localita = (AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			holder.localita.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));

			// Compagni viaggio
			holder.compViaggio = (MultiAutoCompleteTextView) rootView.findViewById(R.id.compagniViaggioAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			holder.compViaggio.setAdapter(new CompagniViaggioAdapter(getActivity(), R.layout.list_item));
			holder.compViaggio.setThreshold(2);
			holder.compViaggio.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
			
			Bundle extra=getActivity().getIntent().getExtras();

			if (extra!=null) {
				if ((extra.getString("tipologia")).equals("Svago"))
					holder.svago.setChecked(true);
				else if ((extra.getString("tipologia")).equals("Lavoro"))
					holder.lavoro.setChecked(true);

				holder.dataA.setText(extra.getString("dataA"));
				holder.dataR.setText(extra.getString("dataR"));
				holder.compViaggio.setText(extra.getString("compagni"));
				holder.descrizione.setText(extra.getString("descrizione"));
				holder.id=extra.getInt("id");
				
				addLocalita(rootView);
			}
			
			

			return rootView;
		}
		
		private void addLocalita(View rootView) {
			dbHandler=new DatabaseHandler(getActivity());
			ViewHolder holder=new ViewHolder();
			holder.localita=(AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
			holder.ll=(LinearLayout) rootView.findViewById(R.id.localita);
			String localita=(holder.localita.getText()).toString();
			listaLocalita.add(new Localita(localita));
			

			if (!(localita.equals(""))) {
				TextView textView=new TextView(getActivity());
				textView.setTag(holder.ll.getChildCount());
				Log.d("child ll", holder.ll.getChildCount()+"");
				textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT));
				textView.setPadding(0, 0, 0, 20);
				textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_cancel, 0);
				textView.setCompoundDrawablePadding(20);
				textView.setTextAppearance(getActivity(), R.style.CodeFont);
				textView.setClickable(true);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						tag=Integer.parseInt(view.getTag().toString());

						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
				});
				Log.d("add localita", "+"+(holder.localita.getText()).toString()+"+");
				textView.setText(localita);

				holder.ll.addView(textView);

				holder.localita.setText("");
			}
		}
		

		private void showToast(String msg) {
			Context context=getActivity();
			CharSequence text=msg;
			int duration=Toast.LENGTH_SHORT;

			Toast toast=Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	

	//private boolean viaggioSalvato=false;
	private boolean localitaSalvate=false;
	private Travel viaggio=new Travel();

	public void showDatePickerDialogAndata(View v) {
		ViewHolder holder=new ViewHolder();
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setTipologia("Andata");
		((DatePickerFragment) newFragment).setTextView(holder.dataA);
	}

	public void showDatePickerDialogRitorno(View v) {
		ViewHolder holder=new ViewHolder();
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

	private ArrayList<Localita> listaLocalita=new ArrayList<Localita>();

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

	/*	if (viaggioSalvato) {

			Log.d("TRAVEL", "viaggio da modificare");
			Log.d("New travel modifica", viaggio.toString());

			// aggiornare viaggio nel database
			dbHandler.updateTravel(viaggio);
		}*/

		if (modifica) {
			viaggio.setId(extra.getInt("id"));
			dbHandler.updateTravel(viaggio);
			//viaggioSalvato=true;
			
			dbHandler.deleteLocalitas(viaggio.getId());
			
			for (Localita loc: listaLocalita) {
				Log.d("localita", loc.toString());
				dbHandler.addLocalita(new Localita(loc.toString(), viaggio.getId()));
			}
			
			setTitle("Modifica Viaggio");
			Log.d("modifica", "yes");
			
			setResult(RESULT_OK);
			showToast("Viaggio modificato correttamente!");
			goMain();
		}

		else {
			//viaggioSalvato=true;

			viaggio.setId(dbHandler.addTravel(viaggio));
			
			Log.d("New travel", viaggio.toString());
			for (Localita l: listaLocalita)
				dbHandler.addLocalita(new Localita(l.toString(), viaggio.getId()));
			setResult(RESULT_OK);
			showToast("Viaggio salvato correttamente!");
			goMain();
		}
		
		/*if ((!localitaSalvate) && viaggioSalvato) {
			for (Localita l: listaLocalita)
				dbHandler.addLocalita(new Localita(l.toString(), viaggio.getId()));
			
			localitaSalvate=true;
		}*/
		
		
	}

	private void setSvago() {
		viaggio.setTipologiaViaggio("Svago");
	}

	private void setLavoro() {
		viaggio.setTipologiaViaggio("Lavoro");
	}

	public void openMaps(View view){
		ArrayList<String> locString=new ArrayList<String>();
		for (Localita l: listaLocalita)
			locString.add(l.toString());

		Intent intent = new Intent(this, MapsActivity.class);
		intent.putStringArrayListExtra("city", locString);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ViewHolder holder=new ViewHolder();
		Log.d("città", requestCode+" "+resultCode);
		if (requestCode==1 && resultCode==RESULT_OK) {
			Log.d("città", requestCode+" "+resultCode);
			if (data!=null) {
				Log.d("città", requestCode+" "+resultCode);
				String city=data.getExtras().getString("citta");
				holder.localita.setText(city);
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

	private int tag;
	

	public void addLocalita(View view) {
		dbHandler=new DatabaseHandler(this);
		ViewHolder holder=new ViewHolder();
		holder.localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);
		holder.ll=(LinearLayout) findViewById(R.id.localita);
		String localita=(holder.localita.getText()).toString();
		listaLocalita.add(new Localita(localita));
		

		if (!(localita.equals(""))) {
			TextView textView=new TextView(this);
			textView.setTag(holder.ll.getChildCount());
			Log.d("child ll", holder.ll.getChildCount()+"");
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
			});
			Log.d("add localita", "+"+(holder.localita.getText()).toString()+"+");
			textView.setText(localita);

			holder.ll.addView(textView);

			holder.localita.setText("");
		}
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

