package it.unisa.mytraveldiary.db;

import it.unisa.mytraveldiary.entity.HotelRistorante;
import it.unisa.mytraveldiary.entity.Localita;
import it.unisa.mytraveldiary.entity.Museo;
import it.unisa.mytraveldiary.entity.Trasporto;
import it.unisa.mytraveldiary.entity.Travel;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION=19;
	private static final String DATABASE_NAME="mytraveldiary_db";

	private static final String TABLE_TRAVELS="viaggi";
	private static final String T_TIPOLOGIA="tipologia";
	private static final String T_DATA_ANDATA="data_andata";
	private static final String T_DATA_RITORNO="data_ritorno";
	private static final String T_COMPAGNI_VIAGGIO="compagni_viaggio";
	private static final String T_DESCRIZIONE="descrizione";
	private static final String T_ID="id";
	private static final String T_U_ID="u_id";

	private static final String TABLE_HOTELRISTORANTI="hotel_ristoranti";
	private static final String HR_TIPOLOGIA="tipologia";
	private static final String HR_NOME="nome";
	private static final String HR_CITTA="citta";
	private static final String HR_VALUTAZIONE="valutazione";
	private static final String HR_ID="id";
	private static final String HR_T_ID="viaggio_id";

	private static final String TABLE_MUSEO="museo";
	private static final String M_TIPOLOGIA= "tipologia";
	private static final String M_NOME="nome";
	private static final String M_CITTA="citta";
	private static final String M_VALUTAZIONE="valutazione";
	private static final String M_ID="id";
	private static final String M_T_ID="viaggio_id";

	private static final String TABLE_TRASPORTO="trasporto";
	private static final String TR_TIPOLOGIA= "tipologia";
	private static final String TR_COMPAGNIA="compagnia";
	private static final String TR_CITTAPARTENZA="citta_partenza";
	private static final String TR_CITTAARRIVO="citta_arrivo";
	private static final String TR_VALUTAZIONE="valutazione";
	private static final String TR_ID="id";
	private static final String TR_T_ID="viaggio_id";

	private static final String TABLE_LOCALITA="localita";
	private static final String L_T_ID="viaggio_id";
	private static final String L_NOME="nome";
	private static final String L_ID="id";

	private static final String TABLE_FOTO="foto";
	private static final String F_FILE="file";
	private static final String F_T_ID="viaggio_id";
	private static final String F_ID="id";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TRAVELS_TABLE="CREATE TABLE "+TABLE_TRAVELS +" (" +
				T_TIPOLOGIA +" VARCHAR(10),"+
				T_DATA_ANDATA + " DATE,"+
				T_DATA_RITORNO + " DATE,"+
				T_COMPAGNI_VIAGGIO + " VARCHAR(100),"+
				T_DESCRIZIONE + " TEXT,"+
				T_U_ID+" INTEGER,"+
				T_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_TRAVELS_TABLE);

		String CREATE_HOTELRISTORANTI_TABLE="CREATE TABLE "+TABLE_HOTELRISTORANTI+" (" +
				HR_TIPOLOGIA+" VARCHAR(25)," +
				HR_NOME+" VARCHAR(25)," +
				HR_CITTA+" VARCHAR(50)," +
				HR_VALUTAZIONE+" INTEGER," +
				HR_T_ID+" INTEGER,"+
				HR_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"FOREIGN KEY("+HR_T_ID+") REFERENCES "+TABLE_TRAVELS+"("+T_ID+"))";
		db.execSQL(CREATE_HOTELRISTORANTI_TABLE);

		String CREATE_MUSEO_TABLE="CREATE TABLE "+TABLE_MUSEO +" (" +
				M_TIPOLOGIA + " VARCHAR(20)," +
				M_NOME + " VARCHAR(50)," +
				M_CITTA + " VARCHAR(50)," +
				M_VALUTAZIONE + " INTEGER," +
				M_T_ID+" INTEGER,"+
				M_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"FOREIGN KEY("+M_T_ID+") REFERENCES "+TABLE_TRAVELS+"("+T_ID+"))";
		db.execSQL(CREATE_MUSEO_TABLE);

		String CREATE_TRASPORTO_TABLE="CREATE TABLE "+TABLE_TRASPORTO +" (" +
				TR_TIPOLOGIA + " VARCHAR(20)," +
				TR_COMPAGNIA + " VARCHAR(50)," +
				TR_CITTAPARTENZA + " VARCHAR(50)," +
				TR_CITTAARRIVO + " VARCHAR(50)," +
				TR_VALUTAZIONE + " INTEGER," +
				TR_T_ID+" INTEGER,"+
				TR_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"FOREIGN KEY("+TR_T_ID+") REFERENCES "+TABLE_TRAVELS+"("+T_ID+"))";
		db.execSQL(CREATE_TRASPORTO_TABLE);

		String CREATE_LOCALITA_TABLE="CREATE TABLE "+TABLE_LOCALITA+" ("+
				L_NOME+" VARCHAR(30),"+
				L_T_ID+" INTEGER,"+
				L_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"FOREIGN KEY("+L_T_ID+") REFERENCES "+TABLE_TRAVELS+"("+T_ID+"))";
		db.execSQL(CREATE_LOCALITA_TABLE);

		String CREATE_FOTO_TABLE="CREATE TABLE "+TABLE_FOTO+" ("+
				F_FILE+" VARCHAR(50),"+
				F_T_ID+" INTEGER,"+
				F_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"FOREIGN KEY("+F_T_ID+") REFERENCES "+TABLE_TRAVELS+"("+T_ID+"))";
		db.execSQL(CREATE_FOTO_TABLE);

		Log.d("Creating...", "Database");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRAVELS);

		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_HOTELRISTORANTI);

		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_MUSEO);

		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_TRASPORTO);

		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_LOCALITA);

		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_FOTO);

		onCreate(db);
	}

	/* VIAGGIO */

	public int addTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_DATA_ANDATA, travel.getDataAndata());
		values.put(T_DATA_RITORNO, travel.getDataRitorno());
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());
		values.put(T_U_ID, travel.getUId());

		Log.d("DB VIAGGIO", travel.toString());

		int id=(int) db.insert(TABLE_TRAVELS, null, values);

		ArrayList<Localita> localita=travel.getLocalita();

		if (localita!=null) {
			values=new ContentValues();

			for (Localita l: localita) {
				if (localita!=null) {
					values.put(L_NOME, l.getNome());
					values.put(L_T_ID, id);

					db.insert(TABLE_LOCALITA, null, values);
				}
			}
		}

		db.close();

		return id;
	}


	// Aggiorna un viaggio

	public int updateTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(T_TIPOLOGIA, travel.getTipologiaViaggio());
		values.put(T_DATA_ANDATA, travel.getDataAndata());
		values.put(T_DATA_RITORNO, travel.getDataRitorno());
		values.put(T_COMPAGNI_VIAGGIO, travel.getCompagniViaggio());
		values.put(T_DESCRIZIONE, travel.getDescrizione());
		values.put(T_U_ID, travel.getUId());

		Log.d("DB VIAGGIO UPDATE", travel.toString());

		int rows=db.update(TABLE_TRAVELS, values, T_ID + "= ?", new String[] {String.valueOf(travel.getId())});

		ArrayList<Localita> localita=travel.getLocalita();

		if (localita!=null) {
			values=new ContentValues();

			for (Localita l: localita) {
				if (localita!=null) {
					values.put(L_NOME, l.getNome());
					values.put(L_T_ID, travel.getId());

					db.insert(TABLE_LOCALITA, null, values);
				}
			}
		}
		
		db.close();

		return rows;
	}


	// Cancella un viaggio 

	public void deleteTravel(Travel travel) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_TRAVELS, T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.delete(TABLE_HOTELRISTORANTI, HR_T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.delete(TABLE_LOCALITA, L_T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.delete(TABLE_MUSEO, M_T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.delete(TABLE_TRASPORTO, TR_T_ID + "= ?", new String[] {String.valueOf(travel.getId())});
		db.close();
	}


	// Metodi di accesso
	// Ritorna un viaggio

	public Travel getTravel(int id) {
		SQLiteDatabase db=this.getWritableDatabase();

		Cursor cursor=db.query(TABLE_TRAVELS, new String[] {T_TIPOLOGIA, T_DATA_ANDATA, 
				T_DATA_RITORNO, T_COMPAGNI_VIAGGIO, T_DESCRIZIONE, T_U_ID, T_ID}, T_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		String tipologia=cursor.getString(0);
		String dataAndata=cursor.getString(1);
		String dataRitorno=cursor.getString(2);
		String compagniViaggio=cursor.getString(3);
		String descrizione=cursor.getString(4);
		String u_id=cursor.getString(5);
		String idTravel=cursor.getString(6);

		cursor=db.query(TABLE_LOCALITA, new String[] {L_NOME}, L_T_ID+"=?", new String[] {String.valueOf(id)},
				null, null, null);

		ArrayList<Localita> localita=new ArrayList<Localita>();

		if (cursor.moveToFirst()) {
			do {
				localita.add(new Localita(cursor.getString(0), Integer.parseInt(idTravel)));
				Log.d("getTravel", cursor.getString(0));
			} while (cursor.moveToNext());
		}

		Travel travel= new Travel(tipologia, localita, dataAndata, dataRitorno, compagniViaggio, descrizione, 
				Integer.parseInt(u_id), Integer.parseInt(idTravel));


		db.close();

		return travel;
	}

	// Ritorna tutti i viaggi

	public ArrayList<Travel> getAllTravels(int user) {
		ArrayList<Travel> travelList=new ArrayList<Travel>();
		String selectQuery="SELECT * FROM " + TABLE_TRAVELS +" WHERE "+T_U_ID+"="+user;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				String tipologia=cursor.getString(0);
				String dataAndata=cursor.getString(1);
				String dataRitorno=cursor.getString(2);
				String compagniViaggio=cursor.getString(3);
				String descrizione=cursor.getString(4);
				String u_id=cursor.getString(5);
				String idTravel=cursor.getString(6);

				Cursor cursorLoc=db.query(TABLE_LOCALITA, new String[] {L_NOME}, L_T_ID+"=?", 
						new String[] {String.valueOf(idTravel)}, null, null, null);

				ArrayList<Localita> localita=new ArrayList<Localita>();

				if (cursorLoc.moveToFirst()) {
					do {
						localita.add(new Localita(cursorLoc.getString(0), Integer.parseInt(idTravel)));
						Log.d("getTravel", cursorLoc.getString(0));
					} while (cursorLoc.moveToNext());
				}

				Travel travel= new Travel(tipologia, localita, dataAndata, dataRitorno, compagniViaggio, descrizione, 
						Integer.parseInt(u_id), Integer.parseInt(idTravel));

				travelList.add(travel);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return travelList;
	}

	public ArrayList<Travel> doMySearch(String query) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor;

		if (query.equalsIgnoreCase("svago") || query.equalsIgnoreCase("lavoro")) 
			cursor=db.query(TABLE_TRAVELS, null, T_TIPOLOGIA+" LIKE ?", 
					new String[] {"%"+query+"%"}, null, null, null);

		else {
			String sql="SELECT "+T_TIPOLOGIA+", "+T_DATA_ANDATA+", "+T_DATA_RITORNO+", "+T_COMPAGNI_VIAGGIO+
					", "+T_DESCRIZIONE+", "+T_U_ID+","+TABLE_TRAVELS+"."+T_ID+
					" FROM "+TABLE_TRAVELS+", "+TABLE_LOCALITA+
					" WHERE "+L_NOME+" LIKE '%"+query+"%'"+" AND "+TABLE_TRAVELS+"."+T_ID+"="+TABLE_LOCALITA+"."+L_T_ID;
			cursor=db.rawQuery(sql, null);
		}

		ArrayList<Travel> travelList=new ArrayList<Travel>();

		if (cursor.moveToFirst()) {
			do {
				String tipologia=cursor.getString(0);
				String dataAndata=cursor.getString(1);
				String dataRitorno=cursor.getString(2);
				String compagniViaggio=cursor.getString(3);
				String descrizione=cursor.getString(4);
				String u_id=cursor.getString(5);
				String idTravel=cursor.getString(6);

				Cursor cursorLoc=db.query(TABLE_LOCALITA, new String[] {L_NOME}, L_T_ID+"=?", 
						new String[] {String.valueOf(idTravel)}, null, null, null);

				ArrayList<Localita> localita=new ArrayList<Localita>();

				if (cursorLoc.moveToFirst()) {
					do {
						Localita l=new Localita(cursorLoc.getString(0), Integer.parseInt(idTravel));
						localita.add(l);
					} while (cursorLoc.moveToNext());
				}

				Travel travel= new Travel(tipologia, localita, dataAndata, dataRitorno, compagniViaggio, descrizione, 
						Integer.parseInt(u_id), Integer.parseInt(idTravel));

				travelList.add(travel);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return travelList;
	}
	
	public ArrayList<Integer> getMedia(int t_id) {
		ArrayList<Integer> media=new ArrayList<Integer>();
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor;
		String QUERY="SELECT "+TABLE_HOTELRISTORANTI+"."+HR_VALUTAZIONE+", "+
							   TABLE_MUSEO+"."+M_VALUTAZIONE+", "+
							   TABLE_TRASPORTO+"."+TR_VALUTAZIONE+
						" FROM "+TABLE_HOTELRISTORANTI+", "+TABLE_MUSEO+", "+TABLE_TRASPORTO+
						" WHERE "+TABLE_HOTELRISTORANTI+"."+HR_T_ID+"="+t_id+" AND "+
								  TABLE_MUSEO+"."+M_T_ID+"="+t_id+" AND "+
								  TABLE_TRASPORTO+"."+TR_T_ID+"="+t_id;
		
		cursor=db.rawQuery(QUERY, null);
		
		if (cursor.moveToFirst()) {
			do {
				media.add(cursor.getInt(0));
				media.add(cursor.getInt(1));
				media.add(cursor.getInt(2));
			} while (cursor.moveToNext());
		}
		
		return media;
	}

	/* TRASPORTI */

	public int addTrasporto(Trasporto trasporto) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(TR_TIPOLOGIA, trasporto.getTipologia());
		values.put(TR_COMPAGNIA, trasporto.getCompagnia());
		values.put(TR_CITTAPARTENZA, (trasporto.getLocalitaPartenza()).toString());
		values.put(TR_CITTAARRIVO, (trasporto.getLocalitaArrivo()).toString());
		values.put(TR_T_ID, trasporto.getTId());
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
		values.put(TR_CITTAPARTENZA, (trasporto.getLocalitaPartenza()).toString());
		values.put(TR_CITTAARRIVO, (trasporto.getLocalitaArrivo()).toString());
		values.put(TR_T_ID, trasporto.getTId());
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

	public Trasporto getTrasporto(int id) {
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_TRASPORTO, new String[] {TR_TIPOLOGIA, TR_COMPAGNIA, TR_CITTAPARTENZA, TR_CITTAARRIVO, 
				TR_VALUTAZIONE, TR_T_ID, TR_ID}, 
				TR_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Trasporto trasporto= new Trasporto(cursor.getString(0), cursor.getString(1), 
				new Localita(cursor.getString(2), Integer.parseInt(cursor.getString(5))), 
				new Localita(cursor.getString(3), Integer.parseInt(cursor.getString(5))), 
				Integer.parseInt(cursor.getString(4)), 
				Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));

		db.close();

		return trasporto;
	}

	public ArrayList<Trasporto> getTrasporti(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_TRASPORTO, null, TR_T_ID+"=?", new String[] {String.valueOf(t_id)}, null, null, null);
		ArrayList<Trasporto> trasportoList=new ArrayList<Trasporto>();

		if (cursor.moveToFirst()) {
			do {
				Trasporto trasporto=new Trasporto();
				trasporto.setTipologia(cursor.getString(0));
				trasporto.setCompagnia(cursor.getString(1));
				trasporto.setLocalitaPartenza(new Localita(cursor.getString(2), t_id));		
				trasporto.setLocalitaArrivo(new Localita(cursor.getString(3), t_id));
				trasporto.setValutazione(Integer.parseInt(cursor.getString(4)));
				trasporto.setTId(Integer.parseInt(cursor.getString(5)));
				trasporto.setId(Integer.parseInt(cursor.getString(6)));
				trasportoList.add(trasporto);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return trasportoList;
	}

	/* HOTEL E RISTORANTE */
	public int addHotelRistorante(HotelRistorante hotelRistorante) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(HR_TIPOLOGIA, hotelRistorante.getTipologia());
		values.put(HR_NOME, hotelRistorante.getNome());
		values.put(HR_CITTA, (hotelRistorante.getLocalita()).toString());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());
		values.put(HR_T_ID, hotelRistorante.getTId());

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
		values.put(HR_CITTA, (hotelRistorante.getLocalita()).toString());
		values.put(HR_VALUTAZIONE, hotelRistorante.getValutazione());
		values.put(HR_T_ID, hotelRistorante.getTId());

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
				HR_NOME, HR_CITTA, HR_VALUTAZIONE, HR_T_ID, HR_ID}, HR_ID+"=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();

		HotelRistorante hotelRistorante=new HotelRistorante(cursor.getString(0), 
				cursor.getString(1), new Localita(cursor.getString(2), Integer.parseInt(cursor.getString(4))), 
				Integer.parseInt(cursor.getString(3)), 
				Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

		db.close();

		return hotelRistorante;
	}

	public ArrayList<HotelRistorante> getHotel(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_HOTELRISTORANTI, null, HR_T_ID+"=? AND "+HR_TIPOLOGIA+"=?", 
				new String[] {String.valueOf(t_id), "Hotel"}, null, null, null);
		ArrayList<HotelRistorante> hotelRistorantiList=new ArrayList<HotelRistorante>();

		if (cursor.moveToFirst()) {
			do {
				HotelRistorante hotelRistorante=new HotelRistorante();
				hotelRistorante.setTipologia(cursor.getString(0));
				hotelRistorante.setNome(cursor.getString(1));
				hotelRistorante.setLocalita(new Localita(cursor.getString(2), t_id));		
				hotelRistorante.setValutazione(Integer.parseInt(cursor.getString(3)));
				hotelRistorante.setTId(Integer.parseInt(cursor.getString(4)));
				hotelRistorante.setId(Integer.parseInt(cursor.getString(5)));
				hotelRistorantiList.add(hotelRistorante);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return hotelRistorantiList;
	}
	
	public ArrayList<HotelRistorante> getRistoranti(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_HOTELRISTORANTI, null, HR_T_ID+"=? AND "+HR_TIPOLOGIA+"=?", 
				new String[] {String.valueOf(t_id), "Ristorante"}, null, null, null);
		ArrayList<HotelRistorante> hotelRistorantiList=new ArrayList<HotelRistorante>();

		if (cursor.moveToFirst()) {
			do {
				HotelRistorante hotelRistorante=new HotelRistorante();
				hotelRistorante.setTipologia(cursor.getString(0));
				hotelRistorante.setNome(cursor.getString(1));
				hotelRistorante.setLocalita(new Localita(cursor.getString(2), t_id));		
				hotelRistorante.setValutazione(Integer.parseInt(cursor.getString(3)));
				hotelRistorante.setTId(Integer.parseInt(cursor.getString(4)));
				hotelRistorante.setId(Integer.parseInt(cursor.getString(5)));
				hotelRistorantiList.add(hotelRistorante);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return hotelRistorantiList;
	}
	
	/* MUSEO */

	public int addMuseo(Museo museo) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(M_TIPOLOGIA, museo.getTipologia());
		values.put(M_NOME, museo.getNome());
		values.put(M_CITTA, (museo.getLocalita()).toString());
		values.put(M_T_ID, museo.getTId());
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
		values.put(M_CITTA, (museo.getLocalita()).toString());
		values.put(M_T_ID, museo.getTId());
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

	public Museo getMuseo(int id) {
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_MUSEO, new String[] {M_TIPOLOGIA, M_NOME, M_CITTA, M_VALUTAZIONE, M_T_ID, M_ID}, 
				M_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Museo museo= new Museo(cursor.getString(0), cursor.getString(1), 
				new Localita(cursor.getString(2), Integer.parseInt(cursor.getString(4))), 
				Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
				Integer.parseInt(cursor.getString(5)));

		db.close();

		return museo;
	}

	public ArrayList<Museo> getMusei(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_MUSEO, null, M_T_ID+"=?", new String[] {String.valueOf(t_id)}, null, null, null);
		ArrayList<Museo> museiList=new ArrayList<Museo>();

		if (cursor.moveToFirst()) {
			do {
				Museo museo=new Museo();
				museo.setTipologia(cursor.getString(0));
				museo.setNome(cursor.getString(1));
				museo.setLocalita(new Localita(cursor.getString(2), t_id));		
				museo.setValutazione(Integer.parseInt(cursor.getString(3)));
				museo.setTId(Integer.parseInt(cursor.getString(4)));
				museo.setId(Integer.parseInt(cursor.getString(5)));
				museiList.add(museo);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return museiList;
	}

	// LOCALITA

	public int addLocalita(Localita localita) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(L_NOME, localita.getNome());
		values.put(L_T_ID, localita.getTId());

		int id = (int) db.insert(TABLE_LOCALITA, null, values);

		db.close();

		return id;
	}

	public int updateLocalita(Localita localita) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(L_NOME, localita.getNome());
		values.put(L_T_ID, localita.getTId());

		return db.update(TABLE_LOCALITA, values, L_ID + "= ?", 
				new String[] {String.valueOf(localita.getId())});
	}

	public void deleteLocalita(Localita localita) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_LOCALITA, L_ID + "= ?", new String[] {String.valueOf(localita.getId())});
		db.close();
	}

	public Localita getLocalita(int id) {
		SQLiteDatabase db=this.getReadableDatabase();

		Cursor cursor=db.query(TABLE_LOCALITA, new String[] {L_NOME, L_T_ID, L_ID}, 
				L_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);

		if (cursor!=null)
			cursor.moveToFirst();


		Localita localita= new Localita(cursor.getString(0), Integer.parseInt(cursor.getString(1)), 
				Integer.parseInt(cursor.getString(2)));

		db.close();

		return localita;
	}

	public ArrayList<Localita> getLocalitas(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_LOCALITA, null, L_T_ID+"=?", new String[] {String.valueOf(t_id)}, null, null, null);
		ArrayList<Localita> localitaList=new ArrayList<Localita>();

		if (cursor.moveToFirst()) {
			do {
				Localita localita=new Localita();
				localita.setNome(cursor.getString(0));
				localita.setTId(t_id);
				localita.setId(Integer.parseInt(cursor.getString(2)));
				localitaList.add(localita);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return localitaList;
	}

	public void deleteLocalitas(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_LOCALITA, L_T_ID + "= ?", new String[] {String.valueOf(t_id)});
		db.close();
	}

	// FOTO

	public int addFoto(String file, int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put(F_FILE, file);
		values.put(F_T_ID, t_id);

		int id = (int) db.insert(TABLE_FOTO, null, values);

		db.close();

		return id;
	}

	public void deleteFoto(String file) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_FOTO, F_FILE + "= ?", new String[] {file});
		db.close();
	}

	public ArrayList<String> getFoto(int t_id) {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_FOTO, null, F_T_ID+"=?", new String[] {String.valueOf(t_id)}, null, null, null);
		ArrayList<String> fotoList=new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				String foto="";
				foto=cursor.getString(0);
				fotoList.add(foto);
			} 
			while (cursor.moveToNext());
		}

		db.close();

		return fotoList;
	}
}
