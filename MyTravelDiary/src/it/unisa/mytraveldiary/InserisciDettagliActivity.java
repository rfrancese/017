package it.unisa.mytraveldiary;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class InserisciDettagliActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inserisci_dettagli);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inserisci_dettagli, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_inserisci_dettagli, container, false);
			return rootView;
		}
	}
	
	public void avantiFotoVideo(View view){
    	Intent intent = new Intent(this, FotoVideoActivity.class);
    	startActivity(intent);
    }
	
	public void avantiTrasporti(View view){
    	Intent intent = new Intent(this, TrasportiActivity.class);
    	startActivity(intent);
    }
	
	public void avantiMusei(View view){
    	Intent intent = new Intent(this, MuseiActivity.class);
    	startActivity(intent);
    }
	
	public void avantiHotelRistoranti(View view){
    	Intent intent = new Intent(this, HotelRistorantiActivity.class);
    	startActivity(intent);
    }
	
	public void tornaHomepage(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	private void goInfo() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
	}
	
}
