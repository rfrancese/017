package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class VisualizzaViaggioActivity extends ActionBarActivity implements ActionBar.TabListener {

	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizza_viaggio);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.pager, new PlaceholderFragment()).commit();
		}

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizza_viaggio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private Travel t;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_viaggio, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());

			RatingBar valutazione= (RatingBar) rootView.findViewById(R.id.ratingBar);
			TextView tipologiaViaggio= (TextView) rootView.findViewById(R.id.tipologiaViaggio);
			TextView localita= (TextView) rootView.findViewById(R.id.localita);
			TextView dataAndata= (TextView) rootView.findViewById(R.id.dataAndata);
			TextView dataRitorno= (TextView) rootView.findViewById(R.id.dataRitorno);
			TextView compagniViaggio= (TextView) rootView.findViewById(R.id.compagniViaggio);
			TextView descrizione= (TextView) rootView.findViewById(R.id.descrizione);

			if (extra!=null) {
				int value=extra.getInt("id");

				t=dbHandler.getTravel(value);
			}

			valutazione.setRating(0);
			tipologiaViaggio.setText("Tipologia viaggio: "+t.getTipologiaViaggio());
			localita.setText("Localita: "+t.getLocalitaString());
			dataAndata.setText(t.getDataAndata().toString());
			dataRitorno.setText(t.getDataRitorno().toString());
			compagniViaggio.setText(t.getCompagniViaggio());
			descrizione.setText("Descrizione: "+t.getDescrizione());

			return rootView;
		}
	}

	public static class MuseiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<Museo> museiList=new ArrayList<Museo>();
		private ArrayAdapter<Museo> mAdapter;

		public MuseiFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_dettagli, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());
			ListView listView=(ListView) rootView.findViewById(R.id.listViewDettagli);

			if (extra!=null) {
				int value=extra.getInt("id");
				museiList=dbHandler.getMusei(value);
			}

			mAdapter=new ArrayAdapter<Museo>(getActivity(), 
					R.layout.list_item_dettagli, museiList);

			listView.setAdapter(mAdapter);

			return rootView;
		}
	}

	public static class TrasportiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<Trasporto> trasportiList=new ArrayList<Trasporto>();
		private ArrayAdapter<Trasporto> tAdapter;

		public TrasportiFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_dettagli, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());
			ListView listView=(ListView) rootView.findViewById(R.id.listViewDettagli);
			View view=getActivity().findViewById(R.id.noItem);
			listView.setEmptyView(view);

			if (extra!=null) {
				int value=extra.getInt("id");
				trasportiList=dbHandler.getTrasporti(value);
			}

			tAdapter=new ArrayAdapter<Trasporto>(getActivity(), 
					R.layout.list_item_dettagli, trasportiList);

			listView.setAdapter(tAdapter);

			return rootView;
		}
	}

	public static class HotelRistorantiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<HotelRistorante> hotelRistorantiList=new ArrayList<HotelRistorante>();
		private ArrayAdapter<HotelRistorante> hrAdapter;

		public HotelRistorantiFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_visualizza_dettagli, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());
			ListView listView=(ListView) rootView.findViewById(R.id.listViewDettagli);

			if (extra!=null) {
				int value=extra.getInt("id");
				hotelRistorantiList=dbHandler.getHotelRistoranti(value);
			}

			hrAdapter=new ArrayAdapter<HotelRistorante>(getActivity(), 
					R.layout.list_item_dettagli, hotelRistorantiList);

			listView.setAdapter(hrAdapter);

			return rootView;
		}
	}

	private void goLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application.
				return new PlaceholderFragment();

				/*case 1:
                	return new MuseiFragment();*/

			case 2:
				return new HotelRistorantiFragment();

			case 3:
				return new TrasportiFragment();

			case 4:
				return new MuseiFragment();

			default:
				// The other sections of the app are dummy placeholders.
				//Fragment fragment=new InserisciDettagliActivity.PlaceholderFragment();
				return new PlaceholderFragment();
			}
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String tabTitle="";

			switch (position) {
			case 0:
				tabTitle="Viaggio";
				break;

			case 1:
				tabTitle="Foto";
				break;

			case 2:
				tabTitle="Hotel&Ristoranti";
				break;

			case 3:
				tabTitle="Trasporti";
				break;

			case 4:
				tabTitle="Musei";
				break;
			}

			return tabTitle;
		}
	}
}
