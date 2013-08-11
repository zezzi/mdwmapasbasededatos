package com.maestrosmdwmapas.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.maestros.mdwmapasbd.Lugares;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

public class DBAdapterPlace {
	private SQLiteDatabase database;
	private DatabaseHelperClass helper_for_database;
	
	public DBAdapterPlace(Context context) {
		helper_for_database = new DatabaseHelperClass(context);
	}

	public void close() {
		try {
			if (database.isOpen()) {
				database.close();
			}
			if (helper_for_database != null) {
				helper_for_database.close();	
			}
		} catch (Exception e){}
	}
		
	public synchronized void open() throws SQLException {
		try {
			database = helper_for_database.getWritableDatabase();
		} catch (SQLiteException ex) {
			try {
				database = helper_for_database.getReadableDatabase();				
			} catch (SQLiteException ex1) {
			}
		}			
	}
		
	public synchronized long insertPlace(Lugares _place) {	
		try{
		ContentValues lugar_nuevo = buildFromObject(_place);
		if (!database.isOpen()) {
			open();
		}
		long _rowId = -1;
		try {
			_rowId = database.insert(helper_for_database.TABLE_PLACES, null, lugar_nuevo);
		} catch (Exception e){}
			return _rowId;
		}catch(Exception ex){
			long _rowId = -1;
			return _rowId;
		}
	    
	}	
	
	public synchronized void deleteAllPlaces() {
		if (!database.isOpen()) {
			open();
		}		
		try {
			database.delete(helper_for_database.TABLE_PLACES, null, null);
		} catch (Exception e) {}
	}
		
	public synchronized boolean updatePlace(Lugares _lugar) {
		ContentValues nuevo_lugar =   buildFromObject(_lugar);
		if (!database.isOpen()) {
			open();
		}	
		boolean result = false;
		try {
			result = database.update(helper_for_database.TABLE_PLACES, nuevo_lugar,  "_id =" + _lugar.getId(), null) > 0;			
		} catch (Exception e){}
		return result; 
	}	
	
	public boolean removePlace(Lugares _lugar) {
		return  removePlace(_lugar.getId());
	}
	
	public synchronized boolean removePlace(String id) {
		if (!database.isOpen()) {
			open();
		}		
		boolean result = false;
		try {
			result = database.delete(helper_for_database.TABLE_PLACES, "_id =" + id, null) > 0;
		} catch (Exception e){}
		return result;
	}
	
	
	public synchronized Cursor getPlaces(String whereClause) {
		//Log.d("GetPlaces where clause", whereClause);
		boolean op = false;
		if (!database.isOpen()) {
			open();
			op=true;
		}
		Cursor _data = new MatrixCursor(new String[]{""});
		try {
			_data = database.query(helper_for_database.TABLE_PLACES, 
					new String[]{"_id","title","desc","code",
					   "address"}, //FALTA
									  whereClause, null, null, null, null);
		} catch (Exception e){
			
			Log.e("ErrorInGetPlaces", e.toString());
		}	
	    return _data;
	}
	
	public synchronized Cursor getAllPlaces() {
		return getPlaces(null);
	}
	
	public synchronized Lugares getPlaceById(String id) {
		Lugares p = null;
		try {
			Cursor c = getPlaces("_id = " + id);
			c.moveToFirst();
			p = getElementFromCursor(c,1);
			c.close();
		} catch (Exception e){}
		return p;
	}
	
	
	
	public synchronized Lugares getFirstElementFromCursor (Cursor _c) {
		return getElementFromCursor(_c,0);	
	}
	
	public synchronized Lugares getElementFromCursor (Cursor c, int index) {
		
			Lugares p = null;
			if (!c.isClosed() && !c.isAfterLast()) {
				p = new Lugares();
				p.setId(c.getString(c.getColumnIndex("_id")));
				p.setTitle(c.getString(c.getColumnIndex("name")));
				p.setAddress(c.getString(c.getColumnIndex("address")));
				p.setDesc(c.getString(c.getColumnIndex("desc")));
				p.setCode(c.getString(c.getColumnIndex("code")));
			}		
			return p;
		
	}
	
	
	public static String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		if (parts.length > 1) {	
		   for (String part : parts) {
			   if (part.length() > 1)
				   camelCaseString = camelCaseString + toProperCase(part);
			   else
				   camelCaseString = camelCaseString + part.toUpperCase();
		   }
		} else {
		   camelCaseString = toProperCase(s);
		}

		return camelCaseString;
	}
	
	
	public static String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	private ContentValues buildFromObject(Lugares _lugar) {
	    ContentValues nuevo_lugar = new ContentValues();
	    Log.d("insertando en dba:", _lugar.toString());
	   	Lugares lugar =_lugar;
	   	nuevo_lugar.put("_id", lugar.getId());
	   	nuevo_lugar.put("title", lugar.getTitle());
	   	nuevo_lugar.put("latitud", lugar.getLatLng().latitude);
    	nuevo_lugar.put("longitud", lugar.getLatLng().longitude);
	   	nuevo_lugar.put("desc", lugar.getDesc());
	   	nuevo_lugar.put("code", lugar.getCode());
    	nuevo_lugar.put("address", lugar.getAddress());
    	return nuevo_lugar;		
	}
	
	
	
}
