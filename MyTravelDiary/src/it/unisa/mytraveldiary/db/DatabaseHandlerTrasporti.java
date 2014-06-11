package it.unisa.mytraveldiary.db;

import it.unisa.mytraveldiary.entity.Trasporto;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTrasporti extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=9;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRASPORTO="trasporto";
	private static final String TR_TIPOLOGIA= "tipologia";
	private static final String TR_COMPAGNIA="compagnia";
	private static final String TR_CITTAPARTENZA="citt‡Partenza";
	private static final String TR_CITTAARRIVO="citt‡Arrivo";
	private static final String TR_VALUTAZIONE="valutazione";
	private static final String TR_ID="id";


	public DatabaseHandlerTrasporti(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRASPORTO_TABLE="CREATE TABLE "+TABLE_TRASPORTO +" (" +
				TR_TIPOLOGIA + " VARCHAR(20)," +
				TR_COMPAGNIA + " VARCHAR(50)," +
				TR_CITTAPARTENZA + " VARCHAR(30)," +
				TR_CITTAARRIVO + " VARCHAR(30)," +
				TR_VALUTAZIONE + " INTEGER," +
				TR_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_TRASPORTO_TABLE);
		
		Log.d("Creating...", "Trasporto");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRASPORTO);

		onCreate(db);
	}


	// Metodi di modifica
	// Aggiunge un trasporto

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
		//values.put(TR_ID, trasporto.getId());

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
}
