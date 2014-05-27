package it.unisa.mytraveldiary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import it.unisa.mytraveldiary.db.DatabaseHandlerTravel;
import it.unisa.mytraveldiary.entity.Travel;
import it.unisa.mytraveldiary.entity.User;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
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
		
		switch (item.getItemId()) {
		case R.id.action_new_travel:
			return true;
			
		case R.id.action_search:
			return true;
			
		case R.id.action_info:
			goInfo();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class NetwokAccess extends AsyncTask<String, Void, String> {

		private Travel travel=new Travel();

		@Override
		protected String doInBackground(String... urls) {

			InputStream is=null;
			String contentType=null;
			URL url;
			HttpURLConnection connection = null;

			try {
				String urlParameters = "tipologia="+urls[1]+"&localita="+urls[2]+"&data_andata="+urls[3]+
										"&data_ritorno="+urls[4]+"&compagni_viaggio="+urls[5]+"&descrizione="+urls[6];
				url = new URL(urls[0]); 
				connection = (HttpURLConnection) url.openConnection();           
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false); 
				connection.setRequestMethod("POST"); 
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
				connection.setRequestProperty("charset", "utf-8");
				connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches (false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				connection.connect();

				is=connection.getInputStream();
				contentType=connection.getContentType();

				Log.d("CONTENT TYPE", contentType);

				String ret=null;

				if (contentType.equals("application/json")) {
					ret=getStringFromInputStream(is);


					if (!(ret.equals("Nessun risultato"))) {
						Log.d("CONNECTION", "Response text: "+ret);
						Log.d("CONNECTION", "Response text is: "+is);
						JSONObject object;
						try {
							object = new JSONObject(ret);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					else {
						Log.d("RESPONSE", "Nessun risultato response");
					}	
				}
				
				is.close();
				return ret;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "Error";
			} catch (IOException e) {
				e.printStackTrace();
				return "Error";
			}  finally {
				connection.disconnect();
			}
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("CONNECTION", "Response text onPost: "+result);
		}

		private String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return sb.toString();
		}
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
			// Create the adapter and set it to the AutoCompleteTextView 
			textView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item));
			
			// Compagni viaggio
			MultiAutoCompleteTextView compagniViaggio = (MultiAutoCompleteTextView) rootView.findViewById(R.id.compagniViaggioAutocomplete);
			// Create the adapter and set it to the AutoCompleteTextView 
			compagniViaggio.setAdapter(new CompagniViaggioAdapter(getActivity(), R.layout.list_item));
			compagniViaggio.setThreshold(2);
			compagniViaggio.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			return rootView;
		}
	}

	private boolean viaggioSalvato=false;
	private Travel viaggio;

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

	public void salvaViaggio(View view) throws ParseException {
		
		String stringUrl = "http://mtd.altervista.org/addTravel.php";
		ConnectivityManager connMgr = (ConnectivityManager)	getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		viaggio=new Travel();
		
		// GET
		// Tipologia viaggio
		RadioButton svago= (RadioButton) findViewById(R.id.Svago);
		RadioButton lavoro= (RadioButton) findViewById(R.id.Lavoro);
		
		// Data andata e ritorno
		TextView dataAndata=(TextView) findViewById(R.id.andataText);
		TextView dataRitorno=(TextView) findViewById(R.id.ritornoText);
		
		// Descrizione
		EditText descrizione= (EditText) findViewById(R.id.descrizioneViaggioInput);
		
		// Località
		AutoCompleteTextView localita=(AutoCompleteTextView) findViewById(R.id.localitaAutoComplete);

		// Compagni viaggio
		MultiAutoCompleteTextView compagniViaggio=(MultiAutoCompleteTextView) findViewById(R.id.compagniViaggioAutocomplete);

		// SET
		// Tipologia viaggio
		if (svago.isChecked()) 
			viaggio.setTipologiaViaggio("Svago");

		else if (lavoro.isChecked()) 
			viaggio.setTipologiaViaggio("Lavoro");
		
		// Località
		String localitaText=localita.getText().toString();
		viaggio.setLocalità(localitaText);
		
		// Data andata e ritorno
		String dataAndataString=dataAndata.getText().toString();
		String dataRitornoString=dataRitorno.getText().toString();
		
		if (!(dataAndataString.equals(""))) {
			Date dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataAndataString);
			viaggio.setDataAndata(dataA);
		}
		
		if (!(dataRitornoString.equals(""))) {
			Date dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataRitornoString);
			viaggio.setDataRitorno(dataR);
		}
		
		// Descrizione
		viaggio.setDescrizione(descrizione.getText().toString());
		
		// TODO Compagni viaggio
		ArrayList<User> compViaggio=new ArrayList<User>();
		viaggio.setCompagniViaggio(compViaggio);
		
		viaggioSalvato=true;

		Log.d("New travel", viaggio.toString());
		
		DatabaseHandlerTravel dbHandler=new DatabaseHandlerTravel(this);
		dbHandler.addTravel(viaggio);
		
		if (networkInfo != null && networkInfo.isConnected()) {
			new NetwokAccess().execute(stringUrl, viaggio.getTipologiaViaggio(), viaggio.getLocalità(), 
										viaggio.getDataAndata().toString(), viaggio.getDataRitorno().toString(),
										viaggio.getCompagniViaggio(), viaggio.getDescrizione());
		} else {
			Log.d("CONNECTION","No network connection available.");
			showToast("Nessuna connessione!");
		}
		
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

