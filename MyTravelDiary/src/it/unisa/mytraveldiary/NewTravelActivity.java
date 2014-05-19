package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.entity.Travel;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
		
		private Travel viaggio;
		
		public PlaceholderFragment() {
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
					
			View rootView = inflater.inflate(R.layout.fragment_new_travel_message, container, false);
			
			// Get a reference to the AutoCompleteTextView in the layout
			AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
			
			
			// Tipologia viaggio
			RadioButton svago= (RadioButton) rootView.findViewById(R.id.Svago);
			RadioButton lavoro= (RadioButton) rootView.findViewById(R.id.Lavoro);
			
			if (svago.isSelected()) 
				viaggio.setTipologiaViaggio("Svago");
			
			else if (lavoro.isSelected()) 
				viaggio.setTipologiaViaggio("Lavoro");
			
			// Data andata
			
			// Compagni viaggio
			/*AutoCompleteTextView compagniViaggio = (AutoCompleteTextView) rootView.findViewById(R.id.compagniViaggioAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			compagniViaggio.setAdapter(new CompagniViaggioAdapter(getActivity(), R.layout.list_item));*/
			
			return rootView;
		}
	}
	
	private boolean viaggioSalvato=false;

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
	
	public void avantiInserisciDettagli(View view){
    	if (viaggioSalvato)
    		goInserisciDettagli();
    	
    	else
    		showToast("Salva prima il viaggio!");
    }
	
	public void salvaViaggio(View view) {
		viaggioSalvato=true;
		showToast("Viaggio salvato correttamente!");
	}
	
	public void openMaps(View view){
    	Intent intent = new Intent(this, MapsActivity.class);
    	//intent.putExtra("Citta", value)
    	startActivity(intent);
    }
	
	private void goInserisciDettagli() {
		Intent intent = new Intent(this, InserisciDettagliActivity.class);
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

