package it.unisa.mytraveldiary.db;


import it.unisa.mytraveldiary.entity.Museo;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerMusei extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION=9;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_MUSEO="museo";
	private static final String M_TIPOLOGIA= "tipologia";
	private static final String M_NOME="nome";
	private static final String M_CITTA="città";
	private static final String M_VALUTAZIONE="valutazione";
	private static final String M_ID="id";


	public DatabaseHandlerMusei(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db=getWritableDatabase();
		String CREATE_MUSEO_TABLE="CREATE TABLE "+TABLE_MUSEO +" (" +
				M_TIPOLOGIA + " VARCHAR(20)," +
				M_NOME + " VARCHAR(50)," +
				M_CITTA + " VARCHAR(30)," +
				M_VALUTAZIONE + " INTEGER," +
				M_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_MUSEO_TABLE);
		
		Log.d("Creating...", "Museo");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_MUSEO);

		onCreate(db);
	}


	// Metodi di modifica
	// Aggiunge un Museo

	public int addMuseo(Museo museo) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(M_TIPOLOGIA, museo.getTipologia());
		values.put(M_NOME, museo.getNome());
		values.put(M_CITTA, museo.getCittà());
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
		values.put(M_CITTA, museo.getCittà());
		values.put(M_VALUTAZIONE, museo.getValutazione());
		//values.put(M_ID, museo.getId());

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
				museo.setCittà(cursor.getString(2));		
				museo.setValutazione(Integer.parseInt(cursor.getString(3)));
				museo.setId(Integer.parseInt(cursor.getString(4)));
				museoList.add(museo);
			} 
			while (cursor.moveToNext());
		}
		return museoList;
	}
}
