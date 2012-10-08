package com.gpacalculator.com;

import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class GPACalculatorContentProvider extends ContentProvider  {
	
	private GPACalculatorDB db;
		
	private String logText = "GPA";

	private static final String AUTHORITY = "com.GpaCalculator.app.contentprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
	// TODO Auto-generated method stub
	return 0;
	}

	@Override
	public String getType(Uri uri) {
	// TODO Auto-generated method stub
	return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	// TODO Auto-generated method stub
	SQLiteDatabase dbconnect = db.getWritableDatabase();
	long dbinsert = 0;
	dbinsert = dbconnect.insert(GPACalculatorDB.TABLE, null, values);
	getContext().getContentResolver().notifyChange(uri, null);
	return null;
	}

	public boolean onCreate() {
	db = new GPACalculatorDB(getContext());
	return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	String[] selectionArgs, String sortOrder) {
	// TODO Auto-generated method stub
	return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
	String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
	}
}
