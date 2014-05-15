package it.unisa.mytraveldiary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MapsActivity extends ActionBarActivity {

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.

				mMap.setMyLocationEnabled(true);
				mMap.getUiSettings().setMyLocationButtonEnabled(true);

				Marker marker =mMap.addMarker(new MarkerOptions()
				.position(new LatLng(10, 10))
				.title("Hello world")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
				
				ConnectivityManager connMgr = (ConnectivityManager)	getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

				//String stringUrl = "input=Vict&types=geocode&language=it&sensor=true&key="+APIKEY;
				if (networkInfo != null && networkInfo.isConnected()) {
					new NetwokAccess().execute();
				} else {
					Log.d("CONNECTION","No network connection available.");
				}
			}
		}
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

	private class NetwokAccess extends AsyncTask<String, Void, String> {	
		@Override
		protected String doInBackground(String... urls) {
			ArrayList<String> risultati =autocomplete("Parigi");
			return null;
		}

			/*InputStream is=null;
			String contentType=null;
			URL url;
			HttpURLConnection connection = null;

			try {
				url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json");
				connection = (HttpURLConnection) url.openConnection();           
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false); 
				connection.setRequestMethod("POST"); 
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
				connection.setRequestProperty("charset", "utf-8");
				connection.setRequestProperty("Content-Length", "" + Integer.toString(urls[0].getBytes().length));
				connection.setUseCaches (false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(urls[0]);
				wr.flush();
				wr.close();

				connection.connect();

				is=connection.getInputStream();
				contentType=connection.getContentType();

				Log.d("CONTENT TYPE", contentType);

				String ret=null;

				if (contentType.equals("application/json")) {
					ret=getStringFromInputStream(is);
					JSONObject object;
					try {
						object = new JSONObject(ret);

						if (!(object.getString("status").equals("OK"))){
							Log.d("CONNECTION", "Response text: "+ret);
							Log.d("CONNECTION", "Response text is: "+is);
						} 
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}
				else {
					Log.d("RESPONSE", "Nessun risultato response");
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
		// onPostExecute displays the results of the AsyncTask.*/
		
		@Override
		protected void onPostExecute(String result) {
			Log.d("CONNECTION", "Response text onPost: "+result);
         }

		private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
		private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
		private static final String OUT_JSON = "/json";

		private static final String API_KEY = "AIzaSyBLZvrCDM4sxIvdlBNwW0uwSf_ZQUmwvWQ";

		private ArrayList<String> autocomplete(String input) {
		    ArrayList<String> resultList = null;

		    HttpURLConnection conn = null;
		    StringBuilder jsonResults = new StringBuilder();
		    try {
		        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
		        sb.append("?sensor=false&key=" + API_KEY);
		        sb.append("&components=country:it");
		        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

		        URL url = new URL(sb.toString());
		        conn = (HttpURLConnection) url.openConnection();
		        InputStreamReader in = new InputStreamReader(conn.getInputStream());

		        // Load the results into a StringBuilder
		        int read;
		        char[] buff = new char[1024];
		        while ((read = in.read(buff)) != -1) {
		            jsonResults.append(buff, 0, read);
		            Log.d("MAPS", buff.toString());
		        }
		    } catch (MalformedURLException e) {
		        Log.e("MAPS", "Error processing Places API URL", e);
		        return resultList;
		    } catch (IOException e) {
		        Log.e("MAPS", "Error connecting to Places API", e);
		        return resultList;
		    } finally {
		        if (conn != null) {
		            conn.disconnect();
		        }
		    }

		    try {
		        // Create a JSON object hierarchy from the results
		        JSONObject jsonObj = new JSONObject(jsonResults.toString());
		        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

		        // Extract the Place descriptions from the results
		        resultList = new ArrayList<String>(predsJsonArray.length());
		        for (int i = 0; i < predsJsonArray.length(); i++) {
		            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
		        }
		    } catch (JSONException e) {
		        Log.e("MAPS", "Cannot process JSON results", e);
		    }

		    return resultList;
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
}