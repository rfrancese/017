package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandlerTrasporti;
import it.unisa.mytraveldiary.entity.Trasporto;
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

public class TrasportiActivity extends ActionBarActivity {

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

	private boolean trasportoSalvato = false;
	private Trasporto trasporto = new Trasporto();
    
//Salva Viaggio
	public void salvaTrasporto(View view){
		
	//TipologiaTrasporto
		Spinner tipologiaTrasporto = (Spinner) findViewById(R.id.tipoTrasporti);
		
	//CompagniaTrasporto
        EditText editCompagnia = (EditText) findViewById(R.id.compagniaSugg);
     
     //Citt‡
        AutoCompleteTextView cittaP = (AutoCompleteTextView) findViewById(R.id.cittaPartenzaAutocomplete);
        AutoCompleteTextView cittaA = (AutoCompleteTextView) findViewById(R.id.cittaArrivoAutocomplete);

    //Valutazione
        RatingBar valutazioneT = (RatingBar) findViewById(R.id.ratingBar);
        
    //TipologiaTrasporto
        trasporto.setTipologia(tipologiaTrasporto.getSelectedItem().toString());
		 
	//CompagniaTrasporto
        trasporto.setCompagnia(editCompagnia.getText().toString());
        
    //Citt‡
        trasporto.setCitt‡Partenza(cittaP.getText().toString());
        trasporto.setCitt‡Arrivo(cittaA.getText().toString());
        
   //Valutazione
        trasporto.setValutazione((int) valutazioneT.getRating());
        
        
        Log.d("TRASPORTI", trasporto.toString());

        DatabaseHandlerTrasporti dbHandler = new DatabaseHandlerTrasporti(this);
        
        if(trasportoSalvato){
        	dbHandler.updateTrasporto(trasporto);
        }
        else {
        	trasportoSalvato = true;
        	
        	trasporto.setId(dbHandler.addTrasporto(trasporto));
        }
        
		showToast("Trasporto salvato correttamente!");

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
