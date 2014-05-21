package it.unisa.mytraveldiary.db;

import java.text.ParseException;
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

	private static final int DATABASE_VERSION=7;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_TRAVELS="travels";
	private static final String T_ID="id";

	public DatabaseHandlerTravel(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRAVELS_TABLE="CREATE TABLE "+TABLE_TRAVELS +" (" +
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
		
		values.put(T_ID, travel.getId());

		db.insert(TABLE_TRAVELS, null, values);
		db.close();
	}


	// Aggiorna un viaggio

	public int updateTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(T_ID, travel.getId());

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


		Travel travel= new Travel(null, null, null, null, null, null, Integer.parseInt(cursor.getString(0)));

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
				Travel travel=new Travel();;
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