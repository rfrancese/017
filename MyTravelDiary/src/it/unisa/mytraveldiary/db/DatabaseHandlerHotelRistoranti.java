package it.unisa.mytraveldiary.db;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.User;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerHotelRistoranti extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=3;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_HOTELRISTORANTI="hotelRistoranti";
	private static final String HR_TIPOLOGIA="tipologia";
	private static final String HR_NOME="nome";
	private static final String HR_CITTA="citta";
	private static final String HR_VALUTAZIONE="valutazione";
	private static final String HR_ID="id";

	public DatabaseHandlerHotelRistoranti(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_HOTELRISORANTI_TABLE="CREATE TABLE "+TABLE_HOTELRISTORANTI+" (" +
				HR_TIPOLOGIA+" VARCHAR(25) NOT NULL," +
				HR_NOME+" VARCHAR(25) NOT NULL," +
				HR_CITTA+" VARCHAR(30) NOT NULL," +
				HR_VALUTAZIONE+" INTEGER NOT NULL," +
				HR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_HOTELRISORANTI_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTs "+TABLE_HOTELRISTORANTI);

		onCreate(db);
	}

	// Metodi di modifica
	// Aggiunge un hotel/ristorante
	public void addHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(HR_TIPOLOGIA, hotelRistorante.getTipologia());
		values.put(HR_NOME, hotelRistorante.getNome());
		values.put(HR_CITTA, hotelRistorante.getCitta());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());
		values.put(HR_ID, hotelRistorante.getId());

		db.insert(TABLE_HOTELRISTORANTI, null, values);
		db.close();
	}

	// Aggiorna un hotel/ristorante
	public int updateHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(HR_TIPOLOGIA, hotelRistorante.getTipologia());
		values.put(HR_NOME, hotelRistorante.getNome());
		values.put(HR_CITTA, hotelRistorante.getCitta());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());
		values.put(HR_ID, hotelRistorante.getId());

		return db.update(TABLE_HOTELRISTORANTI, values, HR_TIPOLOGIA+" = ?", new String[] {String.valueOf(hotelRistorante.getId())});
	}

	// Cancella un hotel/ristorante 
	public void deleteUser(HotelRistorante hotelRistorante) {
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

	// Ritorna tutti gli utenti
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
}
