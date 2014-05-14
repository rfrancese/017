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

import android.os.AsyncTask;
import android.util.Log;

public class Network extends AsyncTask<String, Void, String> {

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
				
				User user=new User();
				
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
					
					//goWelcome();
				}

				else {
					Log.d("RESPONSE", "Nessun risultato response");
					//showToast("Username/password errati!");
				}
				
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
