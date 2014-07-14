package it.unisa.mytraveldiary;

import it.unisa.mytraveldiary.db.DatabaseHandler;
import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizzaViaggioActivity extends ActionBarActivity implements ActionBar.TabListener {

	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ViewPager mViewPager;
	private int value;
	private static final int MODIFICA=1;

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
		
		int[] resId={R.drawable.ic_action_about, R.drawable.ic_action_picture, R.drawable.ic_hotel, R.drawable.ic_ristorante,
						R.drawable.ic_trasporti, R.drawable.ic_museo, R.drawable.ic_action_place};

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setIcon(resId[i])
					.setTabListener(this));
		}
		
		Bundle extra=getIntent().getExtras();
		value=-1;
		
		if (extra!=null) 
			value=extra.getInt("id");
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
		case R.id.action_add_dettagli:
			showInserisciDettagli();
			return true;
		case R.id.action_modifica:
			goModifica();
			return true;
		case R.id.action_elimina:
			showElimina();
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
	
	private void showElimina() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.eliminaInfo);
		builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			DatabaseHandler dbHandler;
			Travel t;
			
			public void onClick(DialogInterface dialog, int id) {
				dbHandler=new DatabaseHandler(getApplicationContext());
				t=dbHandler.getTravel(value);

					if (dbHandler!=null) {
						dbHandler.deleteTravel(t);
					}
				showToast("Viaggio eliminato!");
				goMain();
			}
		});
		builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				showToast("Eliminazione annullata...");
			}
		});
		builder.setIcon(R.drawable.ic_action_warning);
		builder.setTitle(R.string.eliminaViaggio);

		builder.create();
		builder.show();
	}
	
	private void showInserisciDettagli() {
		SharedPreferences settings = getSharedPreferences("viaggio", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("id", value);

		// Commit the edits!
		editor.commit();

		DettagliDialogFragment dettagli=new DettagliDialogFragment();
		dettagli.show(getFragmentManager(), "dettagli");
	}
	
	private void goModifica() {
		Intent intent = new Intent(this, NewTravelActivity.class);
		intent.putExtra("modifica", true);
		intent.putExtra("id", value);
		startActivityForResult(intent, MODIFICA);
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

			TextView tipologiaViaggio= (TextView) rootView.findViewById(R.id.tipologiaViaggio);
			TextView localita= (TextView) rootView.findViewById(R.id.localita);
			TextView date= (TextView) rootView.findViewById(R.id.date);
			TextView compagniViaggio= (TextView) rootView.findViewById(R.id.compagniViaggio);
			TextView descrizione= (TextView) rootView.findViewById(R.id.descrizione);

			if (extra!=null) {
				int value=extra.getInt("id");

				t=dbHandler.getTravel(value);
			}

			tipologiaViaggio.setText(t.getTipologiaViaggio());
			localita.setText(t.getLocalitaString());
			date.setText(t.getDataAndata().toString()+" - "+t.getDataRitorno().toString());
			compagniViaggio.setText(t.getCompagniViaggio());
			descrizione.setText(t.getDescrizione());

			return rootView;
		}
	}

	public static class FotoFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<String> fotoList=new ArrayList<String>();

		public FotoFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_foto_video, container, false);

			Bundle extra=getActivity().getIntent().getExtras();
			dbHandler=new DatabaseHandler(getActivity());

			if (extra!=null) {
				int value=extra.getInt("id");
				fotoList=dbHandler.getFoto(value);
			}

			for (String s: fotoList) {
				LinearLayout ll=(LinearLayout) rootView.findViewById(R.id.linear);
				LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 0, 0, 8);
				ImageView imageView=new ImageView(getActivity());
				imageView.setLayoutParams(lp);
				imageView.setImageBitmap(FotoVideoActivity.decodeSampledBitmapFromResource(getResources(), 450, 450, s));
				ll.addView(imageView);
			}

			return rootView;
		}
	}

	public static class MuseiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<Museo> museiList=new ArrayList<Museo>();
		private MuseiAdapter mAdapter;

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

			mAdapter=new MuseiAdapter(getActivity(), R.layout.list_item_museo, museiList);
			View empty = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view, container, false);
			//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listView.setEmptyView(empty);
			listView.setAdapter(mAdapter);

			return rootView;
		}
	}

	public static class TrasportiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<Trasporto> trasportiList=new ArrayList<Trasporto>();
		private TrasportiAdapter tAdapter;

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

			tAdapter=new TrasportiAdapter(getActivity(), 
					R.layout.list_item_trasporto, trasportiList);
			View empty = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view, container, false);
			//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listView.setEmptyView(empty);
			listView.setAdapter(tAdapter);

			return rootView;
		}
	}

	public static class HotelFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<HotelRistorante> hotelList=new ArrayList<HotelRistorante>();
		private HotelsAdapter hAdapter;

		public HotelFragment() {
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
				hotelList=dbHandler.getHotel(value);
			}

			hAdapter=new HotelsAdapter(getActivity(), 
					R.layout.list_item_dettagli, hotelList);
			View empty = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view, container, false);
			//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listView.setEmptyView(empty);
			listView.setAdapter(hAdapter);

			return rootView;
		}
	}
	
	public static class RistorantiFragment extends Fragment {
		private DatabaseHandler dbHandler;
		private ArrayList<HotelRistorante> ristorantiList=new ArrayList<HotelRistorante>();
		private RistorantiAdapter rAdapter;

		public RistorantiFragment() {
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
				ristorantiList=dbHandler.getRistoranti(value);
			}

			rAdapter=new RistorantiAdapter(getActivity(), 
					R.layout.list_item_dettagli, ristorantiList);
			View empty = getActivity().getLayoutInflater().inflate(R.layout.empty_list_view, container, false);
			//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listView.setEmptyView(empty);
			listView.setAdapter(rAdapter);

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

			case 1:
				return new FotoFragment();

			case 2:
				return new HotelFragment();
				
			case 3:
				return new RistorantiFragment();

			case 4:
				return new TrasportiFragment();

			case 5:
				return new MuseiFragment();

			default:
				return new PlaceholderFragment();
			}
		}

		@Override
		public int getCount() {
			return 6;
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
				tabTitle="Hotel";
				break;
				
			case 3:
				tabTitle="Ristoranti";
				break;

			case 4:
				tabTitle="Trasporti";
				break;

			case 5:
				tabTitle="Musei";
				break;
			}

			return tabTitle;
		}
	}
	
	private void showToast(String msg) {
		Context context=getApplicationContext();
		CharSequence text=msg;
		int duration=Toast.LENGTH_SHORT;

		Toast toast=Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void goMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
