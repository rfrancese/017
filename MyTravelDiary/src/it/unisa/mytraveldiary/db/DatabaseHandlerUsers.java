package it.unisa.mytraveldiary.db;

import java.util.ArrayList;

import it.unisa.mytraveldiary.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerUsers extends SQLiteOpenHelper {
	
	private static final String CREATE_TABLE="CREATE TABLE ";
	private static final String COMMA=", ";
	private static final String PARENTHESIS_OPEN=" (";
	private static final String PARENTHESIS_CLOSED=") ";
	
	private static final int DATABASE_VERSION=6;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_USERS="users";
	private static final String U_USERNAME="username";
	private static final String U_PASSWORD="password";
	private static final String U_NOME="nome";
	private static final String U_COGNOME="cognome";
	private static final String U_LOCALITA="localita";
	private static final String U_ID="id";
	
	public DatabaseHandlerUsers(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE=CREATE_TABLE+TABLE_USERS+PARENTHESIS_OPEN+
									U_USERNAME+COMMA+
									U_PASSWORD+COMMA+
									U_NOME+COMMA+
									U_COGNOME+COMMA+
									U_LOCALITA+COMMA+PARENTHESIS_CLOSED+
									"VALUES"+PARENTHESIS_OPEN +
									U_USERNAME+" VARCHAR(30) NOT NULL"+COMMA+
									U_PASSWORD+" VARCHAR(30) NOT NULL"+COMMA+
									U_NOME+" VARCHAR(15) NOT NULL"+COMMA+
									U_COGNOME+" VARCHAR(15) NOT NULL"+COMMA+
									U_LOCALITA+" VARCHAR(25) NOT NULL"+COMMA+
									U_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"+
									PARENTHESIS_CLOSED;
		db.execSQL(CREATE_USERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
		
		onCreate(db);
	}

	// Metodi di modifica
	// Aggiunge un utente
	public void addUser(User user) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(U_USERNAME, user.getUsername());
		values.put(U_PASSWORD, user.getPassword());
		values.put(U_NOME, user.getNome());
		values.put(U_COGNOME, user.getCognome());
		values.put(U_LOCALITA, user.getLocalita());
		
		db.insert(TABLE_USERS, null, values);
		db.close();
	}
	
	// Aggiorna un utente
	public int updateUser(User user) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(U_USERNAME, user.getUsername());
		values.put(U_PASSWORD, user.getPassword());
		values.put(U_NOME, user.getNome());
		values.put(U_COGNOME, user.getCognome());
		values.put(U_LOCALITA, user.getLocalita());
		//values.put(U_ID, user.getId());
		
		return db.update(TABLE_USERS, values, U_ID+" = ?", new String[] {String.valueOf(user.getId())});
	}
	
	// Cancella un utente 
	public void deleteUser(User user) {
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_USERS, U_ID+" = ?", new String[] {String.valueOf(user.getId())});
		db.close();
	}
	
	// Metodi di accesso
	// Ritorna un utente
	public User getUser(int id) {
		SQLiteDatabase db=this.getReadableDatabase();
		
		Cursor cursor=db.query(TABLE_USERS, new String[] {U_USERNAME, 
								U_PASSWORD, U_ID}, U_ID+"=?",
								new String[] {String.valueOf(id)}, null, null, null, null);
		
		if (cursor!=null)
			cursor.moveToFirst();
		
		User user=new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
					cursor.getString(4), Integer.parseInt(cursor.getString(5)));
		
		return user;
	}
	
	// Ritorna tutti gli utenti
	public ArrayList<User> getAllUsers() {
		ArrayList<User> userList=new ArrayList<User>();
		String selectQuery="SELECT * FROM "+TABLE_USERS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				User user=new User();
				user.setUsername(cursor.getString(0));
				user.setPassword(cursor.getString(1));
				user.setNome(cursor.getString(2));
				user.setCognome(cursor.getString(3));
				user.setLocalita(cursor.getString(4));
				user.setId(Integer.parseInt(cursor.getString(2)));
				userList.add(user);
			} while (cursor.moveToNext());
		}
		
		return userList;
	}
	
	// Ritorna il numero di utenti
	public int getUsersCount() {
		String countQuery="SELECT * FROM "+TABLE_USERS;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(countQuery, null);
		//cursor.close();
		
		return cursor.getCount();
	}
	
	
}
