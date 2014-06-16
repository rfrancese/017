package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_search:
			return true;

		case R.id.action_new_travel:
			goNewTravel();
			return true;

		case R.id.action_info:
			goInfo();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private ListView listView;
		//private DatabaseHandler dbHander;
		//private ArrayAdapter<String> adapter;
		//private ArrayList<Travel> viaggiList=new ArrayList<Travel>();


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_main, container, false);

			listView= (ListView) rootView.findViewById(R.id.listView1);

			try {
				ViaggiAdapter adapter = new ViaggiAdapter(getActivity().getApplicationContext());
				listView.setAdapter(adapter);

			} catch (ParseException e) {
				e.printStackTrace();
			}


			return rootView;
		}
	}

	public void avantiVisualizzaViaggio(View view){
		Intent intent = new Intent(this, VisualizzaViaggioActivity.class);
		startActivity(intent);
	}

	private void goNewTravel() {
		Intent intent = new Intent(this, NewTravelActivity.class);
		startActivity(intent);
	}

	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}
}