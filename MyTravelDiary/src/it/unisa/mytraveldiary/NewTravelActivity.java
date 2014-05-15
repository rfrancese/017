package it.unisa.mytraveldiary;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;


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

		public PlaceholderFragment() {
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
					
			View rootView = inflater.inflate(R.layout.fragment_new_travel_message, container, false);
			
			// Get a reference to the AutoCompleteTextView in the layout
			AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.localitaAutoComplete);
			// Get the string array
			String[] countries = getResources().getStringArray(R.array.localita);
			// Create the adapter and set it to the AutoCompleteTextView 
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
			textView.setAdapter(adapter);

			return rootView;
		}
	}

	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void avantiInserisciDettagli(View view){
    	Intent intent = new Intent(this, InserisciDettagliActivity.class);
    	startActivity(intent);
    }
	
	public void openMaps(View view){
    	Intent intent = new Intent(this, MapsActivity.class);
    	startActivity(intent);
    }
}

