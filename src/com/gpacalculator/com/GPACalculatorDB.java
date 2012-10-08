package com.gpacalculator.com;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GPACalculatorDB extends SQLiteOpenHelper{
private static final int DB_VERSION = 1;
private static final String DB_NAME = "gpaCalculator";
public static final String TABLE = "gpa";
//public static final String col1 = "id";
public static final String col2 = "term";
public static final String col3 = "year";
public static final String col4 = "courseTitle1";
public static final String col5 = "courseTitle2";
public static final String col6 = "courseTitle3";
public static final String col7 = "courseTitle4";
public static final String col8 = "courseTitle5";
public static final String col9 = "courseTitle6";
public static final String col10 = "creditHrs1";
public static final String col11 = "creditHrs2";
public static final String col12 = "creditHrs3";
public static final String col13 = "creditHrs4";
public static final String col14 = "creditHrs5";
public static final String col15 = "creditHrs6";
public static final String col16 = "grade1";
public static final String col17 = "grade2";
public static final String col18 = "grade3";
public static final String col19 = "grade4";
public static final String col20 = "grade5";
public static final String col21 = "grade6";
public static final String col22 = "gpa";
public static final String col23 = "cgpa";
private static final String CREATE_TABLE_TUTORIALS = "create table " +TABLE+ "("+col2+" text, "+col3+" text," +col4+ " text," +col5+ " text," +col6+ " text,"
											+col7+" text,"+col8+" text,"+col9+" text,"+col10+" text,"+col11+" text,"
											+col12+" text,"+col13+" text,"+col14+" text,"
											+col15+" text,"+col16+" text,"+col17+" text,"+col18+" text,"
											+col19+" text,"+col20+" text,"+col21+" text," +col22+" text);";

private static final String DB_SCHEMA = CREATE_TABLE_TUTORIALS;

public GPACalculatorDB(Context context) {
super(context, DB_NAME, null, DB_VERSION);
}

public void onCreate(SQLiteDatabase db) {
db.execSQL(DB_SCHEMA);
}

public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
onCreate(db);
}


}