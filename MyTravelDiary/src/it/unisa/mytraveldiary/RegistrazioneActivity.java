package it.unisa.mytraveldiary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrazioneActivity extends ActionBarActivity {
	
	private boolean login=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrazione);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	private class NetwokAccess extends AsyncTask<String, Void, String> {
		

		@Override
		protected String doInBackground(String... urls) {

			InputStream is=null;
			String contentType=null;
			URL url;
			HttpURLConnection connection = null;


			try {

				String urlParameters = "nome="+urls[1]+"&cognome="+urls[2]+"&localita="+urls[3]+"&username="+urls[4]+
						"&password="+urls[5];
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

				ret=getStringFromInputStream(is);
               
				Log.d("RESPONSE QUERY", ret);

				
				// modificare

				if (! ret.equals("OK")) {
					//showToast("Utente registrato!");
					login=true;
					goLogin();
				}

				else {
					//caso in cui l'utente non � inserito nel DataBase
					Log.d("RESPONSE", "Nessun risultato response");
					//showToast("Errore: utente non registrato. Riprova!");
					login=false;
				}

			return ret;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "Error";
		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		}  finally {
			//TODO si dovrebbe chiudere is (InputStream)
			connection.disconnect();
		}
	}
	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		Log.d("CONNECTION", "Response text onPost: "+result);
		
		if (login) {
			showToast("Utente registrato!");
		} else {
			showToast("Errore: utente non registrato. Riprova!");
		}
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

@Override
public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.registrazione, menu);
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
		View rootView = inflater.inflate(R.layout.fragment_registrazione,
				container, false);
		return rootView;
	}
}

public void salvaRegistrazione(View view) {
	makeControls();
}

private void makeControls() {

	String inputUsername, inputPassword, inputConfermaPassword, inputNome, inputCognome, inputLocalita;

	EditText editUsername = (EditText) findViewById(R.id.usernameUtenteInput);
	inputUsername = editUsername.getText().toString(); 

	EditText editPassword = (EditText) findViewById(R.id.passwordUtenteInput);
	inputPassword = editPassword.getText().toString(); 

	EditText editConfermaPassword = (EditText) findViewById(R.id.confermaPasswordUtenteInput);
	inputConfermaPassword = editConfermaPassword.getText().toString(); 

	EditText editNome = (EditText) findViewById(R.id.nomeUtenteInput);
	inputNome = editNome.getText().toString(); 

	EditText editCognome = (EditText) findViewById(R.id.cognomeUtenteInput);
	inputCognome = editCognome.getText().toString(); 

	EditText editLocalita = (EditText) findViewById(R.id.localitaUtenteInput);
	inputLocalita = editLocalita.getText().toString(); 


	//Log.d("LOGIN", "username: "+inputUsername+"; password: "+inputPassword);

	if (inputUsername.equals("") || inputPassword.equals("") || inputConfermaPassword.equals("") ||
			inputNome.equals("") || inputCognome.equals("") || inputLocalita.equals("")) {

		showToast("Inserisci tutti i campi!");
	}

	else if (!inputUsername.equals("") && !inputPassword.equals("") && !inputConfermaPassword.equals("") &&
			!inputNome.equals("") && !inputCognome.equals("") && !inputLocalita.equals("")) {

		if (!inputPassword.equals(inputConfermaPassword)) {
			showToast("Le password non coincidono!");
		}
		else {

		String stringUrl = "http://mtd.altervista.org/registrazione.php";
		ConnectivityManager connMgr = (ConnectivityManager)	getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			new NetwokAccess().execute(stringUrl, inputNome, inputCognome, inputLocalita, inputUsername,
					inputPassword);
		} else {
			Log.d("CONNECTION","No network connection available.");
			showToast("Nessuna connesione!");
		}
		//showToast("ok");
		
		
	}
}
}

private void showToast(String msg) {
	Context context=getApplicationContext();
	CharSequence text=msg;
	int duration=Toast.LENGTH_SHORT;

	Toast toast=Toast.makeText(context, text, duration);
	toast.show();
}

private void goLogin() {
	Intent intent = new Intent(this, LoginActivity.class);
	startActivity(intent);
}
}


