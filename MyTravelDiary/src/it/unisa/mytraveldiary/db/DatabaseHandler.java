package it.unisa.mytraveldiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="mytraveldiary_db";
	private static final String TABLE_USERS="users";
	private static final String U_USERNAME="username";
	private static final String U_PASSWORD="password";
	private static final String U_ID="id";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE="CREATE TABLE "+TABLE_USERS+" (" +
									U_USERNAME+" VARCHAR(25) NOT NULL," +
									U_PASSWORD+" VARCHAR(25) NOT NULL," +
									U_ID+" INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL)";
		db.execSQL(CREATE_USERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXIST "+TABLE_USERS);
		
		onCreate(db);
	}

	
}
