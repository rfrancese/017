package it.unisa.mytraveldiary;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class CompagniViaggioAdapter extends ArrayAdapter<String> implements Filterable {
	private ArrayList<String> resultList;

	public CompagniViaggioAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
				else {
					notifyDataSetInvalidated();
				}
			}};
			return filter;
	}

	private static final String MTD = "http://mtd.altervista.org/compagniViaggio.php";

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		if (!input.equals(null)) {
			String[] utente=input.split(" ");
			String nome=utente[0];
			String cognome=utente[1];

			Log.d("COMPAGNI_VIAGGIO", nome+" "+cognome);

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			try {
				StringBuilder sb = new StringBuilder(MTD);
				sb.append("?nome=" + URLEncoder.encode(nome, "utf8"));
				sb.append("&cognome=" + URLEncoder.encode(cognome, "utf8"));

				URL url = new URL(sb.toString());
				conn = (HttpURLConnection) url.openConnection();
				InputStreamReader in = new InputStreamReader(conn.getInputStream());

				// Load the results into a StringBuilder
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
			} catch (MalformedURLException e) {
				Log.e("COMPAGNI_VIAGGIO", "Error processing compagniViaggio.php", e);
				return resultList;
			} catch (IOException e) {
				Log.e("COMPAGNI_VIAGGIO", "Error connecting compagniViaggio.php", e);
				return resultList;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}

			try {
				// Create a JSON object hierarchy from the results
				Log.d("COMPAGNI_VIAGGIO", jsonResults.toString());
				JSONObject jsonObj = new JSONObject(jsonResults.toString());
				JSONArray predsJsonArray = jsonObj.getJSONArray("compagniViaggio");

				// Extract the Place descriptions from the results
				resultList = new ArrayList<String>(predsJsonArray.length());
				for (int i = 0; i < predsJsonArray.length(); i++) {
					resultList.add(predsJsonArray.getJSONObject(i).getString("username"));
				}

			} catch (JSONException e) {
				Log.e("COMPAGNI_VIAGGIO", "Cannot process JSON results", e);
			}
		}

		return resultList;
	}
}