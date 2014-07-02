package it.unisa.mytraveldiary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapsActivity extends ActionBarActivity  implements OnMapClickListener {

	private GoogleMap mMap;
	private Marker marker=null;
	private ProgressDialog progressDialog;
	private String city;


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
				mMap.setOnMapClickListener(this);

				ArrayList<String> listaLocalita=getIntent().getStringArrayListExtra("city");

				if (listaLocalita!=null) {
					setMarkerFromList(listaLocalita);
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_accept:
			setResult(RESULT_OK, getIntent().putExtra("citta", city));
			finish();
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
	public void onMapClick(LatLng point) {

		if (marker==null) {
			setMarker(point);
		}

		else {
			marker.remove();
			setMarker(point);
		}
	}

	private void setMarkerFromList(ArrayList<String> listaLoc) {
		Geocoder geodecoder=new Geocoder(this);
		PolylineOptions rectOptions = new PolylineOptions();
		
		for (String s: listaLoc) {
			List<Address> listAdress;
			try {
				listAdress = geodecoder.getFromLocationName(s, 1);
				Address a=listAdress.get(0);
				LatLng point=new LatLng(a.getLatitude(), a.getLongitude());
				rectOptions.add(point);
				
				marker=mMap.addMarker(new MarkerOptions()
				.position(point)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
		}
		
		mMap.addPolyline(rectOptions
				.width(7)
				.color(Color.BLUE)
				.geodesic(true));
	}

	private void setMarker(LatLng point) {

		Location location=new Location("Maps");
		location.setLatitude(point.latitude);
		location.setLongitude(point.longitude);

		progressDialog=ProgressDialog.show(this, "Attendi...", "Ricerca località...");
		progressDialog.setCancelable(false);

		(new MapsActivity.GetAddressTask(this)).execute(location);

		marker=mMap.addMarker(new MarkerOptions()
		.position(new LatLng(point.latitude, point.longitude))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

		// Move the camera instantly with a zoom of 10.
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10));

		Log.d("MAPS", point.latitude+", "+point.longitude);
	}

	protected class GetAddressTask extends AsyncTask<Location, Void, String> {

		// Store the context passed to the AsyncTask when the system instantiates it.
		Context localContext;

		// Constructor called by the system to instantiate the task
		public GetAddressTask(Context context) {

			// Required by the semantics of AsyncTask
			super();

			// Set a Context for the background task
			localContext = context;
		}

		/**
		 * Get a geocoding service instance, pass latitude and longitude to it, format the returned
		 * address, and return the address to the UI thread.
		 */
		@Override
		protected String doInBackground(Location... params) {
			/*
			 * Get a new geocoding service instance, set for localized addresses. This example uses
			 * android.location.Geocoder, but other geocoders that conform to address standards
			 * can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			// Get the current location from the input parameter list
			Location location = params[0];

			// Create a list to contain the result address
			List <Address> addresses = null;

			// Try to get an address for the current location. Catch IO or network problems.
			try {

				/*
				 * Call the synchronous getFromLocation() method with the latitude and
				 * longitude of the current location. Return at most 1 address.
				 */
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1
						);

				// Catch network or other I/O problems.
			} catch (IOException exception1) {

				// print the stack trace
				exception1.printStackTrace();

				// Return an error message
				return "IO Exception in Geocoder.getFromLocation()";

				// Catch incorrect latitude or longitude values
			} catch (IllegalArgumentException exception2) {

				exception2.printStackTrace();

				return  "Illegal arguments: Latitude +"+location.getLatitude()+
						" Longitude "+location.getLongitude();
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address
				Address address = addresses.get(0);

				// Format the first line of address
				String addressText = getString(R.string.address_output_string,

						// Locality is usually a city
						address.getLocality()
						);

				city=addressText;

				// Return the text
				return addressText;

				// If there aren't any addresses, post a message
			} else {
				return "No address found for location";
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Set the text of the
		 * UI element that displays the address. This method runs on the UI thread.
		 */
		@Override
		protected void onPostExecute(String address) {

			// Turn off the progress bar
			progressDialog.dismiss();

			if (marker!=null) {
				marker.setTitle(address);
			}

			// Set the address in the UI
			Log.d("MAPS", address);
			showToast("Hai selezionato "+address);
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
