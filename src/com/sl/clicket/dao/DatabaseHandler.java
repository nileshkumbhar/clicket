package com.sl.clicket.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sl.clicket.entity.HighScore;

public class DatabaseHandler extends SQLiteOpenHelper  {

		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "contactsManager";

		private static final String TABLE_HIGH_SCORES = "high_scores";
		
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
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);
				onCreate(db);
	}
	
	
	
	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void add(HighScore highScore) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LEVEL, highScore.getLevel()); 
		values.put(SCORE, highScore.getScore()); 

		db.insert(TABLE_HIGH_SCORES, null, values);
		db.close();
	}

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
		return highScore;
	}
	
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
		return highScore;
	}
	
	public List<HighScore> getAllHighScores() {
		List<HighScore> hignScoreList = new ArrayList<HighScore>();
		String selectQuery = "SELECT  * FROM " + TABLE_HIGH_SCORES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				HighScore highScore = new HighScore(Integer.parseInt(cursor.getString(0)),
						Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
				hignScoreList.add(highScore);
			} while (cursor.moveToNext());
		}

		return hignScoreList;
	}

	public int updateHighScore(HighScore highScore) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LEVEL, highScore.getLevel());
		values.put(SCORE, highScore.getScore());

		return db.update(TABLE_HIGH_SCORES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(highScore.getId()) });
	}

	public int getHighScoreRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_HIGH_SCORES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

}
