package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandlerMusei;
import it.unisa.mytraveldiary.entity.Museo;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;


public class MuseiActivity extends ActionBarActivity {

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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_musei, container, false);
			
			AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.cittaMuseoAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
			
			return rootView;
		}
	}
	
	
	private boolean museoSalvato = false;
	private Museo museo = new Museo();
    
//Salva Viaggio
	public void salvaMuseo(View view){
		
	//Tipologia Museo
		Spinner tipologiaMuseo = (Spinner) findViewById(R.id.tipoMusei);
		
	//Nome Museo
        EditText editNome = (EditText) findViewById(R.id.nomeMuseoSugg);
     
     //Citt� Museo
        AutoCompleteTextView cittaMuseo = (AutoCompleteTextView) findViewById(R.id.cittaMuseoAutocomplete);
        
    //Valutazione
        RatingBar valutazioneM = (RatingBar) findViewById(R.id.ratingBar);
        
    //Tipologia Museo
        museo.setTipologia(tipologiaMuseo.getSelectedItem().toString());
		 
	//Nome Museo
        museo.setNome(editNome.getText().toString());
        
    //Citt�
        museo.setCitt�(cittaMuseo.getText().toString());
                
   //Valutazione
        museo.setValutazione((int) valutazioneM.getRating());
        
        
        Log.d("MUSEO", museo.toString());

        DatabaseHandlerMusei dbHandler = new DatabaseHandlerMusei(this);
        
        if(museoSalvato){
        	dbHandler.updateMuseo(museo);
        }
        else {
        	museoSalvato = true;
        	
        	museo.setId(dbHandler.addMuseo(museo));
        }
        
		showToast("Museo salvato correttamente!");

	}


	public void goInserisci(View view) {
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
