package it.unisa.mytraveldiary.db;

import it.unisa.mytraveldiary.entity.Trasporti;
import it.unisa.mytraveldiary.entity.Travel;
import it.unisa.mytraveldiary.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTrasporti extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=4;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRASPORTI="trasporti";
	private static final String TR_TIPOLOGIA= "tipologia";
	private static final String TR_COMPAGNIA="compagnia";
	private static final String TR_CITTAPARTENZA="citt‡Partenza";
	private static final String TR_CITTARITORNO="citt‡Ritorno";
	private static final String TR_VALUTAZIONE="valutazione";
	private static final String TR_ID="id";


	public DatabaseHandlerTrasporti(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRASPORTI_TABLE="CREATE TABLE "+TABLE_TRASPORTI +" (" +
				TR_TIPOLOGIA + " VARCHAR(20) NOT NULL," +
				TR_COMPAGNIA + " VARCHAR(50) NOT NULL," +
				TR_CITTAPARTENZA + " VARCHAR(30) NOT NULL," +
				TR_CITTARITORNO + " VARCHAR(30) NOT NULL," +
				TR_VALUTAZIONE + " INTEGER NOT NULL," +
				TR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_TRASPORTI_TABLE);
		
		Log.d("Creating...", "Trasporti");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRASPORTI);

		onCreate(db);
	}


	// Metodi di modifica
	// Aggiunge un trasporto

	public void addTrasporto(Trasporti trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(TR_TIPOLOGIA, trasporto.getTipologia());
		values.put(TR_COMPAGNIA, trasporto.getCompagnia());
		values.put(TR_CITTAPARTENZA, trasporto.getCitt‡Partenza());
		values.put(TR_CITTARITORNO, trasporto.getCitt‡Ritorno());
		values.put(TR_VALUTAZIONE, trasporto.getValutazione());
		
		db.insert(TABLE_TRASPORTI, null, values);
		db.close();
	}


	// Aggiorna un viaggio

	public int updateTrasporto(Trasporti trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(TR_TIPOLOGIA, trasporto.getTipologia());
		values.put(TR_COMPAGNIA, trasporto.getCompagnia());
		values.put(TR_CITTAPARTENZA, trasporto.getCitt‡Partenza());
		values.put(TR_CITTARITORNO, trasporto.getCitt‡Ritorno());
		values.put(TR_VALUTAZIONE, trasporto.getValutazione());
		values.put(TR_ID, trasporto.getId());

		return db.update(TABLE_TRASPORTI, values, TR_ID + "= ?", new String[] {String.valueOf(trasporto.getId())});
	}


	// Cancella un trasporto

	public void deleteTrasporti(Trasporti trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_TRASPORTI, TR_ID + "= ?", new String[] {String.valueOf(trasporto.getId())});
		db.close();
	}


	// Metodi di accesso
	// Ritorna un trasporto

	public Trasporti getTrasporto(int id) throws NumberFormatException{
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_TRASPORTI, new String[] {TR_TIPOLOGIA, TR_COMPAGNIA, TR_CITTAPARTENZA, TR_CITTARITORNO,
				TR_VALUTAZIONE, TR_ID}, 
				TR_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Trasporti trasporto= new Trasporti(cursor.getString(0), cursor.getString(1), cursor.getString(2), 
				cursor.getString(3), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));

		return trasporto;
	}

	// Ritorna tutti i trasporti
	
	public ArrayList<Trasporti> getAllTrasporti() {
		ArrayList<Trasporti> trasportoList=new ArrayList<Trasporti>();
		String selectQuery="SELECT * FROM " + TABLE_TRASPORTI;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Trasporti trasporto=new Trasporti();
				trasporto.setTipologia(cursor.getString(0));
				trasporto.setCompagnia(cursor.getString(1));
				trasporto.setCitt‡Partenza(cursor.getString(2));		
				trasporto.setCitt‡Ritorno(cursor.getString(3));
				trasporto.setValutazione(Integer.parseInt(cursor.getString(4)));
				trasporto.setId(Integer.parseInt(cursor.getString(5)));
				trasportoList.add(trasporto);
			} 
			while (cursor.moveToNext());
		}
		return trasportoList;
	}
}
