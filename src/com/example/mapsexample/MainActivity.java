package com.example.mapsexample;

import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener,
		OnMapClickListener, OnMapLongClickListener {

	GoogleMap googleMap;
	List<Address> matches;
	TextView tvLocation;
	String addressText;
	double latitude ;
	double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		tvLocation = (TextView) findViewById(R.id.tv_location);

		// Getting latitude of the current location
		 latitude = location.getLatitude();

		// Getting longitude of the current location
	 longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		
		googleMap.addMarker(new MarkerOptions().position(
				new LatLng(latitude, longitude)).title(
				addressText));
		
		googleMap
				.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker marker) {
						Toast.makeText(getBaseContext(),"marker clicked",Toast.LENGTH_LONG).show();
						return false;
					}

				});

		// TODO Auto-generated method stub

		Geocoder geoCoder = new Geocoder(this);

		try {
			matches = geoCoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
		addressText = String.format("%s, %s, %s", bestMatch
				.getMaxAddressLineIndex() > 0 ? bestMatch.getAddressLine(0)
				: "", bestMatch.getLocality(), bestMatch.getCountryName());

	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub

	}
}
