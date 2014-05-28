package it.unisa.mytraveldiary.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.unisa.mytraveldiary.entity.Travel;
import it.unisa.mytraveldiary.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTravel extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=8;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRAVELS="travels";
	private static final String T_TIPOLOGIA="tipologia";
	private static final String T_LOCALITA="localita";
	private static final String T_DATA_ANDATA="dataAndata";
	private static final String T_DATA_RITORNO="dataRitorno";
	private static final String T_COMPAGNI_VIAGGIO="compagniViaggio";
	private static final String T_DESCRIZIONE="descrizione";
	private static final String T_ID="id";

	public DatabaseHandlerTravel(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRAVELS_TABLE="CREATE TABLE "+TABLE_TRAVELS +" (" +
				T_TIPOLOGIA +" VARCHAR(10),"+
				T_LOCALITA + " VARCHAR(50),"+
				T_DATA_ANDATA + " DATE,"+
				T_DATA_RITORNO + " DATE,"+
				T_COMPAGNI_VIAGGIO + " VARCHAR(100),"+
				T_DESCRIZIONE + " TEXT,"+
				T_ID + " INTEGER NOT NULL PRIMARY KEY)";
		db.execSQL(CREATE_TRAVELS_TABLE);
		
		Log.d("Creating...", "Travels");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRAVELS);

		onCreate(db);
	}


	// Metodi di modifica
	// Aggiunge un viaggio

	public void addTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		String dataA=null, dataR=null;
		
		if (!(travel.getDataAndata()==null)) {
			dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataAndata());
		}
		
		if (!(travel.getDataRitorno()==null)) {
			dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataRitorno());
		}
		
		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATA_ANDATA, dataA);
		values.put(T_DATA_RITORNO, dataR);
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());

		Log.d("DB VIAGGIO", travel.toString());
		
		db.insert(TABLE_TRAVELS, null, values);
		db.close();
	}


	// Aggiorna un viaggio

	public int updateTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATA_ANDATA, (travel.getDataAndata()).toString());
		values.put(T_DATA_RITORNO, (travel.getDataRitorno()).toString());
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());

		return db.update(TABLE_TRAVELS, values, T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
	}


	// Cancella un viaggio 

	public void deleteTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_TRAVELS, T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.close();
	}


	// Metodi di accesso
	// Ritorna un viaggio

	public Travel getTravel(int id) throws NumberFormatException, ParseException {
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_TRAVELS, new String[] {T_ID}, T_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		String tipologia=cursor.getString(0);
		String localita=cursor.getString(1);
		String dataAndata=cursor.getString(2);
		String dataRitorno=cursor.getString(3);
		String compagniViaggio=cursor.getString(4);
		String descrizione=cursor.getString(5);
		String idTravel=cursor.getString(6);
		
		Date dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataAndata);
		Date dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataRitorno);
		
		Travel travel= new Travel(tipologia, localita, dataA, dataR, compagniViaggio, descrizione, 
				Integer.parseInt(idTravel));

		
		return travel;
	}

	// Ritorna tutti i viaggi

	public ArrayList<Travel> getAllTravels() throws ParseException {
		ArrayList<Travel> travelList=new ArrayList<Travel>();
		String selectQuery="SELECT * FROM " + TABLE_TRAVELS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
		ArrayList<User> compagniV;

		if (cursor.moveToFirst()) {
			do {
				String tipologia=cursor.getString(0);
				String localita=cursor.getString(1);
				String dataAndata=cursor.getString(2);
				String dataRitorno=cursor.getString(3);
				String compagniViaggio=cursor.getString(4);
				String descrizione=cursor.getString(5);
				String idTravel=cursor.getString(6);
				
				Date dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataAndata);
				Date dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).parse(dataRitorno);
				
				Travel travel= new Travel(tipologia, localita, dataA, dataR, compagniViaggio, descrizione, 
						Integer.parseInt(idTravel));
				
				travelList.add(travel);
			} 
			while (cursor.moveToNext());
		}
		return travelList;
	}

	private ArrayList<User> getcompagniViaggio(String compagniViaggio){
		String[] idCompViaggio= compagniViaggio.split(",");
		User compagno=new User();
		ArrayList<User> compViaggio=new ArrayList<User>();

		for (int i=0; i<idCompViaggio.length; i++) {
			String selectQuery="SELECT * FROM users WHERE id="+idCompViaggio[i];
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cursor=db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				compagno.setUsername(cursor.getString(0));
				compagno.setPassword(cursor.getString(1));
				compagno.setId(Integer.parseInt(cursor.getString(2)));

				compViaggio.add(compagno);
			}
		}
		return compViaggio;
	}
}