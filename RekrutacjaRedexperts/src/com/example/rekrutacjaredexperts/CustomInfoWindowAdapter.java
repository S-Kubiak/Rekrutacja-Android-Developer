package com.example.rekrutacjaredexperts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rekrutacjaredexperts.json.JsonManager;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

class CustomInfoWindowAdapter implements InfoWindowAdapter {
	
	JsonManager jsonManager = JsonManager.getSingletonInstance();
	
		LayoutInflater inflater=null;
		Bitmap myBitmap, resizedBitmap;
		
		CustomInfoWindowAdapter(LayoutInflater inflater) {
		    this.inflater=inflater;
		  }
		
		@Override
		public View getInfoContents(Marker marker) {
			
			View popup=inflater.inflate(R.layout.custom_info_window, null);
			
			if(myBitmap==null){
				myBitmap = getBitmapFromURL("http://bit.ly/1c2NIdu");
				resizedBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, false);
			}
			
			
       	 	ImageView iv = (ImageView)popup.findViewById(R.id.icon);
       	 	iv.setImageBitmap(resizedBitmap);
       	 	
		    TextView tv=(TextView)popup.findViewById(R.id.title);

		    tv.setText(marker.getTitle());
		    tv=(TextView)popup.findViewById(R.id.snippet);
		    tv.setText(marker.getSnippet());

		    return(popup);
		}
		
		public  Bitmap getBitmapFromURL(String src) {
			
		    try {
		        URL url = new URL(jsonManager.getImageUrl());
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        myBitmap = BitmapFactory.decodeStream(input);
		        System.out.println("obrazek pobrano");
		        connection.disconnect();
		        return myBitmap;
		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Problem z pobraniem obrazka");
		        return null;
		    }
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}