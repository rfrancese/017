package it.unisa.mytraveldiary.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import it.unisa.mytraveldiary.entity.Travel;
import it.unisa.mytraveldiary.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTravel extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=6;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRAVELS="travels";
	private static final String T_TIPOLOGIAVIAGGIO= "tipologiaViaggio";
	private static final String T_LOCALITA="località";
	private static final String T_DATAANDATA="dataAndata";
	private static final String T_DATARITORNO="dataRitorno";
	private static final String T_COMPAGNIVIAGGIO="compagniViaggio";
	private static final String T_DESCRIZIONE="descrizione";
	private static final String T_ID="id";
	private final SimpleDateFormat parser= new SimpleDateFormat("dd-MM-yyyy");

	public DatabaseHandlerTravel(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db=getWritableDatabase();
		String CREATE_TRAVELS_TABLE="CREATE TABLE "+TABLE_TRAVELS +" (" +
				T_TIPOLOGIAVIAGGIO + " VARCHAR(10) NOT NULL," +
				T_LOCALITA + " VARCHAR(50) NOT NULL," +
				T_DATAANDATA + " DATE NOT NULL," +
				T_DATARITORNO + " DATE NOT NULL," +
				T_COMPAGNIVIAGGIO + " VARCHAR(100)," +
				T_DESCRIZIONE + " TEXT NOT NULL," +
				T_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
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

		values.put(T_TIPOLOGIAVIAGGIO, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATAANDATA, parser.format(travel.getDataAndata()));
		values.put(T_DATARITORNO, parser.format(travel.getDataRitorno()));
		values.put(T_COMPAGNIVIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());

		db.insert(TABLE_TRAVELS, null, values);
		db.close();
	}


	// Aggiorna un viaggio

	public int updateTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(T_TIPOLOGIAVIAGGIO, travel.getTipologiaViaggio());
		values.put(T_LOCALITA, travel.getLocalità());
		values.put(T_DATAANDATA, parser.format(travel.getDataAndata()));
		values.put(T_DATARITORNO, parser.format(travel.getDataRitorno()));
		values.put(T_COMPAGNIVIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());
		//values.put(T_ID, travel.getId());

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

		Cursor cursor=db.query(TABLE_TRAVELS, new String[] {T_TIPOLOGIAVIAGGIO, T_LOCALITA, T_DATAANDATA, T_DATARITORNO,
				T_COMPAGNIVIAGGIO, T_DESCRIZIONE, T_ID}, 
				T_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Travel travel= new Travel(cursor.getString(0), cursor.getString(1), parser.parse(cursor.getString(2)), 
				parser.parse(cursor.getString(3)), new ArrayList<User>(), cursor.getString(5), Integer.parseInt(cursor.getString(6)));

		return travel;
	}

	// Ritorna tutti i viaggi

	public ArrayList<Travel> getAllTravels() throws ParseException {
		ArrayList<Travel> travelList=new ArrayList<Travel>();
		String selectQuery="SELECT * FROM " + TABLE_TRAVELS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
		ArrayList<User> compagniViaggio;

		if (cursor.moveToFirst()) {
			do {
				Travel travel=new Travel();
				travel.setTipologiaViaggio(cursor.getString(0));
				travel.setLocalità(cursor.getString(1));

				travel.setDataAndata(parser.parse(cursor.getString(2)));		
				travel.setDataRitorno(parser.parse(cursor.getString(3)));
				compagniViaggio= getcompagniViaggio(cursor.getString(4));
				travel.setCompagniViaggio(compagniViaggio);

				travel.setDescrizione(cursor.getString(5));
				travel.setId(Integer.parseInt(cursor.getString(6)));
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