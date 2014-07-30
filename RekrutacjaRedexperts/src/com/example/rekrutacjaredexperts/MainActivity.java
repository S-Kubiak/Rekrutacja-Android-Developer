package com.example.rekrutacjaredexperts;



import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekrutacjaredexperts.file.FileManager;
import com.example.rekrutacjaredexperts.json.JsonManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener  {
	LocationManager locationManager;
	LocationListener locationListener;
	
	LatLng somePoint;
	Marker secondMarker = null;
	JsonManager jsonManager = JsonManager.getSingletonInstance();
    // Google Map
    private GoogleMap googleMap;
    
    ImageView imgView;
    
    String distance = "";

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy Tpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(Tpolicy); 

       
        imgView=(ImageView)findViewById(R.id.icon);
        
        FileManager fm = FileManager.getSingletonInstance();
        
        
        try {
            // Loading map
            initilizeMap();
            
            fm.startDownload();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

	/**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            
            FragmentManager fragmentManager = getSupportFragmentManager();
            SupportMapFragment mapFragment =  (SupportMapFragment)
                fragmentManager.findFragmentById(R.id.map);
            googleMap = mapFragment.getMap();
            googleMap.setMyLocationEnabled(true);
            
            createMarker();
            CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(getLayoutInflater());
            googleMap.setInfoWindowAdapter(customInfoWindow);
            
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    private void createMarker() {
    	double latitude = 0;
    	double longitude = 0;
    	String sampleText = "";
    	
        jsonManager.readJson();
    	if(jsonManager.getLatitude()!=null){

        	 latitude = Double.valueOf(jsonManager.getLatitude());
        	 longitude = Double.valueOf(jsonManager.getLongitude());
        	 sampleText = jsonManager.getSampleText();
        	 System.out.println("m"+latitude + jsonManager.getLatitude());
        	 
        	 
        	 
    	}
    	
     latitude = Double.valueOf(jsonManager.getLatitude());
	 longitude = Double.valueOf(jsonManager.getLongitude());
		somePoint = new LatLng(latitude, longitude);
    	
        Marker myMarker = googleMap.addMarker(new MarkerOptions()
        .position(getPinPosition())
        .title("From json")
        .snippet(sampleText)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        
       
	}
        
	private void calculateDistance(LatLng bluePin, LatLng redPin) {
		
		
		float [] results = new float[3];
		
		Location.distanceBetween(redPin.latitude, redPin.longitude,
				bluePin.latitude, bluePin.longitude, results);
		System.out.println(results[0]/1000 + "km");
		 
		distance = (float)Math.round(results[0]/1000) + "km";
		
	        
		
	}
	
	@Override
    protected void onStart() {
        super.onStart();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        super.onStop();
    }

	
	@Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
	
	@Override
	public void onLocationChanged(Location location) {
		LatLng anotherPoint = new LatLng(location.getLatitude(), location.getLongitude());
		double latitude = Double.valueOf(jsonManager.getLatitude());
		double longitude = Double.valueOf(jsonManager.getLongitude());
		somePoint = new LatLng(latitude, longitude);
		
		calculateDistance(somePoint, anotherPoint);
		if(secondMarker!=null)secondMarker.remove();
		
		secondMarker = googleMap.addMarker(new MarkerOptions()
        .position(anotherPoint)
        .title("My location")
        .snippet(distance));
	}
	
	public LatLng getPinPosition(){return somePoint;}
	 
	@Override
	public void onProviderDisabled(String provider) {
	Log.d("Latitude","disable");
	}
	 
	@Override
	public void onProviderEnabled(String provider) {
	Log.d("Latitude","enable");
	}
	 
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	Log.d("Latitude","status");
	}
	
	public String getDistance(){return distance;}
	
	
}


