package it.unisa.mytraveldiary;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MapsActivity extends ActionBarActivity  implements OnMapClickListener{

	private GoogleMap mMap;
	private Marker marker=null;

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

				/*marker =mMap.addMarker(new MarkerOptions()
				.position(new LatLng(10, 10))
				.title("Hello world")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));*/
				
				mMap.setOnMapClickListener(this);
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
	
	private void setMarker(LatLng point) {
		marker=mMap.addMarker(new MarkerOptions()
		.position(new LatLng(point.latitude, point.longitude))
		.title("Hello world")
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
		
		// Move the camera instantly with a zoom of 15.
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
		
		mMap.animateCamera(CameraUpdateFactory.scrollBy(xPixel, yPixel));
		// Zoom in, animating the camera.
		//mMap.animateCamera(CameraUpdateFactory.zoomIn());
	}

}