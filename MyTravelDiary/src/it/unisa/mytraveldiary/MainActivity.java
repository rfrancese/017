package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private ViaggiAdapter adapter;
	private DatabaseHandler dbHandler;
	private ArrayList<Travel> listaViaggi;
	private static final int MODIFICA=1;
	private static final int NUOVO=2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ViewHolder holder=new ViewHolder();
		dbHandler=new DatabaseHandler(this);
		listaViaggi=dbHandler.getAllTravels();
		holder.listView=(ListView) findViewById(R.id.listView1);

		adapter = new ViaggiAdapter(getApplicationContext(), this, listaViaggi);
		holder.listView.setAdapter(adapter);

		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goVisualizza(position);
			}
		});
		
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
		ViewHolder holder=new ViewHolder();
		listaViaggi=new ArrayList<Travel>();
		listaViaggi=dbHandler.doMySearch(query);

		Log.d("search", query);


		for (Travel t: listaViaggi)
			Log.d("search", t.toString());

		holder.listView=(ListView) findViewById(R.id.listView1);
		adapter=new ViaggiAdapter(getApplicationContext(), this, listaViaggi);

		holder.listView.setAdapter(adapter);

		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goVisualizza(position);
			}
		});
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == MODIFICA || requestCode == NUOVO) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	listaViaggi=dbHandler.getAllTravels();
	        	adapter.clear();
	        	adapter=new ViaggiAdapter(getApplicationContext(), this, listaViaggi);

				adapter.notifyDataSetChanged();
	        }
	    }
	}
	
	public void showPopup(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.context_main, popup.getMenu());
	    popup.show();
	}
	
	private void showInserisciDettagli(int position) {
		SharedPreferences settings = getSharedPreferences("viaggio", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("id", (listaViaggi.get(position)).getId());

		// Commit the edits!
		editor.commit();

		DettagliDialogFragment dettagli=new DettagliDialogFragment();
		dettagli.show(getFragmentManager(), "dettagli");
	}
	
	private void goModifica(int position) {
		Intent intent = new Intent(this, NewTravelActivity.class);
		intent.putExtra("id", adapter.getTravelId(position));
		startActivityForResult(intent, MODIFICA);
	}
	
	private void showElimina(int position) {
		final int pos=position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.eliminaInfo);
		builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Travel t=listaViaggi.get(pos);
				
				if (dbHandler!=null) {
					dbHandler.deleteTravel(t);

				}

				listaViaggi.remove(pos);

				adapter.notifyDataSetChanged();
				showToast("Viaggio eliminato!");
			}
		});
		builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				showToast("Eliminazione annullata...");
			}
		});
		builder.setIcon(R.drawable.ic_action_warning);
		builder.setTitle(R.string.eliminaViaggio);

		builder.create();
		builder.show();
	}

	private void goNewTravel() {
		Intent intent = new Intent(this, NewTravelActivity.class);
		startActivityForResult(intent, NUOVO);
	}

	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}

	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	private void goVisualizza(int position) {
		Intent intent = new Intent(this, VisualizzaViaggioActivity.class);
		intent.putExtra("id", adapter.getTravelId(position));
		startActivity(intent);
	}

	private void showToast(String msg) {
		Context context=getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}

	private static class ViewHolder {
		ListView listView;
	}
}