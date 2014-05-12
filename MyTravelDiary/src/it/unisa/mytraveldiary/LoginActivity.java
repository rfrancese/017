package it.unisa.mytraveldiary;


import it.unisa.mytraveldiary.entity.User;

import java.io.BufferedReader;
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

		/*ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			Log.d("Connecting...", "OK");
		} else {
			Log.d("Connecting...", "NO");
		}

		DatabaseHandlerUsers db=new DatabaseHandlerUsers(this);
		int count=db.getUsersCount();

		Log.d("Reading: ", "CountMain: "+count); */

		// Gets the URL from the UI's text field.
		String stringUrl = "http://mtd.altervista.org/index.php";
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new NetwokAccess().execute(stringUrl);
		} else {
			Log.d("CONNECTION","No network connection available.");
		}
	}

	private class NetwokAccess extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			InputStream is=null;
			String contentType=null;

			try {
				URL url=new URL(urls[0]);
				HttpURLConnection connection= (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();

				int response=connection.getResponseCode();
				Log.d("CONNECTION", "Response code: "+response);
				is=connection.getInputStream();
				contentType=connection.getContentType();

				Log.d("CONTENT TYPE", contentType);

				String ret=null;
				User user=new User();
				
				if (contentType.equals("application/json")) {
					ret=getStringFromInputStream(is);
					Log.d("CONNECTION", "Response text: +"+ret+"+");
					JSONObject object=new JSONObject(ret);
					user.setUsername(object.getString("username"));
					user.setPassword(object.getString("password"));
					
					Log.d("USER", user.getUsername()+", "+user.getPassword());
				}

				return ret;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "Error";
			} catch (IOException e) {
				e.printStackTrace();
				return "Error";
			} catch (JSONException e) {
				e.printStackTrace();
				return "Error";
			} finally {


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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			return rootView;
		}
	}

	/**
	 * Chiamato quando l'utente clicca sul bottone Entra
	 * @param view
	 */
	public void avanti(View view){
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
	}


}