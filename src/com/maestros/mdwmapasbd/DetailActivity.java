package com.maestros.mdwmapasbd;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout); 
        Bundle extras = getIntent().getExtras();
        if (extras != null) {          
        	setTitle(extras.getString("lugar_title"));
        	setText(extras.getString("lugar_desc"));
            setAddress(extras.getString("lugar_address"));
            Log.d("markerdetail", "Received"+ extras.getString("lugar_title")); 
        }   
	}
	
  public void setText(String item) {
    TextView view = (TextView)findViewById(R.id.detalle_texto);
    view.setText(item);
  }
  
  public void setTitle(String item) {
	    TextView view = (TextView)findViewById(R.id.nombre_establecimiento);
	    view.setText(item);
	  }
  
  public void setAddress(String item) {
	    TextView view = (TextView)findViewById(R.id.direccion);
	    view.setText(item);
	  }
  
  public void setLugarCode(String item) {
	    TextView view = (TextView)findViewById(R.id.tipo);
	    view.setText(item);
	  }
  
}
