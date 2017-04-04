package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Rabbia on 3/28/2017.
 */

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.App;

public class LocationService extends IntentService implements LocationListener {

    private LocationManager locationManager;


    public LocationService() {

        super("LocationService");

    }


    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = null;
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {

            Log.e("latitude", location.getLatitude() + "");
            Log.e("longitude", location.getLongitude() + "");

            String msg = "New Latitude: " + location.getLatitude()
                    + "New Longitude: " + location.getLongitude();

            App.setLongitude(location.getLongitude());
            App.setLatitude(location.getLatitude());

        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                2000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        App.setLongitude(location.getLongitude());
        App.setLatitude(location.getLatitude());

    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}