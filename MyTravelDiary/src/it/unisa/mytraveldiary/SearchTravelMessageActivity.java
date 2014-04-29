package it.unisa.mytraveldiary;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.os.Build;

public class SearchTravelMessageActivity extends ActionBarActivity {

	private ListView mainListView;
	private ArrayAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_travel_message);
		
		mainListView=(ListView) findViewById(R.id.risultatiRicercaTabella);
		String[] listaViaggi=new String[]{"Viaggio 1", "Viaggio 2", "Viaggio 3"};
		
		final ArrayList<String> list=new ArrayList<String>();
		
		for (int i=0; i<listaViaggi.length; i++) {
			list.add(listaViaggi[i]);
		}
		
		final ArrayAdapter<String> adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		mainListView.setAdapter(listAdapter);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_travel_message, menu);
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_search_travel_message, container, false);
			return rootView;
		}
	}

	
}
