package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {
	private ViaggiAdapter adapter;
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent) {
		// Get the intent, verify the action and get the query
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	private void doMySearch(String query) {
		DatabaseHandler dbHandler=new DatabaseHandler(this);
		ArrayList<Travel> listaViaggi=new ArrayList<Travel>();
		listaViaggi=dbHandler.doMySearch(query);

		Log.d("search", query);


		for (Travel t: listaViaggi)
			Log.d("search", t.toString());

		ListView listView=(ListView) findViewById(R.id.listView1);
		adapter=new ViaggiAdapter(getApplicationContext(), this, listaViaggi);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(), VisualizzaViaggioActivity.class);
				intent.putExtra("id", adapter.getTravelId(position));
				startActivity(intent);
			}
		});
	}

	private void filter(CharSequence charSequence) {
		MainActivity.this.adapter.getFilter().filter(charSequence);  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
		searchView.setSubmitButtonEnabled(true);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_search:
			onSearchRequested();
			return true;

		case R.id.action_new_travel:
			goNewTravel();
			return true;

		case R.id.action_info:
			goInfo();
			return true;

		case R.id.svago:
			if (item.isChecked()) {
				item.setChecked(false);
				filter("Svago");
			}

			else
				item.setChecked(true);
			return true;

		case R.id.lavoro:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
			return true;

		case R.id.uno:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
			return true;

		case R.id.due:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
			return true;

		case R.id.tre:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
			return true;

		case R.id.quattro:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
			return true;

		case R.id.cinque:
			if (item.isChecked())
				item.setChecked(false);

			else
				item.setChecked(true);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private ViaggiAdapter adapter;
		private DatabaseHandler dbHandler;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_main, container, false);

			dbHandler=new DatabaseHandler(getActivity());
			ListView listView=(ListView) rootView.findViewById(R.id.listView1);

			adapter = new ViaggiAdapter(getActivity().getApplicationContext(), getActivity(), dbHandler.getAllTravels());
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(getActivity(), VisualizzaViaggioActivity.class);
					intent.putExtra("id", adapter.getTravelId(position));
					startActivity(intent);
				}
			});

			/*listView.setOnLongClickListener(new View.OnLongClickListener() {
			    // Called when the user long-clicks on someView
			    public boolean onLongClick(View view) {
			        if (mActionMode != null) {
			            return false;
			        }

			        // Start the CAB using the ActionMode.Callback defined above
			        mActionMode = getActivity().startActionMode(mActionModeCallback);
			        view.setSelected(true);
			        return true;
			    }
			});*/

			return rootView;
		}

		/*private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		    // Called when the action mode is created; startActionMode() was called
		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate a menu resource providing context menu items
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.context_main, menu);
		        return true;
		    }

		    // Called each time the action mode is shown. Always called after onCreateActionMode, but
		    // may be called multiple times if the mode is invalidated.
		    @Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        return false; // Return false if nothing is done
		    }

		    // Called when the user selects a contextual menu item
		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        switch (item.getItemId()) {
		            case R.id.action_add_dettagli:
		                //shareCurrentItem();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
		        }
		    }

		    // Called when the user exits the action mode
		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		        mActionMode = null;
		    }
		};*/
	}

	private void goNewTravel() {
		Intent intent = new Intent(this, NewTravelActivity.class);
		startActivity(intent);
	}

	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}

	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}