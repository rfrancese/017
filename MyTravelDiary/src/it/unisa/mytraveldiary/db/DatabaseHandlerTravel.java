package it.unisa.mytraveldiary.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.unisa.mytraveldiary.entity.Travel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTravel extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=13;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRAVELS="travels";
	private static final String T_TIPOLOGIA="tipologia";
	private static final String T_LOCALITA="localita";
	private static final String T_DATA_ANDATA="dataAndata";
	private static final String T_DATA_RITORNO="dataRitorno";
	private static final String T_COMPAGNI_VIAGGIO="compagniViaggio";
	private static final String T_DESCRIZIONE="descrizione";
	private static final String T_ID="id";
	
	private static final String TABLE_HOTELRISTORANTI="hotelRistoranti";
	private static final String HR_TIPOLOGIA="tipologia";
	private static final String HR_NOME="nome";
	private static final String HR_CITTA="citta";
	private static final String HR_VALUTAZIONE="valutazione";
	private static final String HR_ID="id";

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
				T_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_TRAVELS_TABLE);
		
		String CREATE_HOTELRISTORANTI_TABLE="CREATE TABLE "+TABLE_HOTELRISTORANTI+" (" +
				HR_TIPOLOGIA+" VARCHAR(25)," +
				HR_NOME+" VARCHAR(25)," +
				HR_CITTA+" VARCHAR(30)," +
				HR_VALUTAZIONE+" INTEGER," +
				HR_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_HOTELRISTORANTI_TABLE);

		Log.d("Creating...", "Travels");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRAVELS);

		onCreate(db);
	}


	// Metodi di modifica
	// Aggiunge un viaggio

	public int addTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		String dataA=null, dataR=null;

		if (!(travel.getDataAndata()==null)) {
			dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataAndata());
		}

		if (!(travel.getDataRitorno()==null)) {
			dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataRitorno());
		}
		
		if (travel.getTipologiaViaggio()==null) {
			travel.setTipologiaViaggio("");
		}

		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATA_ANDATA, dataA);
		values.put(T_DATA_RITORNO, dataR);
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());

		Log.d("DB VIAGGIO", travel.toString());

		int id=(int) db.insert(TABLE_TRAVELS, null, values);
		db.close();

		return id;
	}


	// Aggiorna un viaggio

	public int updateTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		String dataA=null, dataR=null;

		if (!(travel.getDataAndata()==null)) {
			dataA = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataAndata());
		}

		if (!(travel.getDataRitorno()==null)) {
			dataR = new SimpleDateFormat("d/M/y", Locale.ITALIAN).format(travel.getDataRitorno());
		}
		
		if (travel.getTipologiaViaggio()==null) {
			travel.setTipologiaViaggio("");
		}

		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATA_ANDATA, dataA);
		values.put(T_DATA_RITORNO, dataR);
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());
		
		Log.d("DB VIAGGIO UPDATE", travel.toString());

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
}
