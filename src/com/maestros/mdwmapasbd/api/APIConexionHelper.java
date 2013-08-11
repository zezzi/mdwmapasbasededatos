package com.maestros.mdwmapasbd.api;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.maestros.mdwmapasbd.Lugares;

import android.util.Log;



public class APIConexionHelper {
	public final static String URL = "http://mdwplaces.herokuapp.com/places.json";

	public JSONArray readJSONArrayFromURL(String url) {
		JSONArray result = null;
		try {
			HttpClient http_client = new DefaultHttpClient();
			HttpGet http_get = new HttpGet(url);
			HttpResponse response = http_client.execute(http_get);
			HttpEntity entity = response.getEntity();

			InputStream input_stream = entity.getContent();
			BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(input_stream,"iso-8859-1"),8);
	        StringBuilder string_builder = new StringBuilder();	        
	        String line = null;
	        while ((line = buffered_reader.readLine()) != null) {
	        	string_builder.append(line);
	        	
	        }	        
	        input_stream.close();	        
	        result = new JSONArray(string_builder.toString());	
		} catch (Exception e) {			
			Log.e("ApiRestaurantes","Error" + e.toString());			
		}
		return result;
	}

	public ArrayList<Lugares> parseJSONArray(JSONArray data) {
		ArrayList<Lugares> result = new ArrayList<Lugares>();
		if(data != null && data.length() > 0) {
			JSONArray names;
			String current_name;
			Lugares current_place;
			JSONObject current_object;				

			for (int i = 0;i < data.length();i++) {
				try {
					current_object = data.getJSONObject(i);
					names = current_object.names();
					current_place = new Lugares();
					for (int object_index = 0;object_index < current_object.length();object_index++) {
						current_name = names.getString(object_index);
						if (current_name.equals("title")) {
							current_place.setTitle(current_object.getString(current_name));
						} else if (current_name.equals("desc")) {
							current_place.setDesc(current_object.getString(current_name));
						} else if (current_name.equals("code")) {
							current_place.setCode(current_object.getString(current_name));
						} else if (current_name.equals("address")) {
							current_place.setAddress(current_object.getString(current_name));
						} else if (current_name.equals("latitud")) {
							current_place.setLatitud(Double.parseDouble(current_object.getString(current_name)));							
						}else if (current_name.equals("longitud")) {
							current_place.setLongitd(Double.parseDouble(current_object.getString(current_name)));							
						}else if (current_name.equals("id")) {
							current_place.setId(current_object.getString(current_name));
						}
					}
					current_place.setLatLng(current_place.getLatitud(),current_place.getLongitud());
					result.add(current_place);
				} catch (JSONException e) {
					Log.e("ApiRestaurantes","JSON error" + e.toString());
				}
			}
		}
		return result;
	}
}