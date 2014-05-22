package it.unisa.mytraveldiary;


import it.unisa.mytraveldiary.entity.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment())
			.commit();
		}
		Log.d("ACTIVITY", "LoginActivity");
		
		SharedPreferences settings = getSharedPreferences("login", 0);
		//SharedPreferences.Editor editor = settings.edit();
		
		if ((settings.getString("username", null))!=null) {
			goNewTravel();
			super.onStop();
		}

	}

	private class NetwokAccess extends AsyncTask<String, Void, String> {

		private boolean login=false;
		private User user=new User();

		@Override
		protected String doInBackground(String... urls) {

			InputStream is=null;
			String contentType=null;
			URL url;
			HttpURLConnection connection = null;

			try {
				String urlParameters = "username="+urls[1]+"&password="+urls[2];
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
							//user.setUsername(object.getString("username"));
							user.setNome(object.getString("nome"));
							user.setCognome(object.getString("cognome"));
							user.setLocalita(object.getString("localita"));
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// tutti null ???
						Log.d("USER", user.getUsername()+", "+user.getNome()+", "+user.getCognome()+", "+
								user.getLocalita());

						login=true;
						goNewTravel();
					}
					else {
						Log.d("RESPONSE", "Nessun risultato response");
						login=false;
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
				//TODO si dovrebbe chiudere is (InputStream)
				connection.disconnect();
			}
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("CONNECTION", "Response text onPost: "+result);

			if (!login) {
				showToast("Username/Password errati!");
			} else {
				SharedPreferences settings = getSharedPreferences("login", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("username", user.getUsername());
				
				// Commit the edits!
				editor.commit();
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
		getMenuInflater().inflate(R.menu.main, menu);
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			return rootView;
		}
	}

	/**
	 * Chiamato quando l'utente clicca sul bottone Entra
	 * @param view
	 */
	public void avanti(View view){

		// Chiamare una funzione che si occupa di fare i controlli sul'utente che restituisce
		// true o false; se è true si passa alla schermata successiva
		makeControls();
	}

	private void goNewTravel() {
		Intent intent = new Intent(this, NewTravelActivity.class);
		startActivity(intent);
	}

	private void makeControls() {

		String inputUsername, inputPassword;

		EditText editUsername = (EditText) findViewById(R.id.Login);
		inputUsername = editUsername.getText().toString(); 

		EditText editPassword = (EditText) findViewById(R.id.Password);
		inputPassword = editPassword.getText().toString(); 

		Log.d("LOGIN", "username: "+inputUsername+"; password: "+inputPassword);

		if (inputUsername.equals("") && inputPassword.equals("")) {
			showToast("Inserisci le credenziali!");
		}

		else if (!inputUsername.equals("") && inputPassword.equals("")) {
			showToast("Inserisci la password!");
		}

		else if (inputUsername.equals("") && !inputPassword.equals("")) {
			showToast("Inserisci l'username!");
		}

		else if (!inputUsername.equals("") && !inputPassword.equals("")) {

			String stringUrl = "http://mtd.altervista.org/login.php";
			ConnectivityManager connMgr = (ConnectivityManager)	getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			if (networkInfo != null && networkInfo.isConnected()) {
				new NetwokAccess().execute(stringUrl, inputUsername, inputPassword);
			} else {
				Log.d("CONNECTION","No network connection available.");
				showToast("Nessuna connessione!");
			}
			//showToast("...");
		}
		else {
			// nel caso l'utente è autenticato
			Intent intent = new Intent(this, NewTravelActivity.class);
			startActivity(intent);
		}
	}

	private void showToast(String msg) {
		Context context=getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}	

	/**
	 * Quando l'utente deve registrarsi e clicca sul bottone Registrati
	 * @param view
	 * **/

	public void avantiRegistrazione(View view){
		Intent intent = new Intent(this, RegistrazioneActivity.class);
		startActivity(intent);
	}
}
