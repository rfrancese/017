package it.unisa.mytraveldiary.db;

import java.util.ArrayList;

import it.unisa.mytraveldiary.entity.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerUsers extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_USERS="users";
	private static final String U_USERNAME="username";
	private static final String U_PASSWORD="password";
	private static final String U_ID="id";
	
	public DatabaseHandlerUsers(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE="CREATE TABLE "+TABLE_USERS+" (" +
									U_USERNAME+" VARCHAR(25) NOT NULL," +
									U_PASSWORD+" VARCHAR(25) NOT NULL," +
									U_ID+" INTEGER PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(CREATE_USERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXIST "+TABLE_USERS);
		
		onCreate(db);
	}

	// Metodi di modifica
	// Aggiunge un utente
	public void addUser(User user) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(U_USERNAME, user.getUsername());
		values.put(U_PASSWORD, user.getPassword());
		
		db.insert(TABLE_USERS, null, values);
		db.close();
	}
	
	// Aggiorna un utente
	public int updateUser(User user) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(U_USERNAME, user.getUsername());
		values.put(U_PASSWORD, user.getPassword());
		values.put(U_ID, user.getId());
		
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
		
		User user=new User(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
		
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
		cursor.close();
		
		return cursor.getCount();
	}
	
	
}
