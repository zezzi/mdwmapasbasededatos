package com.maestros.mdwmapasbd;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;


public class CustomInfoWindow implements InfoWindowAdapter {
	  LayoutInflater inflater=null;

	  CustomInfoWindow(LayoutInflater inflater) {
	    this.inflater=inflater;
	  }

	  @Override
	  public View getInfoWindow(Marker marker) {
	    return(null);
	  }

	  @Override
	  public View getInfoContents(Marker marker) {
	    View custome=inflater.inflate(R.layout.custom_info_window, null);
	    TextView tv=(TextView)custome.findViewById(R.id.titulo);
	    tv.setText(marker.getTitle());
	    tv=(TextView)custome.findViewById(R.id.snippet);
	    tv.setText(marker.getSnippet());
	    return(custome);
	  }
}
