package it.unisa.mytraveldiary.db;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION=14;
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
	
	private static final String TABLE_MUSEO="museo";
	private static final String M_TIPOLOGIA= "tipologia";
	private static final String M_NOME="nome";
	private static final String M_CITTA="citt‡";
	private static final String M_VALUTAZIONE="valutazione";
	private static final String M_ID="id";
	
	private static final String TABLE_TRASPORTO="trasporto";
	private static final String TR_TIPOLOGIA= "tipologia";
	private static final String TR_COMPAGNIA="compagnia";
	private static final String TR_CITTAPARTENZA="citt‡Partenza";
	private static final String TR_CITTAARRIVO="citt‡Arrivo";
	private static final String TR_VALUTAZIONE="valutazione";
	private static final String TR_ID="id";
	
	

	public DatabaseHandler(Context context) {
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
		
		String CREATE_MUSEO_TABLE="CREATE TABLE "+TABLE_MUSEO +" (" +
				M_TIPOLOGIA + " VARCHAR(20)," +
				M_NOME + " VARCHAR(50)," +
				M_CITTA + " VARCHAR(30)," +
				M_VALUTAZIONE + " INTEGER," +
				M_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_MUSEO_TABLE);
		
		String CREATE_TRASPORTO_TABLE="CREATE TABLE "+TABLE_TRASPORTO +" (" +
				TR_TIPOLOGIA + " VARCHAR(20)," +
				TR_COMPAGNIA + " VARCHAR(50)," +
				TR_CITTAPARTENZA + " VARCHAR(30)," +
				TR_CITTAARRIVO + " VARCHAR(30)," +
				TR_VALUTAZIONE + " INTEGER," +
				TR_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_TRASPORTO_TABLE);

		Log.d("Creating...", "Database");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRAVELS);
		
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_HOTELRISTORANTI);
		
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_MUSEO);
		
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRASPORTO);
		
		onCreate(db);
	}
	
	/* VIAGGIO */
	
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
		values.put(T_LOCALITA, travel.getLocalit‡());
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
		values.put(T_LOCALITA, travel.getLocalit‡());
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

	/* TRASPORTI */
	
	public int addTrasporto(Trasporto trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(TR_TIPOLOGIA, trasporto.getTipologia());
		values.put(TR_COMPAGNIA, trasporto.getCompagnia());
		values.put(TR_CITTAPARTENZA, trasporto.getCitt‡Partenza());
		values.put(TR_CITTAARRIVO, trasporto.getCitt‡Arrivo());
		values.put(TR_VALUTAZIONE, trasporto.getValutazione());		
		
		int id = (int) db.insert(TABLE_TRASPORTO, null, values);
		db.close();
		
		return id;
	}


	// Aggiorna un viaggio

	public int updateTrasporto(Trasporto trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(TR_TIPOLOGIA, trasporto.getTipologia());
		values.put(TR_COMPAGNIA, trasporto.getCompagnia());
		values.put(TR_CITTAPARTENZA, trasporto.getCitt‡Partenza());
		values.put(TR_CITTAARRIVO, trasporto.getCitt‡Arrivo());
		values.put(TR_VALUTAZIONE, trasporto.getValutazione());

		return db.update(TABLE_TRASPORTO, values, TR_ID + "= ?", new String[] {String.valueOf(trasporto.getId())});
	}


	// Cancella un trasporto

	public void deleteTrasporti(Trasporto trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_TRASPORTO, TR_ID + "= ?", new String[] {String.valueOf(trasporto.getId())});
		db.close();
	}


	// Metodi di accesso
	// Ritorna un trasporto

	public Trasporto getTrasporto(int id) throws NumberFormatException{
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_TRASPORTO, new String[] {TR_TIPOLOGIA, TR_COMPAGNIA, TR_CITTAPARTENZA, TR_CITTAARRIVO, 
				TR_VALUTAZIONE, TR_ID}, 
				TR_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Trasporto trasporto= new Trasporto(cursor.getString(0), cursor.getString(1), cursor.getString(2), 
				cursor.getString(3), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));

		return trasporto;
	}

	// Ritorna tutti i trasporti
	
	public ArrayList<Trasporto> getAllTrasporti() {
		ArrayList<Trasporto> trasportoList=new ArrayList<Trasporto>();
		String selectQuery="SELECT * FROM " + TABLE_TRASPORTO;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Trasporto trasporto=new Trasporto();
				trasporto.setTipologia(cursor.getString(0));
				trasporto.setCompagnia(cursor.getString(1));
				trasporto.setCitt‡Partenza(cursor.getString(2));		
				trasporto.setCitt‡Arrivo(cursor.getString(3));
				trasporto.setValutazione(Integer.parseInt(cursor.getString(4)));
				trasporto.setId(Integer.parseInt(cursor.getString(5)));
				trasportoList.add(trasporto);
			} 
			while (cursor.moveToNext());
		}
		return trasportoList;
	}

	/* HOTEL E RISTORANTE */
	public int addHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(HR_TIPOLOGIA, hotelRistorante.getTipologia());
		values.put(HR_NOME, hotelRistorante.getNome());
		values.put(HR_CITTA, hotelRistorante.getCitta());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());
		
		Log.d("DB HR", hotelRistorante.toString());

		int id=(int) db.insert(TABLE_HOTELRISTORANTI, null, values);
		db.close();
		
		return id;
	}

	// Aggiorna un hotel/ristorante
	public int updateHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(HR_TIPOLOGIA, hotelRistorante.getTipologia());
		values.put(HR_NOME, hotelRistorante.getNome());
		values.put(HR_CITTA, hotelRistorante.getCitta());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());

		return db.update(TABLE_HOTELRISTORANTI, values, HR_ID+" = ?", new String[] {String.valueOf(hotelRistorante.getId())});
	}

	// Cancella un hotel/ristorante 
	public void deleteHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_HOTELRISTORANTI, HR_ID+" = ?", new String[] {String.valueOf(hotelRistorante.getId())});
		db.close();
	}

	// Metodi di accesso
	// Ritorna un hotel/risotrante
	public HotelRistorante getHotelRistorante(int id) {
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_HOTELRISTORANTI, new String[] {HR_TIPOLOGIA, 
				HR_NOME, HR_CITTA, HR_VALUTAZIONE, HR_ID}, HR_ID+"=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();

		HotelRistorante hotelRistorante=new HotelRistorante(cursor.getString(0), 
				cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(2)), 
				Integer.parseInt(cursor.getString(3)));

		return hotelRistorante;
	}

	// Ritorna tutti gli hotel e ristoranti
	public ArrayList<HotelRistorante> getAllHotelRistoranti() {
		ArrayList<HotelRistorante> hotelRistoranteList=new ArrayList<HotelRistorante>();
		String selectQuery="SELECT * FROM "+TABLE_HOTELRISTORANTI;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				HotelRistorante hotelRistorante=new HotelRistorante();
				hotelRistorante.setTipologia(cursor.getString(0));
				hotelRistorante.setNome(cursor.getString(1));
				hotelRistorante.setCitta(cursor.getString(2));
				hotelRistorante.setValutazione(Integer.parseInt(cursor.getString(3)));
				hotelRistorante.setId(Integer.parseInt(cursor.getString(4)));
				hotelRistoranteList.add(hotelRistorante);
			} while (cursor.moveToNext());
		}

		return hotelRistoranteList;
	}

	/* MUSEO */
	
	public int addMuseo(Museo museo) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(M_TIPOLOGIA, museo.getTipologia());
		values.put(M_NOME, museo.getNome());
		values.put(M_CITTA, museo.getCitt‡());
		values.put(M_VALUTAZIONE, museo.getValutazione());
				
		int id = (int) db.insert(TABLE_MUSEO, null, values);
		
		db.close();
		
		return id;
	}


	// Aggiorna un museo

	public int updateMuseo(Museo museo) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(M_TIPOLOGIA, museo.getTipologia());
		values.put(M_NOME, museo.getNome());
		values.put(M_CITTA, museo.getCitt‡());
		values.put(M_VALUTAZIONE, museo.getValutazione());

		return db.update(TABLE_MUSEO, values, M_ID + "= ?", new String[] {String.valueOf(museo.getId())});
	}


	// Cancella un museo

	public void deleteMuseo(Museo museo) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_MUSEO, M_ID + "= ?", new String[] {String.valueOf(museo.getId())});
		db.close();
	}


	// Metodi di accesso
	// Ritorna un museo

	public Museo getMuseo(int id) throws NumberFormatException{
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_MUSEO, new String[] {M_TIPOLOGIA, M_NOME, M_CITTA, M_VALUTAZIONE, M_ID}, 
				M_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Museo museo= new Museo(cursor.getString(0), cursor.getString(1), cursor.getString(2), 
				                Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));

		return museo;
	}

	// Ritorna tutti i musei
	
	public ArrayList<Museo> getAllMusei() {
		ArrayList<Museo> museoList=new ArrayList<Museo>();
		String selectQuery="SELECT * FROM " + TABLE_MUSEO;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Museo museo=new Museo();
				museo.setTipologia(cursor.getString(0));
				museo.setNome(cursor.getString(1));
				museo.setCitt‡(cursor.getString(2));		
				museo.setValutazione(Integer.parseInt(cursor.getString(3)));
				museo.setId(Integer.parseInt(cursor.getString(4)));
				museoList.add(museo);
			} 
			while (cursor.moveToNext());
		}
		return museoList;
	}

}
