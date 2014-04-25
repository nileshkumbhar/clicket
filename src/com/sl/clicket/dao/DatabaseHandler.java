package com.sl.clicket.dao;

import java.util.ArrayList;
import java.util.List;

import com.sl.clicket.entity.HighScore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper  {

	// All Static variables
		// Database Version
		private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "contactsManager";

		// high_scores table name
		private static final String TABLE_HIGH_SCORES = "high_scores";
		
		// high_scores Table Columns names
		private static final String KEY_ID = "id";
		private static final String LEVEL = "level";
		private static final String SCORE = "score";
		
		
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_HIGH_SCORE_TABLE = "CREATE TABLE " + TABLE_HIGH_SCORES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + LEVEL + " INTEGER,"
				+ SCORE + " INTEGER" + ")";
		db.execSQL(CREATE_HIGH_SCORE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);

				// Create tables again
				onCreate(db);
		
	}
	
	
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void add(HighScore highScore) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LEVEL, highScore.getLevel()); // Contact Name
		values.put(SCORE, highScore.getScore()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_HIGH_SCORES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	HighScore getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_HIGH_SCORES, new String[] { KEY_ID,
				LEVEL, SCORE}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		HighScore highScore = null;
		if (cursor != null){
			cursor.moveToFirst();

			highScore = new HighScore(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
		}
		// return contact
		return highScore;
	}
	
	// Getting single contact
	public HighScore getHighScoreByLevel(int level) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_HIGH_SCORES, new String[] { KEY_ID,
				LEVEL, SCORE}, LEVEL + "=?",
				new String[] { String.valueOf(level) }, null, null, null, null);
		
		HighScore highScore = null;
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();

			highScore = new HighScore(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
		}
		// return contact
		return highScore;
	}
	
	// Getting All Contacts
	public List<HighScore> getAllHighScores() {
		List<HighScore> hignScoreList = new ArrayList<HighScore>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_HIGH_SCORES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HighScore highScore = new HighScore(Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				hignScoreList.add(highScore);
			} while (cursor.moveToNext());
		}

		// return contact list
		return hignScoreList;
	}

	// Updating single contact
	public int updateHighScore(HighScore highScore) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LEVEL, highScore.getLevel());
		values.put(SCORE, highScore.getScore());

		// updating row
		return db.update(TABLE_HIGH_SCORES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(highScore.getId()) });
	}

	/*// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}*/


	// Getting contacts Count
	public int getHighScoreRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_HIGH_SCORES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
