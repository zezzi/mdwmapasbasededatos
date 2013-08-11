package com.maestrosmdwmapas.data;

import java.lang.reflect.Field;

import com.google.android.gms.maps.model.LatLng;
import com.maestros.mdwmapasbd.Lugares;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DatabaseHelperClass extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;	
	final String TABLE_PLACES = "lugares";
	private static final String DATABASE_NAME = "mdwmaps.db";	
				
	public DatabaseHelperClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DatabaseHelperClass(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String lugares_sql = "";	
		Class<?> lugares_class = Lugares.class;
		Field[] fl = lugares_class.getDeclaredFields();
		for (Field f : fl) {
			try {
				String _name = f.getName();
				if (f.getType().equals(Bitmap.class)) {
					lugares_sql += _name + " blob,";
				} else if (f.getType().equals(Integer.class)) {
					lugares_sql += _name + " integer,";
				} else if (f.getType().equals(String.class)) {
					if (_name.equals("id")) {
						lugares_sql += "_id integer primary key, type integer,";	
					} else {
						lugares_sql += _name + " text,";	
					}						
				} else if(f.getType().equals(LatLng.class)){
					lugares_sql += "latitud" + " integer,";
					lugares_sql += "longitud" + " integer,";
				}
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {						
			}
		}
		
					
		Log.d("TABLA PLACES::",lugares_sql );		
		lugares_sql = "CREATE TABLE " + TABLE_PLACES + " (" + lugares_sql.substring(0, lugares_sql.length()-1) + ")";
		database.execSQL(lugares_sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelperClass.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
					
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
		onCreate(db);
	}

}


