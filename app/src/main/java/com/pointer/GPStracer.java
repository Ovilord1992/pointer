package com.pointer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class GPStracer implements LocationListener {
    Context context;


    public GPStracer(Context c) {
        context = c;
    }


    public Location getLocation() {

//        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(context,"Permission not granted",Toast.LENGTH_SHORT).show();
//            return null;
//        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnable) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Permission not granted",Toast.LENGTH_SHORT).show();
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        }else{
            Toast.makeText(context,"Включите GPS", Toast.LENGTH_LONG).show();
        }
        return null;
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
