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

public class CityAdapter extends ArrayAdapter<String> implements Filterable {
	
	private ArrayList<String> resultCityList;

	public CityAdapter(Context context, int resource) {
		super(context, resource);
	}
	
	 @Override
	    public int getCount() {
	        return resultCityList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultCityList.get(index);
	    }


	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultCityList = getNearbyCities(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultCityList;
                    filterResults.count = resultCityList.size();
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

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBLZvrCDM4sxIvdlBNwW0uwSf_ZQUmwvWQ";

    private ArrayList<String> getNearbyCities(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_SEARCH + OUT_JSON);
            sb.append("?sensor=true&key=" + API_KEY);
            sb.append("&location=");
            sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

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
}
